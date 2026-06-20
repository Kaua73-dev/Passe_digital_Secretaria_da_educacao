package com.PasseDigital.system.model.factory;


import com.PasseDigital.system.model.dto.request.user.StudentClassRequest;
import com.PasseDigital.system.model.entity.user.flyweight.UserStudentClass;
import com.PasseDigital.system.model.repository.user.UserStudentClassRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class UserStudentClassFactory {

    private final UserStudentClassRepository userStudentClassRepository;
    private final Map<StudentClassRequest, UserStudentClass> cache = new HashMap<>();


    public UserStudentClassFactory(UserStudentClassRepository userStudentClassRepository) {
        this.userStudentClassRepository = userStudentClassRepository;
    }

    public UserStudentClass getStudentClass(StudentClassRequest request){

        UserStudentClass student = cache.get(request);

        if(student == null){
            student = userStudentClassRepository.findByStudentClass(request.studentClass()).orElseThrow();
            cache.put(request, student);
        }

        return student;

    }




}
