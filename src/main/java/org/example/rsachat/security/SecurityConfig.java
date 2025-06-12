package org.example.rsachat.security;

import org.example.rsachat.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
public class SecurityConfig {

    private final UserService userService;
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userService.findByUsername(username)
                .map(u -> org.springframework.security.core.userdetails.User
                        .withUsername(u.getUsername())
                        .password(u.getPassword())
                        .roles("USER")
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1) дозволяємо вільний доступ до реєстрації, стилів і H2-консолі
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/css/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 2) вимикаємо CSRF лише для H2-консолі
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                )
                // 3) дозволяємо показувати фрейми для консолі
                .headers(headers -> headers
                        .frameOptions().disable()
                )
                // 4) налаштовуємо форму входу з редиректом на /chats
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/chats", true)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
