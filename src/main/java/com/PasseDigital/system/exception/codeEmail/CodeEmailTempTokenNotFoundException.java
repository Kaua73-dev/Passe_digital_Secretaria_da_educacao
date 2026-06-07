package com.PasseDigital.system.exception.codeEmail;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CodeEmailTempTokenNotFoundException extends RuntimeException {
    public CodeEmailTempTokenNotFoundException() {
        super("Token not found");
    }
}
