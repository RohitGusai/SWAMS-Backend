package com.project.demo.entity;

import com.project.demo.enums.ResStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "workflow")
public class WorkFlowRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable=false)
    private User requester;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ResStatus action = ResStatus.PENDING;

    @OneToMany(mappedBy = "workFlowRequest")
    private List<ApprovalHistory> approval = new ArrayList<>();

    @PrePersist
    public void onCreate()
    {
        this.createdAt = LocalDateTime.now();
    }

}
