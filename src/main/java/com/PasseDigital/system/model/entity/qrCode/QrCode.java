package com.PasseDigital.system.model.entity.qrCode;


import com.PasseDigital.system.model.entity.user.User;
import com.PasseDigital.system.model.roles.qrCode.QrCodeStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="qr_code")
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    private LocalDateTime createAt;

    private LocalDateTime expirationAt;

    private LocalDateTime validateAt;

    @Enumerated(EnumType.STRING)
    private QrCodeStatusEnum qrCodeStatusEnum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}