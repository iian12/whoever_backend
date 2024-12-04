package com.jygoh.whoever.global.security;

import com.jygoh.whoever.global.auth.CustomOAuth2SuccessHandler;
import com.jygoh.whoever.global.auth.CustomUserDetailsService;
import com.jygoh.whoever.global.security.jwt.JwtTokenFilter;
import com.jygoh.whoever.global.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;


    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
        CustomUserDetailsService userDetailsService,
        CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
                authorizeRequests -> authorizeRequests.requestMatchers("/api/v1/auth/**").permitAll()
                    .requestMatchers("/api/v1/member/register").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                    .requestMatchers("/rss/**").permitAll().requestMatchers("/login/**").permitAll()
                    .anyRequest().authenticated())
            .oauth2Login(oauth2 -> oauth2.successHandler(customOAuth2SuccessHandler))
            .addFilterAfter(jwtTokenFilter(), OAuth2LoginAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenProvider, userDetailsService);
    }
}
