package com.aldolagunas.usersapi.model;

import com.aldolagunas.usersapi.validation.PhoneNumber;
import com.aldolagunas.usersapi.validation.RfcFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User information")
public class User {

    @Schema(description = "User unique identifier (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User's email address", example = "user@mail.com")
    private String email;

    @NotBlank(message = "Name is required")
    @Schema(description = "User's full name", example = "user1")
    private String name;

    @NotBlank(message = "Phone number is required")
    @PhoneNumber
    @Schema(description = "User's phone number", example = "+1234567890")
    private String phone;

    @NotBlank(message = "Password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "User's password", example = "P@ssw0rd")
    private String password;

    @NotBlank(message = "Tax ID is required")
    @RfcFormat
    @JsonProperty("tax_id")
    @Schema(description = "User's tax identification number", example = "123-45-6789")
    private String taxId;

    @JsonProperty("created_at")
    @Schema(description = "User creation timestamp in ISO 8601 format", example = "2024-06-01T12:00:00Z")
    private String createdAt;

    @Valid
    @NotNull(message = "Addresses cannot be null")
    @Schema(description = "List of user addresses")
    private List<Address> addresses;
}