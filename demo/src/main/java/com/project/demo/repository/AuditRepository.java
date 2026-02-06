package com.project.demo.repository;

import com.project.demo.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditLog,Integer> {
}
