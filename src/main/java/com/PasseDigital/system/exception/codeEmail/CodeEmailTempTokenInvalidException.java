package com.PasseDigital.system.exception.codeEmail;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CodeEmailTempTokenInvalidException extends RuntimeException {
    public CodeEmailTempTokenInvalidException() {
        super("Token invalid");
    }
}
