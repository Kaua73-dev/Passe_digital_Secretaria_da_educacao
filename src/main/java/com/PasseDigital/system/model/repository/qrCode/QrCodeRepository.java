package com.PasseDigital.system.model.repository.qrCode;

import com.PasseDigital.system.model.entity.qrCode.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, Integer> {

    Optional<QrCode> findByContent(String content);

}
