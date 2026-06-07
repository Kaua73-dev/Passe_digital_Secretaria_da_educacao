package com.PasseDigital.system.model.repository.codeEmail;

import com.PasseDigital.system.model.entity.codeEmail.CodeEmail;
import com.PasseDigital.system.model.entity.user.User;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeEmailRepository extends JpaRepository<CodeEmail, Integer> {

    Optional<CodeEmail> findByCode(String code);
    Optional<CodeEmail> findByTempToken(String tempToken);
    Optional<CodeEmail> findTopByUserOrderByCreatedAtDesc(User user);

}
