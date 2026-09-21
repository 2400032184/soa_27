package com.klu.dto;

public class AssignmentRequest {

    private Long complaintId;

    private Long userId;

    private String department;

    private Long staffId;

    public AssignmentRequest() {
    }

    public AssignmentRequest(
            Long complaintId,
            Long userId,
            String department,
            Long staffId) {

        this.complaintId = complaintId;
        this.userId = userId;
        this.department = department;
        this.staffId = staffId;
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
}