package com.PasseDigital.system.service.user;

import com.PasseDigital.system.config.TokenConfig;
import com.PasseDigital.system.exception.user.UserNotAllowedException;
import com.PasseDigital.system.exception.user.UserNotFoundException;
import com.PasseDigital.system.model.dto.request.user.UserLoginRequest;
import com.PasseDigital.system.model.dto.response.user.StudentResponse;
import com.PasseDigital.system.model.dto.response.user.UserLoginResponse;
import com.PasseDigital.system.model.entity.User;
import com.PasseDigital.system.model.repository.UserRepository;
import com.PasseDigital.system.model.roles.user.UserEnum;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenConfig tokenConfig;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenConfig tokenConfig, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenConfig = tokenConfig;
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
                student.getStudentClass(),
                student.getEducation(),
                student.getBirth(),
                student.getUserStudentShiftEnum()
        );
    }


}
