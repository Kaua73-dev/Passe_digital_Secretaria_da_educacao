package com.PasseDigital.system.service.codeEmail;

import com.PasseDigital.system.exception.codeEmail.CodeEmailExpirationException;
import com.PasseDigital.system.exception.codeEmail.CodeEmailInvalidException;
import com.PasseDigital.system.exception.codeEmail.CodeEmailNotFoundException;
import com.PasseDigital.system.exception.codeEmail.CodeEmailTooManyRequestsException;
import com.PasseDigital.system.model.dto.request.codeEmailRequest.CodeEmailRequest;
import com.PasseDigital.system.model.dto.response.codeEmail.CodeEmailValidateCodeResponse;
import com.PasseDigital.system.model.entity.codeEmail.CodeEmail;
import com.PasseDigital.system.model.entity.user.User;
import com.PasseDigital.system.model.repository.codeEmail.CodeEmailRepository;
import com.PasseDigital.system.model.roles.codeEmail.CodeEmailEnum;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class CodeEmailService {

    private final CodeEmailRepository codeEmailRepository;

    public CodeEmailService(CodeEmailRepository codeEmailRepository) {
        this.codeEmailRepository = codeEmailRepository;
    }


    public String generateCode(User user){

        Optional<CodeEmail> lastCode =
                codeEmailRepository.findTopByUserOrderByCreatedAtDesc(user);

        if(lastCode.isPresent()){
            LocalDateTime nextAllowedTime = lastCode.get().getCreateAt().plusMinutes(1);

            // se o proximo horario permitido for depois da atual
            if(nextAllowedTime.isAfter(LocalDateTime.now())){
                throw new CodeEmailTooManyRequestsException();
            }

        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < 6; i++){
            int index = random.nextInt(chars.length());
            builder.append(chars.charAt(index));
        }

        CodeEmail codeEmail = new CodeEmail();
        codeEmail.setCode(builder.toString());
        codeEmail.setCreateAt(LocalDateTime.now());
        codeEmail.setExpirationAt(LocalDateTime.now().plusMinutes(15));
        codeEmail.setCodeEmailEnum(CodeEmailEnum.ACTIVE);
        codeEmail.setUser(user);

        codeEmailRepository.save(codeEmail);

        return builder.toString();

    }

    @Transactional
    public CodeEmailValidateCodeResponse validateCode(CodeEmailRequest request){

        CodeEmail codeEmail = codeEmailRepository.findByCode(request.code()).orElseThrow(CodeEmailNotFoundException::new
        );

        if(codeEmail.getExpirationAt().isBefore(LocalDateTime.now())){
            codeEmail.setCodeEmailEnum(CodeEmailEnum.EXPIRED);
            throw new CodeEmailExpirationException();
        }

        if(codeEmail.getCodeEmailEnum() != CodeEmailEnum.ACTIVE){
            throw new CodeEmailInvalidException();
        }

        String token = UUID.randomUUID().toString();


        codeEmail.setCodeEmailEnum(CodeEmailEnum.EXPIRED);
        codeEmail.setTempToken(token);
        codeEmail.setExpirationToken(
                LocalDateTime.now().plusMinutes(5)
        );
        codeEmail.setUserCanChangePassword(true);

        codeEmailRepository.save(codeEmail);

        return new CodeEmailValidateCodeResponse(
                codeEmail.getTempToken()
        );

    }

}
