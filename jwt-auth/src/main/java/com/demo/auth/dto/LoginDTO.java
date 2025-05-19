package com.demo.auth.dto;

import com.demo.member.entity.Member;
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
public class LoginDTO extends Member implements UserDetails {
    private int loginFailCnt;

    public void setLoginFailCnt(int loginFailCnt) {
        this.loginFailCnt = loginFailCnt;
    }

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
