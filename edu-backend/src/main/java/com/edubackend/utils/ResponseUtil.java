package com.edubackend.utils;

import org.springframework.http.ResponseEntity;



public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>("success", message, data);
        return ResponseEntity.ok(response);
    }


    public static <T> ResponseEntity<ApiResponse<T>> error(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>("error", message, data);
        return ResponseEntity.badRequest().body(response);
    }

}
