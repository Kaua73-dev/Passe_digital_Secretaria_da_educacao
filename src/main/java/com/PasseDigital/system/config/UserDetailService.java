package com.PasseDigital.system.config;

import com.PasseDigital.system.exception.user.UserNotFoundException;
import com.PasseDigital.system.model.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String registration) throws UserNotFoundException {
        return userRepository.findByRegistration(registration).orElseThrow(UserNotFoundException::new);
    }
}
