package com.example.bi.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_name", nullable = false, unique = true, length = 100)
    private String roleName;

    @Column(name = "display_name", nullable = false, unique = true, length = 150)
    private String displayName;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "creation_ts", updatable = false)
    @CreationTimestamp
    private LocalDateTime creationTs;

    @Column(name = "lastmodified_ts")
    @UpdateTimestamp
    private LocalDateTime lastModifiedTs;

    @ManyToOne
    @JoinColumn(name = "lastmodified_by", foreignKey = @ForeignKey(name = "fk_roles_users"))
    private BiFlyerUser lastModifiedBy;
}
