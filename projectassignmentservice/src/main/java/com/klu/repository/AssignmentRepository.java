package com.klu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klu.entity.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByComplaintId(Long complaintId);

    List<Assignment> findByStaffId(Long staffId);

    List<Assignment> findByDepartment(String department);

    List<Assignment> findByStatus(String status);
}