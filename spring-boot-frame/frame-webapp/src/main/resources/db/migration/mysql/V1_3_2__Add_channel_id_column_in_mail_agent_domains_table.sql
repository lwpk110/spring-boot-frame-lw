ALTER TABLE `mail_agent_domains`
ADD COLUMN channel_id int(11) ;


ALTER TABLE `user_mail_delivery_tasks`
ADD COLUMN mailing_id int(11) ;