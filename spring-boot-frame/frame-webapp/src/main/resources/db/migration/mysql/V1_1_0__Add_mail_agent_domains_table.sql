ALTER TABLE `user_mail_delivery_settings`
ADD COLUMN `is_agent_send` bit NOT NULL DEFAULT 0 AFTER `user_id`;

CREATE TABLE `mail_agent_domains` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_date` datetime,
  `last_modified_date` datetime,
  `disabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否可用',
  `mail_agent` varchar(200) DEFAULT NULL COMMENT '后缀',
  `domain_info` varchar(200) DEFAULT NULL COMMENT '行业',
  `use_count` int(11) NOT NULL DEFAULT '0' COMMENT '使用次数',
  PRIMARY KEY (`id`)
);


ALTER TABLE `user_mail_delivery_tasks`
ADD COLUMN `is_agent_send` bit NOT NULL DEFAULT 0 AFTER `user_id`;

--设置字段允许为空
ALTER TABLE `user_mail_delivery_settings`
MODIFY COLUMN `sender_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER `reply_name`,
MODIFY COLUMN `sender_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER `sender_email`;