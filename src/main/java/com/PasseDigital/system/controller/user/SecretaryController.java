package com.PasseDigital.system.controller.user;

import com.PasseDigital.system.model.dto.request.user.StudentRegisterRequest;
import com.PasseDigital.system.model.dto.request.user.StudentUpdateRequest;
import com.PasseDigital.system.model.dto.response.user.StudentResponse;
import com.PasseDigital.system.service.user.SecretaryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secretary")
public class SecretaryController {

    private final SecretaryService secretaryService;

    public SecretaryController(SecretaryService secretaryService) {
        this.secretaryService = secretaryService;
    }

    @PostMapping("/student/register")
    public StudentResponse studentRegister(@RequestBody StudentRegisterRequest request){
        return secretaryService.studentRegister(request);
    }

    @PutMapping("/student/{studentId}")
    public StudentResponse updateStudent(@PathVariable Integer studentId, @RequestBody StudentUpdateRequest request){
        return secretaryService.updateStudent(studentId, request);
    }

}
