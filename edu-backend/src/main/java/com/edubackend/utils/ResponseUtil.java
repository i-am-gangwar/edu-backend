package com.edubackend.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>("success", message, data);
        return ResponseEntity.ok(response);
    }


    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>("Not Found", message, data);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>("BAD_REQUEST", message, data);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> conflict(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>("CONFLICT", message, data);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>("INTERNAL_SERVER_ERROR", message, data);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}
