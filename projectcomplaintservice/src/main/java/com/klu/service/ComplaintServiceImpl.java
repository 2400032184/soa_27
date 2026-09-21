package com.klu.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.klu.dto.AssignmentRequest;
import com.klu.entity.Complaint;
import com.klu.repository.ComplaintRepository;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final RestTemplate restTemplate;

    public ComplaintServiceImpl(
            ComplaintRepository complaintRepository,
            RestTemplate restTemplate) {

        this.complaintRepository = complaintRepository;
        this.restTemplate = restTemplate;
    }

    // Create Complaint
    @Override
    public Complaint createComplaint(Complaint complaint) {

        complaint.setStatus("OPEN");
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());

        Complaint savedComplaint =
                complaintRepository.save(complaint);

        // Call Assignment Service
        try {

        	AssignmentRequest request = new AssignmentRequest(
        	        savedComplaint.getId(),
        	        savedComplaint.getUserId(),
        	        savedComplaint.getCategory(),
        	        null
        	);

        	restTemplate.postForObject(
        	        "http://projectassignmentservice/assignments",
        	        request,
        	        String.class
        	);

            System.out.println(
                    "Assignment created successfully for complaint ID: "
                    + savedComplaint.getId()
            );

        } catch (Exception e) {

            System.out.println(
                    "Assignment Service call failed: "
                    + e.getMessage()
            );
        }

        return savedComplaint;
    }

    // Get All Complaints
    @Override
    public List<Complaint> getAllComplaints() {

        return complaintRepository.findAll();
    }

    // Get Complaint By ID
    @Override
    public Complaint getComplaintById(Long id) {

        return complaintRepository
                .findById(id)
                .orElse(null);
    }

    // Get Complaints By User ID
    @Override
    public List<Complaint> getComplaintsByUserId(Long userId) {

        return complaintRepository.findByUserId(userId);
    }

    // Get Complaints By Status
    @Override
    public List<Complaint> getComplaintsByStatus(String status) {

        return complaintRepository.findByStatus(status);
    }

    // Get Complaints By Category
    @Override
    public List<Complaint> getComplaintsByCategory(String category) {

        return complaintRepository.findByCategory(category);
    }

    // Update Complaint Status
    @Override
    public Complaint updateComplaintStatus(
            Long id,
            String status) {

        Complaint complaint =
                complaintRepository
                        .findById(id)
                        .orElse(null);

        if (complaint != null) {

            complaint.setStatus(status);
            complaint.setUpdatedAt(LocalDateTime.now());

            return complaintRepository.save(complaint);
        }

        return null;
    }

    // Update Complaint
    @Override
    public Complaint updateComplaint(
            Long id,
            Complaint updatedComplaint) {

        Complaint complaint =
                complaintRepository
                        .findById(id)
                        .orElse(null);

        if (complaint != null) {

            complaint.setTitle(
                    updatedComplaint.getTitle()
            );

            complaint.setDescription(
                    updatedComplaint.getDescription()
            );

            complaint.setCategory(
                    updatedComplaint.getCategory()
            );

            complaint.setPriority(
                    updatedComplaint.getPriority()
            );

            complaint.setUpdatedAt(
                    LocalDateTime.now()
            );

            return complaintRepository.save(complaint);
        }

        return null;
    }

    // Delete Complaint
    @Override
    public void deleteComplaint(Long id) {

        complaintRepository.deleteById(id);
    }
}