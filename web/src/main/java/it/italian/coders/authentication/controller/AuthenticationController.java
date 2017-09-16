package it.italian.coders.authentication.controller;


import it.italian.coders.authentication.dto.JwtAuthenticationResponse;
import it.italian.coders.authentication.jwt.JwtAuthenticationRequest;
import it.italian.coders.authentication.jwt.JwtTokenUtil;
import it.italian.coders.authentication.jwt.JwtUser;
import it.italian.coders.model.authentication.User;
import it.italian.coders.service.authentication.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



import java.util.List;

import javax.servlet.http.HttpServletRequest;


@RestController
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserManager userManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;



    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device, HttpServletRequest request) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( authenticationRequest.getUsername(),
                        authenticationRequest.getPassword() ) );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails, device);


        User user = userManager.findByUsername(authenticationRequest.getUsername());


        /*
        if(!user.isEnabled()){
            throw new UserDisabledException();
        }
        */


        List<String> list = user.getAuthorities();

        return ResponseEntity.ok(new JwtAuthenticationResponse(token, user, list, request.getRemoteAddr()));


    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        String refreshedToken = jwtTokenUtil.refreshToken(token);
        User currentUser = userManager.findByUsernameIgnoreCase(user.getUsername());
        List<String> list = currentUser.getAuthorities();
        return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken,currentUser,list,request.getRemoteAddr()));

    }

}
