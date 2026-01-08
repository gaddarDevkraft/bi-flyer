package com.example.bi.repo;

import com.example.bi.entity.BiFlyerUser;
import com.example.bi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<BiFlyerUser, Long> {
    Optional<BiFlyerUser> findByEmail(String email);
}
