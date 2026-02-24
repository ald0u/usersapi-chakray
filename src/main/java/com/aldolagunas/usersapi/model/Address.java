package com.aldolagunas.usersapi.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User address information")
public class Address {

    @Schema(description = "Address unique identifier", example = "1")
    private Long id;

    @NotBlank(message = "Address name is required")
    @Schema(description = "Address name", example = "WorkAddress")
    private String name;

    @NotBlank(message = "Street is required")
    @Schema(description = "Street is required", example = "123 Main St")
    private String street;

    @NotBlank(message = "CountryCode is required")
    @JsonProperty("country_code")
    @Schema(description = "CountryCode is required", example = "UK")
    private String countryCode;
}