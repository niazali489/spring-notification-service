package com.notificationservice.repository;

import com.notificationservice.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Notification entity
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Find notifications by type
     */
    List<Notification> findByType(String type);

    /**
     * Find notifications by recipient
     */
    List<Notification> findByRecipient(String recipient);

    /**
     * Find notifications by status
     */
    List<Notification> findByStatus(String status);

    /**
     * Find notifications by type and status
     */
    List<Notification> findByTypeAndStatus(String type, String status);

    /**
     * Find notifications created between dates
     */
    List<Notification> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Find failed notifications
     */
    @Query("SELECT n FROM Notification n WHERE n.status = 'FAILED'")
    List<Notification> findFailedNotifications();

    /**
     * Find recent notifications by recipient
     */
    @Query("SELECT n FROM Notification n WHERE n.recipient = :recipient ORDER BY n.createdAt DESC")
    List<Notification> findRecentByRecipient(@Param("recipient") String recipient);

    /**
     * Count notifications by status
     */
    long countByStatus(String status);

    /**
     * Count notifications by type and date range
     */
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.type = :type AND n.createdAt BETWEEN :start AND :end")
    long countByTypeAndDateRange(@Param("type") String type,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);
}