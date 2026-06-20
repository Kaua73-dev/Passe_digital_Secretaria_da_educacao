package com.PasseDigital.system.model.repository.user;


import com.PasseDigital.system.model.entity.user.flyweight.UserStudentEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStudentEducationRepository extends JpaRepository<UserStudentEducation, Integer> {

    Optional<UserStudentEducation> findByStudentEducation(String studentEducation);


}
