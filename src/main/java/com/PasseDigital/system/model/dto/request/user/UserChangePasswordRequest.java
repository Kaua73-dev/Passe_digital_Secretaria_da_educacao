package com.PasseDigital.system.model.dto.request.user;

public record UserChangePasswordRequest(String token, String newPassword) {
}
