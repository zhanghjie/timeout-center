# 超时中心 (Timeout Center)

[![Java Version](https://img.shields.io/badge/Java-8+-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## 项目简介

超时中心是一款企业级分布式延时任务调度系统，专为解决大规模延时任务调度和管理而设计。系统采用时间轮算法作为核心调度引擎，支持多种RPC协议，提供高性能、高可用、易扩展的延时任务解决方案。

### 核心特性

- 🚀 **高性能时间轮算法** - 基于层次化时间轮实现，支持海量任务高效调度
- 🔄 **多协议RPC支持** - 支持Dubbo、gRPC、HTTP、Netty、Feign等多种通信协议
- 📊 **三层队列架构** - Store Queue、Prepare Queue、Dead Queue分层管理
- 🎯 **任务优先级调度** - 支持任务优先级，确保重要任务优先执行
- 💾 **多存储支持** - MySQL持久化 + Redis缓存 + 内存调度
- 🔧 **Web管理控制台** - 提供任务管理、监控、配置等Web界面
- 📈 **实时监控统计** - 任务执行状态、性能指标实时监控
- 🌐 **集群部署支持** - 支持分布式集群部署和故障转移
- 🔒 **可靠性保障** - 支持任务重试、失败回调、死信队列管理

### 技术架构

```
┌─────────────────────────────────────────────────────────────────┐
│                         Client Layer                           │
├─────────────────────────────────────────────────────────────────┤
│  Dubbo Client │ gRPC Client │ HTTP Client │ Netty Client │...  │
├─────────────────────────────────────────────────────────────────┤
│                      RPC Adapter Layer                         │
├─────────────────────────────────────────────────────────────────┤
│                     Service Layer                              │
├─────────────────────────────────────────────────────────────────┤
│              Time Wheel Scheduler Engine                       │
├─────────────────────────────────────────────────────────────────┤
│  Store Queue  │  Prepare Queue  │  Dead Queue  │  Cache Layer  │
├─────────────────────────────────────────────────────────────────┤
│              Storage Layer (MySQL + Redis)                     │
└─────────────────────────────────────────────────────────────────┘
```

## 快速开始

### 环境要求

- JDK 8+
- Maven 3.6+
- MySQL 5.7+
- Redis 5.0+
- Zookeeper 3.6+ (使用Dubbo协议时需要)

### 本地部署

1. **克隆项目**
```bash
git clone https://github.com/zhanghjie/timeout-center.git
cd timeout-center
```

2. **配置数据库**
```sql
-- 创建数据库
CREATE DATABASE timeout DEFAULT CHARACTER SET utf8mb4;

-- 导入表结构
source infrastructure/src/main/resources/sql/schema.sql

-- 导入初始数据
source infrastructure/src/main/resources/sql/init-data.sql
```

3. **配置Redis**
```bash
# 启动Redis服务
redis-server

# 验证连接
redis-cli ping
```

4. **修改配置文件**
```yaml
# infrastructure/src/main/resources/beta/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/timeout
spring.datasource.username=root
spring.datasource.password=your_password

spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=your_redis_password
```

5. **编译启动**
```bash
# 编译项目
mvn clean package -DskipTests

# 启动应用
java -jar starter/target/timeout-center.jar
```

6. **验证部署**
```bash
# 健康检查
curl http://localhost:8082/api/monitor/health

# 访问Swagger文档
http://localhost:8082/swagger-ui/index.html
```

### Docker部署

```bash
# 构建镜像
docker build -t timeout-center:latest .

# 启动容器
docker-compose up -d
```

## 使用指南

### 基本概念

- **业务类型(bizType)**: 业务场景标识，如"ORDER_TIMEOUT"、"COUPON_EXPIRE"
- **业务ID(bizId)**: 业务唯一标识，如订单号、优惠券ID
- **执行时间(actionTime)**: 任务期望执行的时间戳（毫秒）
- **任务数据(data)**: 业务自定义数据，JSON格式
- **优先级(order)**: 任务执行优先级，数值越小优先级越高

### API使用示例

#### 1. 添加延时任务

```bash
curl -X POST http://localhost:8082/api/timeout-task/add \
  -H "Content-Type: application/json" \
  -d '{
    "bizType": "ORDER_TIMEOUT",
    "bizId": "ORDER_123456",
    "actionTime": 1642089600000,
    "data": "{\"orderId\":\"123456\",\"userId\":\"user001\"}",
    "order": 1
  }'
```

#### 2. 查询任务状态

```bash
curl "http://localhost:8082/api/timeout-task/query?bizType=ORDER_TIMEOUT&bizId=ORDER_123456"
```

#### 3. 取消任务

```bash
curl -X DELETE "http://localhost:8082/api/timeout-task/cancel?bizType=ORDER_TIMEOUT&bizId=ORDER_123456"
```

### Java客户端使用

#### Maven依赖
```xml
<dependency>
    <groupId>com.public.common</groupId>
    <artifactId>timeout-center-api</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```

#### 代码示例
```java
@Autowired
private TimeoutCenterRpcService timeoutCenterService;

// 添加延时任务
AddTimeoutTaskDTO task = new AddTimeoutTaskDTO();
task.setBizType("ORDER_TIMEOUT");
task.setBizId("ORDER_123456");
task.setActionTime(System.currentTimeMillis() + 30 * 60 * 1000); // 30分钟后执行
task.setData("{\"orderId\":\"123456\"}");

RpcResponse<WebResponse> response = timeoutCenterService.addTimeoutTask(task);
```

### RPC协议配置

系统支持多种RPC协议，可通过配置文件切换：

```yaml
timeout-center:
  rpc:
    protocol: dubbo  # 可选: dubbo, grpc, http, netty, feign
```

#### Dubbo协议配置
```yaml
timeout-center:
  rpc:
    protocol: dubbo
    dubbo:
      registry: zookeeper://localhost:2181
      port: 20880
      group: timeout-center
```

#### gRPC协议配置
```yaml
timeout-center:
  rpc:
    protocol: grpc
    grpc:
      port: 9090
      host: localhost
```

#### HTTP协议配置
```yaml
timeout-center:
  rpc:
    protocol: http
    http:
      base-url: http://localhost:8080
      context-path: /timeout-center
```

## 架构设计

### 时间轮调度器

采用层次化时间轮算法，支持：
- 多层时间轮自动扩展
- 高精度时间调度（毫秒级）
- 海量任务高效管理
- 内存使用优化

### 三层队列架构

1. **Store Queue（存储队列）**
   - 基于Redis有序集合实现
   - 按执行时间排序存储
   - 支持快速查询到期任务

2. **Prepare Queue（准备队列）**
   - 存储即将执行的任务
   - 支持优先级调度
   - 提供批量处理能力

3. **Dead Queue（死信队列）**
   - 存储执行失败的任务
   - 支持重试机制
   - 便于问题排查和数据恢复

### 任务状态流转

```
WAIT → EXECUTION → SUCCESS
  ↓        ↓
CANCEL   FAILED → Dead Queue → RETRY
```

- **WAIT**: 等待执行
- **EXECUTION**: 执行中
- **SUCCESS**: 执行成功
- **FAILED**: 执行失败
- **CANCEL**: 已取消

### 集群架构

支持多节点集群部署：
- 节点自动发现和注册
- 任务负载均衡分配
- 故障自动转移
- 数据一致性保障

## 监控和运维

### Web管理控制台

访问 `http://localhost:8082/swagger-ui/index.html` 使用Web控制台：

- 📊 **任务管理**: 查看、添加、取消、重试任务
- 📈 **监控大盘**: 实时任务状态、性能指标
- ⚙️ **配置管理**: 任务类型配置、系统参数调整
- 🔧 **协议切换**: 动态切换RPC通信协议

### 监控指标

系统提供丰富的监控指标：

- **业务指标**: 任务总数、成功率、失败率、平均延迟
- **性能指标**: QPS、内存使用率、线程池状态
- **系统指标**: JVM状态、连接数、时间轮状态

### 健康检查

```bash
# 系统健康检查
curl http://localhost:8082/api/monitor/health

# 获取系统状态
curl http://localhost:8082/api/monitor/status

# 获取性能指标
curl http://localhost:8082/api/monitor/metrics
```

## 性能基准

### 测试环境
- CPU: 8核
- 内存: 16GB
- 磁盘: SSD
- 网络: 千兆网卡

### 性能数据
- **单机QPS**: 1000+
- **任务精度**: 毫秒级
- **内存使用**: 优化的时间轮结构，内存效率高
- **延迟**: 平均延迟 < 10ms（非拥堵情况下）

### 容量规划
- **建议单机任务量**: < 500 QPS
- **集群扩展**: 支持水平扩展，线性提升处理能力
- **存储容量**: 支持海量数据存储（分库分表）

## 常见问题

### Q: 如何保证任务不重复执行？
A: 系统通过数据库乐观锁机制，确保任务状态变更的原子性，避免重复执行。

### Q: 任务延迟精度如何？
A: 在非拥堵情况下，任务延迟精度为毫秒级，实际延迟通常在10ms以内。

### Q: 如何处理大批量任务？
A: 系统支持批量处理和限流机制，当单业务QPS超过500时会自动限流保护。

### Q: 支持动态扩容吗？
A: 支持。可通过配置管理动态调整集群节点，系统会自动进行负载均衡。

### Q: 如何切换RPC协议？
A: 可通过配置文件修改或使用管理接口动态切换，无需重启服务。

## 开发指南

### 项目结构

```
timeout-center/
├── api/                    # API定义和RPC协议
├── domain/                 # 领域模型和业务逻辑
├── infrastructure/         # 基础设施层
├── timeout-interface/      # 接口实现层
├── starter/               # 启动模块
└── docs/                  # 文档
```

### 本地开发

1. 导入IDE (推荐IntelliJ IDEA)
2. 配置开发环境数据库和Redis
3. 启动 `TimeoutCenterApplication`
4. 访问 `http://localhost:8082/swagger-ui/index.html` 进行API测试

### 扩展开发

#### 添加新的RPC协议
1. 实现 `TimeoutCenterRpcService` 接口
   2. 在 `RpcServiceFactory` 中注册新协议
3. 添加对应的配置类

#### 添加新的任务类型
1. 在数据库中配置任务类型
2. 实现对应的任务处理器
3. 配置消息队列Topic

### 代码规范

- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 完善的单元测试覆盖
- 详细的API文档注释

## 版本历史

### v1.0.1-SNAPSHOT (当前版本)
- ✨ 新增多协议RPC支持
- ✨ 重构三队列架构
- ✨ 添加Web管理控制台
- ✨ 完善监控和统计功能
- 🐛 修复时间轮内存泄漏问题
- 🐛 修复Redis Lua脚本语法错误
- ⚡ 优化任务调度性能

### v1.0.0
- 🎉 初始版本发布
- ⚡ 基于时间轮的任务调度
- 💾 MySQL + Redis存储支持
- 🔄 基本的任务生命周期管理

## 贡献指南

我们欢迎社区贡献！请查看 [CONTRIBUTING.md](CONTRIBUTING.md) 了解详细信息。

### 贡献方式
1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

## 许可证

本项目采用 Apache License 2.0 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

## 联系我们

- 项目主页: https://github.com/zhanghjie/timeout-center
- 问题反馈: https://github.com/zhanghjie/timeout-center/issues
- 邮箱: zhanghjie7@163.com

---

**⭐ 如果这个项目对您有帮助，请给我们一个Star！**