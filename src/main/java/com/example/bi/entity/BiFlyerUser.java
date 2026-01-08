package com.example.bi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;


@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "bi_flyer_user")
public class BiFlyerUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bi_flyer_user_seq")
    @SequenceGenerator(name = "bi_flyer_user_seq", sequenceName = "bi_flyer_user_seq", allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "keycloak_user_id", unique = true)
    private String keycloakUserId;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
}