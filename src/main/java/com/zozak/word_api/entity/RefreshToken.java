package com.zozak.word_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
@Data
public class RefreshToken extends BaseEntity{

    @Column(nullable = false, unique = true, name = "token")
    private String token;

    @Column(nullable = false, updatable = false, name = "expires_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date expiresAt;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
