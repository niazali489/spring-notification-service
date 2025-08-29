-- Database initialization script for production MySQL setup
-- This file is automatically executed when MySQL container starts

CREATE DATABASE IF NOT EXISTS notification_db;
USE notification_db;

-- Create application user with proper permissions
CREATE USER IF NOT EXISTS 'notification_user'@'%' IDENTIFIED BY 'notification_password';
GRANT ALL PRIVILEGES ON notification_db.* TO 'notification_user'@'%';
FLUSH PRIVILEGES;

-- Create indexes for better performance (tables will be created by Hibernate)
-- These will be applied after tables are created

-- Index for notifications table
-- ALTER TABLE notifications ADD INDEX idx_notifications_type (type);
-- ALTER TABLE notifications ADD INDEX idx_notifications_recipient (recipient);
-- ALTER TABLE notifications ADD INDEX idx_notifications_status (status);
-- ALTER TABLE notifications ADD INDEX idx_notifications_created_at (created_at);

-- Index for users table
-- ALTER TABLE users ADD INDEX idx_users_username (username);
-- ALTER TABLE users ADD INDEX idx_users_email (email);
-- ALTER TABLE users ADD INDEX idx_users_active (active);

-- Note: Uncomment the ALTER TABLE statements above after first run
-- when tables are created by Hibernate