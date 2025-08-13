-- 超时中心数据库表结构
-- 创建时间: 2024-01-15

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `timeout` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `timeout`;

-- 1. 超时任务表
DROP TABLE IF EXISTS `timeout_task`;
CREATE TABLE `timeout_task` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_time` BIGINT(20) NOT NULL COMMENT '创建时间（毫秒时间戳）',
  `update_time` BIGINT(20) NOT NULL COMMENT '更新时间（毫秒时间戳）',
  `biz_id` VARCHAR(128) NOT NULL COMMENT '业务ID，同一bizType下不可重复',
  `biz_type` VARCHAR(64) NOT NULL COMMENT '业务类型',
  `state` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '任务状态：0-待处理，1-已处理，2-取消，3-失败，4-执行中',
  `action_time` BIGINT(20) NOT NULL COMMENT '期望执行时间（毫秒时间戳）',
  `data` TEXT COMMENT '任务数据（JSON格式）',
  `retry_count` INT(11) NOT NULL DEFAULT 0 COMMENT '重试次数',
  `order` INT(11) NOT NULL DEFAULT 5 COMMENT '优先级（数值越小优先级越高）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_biz_type_id` (`biz_type`, `biz_id`),
  KEY `idx_state_action_time` (`state`, `action_time`),
  KEY `idx_biz_type` (`biz_type`),
  KEY `idx_action_time` (`action_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='超时任务表';

-- 2. 任务类型管理表
DROP TABLE IF EXISTS `timeout_task_type`;
CREATE TABLE `timeout_task_type` (
  `id` VARCHAR(64) NOT NULL COMMENT '主键ID',
  `biz_type` VARCHAR(64) NOT NULL COMMENT '业务类型',
  `biz_name` VARCHAR(128) NOT NULL COMMENT '业务名称',
  `responsible` VARCHAR(64) NOT NULL COMMENT '责任人',
  `mq_topic` VARCHAR(128) DEFAULT NULL COMMENT '消息队列Topic',
  `order` INT(11) NOT NULL DEFAULT 5 COMMENT '优先级',
  `create_time` BIGINT(20) NOT NULL COMMENT '创建时间（毫秒时间戳）',
  `update_time` BIGINT(20) NOT NULL COMMENT '更新时间（毫秒时间戳）',
  `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
  `max_retry_count` INT(11) NOT NULL DEFAULT 3 COMMENT '最大重试次数',
  `retry_interval` BIGINT(20) NOT NULL DEFAULT 60000 COMMENT '重试间隔（毫秒）',
  `timeout` BIGINT(20) NOT NULL DEFAULT 30000 COMMENT '执行超时时间（毫秒）',
  `description` VARCHAR(256) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_biz_type` (`biz_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务类型管理表';

-- 3. 系统配置表
DROP TABLE IF EXISTS `timeout_config`;
CREATE TABLE `timeout_config` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` VARCHAR(128) NOT NULL COMMENT '配置键',
  `config_value` TEXT NOT NULL COMMENT '配置值',
  `config_type` VARCHAR(32) NOT NULL DEFAULT 'STRING' COMMENT '配置类型：STRING、JSON、NUMBER、BOOLEAN',
  `description` VARCHAR(256) DEFAULT NULL COMMENT '配置描述',
  `create_time` BIGINT(20) NOT NULL COMMENT '创建时间（毫秒时间戳）',
  `update_time` BIGINT(20) NOT NULL COMMENT '更新时间（毫秒时间戳）',
  `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 4. 任务执行历史表（可选，用于统计分析）
DROP TABLE IF EXISTS `timeout_task_history`;
CREATE TABLE `timeout_task_history` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `task_id` BIGINT(20) NOT NULL COMMENT '任务ID',
  `biz_id` VARCHAR(128) NOT NULL COMMENT '业务ID',
  `biz_type` VARCHAR(64) NOT NULL COMMENT '业务类型',
  `action_time` BIGINT(20) NOT NULL COMMENT '期望执行时间',
  `actual_execute_time` BIGINT(20) DEFAULT NULL COMMENT '实际执行时间',
  `execute_duration` BIGINT(20) DEFAULT NULL COMMENT '执行耗时（毫秒）',
  `state` TINYINT(4) NOT NULL COMMENT '最终状态',
  `retry_count` INT(11) NOT NULL DEFAULT 0 COMMENT '重试次数',
  `error_message` TEXT COMMENT '错误信息',
  `create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_biz_type_time` (`biz_type`, `create_time`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务执行历史表';

-- 5. 节点信息表（集群管理）
DROP TABLE IF EXISTS `timeout_node`;
CREATE TABLE `timeout_node` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `node_id` VARCHAR(64) NOT NULL COMMENT '节点ID',
  `node_name` VARCHAR(128) NOT NULL COMMENT '节点名称',
  `ip_address` VARCHAR(64) NOT NULL COMMENT 'IP地址',
  `port` INT(11) NOT NULL COMMENT '端口',
  `rpc_protocol` VARCHAR(32) NOT NULL DEFAULT 'dubbo' COMMENT 'RPC协议',
  `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '节点状态：0-离线，1-在线，2-维护',
  `last_heartbeat` BIGINT(20) NOT NULL COMMENT '最后心跳时间',
  `create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
  `update_time` BIGINT(20) NOT NULL COMMENT '更新时间',
  `version` VARCHAR(32) DEFAULT NULL COMMENT '版本号',
  `metadata` TEXT COMMENT '节点元数据（JSON格式）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_node_id` (`node_id`),
  KEY `idx_status_heartbeat` (`status`, `last_heartbeat`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='节点信息表';

-- 创建索引优化查询性能
-- 为高频查询场景创建联合索引
ALTER TABLE `timeout_task` ADD INDEX `idx_biz_state_time` (`biz_type`, `state`, `action_time`);
ALTER TABLE `timeout_task` ADD INDEX `idx_state_order_time` (`state`, `order`, `action_time`);

-- 为历史表创建分区（按月分区，可根据实际情况调整）
-- ALTER TABLE `timeout_task_history` PARTITION BY RANGE (create_time) (
--   PARTITION p202401 VALUES LESS THAN (1706745600000),  -- 2024-02-01
--   PARTITION p202402 VALUES LESS THAN (1709251200000),  -- 2024-03-01
--   PARTITION p202403 VALUES LESS THAN (1711929600000),  -- 2024-04-01
--   PARTITION p_future VALUES LESS THAN MAXVALUE
-- );

-- 创建视图，方便查询任务统计信息
CREATE OR REPLACE VIEW `v_task_statistics` AS
SELECT 
  biz_type,
  COUNT(*) as total_count,
  SUM(CASE WHEN state = 0 THEN 1 ELSE 0 END) as waiting_count,
  SUM(CASE WHEN state = 1 THEN 1 ELSE 0 END) as success_count,
  SUM(CASE WHEN state = 2 THEN 1 ELSE 0 END) as cancelled_count,
  SUM(CASE WHEN state = 3 THEN 1 ELSE 0 END) as failed_count,
  SUM(CASE WHEN state = 4 THEN 1 ELSE 0 END) as executing_count,
  AVG(retry_count) as avg_retry_count,
  MAX(create_time) as last_create_time
FROM timeout_task 
GROUP BY biz_type;

-- 添加表注释
ALTER TABLE `timeout_task` COMMENT = '超时任务表 - 存储所有超时任务的基本信息和状态';
ALTER TABLE `timeout_task_type` COMMENT = '任务类型管理表 - 配置不同业务类型的处理参数';
ALTER TABLE `timeout_config` COMMENT = '系统配置表 - 存储系统运行时配置参数';
ALTER TABLE `timeout_task_history` COMMENT = '任务执行历史表 - 记录任务执行历史，用于统计分析';
ALTER TABLE `timeout_node` COMMENT = '节点信息表 - 记录集群节点信息，用于分布式管理';

-- 创建存储过程：清理过期历史数据
DELIMITER $$
CREATE PROCEDURE `CleanExpiredHistory`(IN `days_to_keep` INT)
BEGIN
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    RESIGNAL;
  END;
  
  START TRANSACTION;
  
  SET @cutoff_time = (UNIX_TIMESTAMP() - days_to_keep * 24 * 3600) * 1000;
  
  DELETE FROM timeout_task_history 
  WHERE create_time < @cutoff_time;
  
  COMMIT;
  
  SELECT ROW_COUNT() AS deleted_rows, @cutoff_time AS cutoff_timestamp;
END$$
DELIMITER ;

-- 执行完成提示
SELECT 'Database schema created successfully!' AS result;
