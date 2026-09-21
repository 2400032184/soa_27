package com.klu.service;

import java.util.List;

import com.klu.entity.Assignment;

public interface AssignmentService {

    Assignment createAssignment(Assignment assignment);

    List<Assignment> getAllAssignments();

    Assignment getAssignmentById(Long id);

    List<Assignment> getAssignmentsByComplaintId(Long complaintId);

    List<Assignment> getAssignmentsByStaffId(Long staffId);

    List<Assignment> getAssignmentsByDepartment(String department);

    List<Assignment> getAssignmentsByStatus(String status);

    Assignment updateAssignmentStatus(Long id, String status);

    Assignment updateAssignment(Long id, Assignment assignment);

    void deleteAssignment(Long id);
}