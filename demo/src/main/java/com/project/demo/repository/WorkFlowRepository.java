package com.project.demo.repository;

import com.project.demo.entity.WorkFlowRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkFlowRepository extends JpaRepository<WorkFlowRequest,Integer> {
    @EntityGraph(attributePaths = {"approval", "requester"})
    List<WorkFlowRequest> findByRequesterEmail(String name);
}
