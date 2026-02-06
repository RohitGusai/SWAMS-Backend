package com.project.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApprovalHistorydto
{
    private Integer id;
    private String approverName;

    private String action;
    private String comment;
    private LocalDateTime actionDate;
}
