package it.italian.coders.authentication.controller;


import it.italian.coders.authentication.dto.JwtAuthenticationResponse;
import it.italian.coders.authentication.jwt.JwtAuthenticationRequest;
import it.italian.coders.authentication.jwt.JwtTokenUtil;
import it.italian.coders.authentication.jwt.JwtUser;
import it.italian.coders.authentication.jwt.JwtUserFactory;
import it.italian.coders.exception.UserDisabledException;
import it.italian.coders.model.authentication.User;
import it.italian.coders.model.social.SocialEnum;
import it.italian.coders.service.authentication.UserManager;
import it.italian.coders.service.social.SocialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
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

    @Autowired
    private SocialManager socialManager;

    @Autowired
    @Qualifier("errorMessageSource")
    private ReloadableResourceBundleMessageSource messageSource;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device, HttpServletRequest request) throws AuthenticationException {
        User user = null;
        Locale locale = LocaleContextHolder.getLocale();
        /*
         * Each login the user data must be refreshed with the data of the relative social account
         */
        if(authenticationRequest.getSocialAuthentication()!=null && authenticationRequest.getSocialAuthentication() != SocialEnum.None){
            user = socialManager.updInsSocialUser(authenticationRequest.getSocialAuthentication(),authenticationRequest.getUsername(),authenticationRequest.getSocialAccessToken());
        }else{
            if(authenticationRequest.getUsername().contains("@")){
                user = userManager.findByEmail(authenticationRequest.getUsername());
            }else{
                user = userManager.findByUsernameIgnoreCase(authenticationRequest.getUsername());
            }

            //in order to allow login after reset password
            if(user.isResetPassword()){
                user.setResetPassword(false);
                user = userManager.save(user);
            }
        }

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( authenticationRequest.getUsername(),
                        authenticationRequest.getPassword() ) );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if(!user.isEnabled()){
            throw new UserDisabledException(messageSource.getMessage("UserDisabledException.title",null,locale));
        }



        final UserDetails userDetails = JwtUserFactory.create(user);
        final String token = jwtTokenUtil.generateToken(userDetails, device);

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
