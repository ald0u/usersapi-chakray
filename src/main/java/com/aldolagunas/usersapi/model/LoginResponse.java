package com.aldolagunas.usersapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login response")
public class LoginResponse {

    @Schema(description = "Login success status")
    private boolean success;

    @Schema(description = "Message")
    private String message;

    @Schema(description = "Authenticated user details")
    @JsonProperty("tax_id")
    private String taxId;
}
