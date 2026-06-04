package com.PasseDigital.system.exception.user;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StudentAlreadyExistException extends RuntimeException {
    public StudentAlreadyExistException() {
        super("Student Already exist");
    }
}
