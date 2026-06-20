package com.PasseDigital.system.model.dto.request.user;

import com.PasseDigital.system.model.roles.user.UserStudentShiftEnum;

import java.time.LocalDate;

public record StudentUpdateRequest(String name,
                                   String email,
                                   String registration,
                                   StudentClassRequest studentClass,
                                   StudentEducationRequest education,
                                   LocalDate birth,
                                   UserStudentShiftEnum userStudentShiftEnum ) {
}