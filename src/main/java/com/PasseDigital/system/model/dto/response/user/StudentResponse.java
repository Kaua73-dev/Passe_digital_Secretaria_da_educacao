package com.PasseDigital.system.model.dto.response.user;

import com.PasseDigital.system.model.entity.user.flyweight.UserStudentClass;
import com.PasseDigital.system.model.entity.user.flyweight.UserStudentEducation;
import com.PasseDigital.system.model.roles.user.UserStudentShiftEnum;

import java.time.LocalDate;

public record StudentResponse(String name,
                              String email,
                              String registration,
                              StudentClassResponse studentClass,
                              StudentEducationResponse studentEducation,
                              LocalDate birth,
                              UserStudentShiftEnum userStudentShiftEnum) {
}
