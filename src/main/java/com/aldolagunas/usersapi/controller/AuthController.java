package com.aldolagunas.usersapi.controller;

import com.aldolagunas.usersapi.exception.InvalidCredentialsException;
import com.aldolagunas.usersapi.model.LoginRequest;
import com.aldolagunas.usersapi.model.LoginResponse;
import com.aldolagunas.usersapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Authentication", description = "User authentication")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping
    @Operation(
            summary = "Authenticate user",
            description = "Authenticate user using tax_id as username and password"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials"
    )
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse(false, "Invalid credentials", null));
        }
    }
}