package com.klu.dto;

public class StaffAssignmentResponse {

    private Long complaintId;
    private Long staffId;

    public StaffAssignmentResponse() {
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
}