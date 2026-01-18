package com.projectmanagement.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.projectmanagement.model.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.projectmanagement.repository.UserRepository;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

 private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"+username));
                //.orElseThrow(() -> new UsernameNotFoundException("المستخدم غير موجود: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority(user.getRole().name())
                ))
                .disabled(!user.isEnabled())
                .build();
    }
}