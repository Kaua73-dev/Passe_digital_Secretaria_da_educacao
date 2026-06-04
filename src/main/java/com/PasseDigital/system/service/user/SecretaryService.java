package com.PasseDigital.system.service.user;


import com.PasseDigital.system.config.TokenConfig;
import com.PasseDigital.system.exception.user.StudentAlreadyExistException;
import com.PasseDigital.system.exception.user.UserNotFoundException;
import com.PasseDigital.system.model.dto.request.user.StudentRegisterRequest;
import com.PasseDigital.system.model.dto.response.user.StudentRegisterResponse;
import com.PasseDigital.system.model.entity.User;
import com.PasseDigital.system.model.repository.UserRepository;
import com.PasseDigital.system.model.roles.user.UserEnum;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SecretaryService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SecretaryService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // apenas adm / secretary
    public StudentRegisterResponse studentRegister(StudentRegisterRequest request){

        if(userRepository.findByRegistration(request.registration()).isPresent()){
            throw new StudentAlreadyExistException();
        }

        User student = new User();
        student.setName(request.name());
        student.setEmail(request.email());
        student.setPassword(passwordEncoder.encode(request.password()));
        student.setStudentClass(request.studentClass());
        student.setEducation(request.education());
        student.setBirth(request.birth());

        student.setCreateAt(LocalDateTime.now());
        student.setUserEnum(UserEnum.STUDENT);
        student.setUserStudentShiftEnum(request.userStudentShiftEnum());

        User studentSaved = userRepository.save(student);


        return new StudentRegisterResponse(
                studentSaved.getName(),
                studentSaved.getEmail(),
                studentSaved.getRegistration(),
                studentSaved.getStudentClass(),
                studentSaved.getEducation(),
                studentSaved.getBirth(),
                studentSaved.getUserStudentShiftEnum()
        );

    }







}
