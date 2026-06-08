package com.PasseDigital.system.exception.qrCode;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class QrCodeErrorWhenGenerating extends RuntimeException {
    public QrCodeErrorWhenGenerating() {
        super("Error when generating the QrCode");
    }
}

