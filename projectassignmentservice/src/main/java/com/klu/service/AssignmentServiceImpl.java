package com.klu.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.klu.dto.NotificationRequest;
import com.klu.entity.Assignment;
import com.klu.repository.AssignmentRepository;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final RestTemplate restTemplate;

    public AssignmentServiceImpl(
            AssignmentRepository assignmentRepository,
            RestTemplate restTemplate) {

        this.assignmentRepository = assignmentRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Assignment createAssignment(Assignment assignment) {

        assignment.setStatus("ASSIGNED");
        assignment.setAssignedAt(LocalDateTime.now());

        Assignment savedAssignment =
                assignmentRepository.save(assignment);

        try {

            String message =
                    "Your complaint has been assigned to the "
                    + assignment.getDepartment()
                    + " department.";

            NotificationRequest notification =
                    new NotificationRequest(
                            savedAssignment.getUserId(),
                            savedAssignment.getComplaintId(),
                            message,
                            "ASSIGNMENT"
                    );

            restTemplate.postForObject(
                    "http://projectnotificationservice/notifications",
                    notification,
                    String.class
            );

        } catch (Exception e) {

            System.out.println(
                    "Notification Service call failed: "
                    + e.getMessage()
            );
        }

        return savedAssignment;
    }

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Override
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public List<Assignment> getAssignmentsByComplaintId(
            Long complaintId) {

        return assignmentRepository
                .findByComplaintId(complaintId);
    }

    @Override
    public List<Assignment> getAssignmentsByStaffId(
            Long staffId) {

        return assignmentRepository
                .findByStaffId(staffId);
    }

    @Override
    public List<Assignment> getAssignmentsByDepartment(
            String department) {

        return assignmentRepository
                .findByDepartment(department);
    }

    @Override
    public List<Assignment> getAssignmentsByStatus(
            String status) {

        return assignmentRepository
                .findByStatus(status);
    }

    @Override
    public Assignment updateAssignmentStatus(
            Long id,
            String status) {

        Assignment assignment =
                assignmentRepository
                        .findById(id)
                        .orElse(null);

        if (assignment != null) {

            assignment.setStatus(status);

            return assignmentRepository.save(assignment);
        }

        return null;
    }

    @Override
    public Assignment updateAssignment(
            Long id,
            Assignment updatedAssignment) {

        Assignment assignment =
                assignmentRepository
                        .findById(id)
                        .orElse(null);

        if (assignment != null) {

            assignment.setDepartment(
                    updatedAssignment.getDepartment()
            );

            assignment.setStaffId(
                    updatedAssignment.getStaffId()
            );

            assignment.setStatus(
                    updatedAssignment.getStatus()
            );

            return assignmentRepository.save(assignment);
        }

        return null;
    }

    @Override
    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }
}