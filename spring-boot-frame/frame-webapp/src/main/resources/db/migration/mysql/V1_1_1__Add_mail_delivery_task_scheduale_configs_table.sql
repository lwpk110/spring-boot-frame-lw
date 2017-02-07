CREATE TABLE `mail_delivery_task_schedule_configs` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel_node_id` int(11) NOT NULL COMMENT '通道节点id',
  `time_area` varchar(100) NOT NULL DEFAULT '-1' COMMENT '需要同步的任务时间范围',
  `job_type` int(11) NOT NULL DEFAULT '0' COMMENT '任务类型 1：定时  2：循环',
  `cron_expression` varchar(100) NOT NULL DEFAULT '' COMMENT '定时器 触发规则表达式',
  `status` int(10) NOT NULL DEFAULT '0' COMMENT '状态 1：启动  2：未启动',
  `created_date` datetime DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `sync_state` bit(1) NOT NULL DEFAULT b'0' COMMENT '1：同步  2：异步',
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_channelNode` (`channel_node_id`) USING BTREE,
  CONSTRAINT `fk_channelNode` FOREIGN KEY (`channel_node_id`) REFERENCES `mail_delivery_channel_nodes` (`id`)
);
