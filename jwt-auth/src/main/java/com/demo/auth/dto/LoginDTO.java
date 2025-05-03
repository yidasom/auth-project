package com.demo.auth.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * com.demo.auth.dto
 *
 * @author : idasom
 * @data : 5/3/25
 */
@Getter
@Setter
public class LoginDTO implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getAuthorities();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }
}
