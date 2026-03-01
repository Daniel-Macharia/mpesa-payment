package com.tribesystems.payment.auth.repository;

import com.tribesystems.payment.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    @NativeQuery(
            value = " UPDATE user " +
                    " SET password = :newPassword " +
                    " WHERE username = :userName "
    )
    User updateUserPassword(@Param("userName") String userName,
                            @Param("newPassword") String newPassword);
}
