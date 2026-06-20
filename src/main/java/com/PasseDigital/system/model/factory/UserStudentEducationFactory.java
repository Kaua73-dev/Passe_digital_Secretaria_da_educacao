package com.PasseDigital.system.model.factory;


import com.PasseDigital.system.model.dto.request.user.StudentEducationRequest;
import com.PasseDigital.system.model.entity.user.flyweight.UserStudentEducation;
import com.PasseDigital.system.model.repository.user.UserStudentEducationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
@Slf4j
public class UserStudentEducationFactory {

    private final UserStudentEducationRepository userStudentEducationRepository;
    private final Map<StudentEducationRequest, UserStudentEducation> cache = new HashMap<>();

    public UserStudentEducationFactory(UserStudentEducationRepository userStudentEducationRepository) {
        this.userStudentEducationRepository = userStudentEducationRepository;
    }


    public UserStudentEducation getStudentEducation(StudentEducationRequest request){

        UserStudentEducation student = cache.get(request);

        if(student == null){
            student = userStudentEducationRepository.findByEducation(request.studentEducation()).orElseGet(() -> {
                UserStudentEducation newClass = new UserStudentEducation();
                newClass.setEducation(request.studentEducation());

                return userStudentEducationRepository.save(newClass);
            });

            cache.put(request, student);
        }
        return student;
    }



}
