package com.klu.service;

import java.util.List;
import com.klu.entity.Complaint;

public interface ComplaintService {

    Complaint createComplaint(Complaint complaint);

    List<Complaint> getAllComplaints();

    Complaint getComplaintById(Long id);

    List<Complaint> getComplaintsByUserId(Long userId);

    List<Complaint> getComplaintsByStatus(String status);

    List<Complaint> getComplaintsByCategory(String category);

    Complaint updateComplaintStatus(Long id, String status);

    Complaint updateComplaint(Long id, Complaint complaint);

    void deleteComplaint(Long id);
}