package com.PasseDigital.system.controller.qrCode;


import com.PasseDigital.system.model.dto.request.qrCode.QrCodeValidateRequest;
import com.PasseDigital.system.model.dto.response.qrCode.QrCodeValidResponse;
import com.PasseDigital.system.service.qrCode.QrCodeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qrCode")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQrCode(){
        byte[] qrCode = qrCodeService.generateQrCode();
        return ResponseEntity.ok(qrCode);
    }

    @PostMapping("/validate")
    public QrCodeValidResponse validateQrCode(@RequestBody QrCodeValidateRequest request){
        return qrCodeService.validateQrCode(request);
    }



}
