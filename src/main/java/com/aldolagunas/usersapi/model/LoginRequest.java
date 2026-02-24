package com.aldolagunas.usersapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Tax ID is required")
    @JsonProperty("tax_id")
    private String taxId;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "password123")
    private String password;
}
