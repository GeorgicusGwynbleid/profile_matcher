package com.example.profile.matcher.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProfileUserDetailsService implements UserDetailsService {

    /*
    Normally, this will be retrieved from a database, LDAP, or whatever system there is
     */
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("{noop}"+password)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}