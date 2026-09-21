package com.klu.dto;

public class NotificationRequest {

    private Long userId;
    private Long complaintId;
    private String message;
    private String type;

    public NotificationRequest() {
    }

    public NotificationRequest(
            Long userId,
            Long complaintId,
            String message,
            String type) {

        this.userId = userId;
        this.complaintId = complaintId;
        this.message = message;
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}