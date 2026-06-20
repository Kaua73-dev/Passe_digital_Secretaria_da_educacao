package com.PasseDigital.system.model.repository.user;


import com.PasseDigital.system.model.entity.user.flyweight.UserStudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStudentClassRepository extends JpaRepository<UserStudentClass, Integer> {

    Optional<UserStudentClass> findByStudentClass(String studentClass);

}
