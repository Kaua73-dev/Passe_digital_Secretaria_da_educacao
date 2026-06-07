package com.PasseDigital.system.model.repository.email;

import com.PasseDigital.system.model.entity.email.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Integer> {
}
