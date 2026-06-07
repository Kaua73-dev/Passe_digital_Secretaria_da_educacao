package com.PasseDigital.system.exception.codeEmail;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CodeEmailTooManyRequestsException extends RuntimeException {
    public CodeEmailTooManyRequestsException() {
        super("It's not possible to generate code right now");
    }
}
