ALTER TABLE `user_mail_recipient_groups`
ADD COLUMN disabled_recipients longtext DEFAULT NULL ;


ALTER TABLE `user_mail_recipient_groups`
ADD COLUMN disabled_recipient_count int DEFAULT 0;