package com.aldolagunas.usersapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response")
public class ErrorResponse {

    @Schema(description = "Timestamp of the error")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code of the error")
    private int status;

    @Schema(description = "Error message")
    private String message;

    @Schema(description = "List of validation errors")
    private List<String> errors;

    @Schema(description = "Request path")
    private String path;
}
