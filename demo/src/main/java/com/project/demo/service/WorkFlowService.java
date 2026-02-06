package com.project.demo.service;

import com.project.demo.dto.ApprovalHistorydto;
import com.project.demo.dto.WorkRequestdto;
import com.project.demo.entity.ApprovalHistory;
import com.project.demo.entity.AuditLog;
import com.project.demo.entity.User;
import com.project.demo.entity.WorkFlowRequest;
import com.project.demo.enums.ResStatus;
import com.project.demo.repository.ApprovalRepository;
import com.project.demo.repository.AuditRepository;
import com.project.demo.repository.UserRepository;
import com.project.demo.repository.WorkFlowRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkFlowService {

    @Autowired
    private ApprovalRepository approvalRepository;
    @Autowired
    private WorkFlowRepository workFlowRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private NotificationService notificationService;



    @Transactional
    public WorkRequestdto postWorkFlow(WorkFlowRequest request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User newData = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Hello");
        request.setRequester(newData);
        request.setAction(ResStatus.PENDING); //need to check this


        WorkFlowRequest savedRequest = workFlowRepository.save(request);
        AuditLog audit = AuditLog.builder()
                .action("Create")
                .entityName("WorkFlowRequest")
                .details("Created Workflow" + savedRequest.getTitle())
                .performed(newData)
                .build();

        auditRepository.save(audit);

        return mapDto(savedRequest);


    }

    public WorkRequestdto mapDto(WorkFlowRequest request) {

        List<ApprovalHistorydto> historyDto = new ArrayList<>();
        if(request.getApproval()!=null)
        {
            historyDto = request.getApproval().stream()
                    .map(h-> ApprovalHistorydto.builder()
                            .id(h.getId())
                            .comment(h.getComment())
                            .action(h.getAction().toString())
                            .approverName(h.getApprover().getName())
                            .actionDate(h.getActionDate())
                            .build())
                    .toList();

        }

        return WorkRequestdto.builder()
                .id(request.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .requesterId(request.getRequester().getId())
                .requesterEmail(request.getRequester().getEmail())
                .requesterName(request.getRequester().getName())
                .currentStatus(request.getAction().toString())
                .createdAt(request.getCreatedAt())
                .approval(historyDto)
                .build();

//        workFlowRepository.save(work);

    }

    @Transactional
    public WorkRequestdto getData(Integer id) {
        WorkFlowRequest request = workFlowRepository.findById(id).orElseThrow(() -> new RuntimeException("No Data is avaiable"));
        WorkRequestdto work = WorkRequestdto.builder()
                .id(request.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .createdAt(request.getCreatedAt())
                .requesterId(request.getRequester().getId())
                .requesterName(request.getRequester().getName())
                .requesterEmail(request.getRequester().getEmail())
                .build();
        return work;
    }


    @Transactional
    public WorkRequestdto approveReq(Integer id, String comments) {
        WorkFlowRequest work = workFlowRepository.findById(id).orElseThrow(() -> new RuntimeException("WorkFlow not Found"));
        if(work.getAction() == ResStatus.APPROVED)
        {
            throw new RuntimeException("This request is already approved");
        }
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        User approver = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not Found"));


        work.setAction(ResStatus.APPROVED);
//        work.setRequester(approver);

        WorkFlowRequest request = workFlowRepository.save(work);

        ApprovalHistory history = ApprovalHistory.builder()
                .workFlowRequest(request)
                .approver(approver)
                .action(ResStatus.APPROVED)
                .comment(comments)
                .build();
        approvalRepository.save(history);

        AuditLog audit = AuditLog.builder()
                .action("Approve_Action")
                .performed(approver)
                .entityName("WorkFlowRequest")
                .entityId(work.getId().toString())
                .details(work.getTitle())
                .build();

        auditRepository.save(audit);

        notificationService.setNotify(request,work.getAction(),comments);



        return mapDt(request,comments);
    }

    public WorkRequestdto mapDt(WorkFlowRequest request,String comments) {

        List<ApprovalHistorydto> historyDto = request.getApproval().stream().map(h-> ApprovalHistorydto.builder()
                        .id(h.getId())
                        .action(h.getAction().toString())
                .approverName(h.getApprover().getName())
                        .actionDate(h.getActionDate())
                .comment(comments)
                .build())
                        .toList();



        return WorkRequestdto
                .builder()
                .id(request.getId())
                .createdAt(request.getCreatedAt())
                .requesterId(request.getRequester().getId())
                .requesterName(request.getRequester().getName())
                .role(request.getRequester().getRole().getRoleName())
                .requesterEmail(request.getRequester().getEmail())
                .description(request.getDescription())
                .approval(historyDto)
                .title(request.getTitle())

                .currentStatus(request.getAction().toString())
                .build();
    }

    @Transactional
    public WorkRequestdto getStatus(Integer workFlowId) {
        WorkFlowRequest work = workFlowRepository.findById(workFlowId).orElseThrow(() -> new RuntimeException("Workflow is not found"));
        System.out.println("Hello everybody");
        List<ApprovalHistorydto> historyDto = work.getApproval() == null ? List.of() : work.getApproval().stream().map(h-> ApprovalHistorydto.builder()
                        .id(h.getId())
                        .comment(h.getComment())
                        .action(h.getAction() != null ? h.getAction().toString() : "PENDING")
                        .actionDate(h.getActionDate())
                        .approverName(h.getApprover().getName())
                        .build()).toList();

//        System.out.println("Approval size = " +
//                (work.getApproval() != null ? work.getApproval().size() : 0));
//
//        System.out.println("Approval = " + work.getApproval());
//        System.out.println("Role = " + work.getRequester().getRole().getRoleName());
//        System.out.println("History DTO size = " + historyDto.size());


        return WorkRequestdto.builder()
                .id(work.getId())
                .requesterId(work.getRequester().getId())
                .currentStatus(work.getAction() != null ? work.getAction().toString() : "PENDING")
                .createdAt(work.getCreatedAt())
                .description(work.getDescription())
                .title(work.getTitle())
                .requesterEmail(work.getRequester().getEmail())
                .requesterName(work.getRequester().getName())
                .role(work.getRequester().getRole().getRoleName())
                .approval(historyDto)
                .build();

    }

    @Transactional
    public WorkRequestdto rejectReq(Integer id, String comment)
    {
        WorkFlowRequest work = workFlowRepository.findById(id).orElseThrow(()-> new RuntimeException("Workflow id not found"));
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        User reject = userRepository.findByName(username).orElseThrow(()-> new RuntimeException("User not found"));

        if(work.getAction().toString().equals("REJECTED"))
        {
            throw new RuntimeException("Already Rejected");
        }
        System.out.println("hello reject");
        work.setAction(ResStatus.REJECTED);
        WorkFlowRequest saved = workFlowRepository.save(work);

        ApprovalHistory history = ApprovalHistory.builder()
                .workFlowRequest(saved)
                .approver(reject)
                .action(ResStatus.REJECTED)
                .comment(comment)
                .build();
        approvalRepository.save(history);

        AuditLog audit = AuditLog.builder()
                .action("REJECT_ACTION")
                .performed(reject)
                .entityName("WorkFlowRequest")
                .entityId(saved.getId().toString())
                .details("Rejected. Reason: " + comment)
                .build();
        auditRepository.save(audit);

        return rejectF(saved);


    }

    protected WorkRequestdto rejectF(WorkFlowRequest saved)
    {

        List<ApprovalHistorydto> historyDto = saved.getApproval().stream().map(h-> ApprovalHistorydto.builder()
                        .id(h.getId())
                        .comment(h.getComment())
                        .action(h.getAction().toString())
                        .approverName(h.getApprover().getName())
                        .actionDate(h.getActionDate())
                        .build()


                ).toList();



        return WorkRequestdto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .currentStatus(saved.getAction().toString())
                .requesterId(saved.getRequester().getId())
                .requesterEmail(saved.getRequester().getEmail())
                .requesterName(saved.getRequester().getName())
                .role(saved.getRequester().getRole().toString())
                .createdAt(saved.getCreatedAt())
                .approval(historyDto)
                .build();

    }


    public List<WorkRequestdto> getAllData()
    {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        List<WorkFlowRequest> work = workFlowRepository.findByRequesterEmail(username);
        if(work == null)
        {
            System.out.println("Workflow is not found");
        }

        return work.stream().map(this::mapDto).toList();

    }


        public List<WorkRequestdto> allWorkflow()
        {
            List<WorkFlowRequest> workflows = workFlowRepository.findAll();

            return workflows.stream()
                    .map(work -> WorkRequestdto.builder()
                    .id(work.getId())
                            .title(work.getTitle())
                            .description(work.getDescription())
                            .requesterName(work.getRequester().getName())
                            .currentStatus(work.getAction().toString())
                            .createdAt(work.getCreatedAt())
                            .build()




            ).toList();
        }


}
