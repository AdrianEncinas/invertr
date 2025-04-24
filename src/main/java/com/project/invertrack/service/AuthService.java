package com.project.invertrack.service;

import com.project.invertrack.dto.LoginRequestDTO;
import com.project.invertrack.dto.LoginResponseDTO;
import com.project.invertrack.dto.UserCreateDTO;
import com.project.invertrack.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CustomUserDetailsService userDetailsService,
            UserService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO register(UserCreateDTO request) {
        User user = userService.createUser(request);
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);

        return new LoginResponseDTO(jwtToken, "Bearer");
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Intentando autenticar usuario: {}", request.getUsername());
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            log.info("Autenticación exitosa para usuario: {}", request.getUsername());

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String jwtToken = jwtService.generateToken(userDetails);
            log.info("Token JWT generado para usuario: {}", request.getUsername());

            return new LoginResponseDTO(jwtToken, "Bearer");
        } catch (Exception e) {
            log.error("Error en autenticación para usuario {}: {}", request.getUsername(), e.getMessage());
            throw e;
        }
    }
} 