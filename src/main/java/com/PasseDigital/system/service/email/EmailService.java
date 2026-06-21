package com.PasseDigital.system.service.email;


import com.PasseDigital.system.auth.AuthVerifyService;
import com.PasseDigital.system.exception.user.UserNotFoundException;
import com.PasseDigital.system.model.dto.request.email.EmailSenderRequest;
import com.PasseDigital.system.model.dto.response.email.EmailSenderResponse;
import com.PasseDigital.system.model.entity.codeEmail.CodeEmail;
import com.PasseDigital.system.model.entity.email.Email;
import com.PasseDigital.system.model.entity.user.User;
import com.PasseDigital.system.model.repository.email.EmailRepository;
import com.PasseDigital.system.model.repository.user.UserRepository;
import com.PasseDigital.system.model.roles.email.EmailStatusEnum;
import com.PasseDigital.system.service.codeEmail.CodeEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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


    @Value("${spring.mail.password}")
    private String sender;


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


    public void sendEmailQrCodeValidated() {
        User student = authVerifyService.getAuthenticate();
        Email email = new Email();
        email.setUser(student);
        email.setRecipient(student.getEmail());


        try {
            String htmlMessage = "deu certo";

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");


            helper.setTo(student.getEmail());
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
