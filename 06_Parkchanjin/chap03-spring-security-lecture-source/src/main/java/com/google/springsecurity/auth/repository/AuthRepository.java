package com.google.springsecurity.auth.repository;

import com.google.springsecurity.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<RefreshToken, String> {
  RefreshToken token(String token);
}
