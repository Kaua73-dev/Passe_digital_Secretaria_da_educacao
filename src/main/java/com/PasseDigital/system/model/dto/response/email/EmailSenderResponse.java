package com.PasseDigital.system.model.dto.response.email;

import com.PasseDigital.system.model.roles.email.EmailStatusEnum;

import java.time.LocalDateTime;

public record EmailSenderResponse( Integer id,
                                   String recipient,
                                   EmailStatusEnum status,
                                   LocalDateTime sentAt) {
}
