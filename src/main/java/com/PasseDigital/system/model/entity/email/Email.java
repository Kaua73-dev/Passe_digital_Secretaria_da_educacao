package com.PasseDigital.system.model.entity.email;


import com.PasseDigital.system.model.entity.user.User;
import com.PasseDigital.system.model.roles.email.EmailStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="email_log")
public class Email {

    // entity apenas para auditorias

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String recipient;

    private LocalDateTime sendAt;

    private String errorMessage;

    @Enumerated(EnumType.STRING)
    private EmailStatusEnum emailStatusEnum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
