package com.klu.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klu.entity.Notification;
import com.klu.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Create Notification
    // Used internally by Assignment Service
    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @RequestBody Notification notification) {

        return ResponseEntity.ok(
                notificationService.createNotification(notification)
        );
    }

    // Get All Notifications - ADMIN only
    @GetMapping
    public ResponseEntity<?> getAllNotifications(
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                notificationService.getAllNotifications()
        );
    }

    // Get My Notifications
    @GetMapping("/my")
    public ResponseEntity<?> getMyNotifications(
            @RequestHeader("X-User-Id") Long userId) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByUserId(userId)
        );
    }

    // Get Notification By ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        Notification notification =
                notificationService.getNotificationById(id);

        if (notification == null) {
            return ResponseEntity.notFound().build();
        }

        // ADMIN can view any notification
        if ("ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.ok(notification);
        }

        // USER/STAFF can view only their own notification
        if (notification.getUserId() == null
                || !notification.getUserId().equals(userId)) {

            return ResponseEntity.status(403)
                    .body("Access denied. This notification does not belong to you.");
        }

        return ResponseEntity.ok(notification);
    }

    // Get Notifications By User - ADMIN only
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getNotificationsByUserId(
            @PathVariable Long userId,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                notificationService.getNotificationsByUserId(userId)
        );
    }

    // Get Notifications By Complaint - ADMIN only
    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<?> getNotificationsByComplaintId(
            @PathVariable Long complaintId,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        return ResponseEntity.ok(
                notificationService.getNotificationsByComplaintId(
                        complaintId
                )
        );
    }

    // Get My Unread Notifications
    @GetMapping("/my/unread")
    public ResponseEntity<?> getMyUnreadNotifications(
            @RequestHeader("X-User-Id") Long userId) {

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userId)
        );
    }

    // Mark Notification As Read
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        Notification notification =
                notificationService.getNotificationById(id);

        if (notification == null) {
            return ResponseEntity.notFound().build();
        }

        // ADMIN can mark any notification as read
        if ("ADMIN".equalsIgnoreCase(role)) {

            return ResponseEntity.ok(
                    notificationService.markAsRead(id)
            );
        }

        // Other users can mark only their own notification as read
        if (notification.getUserId() == null
                || !notification.getUserId().equals(userId)) {

            return ResponseEntity.status(403)
                    .body("Access denied. This notification does not belong to you.");
        }

        return ResponseEntity.ok(
                notificationService.markAsRead(id)
        );
    }

    // Delete Notification - ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403)
                    .body("Access denied. ADMIN role required.");
        }

        notificationService.deleteNotification(id);

        return ResponseEntity.ok(
                "Notification deleted successfully"
        );
    }
}