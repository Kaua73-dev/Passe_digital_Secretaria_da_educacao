package com.PasseDigital.system.model.entity.user;


import com.PasseDigital.system.model.entity.user.flyweight.UserStudentClass;
import com.PasseDigital.system.model.entity.user.flyweight.UserStudentEducation;
import com.PasseDigital.system.model.roles.user.UserEnum;
import com.PasseDigital.system.model.roles.user.UserStudentShiftEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500, nullable = false)
    private String name;

    @Column(length = 550, unique = true, nullable = false)
    private String email;

    @Column(length = 7, unique = true, nullable = false)
    private String registration;

    @Column(length = 550, nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "student_education_id")
    private UserStudentEducation userStudentEducation; // etapa

    @ManyToOne
    @JoinColumn(name = "student_class_id")
    private UserStudentClass userStudentClass; // turma

    @Column(nullable = false)
    private LocalDate birth;

    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    private UserEnum userEnum;

    @Enumerated(EnumType.STRING)
    private UserStudentShiftEnum userStudentShiftEnum;


    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userEnum));
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    @NonNull
    public String getUsername() {
        return registration;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }


}
