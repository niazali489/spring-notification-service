-- V2__Create_notifications_table.sql
-- Second migration: Create notifications table

CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    recipient VARCHAR(255) NOT NULL,
    content TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP NULL,
    error_message VARCHAR(500)
);

-- Create indexes for better query performance
CREATE INDEX idx_notifications_type ON notifications (type);
CREATE INDEX idx_notifications_recipient ON notifications (recipient);
CREATE INDEX idx_notifications_status ON notifications (status);
CREATE INDEX idx_notifications_created_at ON notifications (created_at);