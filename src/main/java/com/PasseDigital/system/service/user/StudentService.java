package com.PasseDigital.system.service.user;


import com.PasseDigital.system.model.repository.user.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class StudentService {

    private final UserRepository userRepository;

    public StudentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // pensar no que fazer para student


}
