package com.PasseDigital.system.service.user;

import com.PasseDigital.system.auth.AuthVerifyService;
import com.PasseDigital.system.config.TokenConfig;
import com.PasseDigital.system.exception.codeEmail.CodeEmailTempTokenInvalidException;
import com.PasseDigital.system.exception.codeEmail.CodeEmailTempTokenNotFoundException;
import com.PasseDigital.system.exception.user.UserNotAllowedException;
import com.PasseDigital.system.exception.user.UserNotFoundException;
import com.PasseDigital.system.model.dto.request.user.UserChangePasswordRequest;
import com.PasseDigital.system.model.dto.request.user.UserLoginRequest;
import com.PasseDigital.system.model.dto.response.user.StudentClassResponse;
import com.PasseDigital.system.model.dto.response.user.StudentEducationResponse;
import com.PasseDigital.system.model.dto.response.user.StudentResponse;
import com.PasseDigital.system.model.dto.response.user.UserLoginResponse;
import com.PasseDigital.system.model.entity.codeEmail.CodeEmail;
import com.PasseDigital.system.model.entity.user.User;
import com.PasseDigital.system.model.factory.UserStudentClassFactory;
import com.PasseDigital.system.model.factory.UserStudentEducationFactory;
import com.PasseDigital.system.model.repository.codeEmail.CodeEmailRepository;
import com.PasseDigital.system.model.repository.user.UserRepository;
import com.PasseDigital.system.model.roles.user.UserEnum;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenConfig tokenConfig;
    private final AuthVerifyService authVerifyService;
    private final CodeEmailRepository codeEmailRepository;
    private final UserStudentClassFactory userStudentClassFactory;
    private final UserStudentEducationFactory userStudentEducationFactory;
    private final AuthenticationManager authenticationManager;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenConfig tokenConfig, AuthVerifyService authVerifyService, CodeEmailRepository codeEmailRepository, UserStudentClassFactory userStudentClassFactory, UserStudentEducationFactory userStudentEducationFactory, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenConfig = tokenConfig;
        this.authVerifyService = authVerifyService;
        this.codeEmailRepository = codeEmailRepository;
        this.userStudentClassFactory = userStudentClassFactory;
        this.userStudentEducationFactory = userStudentEducationFactory;
        this.authenticationManager = authenticationManager;
    }


    // publica para ambos
    public UserLoginResponse login(UserLoginRequest request){

        if(userRepository.findByRegistration(request.registration()).isEmpty()){
            throw new UserNotFoundException();
        }

        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.registration(), request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);
        User user = (User) authentication.getPrincipal();

        String token = tokenConfig.generateToken(user);
        return new UserLoginResponse(token, user.getUserEnum());

    }

    // secretary / admin
    @Transactional
    public void deleteUser(String registration){
        if(userRepository.findByRegistration(registration).isEmpty()){
            throw new UserNotFoundException();
        }

        userRepository.deleteUserByRegistration(registration);
    }

    // student/admin/secretary
    public StudentResponse getInfoStudent(String registration){
        User student = userRepository.findByRegistration(registration).orElseThrow(UserNotFoundException::new);

        if(!student.getUserEnum().equals(UserEnum.STUDENT)){
            throw new UserNotAllowedException();
        }

        return new StudentResponse(
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
        );
    }

    // public
    @Transactional
    public void changePassword(UserChangePasswordRequest request){
        CodeEmail codeEmail = codeEmailRepository.findByTempToken(request.token())
                .orElseThrow(CodeEmailTempTokenNotFoundException::new);

        if(codeEmail.getUserCanChangePassword() != true){
            throw new CodeEmailTempTokenInvalidException();
        }

        if(codeEmail.getExpirationToken().isBefore(LocalDateTime.now())){
            codeEmail.setUserCanChangePassword(false);
            codeEmail.setTempToken(null);
            codeEmailRepository.save(codeEmail);
            throw new CodeEmailTempTokenInvalidException();
        }

        codeEmail.setUserCanChangePassword(false);
        codeEmail.setTempToken(null);
        codeEmail.setExpirationToken(null);

        User user = codeEmail.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);

    }

}
