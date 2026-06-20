package com.PasseDigital.system.model.entity.user.flyweight;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name="user_student_class")
public class UserStudentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 2, unique = true)
    private String studentClass; // turma -- flyweight





}
