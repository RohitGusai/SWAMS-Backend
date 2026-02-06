package com.project.demo.entity;

import com.project.demo.enums.ResStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;

@Builder
@Entity
@Data
@Table(name="approval_history")
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="workFlowId", nullable = false)
    private WorkFlowRequest workFlowRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="approverId",nullable=false)
    private User approver;

    @Enumerated(EnumType.STRING)
    private ResStatus action;



    private LocalDateTime actionDate;

    @PrePersist
    protected void actionTake()
    {
        this.actionDate = LocalDateTime.now();
    }

}
