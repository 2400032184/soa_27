package com.klu.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.klu.entity.Notification;
import com.klu.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository) {

        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification createNotification(Notification notification) {

        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllNotifications() {

        return notificationRepository.findAll();
    }

    @Override
    public Notification getNotificationById(Long id) {

        return notificationRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Notification not found with id: " + id
                    ));
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {

        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getNotificationsByComplaintId(
            Long complaintId) {

        return notificationRepository.findByComplaintId(complaintId);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {

        return notificationRepository.findByUserIdAndIsRead(
                userId, false
        );
    }

    @Override
    public Notification markAsRead(Long id) {

        Notification notification = getNotificationById(id);

        notification.setRead(true);

        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long id) {

        if (!notificationRepository.existsById(id)) {

            throw new RuntimeException(
                "Notification not found with id: " + id
            );
        }

        notificationRepository.deleteById(id);
    }
}