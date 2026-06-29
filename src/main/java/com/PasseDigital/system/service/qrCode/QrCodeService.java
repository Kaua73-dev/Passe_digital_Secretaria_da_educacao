package com.PasseDigital.system.service.qrCode;


import com.PasseDigital.system.auth.AuthVerifyService;
import com.PasseDigital.system.exception.qrCode.QrCodeDefeatedException;
import com.PasseDigital.system.exception.qrCode.QrCodeInvalidException;
import com.PasseDigital.system.exception.qrCode.QrCodeNotFoundException;
import com.PasseDigital.system.model.dto.request.qrCode.QrCodeValidateRequest;
import com.PasseDigital.system.model.dto.response.qrCode.QrCodeValidResponse;
import com.PasseDigital.system.model.dto.response.user.StudentClassResponse;
import com.PasseDigital.system.model.dto.response.user.StudentEducationResponse;
import com.PasseDigital.system.model.dto.response.user.StudentResponse;
import com.PasseDigital.system.model.entity.qrCode.QrCode;
import com.PasseDigital.system.model.entity.user.User;
import com.PasseDigital.system.model.repository.qrCode.QrCodeRepository;
import com.PasseDigital.system.model.repository.user.UserRepository;
import com.PasseDigital.system.model.roles.qrCode.QrCodeStatusEnum;
import com.PasseDigital.system.service.email.EmailService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QrCodeService {

    private final AuthVerifyService authVerifyService;
    private final QrCodeRepository qrCodeRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final SimpMessagingTemplate messagingTemplate;



    public QrCodeService(AuthVerifyService authVerifyService, QrCodeRepository qrCodeRepository, UserRepository userRepository, EmailService emailService, SimpMessagingTemplate messagingTemplate) {
        this.authVerifyService = authVerifyService;
        this.qrCodeRepository = qrCodeRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public byte[] generateQrCode(){
        User user = authVerifyService.getAuthenticate();

        try {
            String uuidQrCode = UUID.randomUUID().toString();

            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix = qrCodeWriter.encode(
                uuidQrCode,
                BarcodeFormat.QR_CODE,
                300,
                300

            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            QrCode qrCode = new QrCode();
            qrCode.setUser(user);
            qrCode.setQrCodeStatusEnum(QrCodeStatusEnum.VALID);
            qrCode.setContent(uuidQrCode);
            qrCode.setCreateAt(LocalDateTime.now());
            qrCode.setExpirationAt(LocalDateTime.now().plusMinutes(15));

            qrCodeRepository.save(qrCode);

            return outputStream.toByteArray();

        } catch (IOException  | WriterException e) {
            throw new RuntimeException("Error when generating the QrCode: " + e);
        }
    }

    public QrCodeValidResponse validateQrCode(QrCodeValidateRequest request){

        QrCode qrCode = qrCodeRepository.findByContent(request.content())
                .orElseThrow(QrCodeNotFoundException::new);

        if(!qrCode.getQrCodeStatusEnum().equals(QrCodeStatusEnum.VALID)){
            throw new QrCodeInvalidException();
        }

        if(qrCode.getExpirationAt().isBefore(LocalDateTime.now())){
            qrCode.setQrCodeStatusEnum(QrCodeStatusEnum.INVALID);
            qrCodeRepository.save(qrCode);
            throw new QrCodeDefeatedException();
        }

        qrCode.setValidateAt(LocalDateTime.now());
        QrCode qrCodeValid = qrCodeRepository.save(qrCode);

        User student = qrCodeValid.getUser();

        emailService.sendEmailQrCodeValidated(qrCode);

        var response = new QrCodeValidResponse(
                qrCodeValid.getValidateAt(),
                qrCodeValid.getQrCodeStatusEnum(),
                new StudentResponse(
                        student.getName(),
                        student.getEmail(),
                        student.getRegistration(),
                        new StudentClassResponse(
                                student.getUserStudentClass().getStudentClass()
                        ),

                        new StudentEducationResponse(
                                student.getUserStudentEducation().getEducation()
                        ),
                        student.getBirth(),
                        student.getUserStudentShiftEnum()

                ));

        messagingTemplate.convertAndSend(
                "/topic/response",
                response
        );
        return response;

        }

}
