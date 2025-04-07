package com.project.invertrack.service;

import com.project.invertrack.dto.LoginRequestDTO;
import com.project.invertrack.dto.LoginResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(user);

        return new LoginResponseDTO(jwtToken, jwtToken);
    }
} 