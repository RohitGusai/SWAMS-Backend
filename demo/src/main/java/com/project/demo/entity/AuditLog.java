package com.project.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name="audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String action;

    private String entityName;

    private String entityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="performId", nullable = false)
    private User performed;

    @Column(columnDefinition = "TEXT")
    private String details;

    private LocalDateTime timestamp;

    @PrePersist
    protected void onLog() {
        this.timestamp = LocalDateTime.now();
    }

}
