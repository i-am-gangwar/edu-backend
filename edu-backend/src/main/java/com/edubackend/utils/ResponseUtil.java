package com.edubackend.utils;

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

}
