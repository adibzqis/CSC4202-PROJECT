package com.Parfetch.ParFetch.service;

import com.Parfetch.ParFetch.model.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SenderAuthenticationProvider implements AuthenticationProvider {

    private final SenderService senderService;

    @Autowired
    public SenderAuthenticationProvider(SenderService senderService) {
        this.senderService = senderService; // Properly initialize studentService
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumber = authentication.getName();
        Sender sender = senderService.findByPhone(phoneNumber);

        if (sender == null) {
            throw new BadCredentialsException("No student found with phone number: " + phoneNumber);
        }

        return new UsernamePasswordAuthenticationToken(
                phoneNumber,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_SENDER"))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
