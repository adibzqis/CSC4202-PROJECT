package com.Parfetch.ParFetch.config;

import com.Parfetch.ParFetch.service.CustomUserDetailsService;
import com.Parfetch.ParFetch.service.SenderAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final SenderAuthenticationProvider senderAuthenticationProvider;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          SenderAuthenticationProvider senderAuthenticationProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.senderAuthenticationProvider = senderAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(
                daoAuthenticationProvider(),
                senderAuthenticationProvider
        ));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ✅ Allow CSRF except for H2
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**", "/sender-login")
                )

                // ✅ Allow H2 Console frames
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                )

                // ✅ Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/login", "/register", "/css/**", "/js/**").permitAll()

                        // ✅ Both staff and student can access parcel list
                        .requestMatchers("/parcels/list").hasAnyRole("STAFF", "SENDER")

                        .requestMatchers("/student-home", "/parcels/list", "/senders/display").hasRole("SENDER")

                        .requestMatchers("/student-home").hasRole("SENDER")
                        .anyRequest().authenticated()
                )

                // ✅ Form login for staff
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                // ✅ Logout config
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customSuccessHandler())
                        .failureUrl("/login?error=true")
                        .permitAll()
                )


                // ✅ Session config
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        // ✅ Add custom authentication filter for students using phone number
        http.addFilterBefore(new SenderAuthenticationFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"))) {
                response.sendRedirect("/index");
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SENDER"))) {
                response.sendRedirect("/student-home");
            } else {
                response.sendRedirect("/login?error");
            }
        };
    }

}