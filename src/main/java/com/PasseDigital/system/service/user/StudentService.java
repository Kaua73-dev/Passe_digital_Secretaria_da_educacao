package com.PasseDigital.system.service.user;


import com.PasseDigital.system.exception.user.UserNotFoundException;
import com.PasseDigital.system.model.dto.response.user.StudentResponse;
import com.PasseDigital.system.model.entity.User;
import com.PasseDigital.system.model.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class StudentService {

    private final UserRepository userRepository;

    public StudentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




}
