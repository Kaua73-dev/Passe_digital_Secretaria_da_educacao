package com.PasseDigital.system.service.email;


import com.PasseDigital.system.auth.AuthVerifyService;
import com.PasseDigital.system.exception.user.UserNotFoundException;
import com.PasseDigital.system.model.dto.request.email.EmailSenderRequest;
import com.PasseDigital.system.model.dto.response.email.EmailSenderResponse;
import com.PasseDigital.system.model.entity.email.Email;
import com.PasseDigital.system.model.entity.qrCode.QrCode;
import com.PasseDigital.system.model.entity.user.User;
import com.PasseDigital.system.model.repository.email.EmailRepository;
import com.PasseDigital.system.model.repository.user.UserRepository;
import com.PasseDigital.system.model.roles.email.EmailStatusEnum;
import com.PasseDigital.system.service.codeEmail.CodeEmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class EmailService {


    private final JavaMailSender javaMailSender;
    private final AuthVerifyService authVerifyService;
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final CodeEmailService codeEmailService;



    public EmailService(JavaMailSender javaMailSender, AuthVerifyService authVerifyService, EmailRepository emailRepository, UserRepository userRepository, CodeEmailService codeEmailService) {
        this.javaMailSender = javaMailSender;
        this.authVerifyService = authVerifyService;
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
        this.codeEmailService = codeEmailService;
    }

    @Transactional
    public EmailSenderResponse sendCodeEmail(EmailSenderRequest request){

        User user = userRepository.findByRegistration(request.registration()).orElseThrow(UserNotFoundException::new);

        Email emailLog = new Email();
        emailLog.setUser(user);
        emailLog.setRecipient(user.getEmail());

        try {

            String html = """
    <h2 style="color: #2563eb; text-align: center;">
   
    Passe Digital
    </h2>

    <p>Olá, <strong>%s</strong>.</p>

    <p>
    Recebemos uma solicitação para redefinir sua senha.
    Utilize o código abaixo para continuar o processo:
    </p>

    <div style="text-align: center; margin: 30px 0;">
    <span style="
        display: inline-block;
        background-color: #2563eb;
        color: white;
        font-size: 32px;
        font-weight: bold;
        letter-spacing: 8px;
        padding: 15px 25px;
        border-radius: 8px;
    ">
        %s
    </span>
    </div>

    <p>
    Este código é válido por <strong>15 minutos</strong>.
    </p>

    <p>
    Caso você não tenha solicitado a recuperação de senha,
    ignore este e-mail.
     </p>

     <hr style="margin-top: 30px;">

     <p style="font-size: 12px; color: #666;">
      Passe Digital • Sistema de Carteirinha Escolar
      </p>
    """.formatted(user.getName(), codeEmailService.generateCode(user));


            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject("Verificação de Conta - Carteirinha Digital");
            helper.setText(html, true);
            javaMailSender.send(message);

            emailLog.setEmailStatusEnum(EmailStatusEnum.SUCCESS);
            emailLog.setSendAt(LocalDateTime.now());


        } catch (Exception e) {
            emailLog.setEmailStatusEnum(EmailStatusEnum.FAILED);
            emailLog.setErrorMessage(e.getMessage());
        }


        emailRepository.save(emailLog);

        return new EmailSenderResponse(
                emailLog.getId(),
                emailLog.getRecipient(),
                emailLog.getEmailStatusEnum(),
                emailLog.getSendAt()
        );

    }


    @Async
    public void sendEmailQrCodeValidated(QrCode qrCode) {

        Email email = new Email();
        email.setUser(qrCode.getUser());
        email.setRecipient(qrCode.getUser().getEmail());

        try {
            String currentDateTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            String htmlMessage = """
        <!DOCTYPE html>
        <html lang="pt-BR">
        <head>
        <meta charset="UTF-8">
        </head>
        <body style="margin:0;padding:0;background-color:#f4f6f9;font-family:Arial,Helvetica,sans-serif;">

        <div style="max-width:600px;margin:40px auto;background:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.08);">

            <div style="background:linear-gradient(135deg,#4CAF50,#2E7D32);padding:30px;text-align:center;">
                <h1 style="color:white;margin:0;">
                    ✅ QR Code Validado
                </h1>
            </div>

            <div style="padding:40px 30px;">

                <h2 style="color:#333;">
                    Olá, %s!
                </h2>

                <p style="font-size:16px;color:#555;line-height:1.7;">
                    Seu QR Code foi validado com sucesso em nosso sistema.
                </p>

                <div style="background:#f8f9fa;border-left:5px solid #4CAF50;padding:20px;border-radius:8px;margin:25px 0;">
                    <strong>Data da validação:</strong> %s
                </div>

                <p style="font-size:16px;color:#555;line-height:1.7;">
                    Já foi registrado em nosso sistema. Agradecemos o bom fluxo!
                </p>

                <div style="text-align:center;margin-top:30px;">
                    <span style="background:#4CAF50;color:white;padding:14px 28px;border-radius:8px;font-weight:bold;">
                        ✔ Validação Concluída
                    </span>
                </div>

            </div>

            <div style="background:#f8f9fa;padding:20px;text-align:center;color:#777;font-size:13px;">
                Este é um e-mail automático. Não responda esta mensagem.
                <br><br>
                © Passe Digital
            </div>
           </div>
        </body>
        </html>
        """.formatted(qrCode.getUser().getName(), currentDateTime);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");


            helper.setTo(qrCode.getUser().getEmail());
            helper.setSubject("Seu Qr Code foi válidado com sucesso!");
            helper.setText(htmlMessage, true);
            javaMailSender.send(message);


            email.setSendAt(LocalDateTime.now());
            email.setEmailStatusEnum(EmailStatusEnum.SUCCESS);


        } catch (Exception e){
            email.setEmailStatusEnum(EmailStatusEnum.FAILED);
            email.setErrorMessage(e.getMessage());
        }

        emailRepository.save(email);

    }






}
