package com.project.demo.repository;

import com.project.demo.entity.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepository extends JpaRepository<ApprovalHistory,Integer> {
}
