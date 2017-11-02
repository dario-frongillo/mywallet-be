package it.italian.coders.authentication.jwt;


import java.util.ArrayList;
import java.util.List;
import it.italian.coders.model.authentication.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;



public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {

        return JwtUser.newBuilder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .signUpConfirmed(user.isSignUpConfirmed())
                .authorities(mapToGrantedAuthorities(user.getAuthorities()))
                .enabled(user.isEnabled()&&!user.isResetPassword())
                .build();

    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : authorities) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
            grantedAuthorities.add(grantedAuthority);
        }
        return grantedAuthorities;
    }
}
