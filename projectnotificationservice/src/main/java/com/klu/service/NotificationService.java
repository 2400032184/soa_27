package com.klu.service;

import java.util.List;

import com.klu.entity.Notification;

public interface NotificationService {

    Notification createNotification(Notification notification);

    List<Notification> getAllNotifications();

    Notification getNotificationById(Long id);

    List<Notification> getNotificationsByUserId(Long userId);

    List<Notification> getNotificationsByComplaintId(Long complaintId);

    List<Notification> getUnreadNotifications(Long userId);

    Notification markAsRead(Long id);

    void deleteNotification(Long id);
}