package com.PasseDigital.system.exception.user;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotAllowedException extends RuntimeException {
    public UserNotAllowedException() {
        super("User Not Allowed");
    }
}
