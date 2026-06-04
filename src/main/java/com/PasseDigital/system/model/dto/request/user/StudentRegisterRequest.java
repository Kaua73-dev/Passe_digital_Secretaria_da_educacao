package com.PasseDigital.system.model.dto.request.user;

import com.PasseDigital.system.model.roles.user.UserEnum;
import com.PasseDigital.system.model.roles.user.UserStudentShiftEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StudentRegisterRequest(String name,
                                     String email,
                                     String registration,
                                     String password,
                                     String studentClass,
                                     String education,
                                     LocalDate birth,
                                     LocalDateTime createAt,
                                     UserStudentShiftEnum userStudentShiftEnum) {
}
