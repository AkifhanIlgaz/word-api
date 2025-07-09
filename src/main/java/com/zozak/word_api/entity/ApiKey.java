package com.zozak.word_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "api_keys")
public class ApiKey  extends BaseEntity{
    @Column(nullable = false, unique = true, name = "key")
    private String key;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true) // user_id foreign key
    private User user;

    @Column(nullable = false, name = "total_usage")
    private int totalUsage;
}
