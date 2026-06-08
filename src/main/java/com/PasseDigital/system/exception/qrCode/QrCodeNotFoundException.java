package com.PasseDigital.system.exception.qrCode;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QrCodeNotFoundException extends RuntimeException {
    public QrCodeNotFoundException() {
        super("QrCode not found exception");
    }
}
