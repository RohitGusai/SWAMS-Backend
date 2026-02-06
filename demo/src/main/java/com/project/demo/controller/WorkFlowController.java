package com.project.demo.controller;

import com.project.demo.dto.WorkRequestdto;
import com.project.demo.entity.WorkFlowRequest;
import com.project.demo.service.WorkFlowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/work")
public class WorkFlowController {

    @Autowired
    private WorkFlowService workFlowService;

    @PostMapping("/user/add")
    public ResponseEntity<WorkRequestdto> addWorkFlow(@RequestBody WorkFlowRequest workFlowRequest)
    {
        return ResponseEntity.ok(workFlowService.postWorkFlow(workFlowRequest));
    }

    @GetMapping("/get-data/{id}")
    public ResponseEntity<WorkRequestdto> getWorkFlow(@PathVariable Integer id)
    {
        return ResponseEntity.ok(workFlowService.getData(id));
    }

    @PostMapping("/manager/approve/{id}")
    public ResponseEntity<WorkRequestdto> addApprove(@PathVariable Integer id, @RequestParam String comment)
    {
        return ResponseEntity.ok(workFlowService.approveReq(id,comment));
    }

    @PostMapping("/manager/reject/{id}")
    public ResponseEntity<WorkRequestdto> reject(@PathVariable Integer id,@RequestParam String comment)
    {
        return ResponseEntity.ok(workFlowService.rejectReq(id,comment));
    }



    @GetMapping("/check-status/{workFlowId}")
    public ResponseEntity<WorkRequestdto> getData(@PathVariable Integer workFlowId)
    {
        return ResponseEntity.ok(workFlowService.getStatus(workFlowId));
    }


    @GetMapping("/user/workflow")
    public List<WorkRequestdto> allData()
    {
        return workFlowService.getAllData();
    }

    @GetMapping("/manager/get-workflow")
    public List<WorkRequestdto> getWorkflowAll()
    {
        return workFlowService.allWorkflow();
    }




}
