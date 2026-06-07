package com.PasseDigital.system.controller.user;


import com.PasseDigital.system.model.dto.request.user.UserChangePasswordRequest;
import com.PasseDigital.system.model.dto.request.user.UserLoginRequest;
import com.PasseDigital.system.model.dto.response.user.StudentResponse;
import com.PasseDigital.system.model.dto.response.user.UserLoginResponse;
import com.PasseDigital.system.service.user.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginRequest request){
        return userService.login(request);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestBody String registration){
        userService.deleteUser(registration);
    }

    @GetMapping("/student")
    public StudentResponse getInfoStudent(@RequestBody String registration){
        return userService.getInfoStudent(registration);
    }

    @PutMapping("/password")
    public void changePassword(@RequestBody UserChangePasswordRequest request){
        userService.changePassword(request);
    }

}
