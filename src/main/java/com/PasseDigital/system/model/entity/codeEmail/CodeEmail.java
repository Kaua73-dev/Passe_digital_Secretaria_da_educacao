package com.PasseDigital.system.model.entity.codeEmail;

import com.PasseDigital.system.model.entity.user.User;
import com.PasseDigital.system.model.roles.codeEmail.CodeEmailEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@Table(name = "code_email")
public class CodeEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private LocalDateTime createAt;

    private LocalDateTime expirationAt;

    private Boolean userCanChangePassword;

    private String tempToken;

    private LocalDateTime expirationToken;

    @Enumerated(EnumType.STRING)
    private CodeEmailEnum codeEmailEnum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
