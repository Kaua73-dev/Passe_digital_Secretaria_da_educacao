package com.PasseDigital.system.exception.qrCode;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class QrCodeDefeatedException extends RuntimeException {
    public QrCodeDefeatedException() {
        super("QrCode defeated");
    }
}
