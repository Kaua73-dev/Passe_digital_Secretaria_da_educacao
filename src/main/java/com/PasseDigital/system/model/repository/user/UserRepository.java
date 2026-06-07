package com.PasseDigital.system.model.repository.user;



import com.PasseDigital.system.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByRegistration(String registration);

    void deleteUserByRegistration(String registration);


}
