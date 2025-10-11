package com.cluvy.user.repository;

import com.cluvy.user.entity.User;
import com.cluvy.user.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findBySocialId(String socialId);
    Optional<User> findByIdAndStatus(Long id, Status status);
    Optional<User> findBySocialIdAndStatus(String socialId, Status status);
}
