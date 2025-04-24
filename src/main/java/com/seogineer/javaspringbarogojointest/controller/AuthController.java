package com.seogineer.javaspringbarogojointest.controller;

import com.seogineer.javaspringbarogojointest.dto.AuthRequest;
import com.seogineer.javaspringbarogojointest.dto.AuthResponse;
import com.seogineer.javaspringbarogojointest.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.seogineer.javaspringbarogojointest.enums.ErrorMessage.INVALID_USERNAME_OR_PASSWORD;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtUtil.generateToken(request.getUsername());

            return ResponseEntity.ok(new AuthResponse(jwtToken));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_USERNAME_OR_PASSWORD.getMessage());
        }
    }
}
