package com.PasseDigital.system.model.dto.response.user;

import com.PasseDigital.system.model.roles.user.UserStudentShiftEnum;

import java.time.LocalDate;

public record StudentRegisterResponse(String name,
                                      String email,
                                      String registration,
                                      String studentClass,
                                      String education,
                                      LocalDate birth,
                                      UserStudentShiftEnum userStudentShiftEnum) {
}
