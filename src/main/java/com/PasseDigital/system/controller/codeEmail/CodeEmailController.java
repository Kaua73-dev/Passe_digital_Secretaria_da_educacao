package com.PasseDigital.system.controller.codeEmail;


import com.PasseDigital.system.model.dto.request.codeEmailRequest.CodeEmailRequest;
import com.PasseDigital.system.model.dto.response.codeEmail.CodeEmailValidateCodeResponse;
import com.PasseDigital.system.service.codeEmail.CodeEmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code")
public class CodeEmailController {

    private final CodeEmailService codeEmailService;

    public CodeEmailController(CodeEmailService codeEmailService) {
        this.codeEmailService = codeEmailService;
    }

    @PostMapping("/validate")
    public CodeEmailValidateCodeResponse validateCode(@RequestBody CodeEmailRequest request){
        return codeEmailService.validateCode(request);
    }



}
