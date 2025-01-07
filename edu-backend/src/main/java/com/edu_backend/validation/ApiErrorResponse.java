package com.edu_backend.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private int status;
    private String message;
    private Map<String, String> errors;
}