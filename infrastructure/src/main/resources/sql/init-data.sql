-- 超时中心初始化数据
-- 创建时间: 2024-01-15

USE `timeout`;

-- 1. 插入默认任务类型配置
INSERT INTO `timeout_task_type` VALUES 
('ORDER_TIMEOUT', 'ORDER_TIMEOUT', '订单超时处理', 'system', 'order-timeout-topic', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1, 3, 60000, 30000, '处理超时未支付的订单'),
('COUPON_EXPIRE', 'COUPON_EXPIRE', '优惠券过期处理', 'system', 'coupon-expire-topic', 2, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1, 2, 30000, 15000, '处理过期的优惠券'),
('PAYMENT_TIMEOUT', 'PAYMENT_TIMEOUT', '支付超时处理', 'system', 'payment-timeout-topic', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1, 5, 120000, 60000, '处理支付超时的订单'),
('USER_INACTIVE', 'USER_INACTIVE', '用户活跃度检查', 'system', 'user-inactive-topic', 3, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1, 1, 86400000, 30000, '检查长时间未活跃的用户'),
('DATA_CLEANUP', 'DATA_CLEANUP', '数据清理任务', 'system', 'data-cleanup-topic', 5, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1, 2, 3600000, 120000, '定期清理过期数据');

-- 2. 插入系统配置
INSERT INTO `timeout_config` VALUES 
(NULL, 'system.max_concurrent_tasks', '1000', 'NUMBER', '系统最大并发任务数', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'system.default_timeout', '30000', 'NUMBER', '默认任务超时时间（毫秒）', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'system.default_retry_count', '3', 'NUMBER', '默认重试次数', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'system.default_retry_interval', '60000', 'NUMBER', '默认重试间隔（毫秒）', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'system.batch_size', '100', 'NUMBER', '批处理大小', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'system.enable_monitoring', 'true', 'BOOLEAN', '是否启用监控', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'rpc.default_protocol', 'dubbo', 'STRING', '默认RPC协议', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'rpc.connect_timeout', '5000', 'NUMBER', 'RPC连接超时时间（毫秒）', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'rpc.read_timeout', '30000', 'NUMBER', 'RPC读取超时时间（毫秒）', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'time_wheel.tick_duration', '1', 'NUMBER', '时间轮刻度时长（毫秒）', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'time_wheel.wheel_size', '20', 'NUMBER', '时间轮大小', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'time_wheel.worker_threads', '8', 'NUMBER', '时间轮工作线程数', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'queue.store_prefix', 'timeout:store:', 'STRING', '存储队列前缀', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'queue.prepare_prefix', 'timeout:prepare:', 'STRING', '准备队列前缀', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'queue.dead_prefix', 'timeout:dead:', 'STRING', '死信队列前缀', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'cleanup.history_retention_days', '30', 'NUMBER', '历史数据保留天数', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'monitoring.metrics_interval', '60000', 'NUMBER', '监控指标收集间隔（毫秒）', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'cluster.heartbeat_interval', '30000', 'NUMBER', '集群心跳间隔（毫秒）', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1),
(NULL, 'cluster.node_timeout', '90000', 'NUMBER', '节点超时时间（毫秒）', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 1);

-- 3. 插入当前节点信息（示例）
INSERT INTO `timeout_node` VALUES 
(NULL, 'node-001', 'timeout-center-node-1', '127.0.0.1', 8082, 'dubbo', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, '1.0.1-SNAPSHOT', '{"role":"master","region":"default"}');

-- 4. 插入测试任务数据（可选，用于开发测试）
-- INSERT INTO `timeout_task` VALUES 
-- (NULL, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'TEST_001', 'ORDER_TIMEOUT', 0, (UNIX_TIMESTAMP() + 3600) * 1000, '{"orderId":"TEST_001","amount":100}', 0, 1),
-- (NULL, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'TEST_002', 'COUPON_EXPIRE', 0, (UNIX_TIMESTAMP() + 7200) * 1000, '{"couponId":"TEST_002","userId":"user001"}', 0, 2),
-- (NULL, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'TEST_003', 'PAYMENT_TIMEOUT', 0, (UNIX_TIMESTAMP() + 1800) * 1000, '{"paymentId":"TEST_003","orderId":"ORDER_001"}', 0, 1);

-- 创建管理员用户（如果有用户管理模块）
-- CREATE TABLE IF NOT EXISTS `timeout_user` (
--   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
--   `username` VARCHAR(64) NOT NULL,
--   `password` VARCHAR(128) NOT NULL,
--   `role` VARCHAR(32) NOT NULL DEFAULT 'USER',
--   `email` VARCHAR(128) DEFAULT NULL,
--   `phone` VARCHAR(32) DEFAULT NULL,
--   `status` TINYINT(1) NOT NULL DEFAULT 1,
--   `create_time` BIGINT(20) NOT NULL,
--   `update_time` BIGINT(20) NOT NULL,
--   PRIMARY KEY (`id`),
--   UNIQUE KEY `uk_username` (`username`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户管理表';

-- INSERT INTO `timeout_user` VALUES 
-- (NULL, 'admin', '$2a$10$9vIw5KDaB9R8Xv.sKXKKde8vVcJOSgYpCYqQ9B1vNhS1Vz.Kz8nYa', 'ADMIN', 'admin@example.com', '13800138000', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000);

-- 初始化完成
SELECT 'Initial data inserted successfully!' AS result;

-- 显示统计信息
SELECT 
  '任务类型' AS category, 
  COUNT(*) AS count 
FROM timeout_task_type 
WHERE is_active = 1

UNION ALL

SELECT 
  '系统配置' AS category, 
  COUNT(*) AS count 
FROM timeout_config 
WHERE is_active = 1

UNION ALL

SELECT 
  '集群节点' AS category, 
  COUNT(*) AS count 
FROM timeout_node 
WHERE status = 1;
