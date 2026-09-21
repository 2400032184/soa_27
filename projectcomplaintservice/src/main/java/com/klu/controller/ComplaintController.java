package com.klu.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.klu.entity.Complaint;
import com.klu.service.ComplaintService;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final RestTemplate restTemplate;

    public ComplaintController(
            ComplaintService complaintService,
            RestTemplate restTemplate) {

        this.complaintService = complaintService;
        this.restTemplate = restTemplate;
    }

    // Create Complaint
    @PostMapping
    public ResponseEntity<Complaint> createComplaint(
            @RequestBody Complaint complaint,
            @RequestHeader("X-User-Id") Long userId) {

        complaint.setUserId(userId);

        Complaint createdComplaint =
                complaintService.createComplaint(complaint);

        return new ResponseEntity<>(
                createdComplaint,
                HttpStatus.CREATED
        );
    }

    // Get All Complaints - ADMIN only
    @GetMapping
    public ResponseEntity<?> getAllComplaints(
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                complaintService.getAllComplaints()
        );
    }

    // Get My Complaints
    @GetMapping("/my")
    public ResponseEntity<List<Complaint>> getMyComplaints(
            @RequestHeader("X-User-Id") Long userId) {

        return ResponseEntity.ok(
                complaintService.getComplaintsByUserId(userId)
        );
    }

    // Get Complaint By ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getComplaintById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        Complaint complaint =
                complaintService.getComplaintById(id);

        if (complaint == null) {
            return ResponseEntity.notFound().build();
        }

        // ADMIN can view any complaint
        if ("ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.ok(complaint);
        }

        // USER/STAFF can view only their own complaint
        if (complaint.getUserId() == null
                || !complaint.getUserId().equals(userId)) {

            return ResponseEntity.status(403)
                    .body(
                        "Access denied. This complaint does not belong to you."
                    );
        }

        return ResponseEntity.ok(complaint);
    }

    // Get Complaints By User - ADMIN only
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getComplaintsByUserId(
            @PathVariable Long userId,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                complaintService.getComplaintsByUserId(userId)
        );
    }

    // Get Complaints By Status - ADMIN only
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getComplaintsByStatus(
            @PathVariable String status,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                complaintService.getComplaintsByStatus(status)
        );
    }

    // Get Complaints By Category - ADMIN only
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getComplaintsByCategory(
            @PathVariable String category,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                complaintService.getComplaintsByCategory(category)
        );
    }

    // Update Complaint Status - ADMIN or assigned STAFF
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateComplaintStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        Complaint complaint =
                complaintService.getComplaintById(id);

        if (complaint == null) {
            return ResponseEntity.notFound().build();
        }

        // ADMIN can update any complaint
        if ("ADMIN".equalsIgnoreCase(role)) {

            return ResponseEntity.ok(
                    complaintService.updateComplaintStatus(
                            id,
                            status
                    )
            );
        }

        // STAFF can update only complaints assigned to them
        if ("STAFF".equalsIgnoreCase(role)) {

            try {

                restTemplate.getForObject(
                        "http://projectassignmentservice/assignments/complaint/"
                        + id
                        + "/staff/"
                        + userId,
                        Object.class
                );

                return ResponseEntity.ok(
                        complaintService.updateComplaintStatus(
                                id,
                                status
                        )
                );

            } catch (Exception e) {

                return ResponseEntity.status(403)
                        .body(
                            "Access denied. This complaint is not assigned to you."
                        );
            }
        }

        return ResponseEntity.status(403)
                .body(
                    "Access denied. ADMIN or assigned STAFF required."
                );
    }

    // Update Complaint - ADMIN only
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComplaint(
            @PathVariable Long id,
            @RequestBody Complaint complaint,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                complaintService.updateComplaint(
                        id,
                        complaint
                )
        );
    }

    // Delete Complaint - ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaint(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        complaintService.deleteComplaint(id);

        return ResponseEntity.ok(
                "Complaint deleted successfully"
        );
    }
}