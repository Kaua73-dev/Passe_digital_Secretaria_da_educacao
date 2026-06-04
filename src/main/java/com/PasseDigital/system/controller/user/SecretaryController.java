package com.PasseDigital.system.controller.user;

import com.PasseDigital.system.model.dto.request.user.StudentRegisterRequest;
import com.PasseDigital.system.model.dto.response.user.StudentRegisterResponse;
import com.PasseDigital.system.service.user.SecretaryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secretary")
public class SecretaryController {

    private final SecretaryService secretaryService;

    public SecretaryController(SecretaryService secretaryService) {
        this.secretaryService = secretaryService;
    }

    @PostMapping("/student/register")
    public StudentRegisterResponse studentRegister(@RequestBody StudentRegisterRequest request){
        return secretaryService.studentRegister(request);
    }


}
