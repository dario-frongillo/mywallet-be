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
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

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



    @PostConstruct
    private void init() {
        try {
            String[] fieldsToMap = { "id", "about", "age_range", "birthday",
                    "context", "cover", "currency", "devices", "education",
                    "email", "favorite_athletes", "favorite_teams",
                    "first_name", "gender", "hometown", "inspirational_people",
                    "installed", "install_type", "is_verified", "languages",
                    "last_name", "link", "locale", "location", "meeting_for",
                    "middle_name", "name", "name_format", "political",
                    "quotes", "payment_pricepoints", "relationship_status",
                    "religion", "security_settings", "significant_other",
                    "sports", "test_group", "timezone", "third_party_id",
                    "updated_time", "verified", "viewer_can_send_gift",
                    "website", "work" };

            Field field = Class.forName(
                    "org.springframework.social.facebook.api.UserOperations")
                    .getDeclaredField("PROFILE_FIELDS");
            field.setAccessible(true);

            Field modifiers = field.getClass().getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, fieldsToMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
        get image url http://graph.facebook.com/1886821314979753/picture?type=square
        */


        List<String> list = user.getAuthorities();
        Facebook facebook = new FacebookTemplate("EAAFJSPdrzqoBALkDtCbg3cL7vKwBSrDHhPjZAtWFQ3v7ZCWeefYuyTEBN1ir0eLAbsGhUNtamy1UN9cZCZBV2NbUHoTdRZCeKtZCjQNlhjZClZBd0rHEdmsbzwF87pSlB3xQ5wUyz0Pskt1l1QcVz9wPXIXyQxb3WAKwQLEes7C4YFxnB9UQt05kvEPItJ9jrcPlbrbZANONyR1ZAVvkrVEKsgiL3O2dyuPX4ZD", "frongilloprova");
        boolean b =facebook.isAuthorized();
        org.springframework.social.facebook.api.User profile = facebook.userOperations().getUserProfile();
        String [] fields = { "id", "email",  "first_name", "last_name" };
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
