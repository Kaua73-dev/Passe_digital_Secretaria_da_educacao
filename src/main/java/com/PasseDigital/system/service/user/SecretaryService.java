package com.PasseDigital.system.service.user;


import com.PasseDigital.system.exception.user.StudentAlreadyExistException;
import com.PasseDigital.system.exception.user.UserNotAllowedException;
import com.PasseDigital.system.exception.user.UserNotFoundException;
import com.PasseDigital.system.model.dto.request.user.StudentRegisterRequest;
import com.PasseDigital.system.model.dto.request.user.StudentUpdateRequest;
import com.PasseDigital.system.model.dto.response.user.StudentClassResponse;
import com.PasseDigital.system.model.dto.response.user.StudentEducationResponse;
import com.PasseDigital.system.model.dto.response.user.StudentResponse;
import com.PasseDigital.system.model.entity.user.User;
import com.PasseDigital.system.model.entity.user.flyweight.UserStudentClass;
import com.PasseDigital.system.model.entity.user.flyweight.UserStudentEducation;
import com.PasseDigital.system.model.factory.UserStudentClassFactory;
import com.PasseDigital.system.model.factory.UserStudentEducationFactory;
import com.PasseDigital.system.model.repository.user.UserRepository;
import com.PasseDigital.system.model.roles.user.UserEnum;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SecretaryService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserStudentClassFactory userStudentClassFactory;
    private final UserStudentEducationFactory userStudentEducationFactory;


    public SecretaryService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserStudentClassFactory userStudentClassFactory, UserStudentEducationFactory userStudentEducationFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userStudentClassFactory = userStudentClassFactory;
        this.userStudentEducationFactory = userStudentEducationFactory;
    }


    private StudentResponse studentResponse(User user){
        return new StudentResponse(
                user.getName(),
                user.getEmail(),
                user.getRegistration(),

                new StudentClassResponse(
                        user.getUserStudentClass().getStudentClass()
                ),

                new StudentEducationResponse(
                        user.getUserStudentEducation().getEducation()
                ),

                user.getBirth(),
                user.getUserStudentShiftEnum()
        );
    }



    public StudentResponse studentRegister(StudentRegisterRequest request){

        if(userRepository.findByRegistration(request.registration()).isPresent()){
            throw new StudentAlreadyExistException();
        }


        UserStudentClass studentClass = userStudentClassFactory.getStudentClass(request.studentClass());
        UserStudentEducation studentEducation = userStudentEducationFactory.getStudentEducation(request.studentEducation());

        User student = new User();
        student.setName(request.name());
        student.setEmail(request.email());
        student.setRegistration(request.registration());
        student.setPassword(passwordEncoder.encode(request.password()));
        student.setUserStudentClass(studentClass);
        student.setUserStudentEducation(studentEducation);
        student.setBirth(request.birth());
        student.setUserStudentShiftEnum(request.userStudentShiftEnum());


        student.setCreateAt(LocalDateTime.now());
        student.setUserEnum(UserEnum.STUDENT);

        User studentSaved = userRepository.save(student);

        return studentResponse(studentSaved);

    }

    @Transactional
    public StudentResponse updateStudent(Integer studentId, StudentUpdateRequest request){

        User student = userRepository.findById(studentId)
                .orElseThrow(UserNotFoundException::new);

        if(!student.getUserEnum().equals(UserEnum.STUDENT)){
            throw new UserNotAllowedException();
        }

        UserStudentClass studentClass = userStudentClassFactory.getStudentClass(request.studentClass());
        UserStudentEducation studentEducation = userStudentEducationFactory.getStudentEducation(request.education());

        student.setName(request.name());
        student.setEmail(request.email());
        student.setRegistration(request.registration());
        student.setUserStudentClass(studentClass);
        student.setUserStudentEducation(studentEducation);
        student.setBirth(request.birth());
        student.setUserStudentShiftEnum(request.userStudentShiftEnum());

        User studentUpdate = userRepository.save(student);

        return studentResponse(studentUpdate);

    }

}
