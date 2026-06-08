package com.PasseDigital.system.exception.qrCode;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class QrCodeInvalidException extends RuntimeException {
    public QrCodeInvalidException() {
        super("QrCode invalid");
    }
}
