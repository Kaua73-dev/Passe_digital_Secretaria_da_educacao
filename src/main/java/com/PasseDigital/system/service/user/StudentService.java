package com.PasseDigital.system.service.user;

import com.PasseDigital.system.config.TokenConfig;
import com.PasseDigital.system.model.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class StudentService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenConfig tokenConfig;
    private final AuthenticationManager authenticationManager;


    public StudentService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenConfig tokenConfig, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenConfig = tokenConfig;
        this.authenticationManager = authenticationManager;
    }


}
