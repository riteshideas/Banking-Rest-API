package dev.ritesh.Banking_v2.services;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import dev.ritesh.Banking_v2.entities.UserInfo;

public class UserInfoDetails implements UserDetails {
    
    
    private String username;
    private String password;
    private List<GrantedAuthority> authorities;


    public UserInfoDetails(UserInfo userInfo) {
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
