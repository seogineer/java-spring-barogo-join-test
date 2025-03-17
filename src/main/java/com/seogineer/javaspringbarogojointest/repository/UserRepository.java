package com.seogineer.javaspringbarogojointest.repository;

import com.seogineer.javaspringbarogojointest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
