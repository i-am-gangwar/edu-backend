package com.edubackend.utils;


import lombok.Data;

@Data
public class ApiResponse<T> {

    private String status;
    private String message;
    private T data;
    private String timestamp;

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
}