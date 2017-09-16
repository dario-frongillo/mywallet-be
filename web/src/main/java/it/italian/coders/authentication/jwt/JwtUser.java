package it.italian.coders.authentication.jwt;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class JwtUser implements UserDetails {

    private static final long serialVersionUID = 1L;
    private  String username;
    private  String password;
    private  String email;
    private  List<? extends GrantedAuthority> authorities;
    private  boolean enabled;


    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }



    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String username;
        private String password;
        private String email;
        private List<? extends GrantedAuthority> authorities;
        private boolean enabled;

        private Builder() {
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder authorities(List<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public JwtUser build() {
            JwtUser jwtUser = new JwtUser();
            jwtUser.password = this.password;
            jwtUser.username = this.username;
            jwtUser.enabled = this.enabled;
            jwtUser.email = this.email;
            jwtUser.authorities = this.authorities;
            return jwtUser;
        }
    }
}
