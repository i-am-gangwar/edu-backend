package com.edubackend.Exceptions.Exception;

public class OperationFailedException extends RuntimeException {
    public OperationFailedException(String message) {
        super(message);
    }
}