package com.PasseDigital.system.service.user;


import com.PasseDigital.system.config.TokenConfig;
import com.PasseDigital.system.exception.user.StudentAlreadyExistException;
import com.PasseDigital.system.exception.user.UserNotAllowedException;
import com.PasseDigital.system.exception.user.UserNotFoundException;
import com.PasseDigital.system.model.dto.request.user.StudentRegisterRequest;
import com.PasseDigital.system.model.dto.request.user.StudentUpdateRequest;
import com.PasseDigital.system.model.dto.response.user.StudentRegisterResponse;
import com.PasseDigital.system.model.dto.response.user.StudentUpdateResponse;
import com.PasseDigital.system.model.entity.User;
import com.PasseDigital.system.model.repository.UserRepository;
import com.PasseDigital.system.model.roles.user.UserEnum;
import jakarta.transaction.Transactional;
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

    // apenas adm / secretary
    @Transactional
    public StudentUpdateResponse updateStudent(Integer studentId, StudentUpdateRequest request){

        User student = userRepository.findById(studentId).orElseThrow(UserNotFoundException::new);

        if(!student.getUserEnum().equals(UserEnum.STUDENT)){
            throw new UserNotAllowedException();
        }

        student.setName(request.name());
        student.setEmail(request.email());
        student.setRegistration(request.registration());
        student.setStudentClass(request.studentClass());
        student.setEducation(request.education());
        student.setBirth(request.birth());
        student.setUserStudentShiftEnum(request.userStudentShiftEnum());

        User studentUpdate = userRepository.save(student);

        return new StudentUpdateResponse(
          studentUpdate.getName(),
          studentUpdate.getEmail(),
          studentUpdate.getRegistration(),
          studentUpdate.getStudentClass(),
          studentUpdate.getEducation(),
          studentUpdate.getBirth(),
          studentUpdate.getUserStudentShiftEnum()

        );

    }

}
