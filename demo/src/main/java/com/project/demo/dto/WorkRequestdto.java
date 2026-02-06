package com.project.demo.dto;

import com.project.demo.entity.ApprovalHistory;
import com.project.demo.entity.User;
import com.project.demo.entity.WorkFlowRequest;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkRequestdto {
    private Integer id;

    private String title;

    private String description;

    private Integer requesterId;

    private String requesterName;

    private String requesterEmail;

    private String role;

    private String currentStatus;

    private List<ApprovalHistorydto> approval;

    private LocalDateTime createdAt;



//    private List<WorkFlowRequest> workFlowRequest;
}


