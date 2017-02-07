CREATE TABLE `passway_campaigns` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `passway_campaign_key` varchar(20) NOT NULL,
  `disabled` bit(1) DEFAULT b'0',
  `deleted` bit(1) DEFAULT b'0',
  `total_use` int(4) DEFAULT '0',
  `use_date` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

ALTER TABLE `mail_delivery_channel_nodes`
ADD COLUMN `need_campaigns` bit NOT NULL DEFAULT 0 AFTER `channel_id`;

ALTER TABLE `user_mail_delivery_tasks`
ADD COLUMN `passway_campaign_key` varchar(20)  AFTER `channel_id`;