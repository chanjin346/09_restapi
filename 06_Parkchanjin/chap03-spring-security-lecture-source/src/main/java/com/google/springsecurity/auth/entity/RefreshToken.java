package com.google.springsecurity.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

  @Id
  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String token;

  @Column(nullable = false)
  private Date expiryDate;

}
