package com.aldolagunas.usersapi.exception;

public class DuplicateTaxIdException extends RuntimeException {
    public DuplicateTaxIdException(String message) {
        super(message);
    }
}
