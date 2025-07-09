package com.zozak.word_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseEntity{

    @Column(nullable = false, unique = true, name = "token")
    private String token;

    @Column(nullable = false, updatable = false, name = "expires_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date expiresAt;

    @OneToOne()
    private User user;
}
