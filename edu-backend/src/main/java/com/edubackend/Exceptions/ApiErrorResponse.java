package com.edubackend.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private int status;
    private String message;
    private String error;
    private String timestamp;
    private String path;

    public ApiErrorResponse(int status, String message, String error, String path) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.path = path;
    }
}