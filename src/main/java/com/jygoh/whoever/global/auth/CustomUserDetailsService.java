package com.jygoh.whoever.global.auth;

import com.jygoh.whoever.domain.user.entity.Users;
import com.jygoh.whoever.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // CustomUserDetail 생성 시, Member 객체와 Member ID를 전달
        return new CustomUserDetail(users, users.getId());
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Users users = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return new CustomUserDetail(users, users.getId());
    }
}
