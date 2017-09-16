package it.italian.coders.authentication.service;

import it.italian.coders.authentication.jwt.JwtUserFactory;
import it.italian.coders.model.authentication.User;
import it.italian.coders.service.authentication.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserManager userManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;

        if(username.contains("@"))
            user=userManager.findByEmail(username);
        else
            user=userManager.findByUsernameIgnoreCase(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }

    }
}
