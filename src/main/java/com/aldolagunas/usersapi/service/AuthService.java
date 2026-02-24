package com.aldolagunas.usersapi.service;

import com.aldolagunas.usersapi.exception.InvalidCredentialsException;
import com.aldolagunas.usersapi.model.LoginRequest;
import com.aldolagunas.usersapi.model.LoginResponse;
import com.aldolagunas.usersapi.model.User;
import com.aldolagunas.usersapi.repository.UserRepository;
import com.aldolagunas.usersapi.util.EncryptionUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;

    public AuthService(UserRepository userRepository, EncryptionUtil encryptionUtil) {
        this.userRepository = userRepository;
        this.encryptionUtil = encryptionUtil;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByTaxId(request.getTaxId())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        String decryptedPassword = encryptionUtil.decrypt(user.getPassword());

        if (!decryptedPassword.equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        User userResponse = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .taxId(user.getTaxId())
                .createdAt(user.getCreatedAt())
                .addresses(user.getAddresses())
                .build();

        return LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .taxId(user.getTaxId())
                .build();
    }
}