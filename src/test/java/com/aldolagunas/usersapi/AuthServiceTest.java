package com.aldolagunas.usersapi;

import com.aldolagunas.usersapi.exception.InvalidCredentialsException;
import com.aldolagunas.usersapi.model.LoginRequest;
import com.aldolagunas.usersapi.model.LoginResponse;
import com.aldolagunas.usersapi.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Test
    void login_success() {
        LoginRequest request = new LoginRequest("AARR990101XXX", "7c4a8d09ca3762af61e59520943dc26494f8941b");
        LoginResponse response = authService.login(request);
        assertTrue(response.isSuccess());
        assertEquals("AARR990101XXX", response.getTaxId());
    }

    @Test
    void login_invalidCredentials() {
        LoginRequest request = new LoginRequest("AARR990101XXX", "wrong");
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }
}

