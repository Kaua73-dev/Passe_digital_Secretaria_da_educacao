package com.PasseDigital.system.auth;


import com.PasseDigital.system.model.entity.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthVerifyService {

    public User getAuthenticate(){
        return (User) Objects.requireNonNull(Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getPrincipal()
        );
    }


}
