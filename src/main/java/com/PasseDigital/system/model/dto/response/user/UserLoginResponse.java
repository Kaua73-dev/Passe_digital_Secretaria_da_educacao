package com.PasseDigital.system.model.dto.response.user;

import com.PasseDigital.system.model.roles.user.UserEnum;

public record UserLoginResponse(String token, UserEnum userEnum) {
}
