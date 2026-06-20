package com.PasseDigital.system.model.factory;


import com.PasseDigital.system.model.dto.request.user.StudentEducationRequest;
import com.PasseDigital.system.model.entity.user.flyweight.UserStudentEducation;
import com.PasseDigital.system.model.repository.user.UserStudentEducationRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserStudentEducationFactory {

    private final UserStudentEducationRepository userStudentEducationRepository;
    private final Map<StudentEducationRequest, UserStudentEducation> cache = new HashMap<>();

    public UserStudentEducationFactory(UserStudentEducationRepository userStudentEducationRepository) {
        this.userStudentEducationRepository = userStudentEducationRepository;
    }


    public UserStudentEducation getStudentEducation(StudentEducationRequest request){

        UserStudentEducation student = cache.get(request);

        if(student == null){
            student = userStudentEducationRepository.findByStudentEducation(request.studentEducation())
                .orElseThrow();

            cache.put(request, student);
        }

        return student;
    }



}
