package com.klu.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klu.entity.Assignment;
import com.klu.service.AssignmentService;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // Create Assignment
    // Used internally by Complaint Service
    @PostMapping
    public ResponseEntity<Assignment> createAssignment(
            @RequestBody Assignment assignment) {

        return ResponseEntity.ok(
                assignmentService.createAssignment(assignment)
        );
    }

    // Get All Assignments - ADMIN only
    @GetMapping
    public ResponseEntity<?> getAllAssignments(
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                assignmentService.getAllAssignments()
        );
    }

    // Get My Assignments - STAFF
    @GetMapping("/my")
    public ResponseEntity<?> getMyAssignments(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        if (!"STAFF".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. STAFF role required.");
        }

        return ResponseEntity.ok(
                assignmentService.getAssignmentsByStaffId(userId)
        );
    }

    // Get Assignment By ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignmentById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        Assignment assignment =
                assignmentService.getAssignmentById(id);

        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }

        // ADMIN can view any assignment
        if ("ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.ok(assignment);
        }

        // STAFF can view only their own assignment
        if ("STAFF".equalsIgnoreCase(role)
                && assignment.getStaffId() != null
                && assignment.getStaffId().equals(userId)) {

            return ResponseEntity.ok(assignment);
        }

        return ResponseEntity.status(403)
                .body("Access denied. This assignment is not assigned to you.");
    }

    // Get Assignments By Complaint - ADMIN only
    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<?> getAssignmentsByComplaintId(
            @PathVariable Long complaintId,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                assignmentService.getAssignmentsByComplaintId(
                        complaintId
                )
        );
    }

    // Get Assignments By Staff - ADMIN only
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<?> getAssignmentsByStaffId(
            @PathVariable Long staffId,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                assignmentService.getAssignmentsByStaffId(staffId)
        );
    }

    // Get Assignments By Department - ADMIN only
    @GetMapping("/department/{department}")
    public ResponseEntity<?> getAssignmentsByDepartment(
            @PathVariable String department,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                assignmentService.getAssignmentsByDepartment(department)
        );
    }

    // Get Assignments By Status - ADMIN only
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getAssignmentsByStatus(
            @PathVariable String status,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                assignmentService.getAssignmentsByStatus(status)
        );
    }

    // Check whether a complaint is assigned to a particular staff member
    // Used internally by Complaint Service
    @GetMapping("/complaint/{complaintId}/staff/{staffId}")
    public ResponseEntity<?> checkStaffAssignment(
            @PathVariable Long complaintId,
            @PathVariable Long staffId) {

        List<Assignment> assignments =
                assignmentService.getAssignmentsByComplaintId(
                        complaintId
                );

        for (Assignment assignment : assignments) {

            if (assignment.getStaffId() != null
                    && assignment.getStaffId().equals(staffId)) {

                return ResponseEntity.ok(assignment);
            }
        }

        return ResponseEntity.status(404)
                .body(
                    "Complaint is not assigned to this staff member."
                );
    }

    // Update Assignment Status - ADMIN or assigned STAFF
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateAssignmentStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        Assignment assignment =
                assignmentService.getAssignmentById(id);

        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }

        // ADMIN can update any assignment
        if ("ADMIN".equalsIgnoreCase(role)) {

            return ResponseEntity.ok(
                    assignmentService.updateAssignmentStatus(
                            id,
                            status
                    )
            );
        }

        // STAFF can update only their own assignment
        if ("STAFF".equalsIgnoreCase(role)) {

            if (assignment.getStaffId() == null
                    || !assignment.getStaffId().equals(userId)) {

                return ResponseEntity.status(403)
                        .body(
                            "Access denied. This assignment is not assigned to you."
                        );
            }

            return ResponseEntity.ok(
                    assignmentService.updateAssignmentStatus(
                            id,
                            status
                    )
            );
        }

        return ResponseEntity.status(403)
                .body(
                    "Access denied. ADMIN or STAFF role required."
                );
    }

    // Update Assignment - ADMIN only
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(
            @PathVariable Long id,
            @RequestBody Assignment assignment,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                assignmentService.updateAssignment(
                        id,
                        assignment
                )
        );
    }

    // Delete Assignment - ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssignment(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        assignmentService.deleteAssignment(id);

        return ResponseEntity.ok(
                "Assignment deleted successfully"
        );
    }
}