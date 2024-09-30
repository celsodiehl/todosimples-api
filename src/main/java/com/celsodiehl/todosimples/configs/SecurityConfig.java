package com.celsodiehl.todosimples.configs;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.celsodiehl.todosimples.security.JWTUtil;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    private JWTUtil jwtUtil;

    private static final String[] PUBLIC_MATCHERS = { "/" };

    private static final String[] PUBLIC_MATCHERS_POST = { "/user", "/login" };

    // MUDEI ACHADO DA INTERNET
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll();
                    req.requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS).permitAll();
                    // req.requestMatchers(HttpMethod.OPTIONS, "**").permitAll();//allow CORS option
                    // calls
                    req.anyRequest().authenticated();

                    AuthenticationManagerBuilder authenticationManagerBuilder = http
                            .getSharedObject(AuthenticationManagerBuilder.class);
                    try {
                        authenticationManagerBuilder.userDetailsService(this.userDetailsService)
                                .passwordEncoder(bCryptPasswordEncoder());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        this.authenticationManager = authenticationManagerBuilder.build();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        return http.build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
