package com.PasseDigital.system.model.dto.response.qrCode;

import com.PasseDigital.system.model.dto.response.user.StudentResponse;
import com.PasseDigital.system.model.roles.qrCode.QrCodeStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record QrCodeValidResponse(LocalDateTime validateAt, QrCodeStatusEnum qrCodeStatusEnum, StudentResponse studentResponse) {
}
