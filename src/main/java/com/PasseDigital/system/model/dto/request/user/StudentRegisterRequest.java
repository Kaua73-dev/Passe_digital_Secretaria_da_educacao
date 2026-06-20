package com.PasseDigital.system.model.dto.request.user;


import com.PasseDigital.system.model.roles.user.UserStudentShiftEnum;

import java.time.LocalDate;

public record StudentRegisterRequest(String name,
                                     String email,
                                     String registration,
                                     String password,
                                     StudentClassRequest studentClass,
                                     StudentEducationRequest studentEducation,
                                     LocalDate birth,
                                     UserStudentShiftEnum userStudentShiftEnum) {
}
