-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: brushtopic
-- ------------------------------------------------------
-- Server version	8.0.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ai_history`
--

DROP TABLE IF EXISTS `ai_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '历史id',
  `title` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'ai标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'ai内容',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账户',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `chat_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '对话id',
  `status` tinyint DEFAULT NULL COMMENT '1.提出面试题 1.评估答案 ',
  `parent` tinyint DEFAULT NULL COMMENT '1是父层级',
  `mode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'system系统模式 model模型模式 mix混合模式 ',
  `original_title` text COLLATE utf8mb4_unicode_ci COMMENT '原始题目',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ai历史表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_history`
--

LOCK TABLES `ai_history` WRITE;
/*!40000 ALTER TABLE `ai_history` DISABLE KEYS */;
INSERT INTO `ai_history` VALUES (220,'Java基础','### 【Java基础】专题 💡\n\n## 面试题目：\n**Java 中的基本数据类型有哪些？**\n\n> ✨ 你可以的，相信自己！',1,'admin','2025-05-08 11:33:06','2025-05-08 11:33:06','92c6941d-a9f6-4317-8f74-b5ef09d71864',0,1,'system',NULL),(221,'Java 有8种基本数据类型：\n\n整型：byte（1字节）、short（2字节）、int（4字节）、long（8字节）\n浮点型：float（4字节）、double（8字节）\n字符型：char（2字节）\n布尔型：boolean（值只能是 true 或 false）','### 评估信息：\n\n**准确率：优秀（90%-100%）**\n\n你的回答非常完整，涵盖了 Java 中所有的 8 种基本数据类型，并且对每种类型的字节大小和特性也进行了正确的描述。以下是具体评估：\n\n1. **准确性**：\n   - 你正确列出了所有基本数据类型：`byte`、`short`、`int`、`long`、`float`、`double`、`char` 和 `boolean`。\n   - 对每种类型的字节大小和用途的描述完全正确。\n   - 特别是提到 `boolean` 的值只能是 `true` 或 `false`，这一点非常重要，体现了你对细节的关注。\n\n2. **表达清晰度**：\n   - 表达逻辑清晰，结构分明，按照整型、浮点型、字符型和布尔型分类说明，条理清楚。\n   - 使用了简洁明了的语言，易于理解。\n\n3. **完整性状态**：\n   - 回答内容完整，没有遗漏任何关键信息。\n   - 对每种数据类型都提供了足够的细节（例如字节大小），并且明确指出了 `boolean` 类型的取值范围。\n\n### 总结：\n你的回答表现非常优秀，准确率达到了 100%，无需改进！继续保持这种细致和全面的学习态度。\n\n> 请输入\'**继续**\'或者输入新的**题目类型**',1,'admin','2025-05-08 11:33:54','2025-05-08 11:33:58','92c6941d-a9f6-4317-8f74-b5ef09d71864',1,0,'system',NULL),(222,'Java基础','### 【Java基础】专题 💡\n\n## 面试题目：\n** Java 中的线程有哪些状态？**\n\n> ⏳ 时间会证明你的坚持！',1,'admin','2025-05-08 11:33:58','2025-05-08 11:33:57','92c6941d-a9f6-4317-8f74-b5ef09d71864',0,NULL,'system',NULL),(223,'阿斯顿发斯蒂芬几艘大家','### 评估信息：\n\n#### 回答内容：\n用户回答：**阿斯顿发斯蒂芬几艘大家**\n\n#### 准确率评估：\n- **准确率**：较差（0%-49%）\n  - 用户的回答与题目完全无关，未涉及 Java 线程状态的相关内容。\n- **表达清晰度**：较差\n  - 回答内容语义不明，无法理解其意图。\n- **完整性状态**：不完整\n  - 没有提供任何与 Java 线程状态相关的信息。\n\n---\n\n### 改进后的回答：\nJava 中的线程有以下六种状态（根据 `Thread.State` 枚举）：\n1. **NEW（新建）**：线程对象被创建但尚未启动（即未调用 `start()` 方法）。\n2. **RUNNABLE（可运行）**：线程正在 JVM 中执行，但可能在等待操作系统资源（如 CPU）。\n3. **BLOCKED（阻塞）**：线程被阻塞，等待获取一个监视器锁以进入同步块/方法。\n4. **WAITING（等待）**：线程无限期等待另一个线程执行特定操作（如调用 `Object.notify()` 或 `Object.notifyAll()`）。\n5. **TIMED_WAITING（计时等待）**：线程在指定时间内等待另一个线程执行操作（如调用 `Thread.sleep()`、`Object.wait(long timeout)` 等）。\n6. **TERMINATED（终止）**：线程已完成执行或因异常退出。\n\n这些状态可以通过 `Thread.getState()` 方法获取，帮助开发者调试和优化多线程程序。\n\n---\n\n> 请输入\'**继续**\'或者输入新的**题目类型**',1,'admin','2025-05-08 11:34:26','2025-05-08 11:33:57','92c6941d-a9f6-4317-8f74-b5ef09d71864',1,NULL,'system',NULL),(224,'前端三件套','### 【前端三件套】专题 💡\n\n## 面试题目：\n** HTML5 有哪些新特性？**\n\n> 🔥 别放弃，再想想看～',1,'admin','2025-05-08 11:34:39','2025-05-08 11:34:38','92c6941d-a9f6-4317-8f74-b5ef09d71864',0,NULL,'system',NULL),(225,'HTML5 引入了新的语义化标签（如 <header>、<footer>、<article>）、表单控件（如 email、date）、多媒体支持（<audio>、<video>）、Canvas 绘图、本地存储（localStorage/sessionStorage）等','### 评估信息：\n\n#### 准确率：优秀（90%-100%）\n你的回答非常全面，涵盖了 HTML5 的主要新特性，包括语义化标签、表单控件、多媒体支持、Canvas 绘图以及本地存储等内容。这些都是 HTML5 的核心特点，准确率非常高。\n\n#### 表达清晰度：优秀\n你的表达逻辑清晰，条理分明，每个点都简洁明了，易于理解。\n\n#### 完整性状态：优秀\n你提到的内容已经非常完整，基本覆盖了 HTML5 的主要新特性，没有显著遗漏。\n\n---\n\n> 请输入\'**继续**\'或者输入新的**题目类型**',1,'admin','2025-05-08 11:34:56','2025-05-08 11:34:38','92c6941d-a9f6-4317-8f74-b5ef09d71864',1,NULL,'system',NULL),(226,'Redis','### 【Redis】类型 💡\n\n## 面试题目：\n**Redis如何实现分布式锁？**\n\n> 🧠 慢慢来，答案就在前方～',1,'admin','2025-05-08 11:35:45','2025-05-08 11:35:45','bdb2d303-43a0-4329-aa68-8f3dd396f2d6',0,1,'model','Redis如何实现分布式锁？'),(227,'使用setNx实现','### 评估信息：\n\n**准确率：良好（70%-89%）**  \n你的回答提到了使用 `setNx` 实现分布式锁，这确实是 Redis 实现分布式锁的一种核心方法。`SETNX`（Set if Not Exists）命令可以确保只有一个客户端能够成功设置锁，从而实现互斥性。因此，你的回答抓住了关键点。\n\n**表达清晰度：一般**  \n虽然你提到了使用 `setNx`，但没有进一步解释其工作原理、如何保证锁的安全性、如何设置过期时间防止死锁等问题。回答显得过于简略，缺乏细节和深度。\n\n**完整性状态：一般**  \n回答缺少对分布式锁完整实现的描述，比如：\n1. 如何防止死锁（设置过期时间）。\n2. 如何保证锁的原子性（如 Redis 的 `SET` 命令结合 `NX` 和 `EX` 参数）。\n3. 如何释放锁（使用 Lua 脚本保证解锁操作的原子性）。\n4. 其他注意事项（如锁续期、高并发场景下的性能优化等）。\n\n---\n\n### 改进后的回答：\n\nRedis 实现分布式锁的核心思想是利用 Redis 的单线程特性和原子操作来保证多个客户端之间的互斥访问。以下是完整的实现步骤：\n\n1. **使用 SETNX 或 SET 命令加锁**  \n   - 使用 `SET key value NX EX timeout` 命令，其中：\n     - `NX` 表示只有当键不存在时才设置键值对。\n     - `EX timeout` 表示设置键的过期时间，防止死锁。\n     - `value` 通常是唯一标识（如 UUID），用于区分不同客户端的锁。\n   - 示例：`SET lock_key client_id NX EX 10`，表示尝试获取一个名为 `lock_key` 的锁，过期时间为 10 秒。\n\n2. **防止死锁**  \n   - 设置锁的过期时间非常重要，避免因客户端崩溃导致锁无法释放。\n   - 如果业务逻辑执行时间可能超过锁的过期时间，可以引入锁续期机制（如使用定时任务定期延长锁的有效期）。\n\n3. **释放锁**  \n   - 释放锁时需要验证锁的持有者，确保只有加锁的客户端才能释放锁。\n   - 使用 Lua 脚本保证解锁操作的原子性，例如：\n     ```lua\n     if redis.call(\"GET\", KEYS[1]) == ARGV[1] then\n         return redis.call(\"DEL\", KEYS[1])\n     else\n         return 0\n     end\n     ```\n     这段脚本会检查锁的值是否与当前客户端的标识一致，如果一致则删除锁。\n\n4. **其他注意事项**  \n   - 在高并发场景下，建议使用 Redisson 等成熟的分布式锁库，它们封装了上述逻辑并提供了更多高级功能（如公平锁、可重入锁等）。\n   - 如果 Redis 是主从架构，注意主节点故障时可能导致锁丢失的问题，可以考虑使用 Redlock 算法（多节点协调）。\n\n---\n\n### 总结：\n你的回答抓住了核心点，但缺少实现细节和扩展内容。改进后的内容更全面，涵盖了锁的获取、释放、安全性以及实际应用中的注意事项。\n\n> 请输入\'**继续**\'或者输入新的**题目类型**',1,'admin','2025-05-08 11:36:33','2025-05-08 11:36:38','bdb2d303-43a0-4329-aa68-8f3dd396f2d6',1,0,'model','Redis如何实现分布式锁？'),(228,'Redis','### 【Redis】类型 💡\n\n## 面试题目：\n**Redis如何实现缓存穿透的防护？**\n\n> 💡 这道题对你来说不是问题！',1,'admin','2025-05-08 11:36:39','2025-05-08 11:36:39','bdb2d303-43a0-4329-aa68-8f3dd396f2d6',0,NULL,'model','Redis如何实现缓存穿透的防护？'),(229,'Redis如何实现缓存穿透的防护？','好的，请提供你的回答，我会根据内容进行评估并给出准确率、表达清晰度和完整性状态。如果准确率低于良好，我会帮你改进回答。\n\n请输入\'**继续**\'或者输入新的**题目类型**\'',1,'admin','2025-05-08 11:36:50','2025-05-08 11:36:39','bdb2d303-43a0-4329-aa68-8f3dd396f2d6',1,NULL,'model','Redis如何实现缓存穿透的防护？'),(230,'Redis','### 【Redis】类型 💡\n\n## 面试题目：\n**Redis缓存雪崩、缓存穿透、缓存击穿分别是什么？如何解决这些问题？**\n\n> 🌠 梦想就在不远处等着你！',1,'admin','2025-05-08 11:38:23','2025-05-08 11:38:23','067c3057-360c-47cc-9da8-aaefdcab8840',0,1,'model','Redis缓存雪崩、缓存穿透、缓存击穿分别是什么？如何解决这些问题？'),(231,'缓存雪崩\n定义：\n缓存雪崩指的是当大量缓存数据在同一时间段内过期，导致大量的请求直接打到数据库上，造成数据库压力过大甚至崩溃的情况。\n\n解决方案：\n\n设置不同的过期时间： 给每个缓存数据设置随机的过期时间，避免大量缓存同时失效。\n使用互斥锁（mutex lock）: 当某个缓存未命中时，首先尝试获取一个分布式锁，只有成功获得锁的那个请求才会去加载数据并更新缓存，其他请求则等待或直接返回默认值。\n构建多级缓存架构： 在应用层与Redis之间增加一层本地缓存，如Ehcache等，进一步减少对Redis的压力。\n缓存穿透\n定义：\n缓存穿透是指查询一个根本不存在的数据，由于缓存中没有该数据的信息，所以每次都会访问数据库，可能导致数据库过载。\n\n解决方案：\n\n接口层校验： 对于传入参数进行合法性检查，例如用户ID是否为负数或者明显不合理的值。\n布隆过滤器（Bloom Filter）： 使用布隆过滤器预先判断某个key是否存在，如果不存在则直接返回，减少不必要的数据库查询。\n缓存空对象： 如果查询结果为空，则也将这个空结果放入缓存中，并设置较短的有效期。\n缓存击穿\n定义：\n缓存击穿通常指热点数据（即非常频繁被访问的数据）在缓存过期的瞬间，大量并发请求同时到达，这些请求都会试图从数据库中读取数据，从而可能压垮数据库。\n\n解决方案：\n\n永不过期策略： 对于特别热点的数据可以采用逻辑上的永不过期策略，即在业务代码层面忽略缓存过期时间，而是在后台异步更新缓存内容。\n互斥锁机制： 类似于解决缓存雪崩的方法之一，在首次发现缓存缺失时加锁，保证只有一个线程去数据库加载数据并更新缓存，其余请求等待或者返回旧缓存数据。\n提前刷新缓存： 可以根据访问频率预测哪些是热点数据，并在缓存即将过期前主动触发缓存更新操作，防止大量请求同时到达数据库。\n通过上述方法，可以在一定程度上缓解这些问题带来的影响，提高系统的稳定性和响应速度。','### 评估信息\n\n**准确率：优秀（90%-100%）**\n\n你的回答非常全面，涵盖了缓存雪崩、缓存穿透和缓存击穿的概念及解决方案。每个问题的定义清晰准确，提出的解决方案也贴合实际开发中的常见做法，包括设置随机过期时间、使用布隆过滤器、缓存空对象、互斥锁机制等。整体内容逻辑性强，层次分明，且对问题的理解深入。\n\n**表达清晰度：优秀**\n\n你的语言表达流畅，结构清晰，条理分明。通过分段的方式逐一解决问题，并且每个解决方案都给出了具体的操作方法，易于理解。\n\n**完整性状态：完整**\n\n你的回答覆盖了所有核心知识点，没有遗漏重要概念或解决方案，同时提供了多种解决思路，展现了思维的广度和深度。\n\n> 请输入\'**继续**\'或者输入新的**题目类型**\'',1,'admin','2025-05-08 11:39:06','2025-05-08 11:39:11','067c3057-360c-47cc-9da8-aaefdcab8840',1,0,'model','Redis缓存雪崩、缓存穿透、缓存击穿分别是什么？如何解决这些问题？'),(232,'Redis','### 【Redis】类型 💡\n\n## 面试题目：\n**Redis如何实现分布式锁？请详细说明其实现原理和注意事项。**\n\n> 🦸 你就是自己的超级英雄！',1,'admin','2025-05-08 11:39:13','2025-05-08 11:39:12','067c3057-360c-47cc-9da8-aaefdcab8840',0,NULL,'model','Redis如何实现分布式锁？请详细说明其实现原理和注意事项。'),(233,'斯蒂芬斯哦飞机撒旦法','### 评估信息：\n\n#### 准确率：较差（0%-49%）\n你的回答内容“斯蒂芬斯哦飞机撒旦法”完全不相关，无法体现对Redis分布式锁实现原理的理解，也没有提到任何相关的技术细节或注意事项。\n\n#### 表达清晰度：较差\n回答内容完全没有逻辑性和条理性，表达混乱且无法理解。\n\n#### 完整性状态：较差\n回答未包含任何与问题相关的内容，既没有解释Redis分布式锁的实现原理，也没有说明注意事项，完整性严重缺失。\n\n---\n\n### 改进后的回答：\n\nRedis实现分布式锁的核心思想是利用Redis的原子操作特性来确保多个客户端对共享资源的安全访问。以下是详细的实现原理和注意事项：\n\n#### **实现原理**\n1. **使用SETNX命令**：\n   - Redis的`SETNX`（Set if Not Exists）命令可以用来设置一个键值对，但只有在键不存在时才会成功。这保证了只有一个客户端能够成功获取锁。\n   - 示例：`SETNX lock_key 1`，如果返回1，则表示获取锁成功；如果返回0，则表示锁已被其他客户端占用。\n\n2. **设置过期时间**：\n   - 为了防止死锁（例如某个客户端崩溃后未能释放锁），需要为锁设置一个过期时间。可以使用`EXPIRE`命令单独设置，或者直接在`SET`命令中使用`NX`和`EX`选项。\n   - 示例：`SET lock_key 1 NX EX 10`，表示设置一个10秒后自动过期的锁。\n\n3. **唯一标识符**：\n   - 为了避免误删他人持有的锁，每个客户端在获取锁时应将锁的值设置为一个唯一标识符（如UUID）。释放锁时，先检查锁的值是否匹配，只有匹配时才删除锁。\n\n4. **释放锁**：\n   - 释放锁的操作需要保证原子性，可以通过Lua脚本实现：\n     ```lua\n     if redis.call(\"GET\", KEYS[1]) == ARGV[1] then\n         return redis.call(\"DEL\", KEYS[1])\n     else\n         return 0\n     end\n     ```\n   - 这段脚本会先检查锁的值是否与当前客户端的唯一标识符一致，如果一致则删除锁，否则不做任何操作。\n\n#### **注意事项**\n1. **锁的过期时间设置**：\n   - 过期时间需要根据业务场景合理设置，避免过短导致锁提前失效，或过长导致资源浪费。\n\n2. **锁的续期机制**：\n   - 如果业务执行时间可能超过锁的过期时间，可以引入续期机制，例如通过后台线程定期延长锁的有效期。\n\n3. **高可用性**：\n   - 使用单节点Redis可能导致单点故障，建议使用Redis集群或Redlock算法来提高可靠性。\n\n4. **性能优化**：\n   - 在高并发场景下，频繁地尝试获取锁可能会导致性能瓶颈，可以结合随机退避算法减少竞争。\n\n5. **异常处理**：\n   - 需要考虑网络分区、Redis宕机等异常情况，并设计相应的容错机制。\n\n---\n\n> 请输入\'**继续**\'或者输入新的**题目类型**\'',1,'admin','2025-05-08 11:39:58','2025-05-08 11:39:12','067c3057-360c-47cc-9da8-aaefdcab8840',1,NULL,'model','Redis如何实现分布式锁？请详细说明其实现原理和注意事项。'),(234,'前端','### 【前端】类型 💡\n\n## 面试题目：\n**请解释一下什么是虚拟DOM，以及它在React中的作用和优势。**\n\n> 🌠 梦想就在不远处等着你！',1,'admin','2025-05-08 11:40:27','2025-05-08 11:40:27','067c3057-360c-47cc-9da8-aaefdcab8840',0,NULL,'model','请解释一下什么是虚拟DOM，以及它在React中的作用和优势。'),(238,'Mysql','### 【Mysql】专题 💡\n\n## 面试题目：\n**在MySQL中，如何实现全文搜索功能？**\n\n> 🌻 你的努力正在开花结果！',16,'HaoMember','2025-05-08 13:13:20','2025-05-08 13:13:20','4f13afd4-2589-44ac-869e-8c1517acdd87',0,1,'system',NULL),(239,'sdfssjidfijosdf','### 评估信息：\n\n#### 准确率：\n你的回答内容为“sdfssjidfijosdf”，完全与问题无关，未涉及 MySQL 全文搜索功能的实现方法。因此准确率为 **0%**，属于 **较差（0%-49%）**。\n\n#### 表达清晰度：\n回答内容是无意义的字符组合，无法理解表达意图，清晰度评分为 **0%**。\n\n#### 完整性状态：\n回答内容没有提到任何关于 MySQL 全文搜索功能的实现方式，完整性评分为 **0%**。\n\n---\n\n### 改进后的回答：\n在 MySQL 中，可以通过以下步骤实现全文搜索功能：\n\n1. **创建支持全文索引的表**  \n   确保表中有一个或多个字段适合进行全文搜索，并为这些字段添加 FULLTEXT 索引。例如：\n   ```sql\n   CREATE TABLE articles (\n       id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,\n       title VARCHAR(200),\n       body TEXT,\n       FULLTEXT (title, body)\n   ) ENGINE=InnoDB;\n   ```\n\n2. **插入数据**  \n   向表中插入一些测试数据：\n   ```sql\n   INSERT INTO articles (title, body) VALUES\n   (\'MySQL Full-Text Search\', \'This is an example of full-text search in MySQL.\'),\n   (\'Database Indexing\', \'Learn how to use indexing for better performance.\');\n   ```\n\n3. **使用 MATCH() AGAINST() 进行全文搜索**  \n   使用 `MATCH()` 和 `AGAINST()` 函数执行全文搜索查询。例如：\n   ```sql\n   SELECT * FROM articles\n   WHERE MATCH(title, body) AGAINST(\'full-text search\' IN NATURAL LANGUAGE MODE);\n   ```\n\n4. **全文搜索模式**  \n   - **自然语言模式 (NATURAL LANGUAGE MODE)**：默认模式，根据自然语言解析搜索词。\n   - **布尔模式 (BOOLEAN MODE)**：支持更复杂的搜索规则，例如使用 `+`、`-` 操作符。\n   - **查询扩展模式 (WITH QUERY EXPANSION)**：自动扩展搜索结果范围。\n\n5. **注意事项**  \n   - 全文索引仅适用于 MyISAM 和 InnoDB 存储引擎。\n   - 最小和最大词长度由 MySQL 配置参数 (`ft_min_word_len` 和 `ft_max_word_len`) 决定。\n   - 停用词列表会影响搜索结果，某些常见单词（如 \"the\"、\"and\"）会被忽略。\n\n---\n\n> 请输入\'**继续**\'或者输入新的**题目类型**',16,'HaoMember','2025-05-08 13:13:56','2025-05-08 13:14:01','4f13afd4-2589-44ac-869e-8c1517acdd87',1,0,'system',NULL),(240,'Mysql','### 【Mysql】专题 💡\n\n## 面试题目：\n**描述一下MySQL复制（Replication）的工作原理。**\n\n> 🏆 冠军的潜力就在你心中！',16,'HaoMember','2025-05-08 13:14:01','2025-05-08 13:14:01','4f13afd4-2589-44ac-869e-8c1517acdd87',0,NULL,'system',NULL),(241,'前端三件套','### 【前端三件套】专题 💡\n\n## 面试题目：\n**解释一下 Promise 和 async/await 的关系。**\n\n> 🦸 你就是自己的超级英雄！',1,'admin','2025-05-08 13:24:22','2025-05-08 13:24:22','92c6941d-a9f6-4317-8f74-b5ef09d71864',0,NULL,'system',NULL);
/*!40000 ALTER TABLE `ai_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_log`
--

DROP TABLE IF EXISTS `ai_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账户',
  `is_read` tinyint DEFAULT '0' COMMENT '是否已读 0 未读 1已读',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ai操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_log`
--

LOCK TABLES `ai_log` WRITE;
/*!40000 ALTER TABLE `ai_log` DISABLE KEYS */;
INSERT INTO `ai_log` VALUES (127,'生成AI答案成功啦！',1,'admin',0,'2025-05-08 11:19:42'),(128,'生成AI答案成功啦！',1,'admin',0,'2025-05-08 11:22:01'),(129,'AI回复异常',1,'admin',0,'2025-05-08 11:22:28'),(130,'服务器发生异常',1,'admin',0,'2025-05-08 11:22:28'),(131,'服务器发生异常',16,'HaoMember',0,'2025-05-08 11:27:26'),(132,'生成AI答案成功啦！',1,'admin',0,'2025-05-08 11:27:32'),(133,'AI回复异常',1,'admin',0,'2025-05-08 11:27:34'),(134,'服务器发生异常',1,'admin',0,'2025-05-08 11:27:34'),(135,'生成AI答案成功啦！',1,'admin',0,'2025-05-08 11:28:13'),(136,'生成AI答案成功啦！',1,'admin',0,'2025-05-08 11:28:17'),(137,'审核通过',16,'HaoMember',0,'2025-05-08 11:49:29'),(138,'名称包含可能引发误解的词语，不符合公序良俗',16,'HaoMember',0,'2025-05-08 11:49:46'),(139,'服务器发生异常',16,'HaoMember',0,'2025-05-08 11:58:23'),(140,'服务器发生异常',1,'admin',0,'2025-05-08 11:58:31'),(141,'服务器发生异常',16,'HaoMember',0,'2025-05-08 12:28:23'),(142,'AI回复异常',1,'admin',0,'2025-05-08 12:28:34'),(143,'服务器发生异常',1,'admin',0,'2025-05-08 12:28:34'),(144,'服务器发生异常',16,'HaoMember',0,'2025-05-08 12:58:23'),(145,'服务器发生异常',1,'admin',0,'2025-05-08 12:58:29'),(146,'生成AI答案成功啦！',1,'admin',0,'2025-05-08 13:10:42'),(147,'服务器发生异常',16,'HaoMember',0,'2025-05-08 13:28:23'),(148,'服务器发生异常',1,'admin',0,'2025-05-08 13:28:29'),(149,'服务器发生异常',16,'HaoMember',0,'2025-05-08 13:58:23'),(150,'服务器发生异常',1,'admin',0,'2025-05-08 13:58:30'),(151,'服务器发生异常',16,'HaoMember',0,'2025-05-08 15:24:47'),(152,'服务器发生异常',1,'admin',0,'2025-05-08 15:24:48');
/*!40000 ALTER TABLE `ai_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_record`
--

DROP TABLE IF EXISTS `ai_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '用户昵称',
  `count` bigint DEFAULT NULL COMMENT '次数',
  `ai_time` date DEFAULT (curdate()) COMMENT 'ai使用日期',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='ai记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_record`
--

LOCK TABLES `ai_record` WRITE;
/*!40000 ALTER TABLE `ai_record` DISABLE KEYS */;
INSERT INTO `ai_record` VALUES (1,1,'HaoHao',100,'2025-05-04','2025-05-05 14:23:02'),(2,1,'HaoHao',2,'2025-05-05','2025-05-05 14:25:34'),(3,16,'如果',9,'2025-05-06','2025-05-06 09:42:57'),(4,1,'HaoHao',113,'2025-05-06','2025-05-06 14:36:49'),(5,1,'HaoHao',25,'2025-05-07','2025-05-07 03:37:26'),(6,17,'HaoY',1,'2025-05-07','2025-05-07 05:08:50'),(7,1,'HaoHao',22,'2025-05-08','2025-05-08 13:24:22'),(8,16,'如果',4,'2025-05-08','2025-05-08 13:14:01');
/*!40000 ALTER TABLE `ai_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_user`
--

DROP TABLE IF EXISTS `ai_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '账户',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `ai_count` bigint DEFAULT '0' COMMENT 'ai使用次数',
  `status` tinyint DEFAULT '0' COMMENT '状态 0正常 1停用',
  `count` bigint DEFAULT '100' COMMENT '总次数',
  `recently_used_time` timestamp NULL DEFAULT NULL COMMENT '最近使用时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0',
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='ai使用用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_user`
--

LOCK TABLES `ai_user` WRITE;
/*!40000 ALTER TABLE `ai_user` DISABLE KEYS */;
INSERT INTO `ai_user` VALUES (2,'admin',1,322,0,100,'2025-05-08 13:24:22','2025-04-24 13:56:53','2025-05-08 13:24:22',0,'管理员'),(3,'HaoMember',16,15,0,100,'2025-05-08 13:14:01','2025-04-26 03:44:22','2025-05-08 13:14:01',0,'会员'),(4,'HaoHy',17,1,0,100,'2025-05-07 05:08:50','2025-05-07 05:08:50','2025-05-07 05:08:50',0,'用户');
/*!40000 ALTER TABLE `ai_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_feedback`
--

DROP TABLE IF EXISTS `sys_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_feedback` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '反馈账户',
  `user_id` bigint DEFAULT NULL COMMENT '反馈用户id',
  `feedback_content` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '反馈内容',
  `reply_content` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '回复内容',
  `status` tinyint DEFAULT '0' COMMENT '0未回复 1已回复',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 1删除',
  `reply_id` bigint DEFAULT NULL COMMENT '回复人id',
  `reply_account` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '回复人名称',
  `reply_time` timestamp NULL DEFAULT NULL COMMENT '回复时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='用户反馈';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_feedback`
--

LOCK TABLES `sys_feedback` WRITE;
/*!40000 ALTER TABLE `sys_feedback` DISABLE KEYS */;
INSERT INTO `sys_feedback` VALUES (17,'HaoMember',16,'题目54:答案不行啊','不好意思',1,'2025-05-08 13:11:43','2025-05-08 13:15:38',0,1,'admin','2025-05-08 13:15:38'),(18,'HaoMember',16,'系统界面不好看吧','会优惠',1,'2025-05-08 13:17:38','2025-05-08 13:18:03',0,1,'admin','2025-05-08 13:18:03');
/*!40000 ALTER TABLE `sys_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '菜单层级',
  `sorted` int DEFAULT '0' COMMENT '菜单排序',
  `route` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '菜单标识',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 1删除',
  `icon` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (2,'系统管理',0,3,'/system','2025-04-06 12:37:04','2025-04-06 12:37:04',0,'SettingOutlined'),(3,'用户管理',2,1,'/system/user','2025-04-06 13:08:03','2025-04-06 13:08:03',0,'UserOutlined'),(4,'角色管理',2,2,'/system/role','2025-04-06 13:08:35','2025-04-06 13:08:35',0,'MenuOutlined'),(5,'菜单管理',2,3,'/system/menu','2025-04-06 13:08:49','2025-04-06 13:08:49',0,'SafetyCertificateOutlined'),(6,'测试擦地',2,1,'131',NULL,'2025-04-08 08:08:05',1,'AimOutlined'),(7,'测试1',0,31,'11',NULL,'2025-04-08 08:39:05',1,'AlertOutlined'),(8,'测试删除',0,123,'12',NULL,'2025-04-08 08:42:21',1,'AlibabaOutlined'),(9,'124',8,123,'123',NULL,'2025-04-08 08:44:42',1,'AudioMutedOutlined'),(10,'sdf',0,2,'123',NULL,'2025-04-08 08:46:21',1,'ArrowRightOutlined'),(11,'231',0,123,'123','2025-04-08 08:56:30','2025-04-08 08:56:29',1,'AlertOutlined'),(12,'题库管理',0,1,'/topic','2025-04-13 06:12:24','2025-04-13 07:34:36',0,'BookOutlined'),(13,'题目管理',12,1,'/topic/topic','2025-04-13 06:13:21','2025-04-13 07:35:06',0,'FileTextOutlined'),(14,'分类管理',12,2,'/topic/category','2025-04-13 06:13:42','2025-04-13 07:35:28',0,'AppstoreOutlined'),(15,'标签管理',12,3,'/topic/label','2025-04-13 06:14:07','2025-04-13 07:35:37',0,'TagOutlined'),(16,'专题管理',12,4,'/topic/subject','2025-04-13 06:14:54','2025-04-13 07:35:46',0,'ProfileOutlined'),(17,'模型管理',0,4,'/ai','2025-04-18 14:07:36','2025-04-18 14:19:36',0,'RobotOutlined'),(18,'AI管理',17,1,'/ai/manage','2025-04-18 14:12:08','2025-04-18 14:32:49',0,'ApiOutlined'),(19,'AI模型',17,2,'/ai/model','2025-04-18 14:13:17','2025-04-18 14:20:04',0,'ExperimentOutlined'),(20,'个人中心',0,5,'/profile','2025-05-02 14:40:48','2025-05-02 14:40:48',0,'UserOutlined'),(21,'反馈管理',2,4,'/system/feedback','2025-05-02 14:43:26','2025-05-02 14:43:25',0,'InfoCircleOutlined');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `user_id` bigint DEFAULT NULL COMMENT '创建人用户id',
  `content` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '通知内容',
  `status` tinyint DEFAULT NULL COMMENT '0会员支付 1意见反馈 2回复内容 3题目反馈',
  `is_read` tinyint DEFAULT '0' COMMENT '0未读 1已读',
  `recipients_id` bigint DEFAULT NULL COMMENT '接收人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (1,'HaoMember',16,'题目54:答案不行啊',3,1,NULL,'2025-05-08 13:11:43','2025-05-08 13:15:20'),(2,'admin',1,'不好意思',2,1,16,'2025-05-08 13:15:38','2025-05-08 13:15:43'),(3,'HaoMember',16,'系统界面不好看吧',2,0,NULL,'2025-05-08 13:17:38','2025-05-08 13:17:37'),(4,'admin',1,'会优惠',2,1,16,'2025-05-08 13:18:03','2025-05-08 13:18:06');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '角色名称',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 1删除',
  `identify` int DEFAULT NULL COMMENT '标识',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注\n',
  `role_key` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '权限key',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `role_key` (`role_key`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'管理员','2025-04-01 03:16:13','2025-05-03 02:45:54',0,2,'具备系统的所有操作权限','admin'),(2,'用户','2025-04-01 03:16:33','2025-05-03 02:45:32',0,0,'拥有100次AI陪练，能够刷遍非会员题目','user'),(3,'会员','2025-04-01 03:16:51','2025-05-03 02:45:42',0,1,'可后台自定义题目，最大限度AI陪练，所有题目免费刷','member');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_id` bigint DEFAULT NULL COMMENT '角色id',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='菜单角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (69,2,20,'2025-05-03 02:45:31','2025-05-03 02:45:31',0),(70,2,19,'2025-05-03 02:45:31','2025-05-03 02:45:31',0),(71,3,12,'2025-05-03 02:45:42','2025-05-03 02:45:42',0),(72,3,13,'2025-05-03 02:45:42','2025-05-03 02:45:42',0),(73,3,14,'2025-05-03 02:45:42','2025-05-03 02:45:42',0),(74,3,15,'2025-05-03 02:45:42','2025-05-03 02:45:42',0),(75,3,16,'2025-05-03 02:45:42','2025-05-03 02:45:42',0),(76,3,19,'2025-05-03 02:45:42','2025-05-03 02:45:42',0),(77,3,20,'2025-05-03 02:45:42','2025-05-03 02:45:42',0),(78,1,12,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(79,1,2,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(80,1,17,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(81,1,3,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(82,1,4,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(83,1,5,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(84,1,21,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(85,1,13,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(86,1,14,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(87,1,15,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(88,1,16,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(89,1,18,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(90,1,19,'2025-05-03 02:45:54','2025-05-03 02:45:54',0),(91,1,20,'2025-05-03 02:45:54','2025-05-03 02:45:54',0);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `account` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '账户',
  `password` varchar(150) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '密码',
  `avatar` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '头像',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 1删除',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `member_time` timestamp NULL DEFAULT NULL COMMENT '会员注册时间',
  `status` tinyint DEFAULT '0' COMMENT '会员状态 0正常 1停用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `nickname` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '昵称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`),
  UNIQUE KEY `nickname` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='用户信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2a$10$Ni32jdkBfwkc6a.WMgEiaOKVfuduf1ZAfPa6NmqG8JO7hSYnQAtPq','http://114.116.233.218:9000/topic/avatar/20250508/d46bc050b4d04257ac5b1aa843efd1fb',0,'3655161743@qq.com','2025-04-01 03:14:29',0,'2025-04-01 03:14:29','2025-05-08 10:33:45','HaoHao'),(2,'hao','$2a$10$d1px1FsbEKZgZoqlP9CS9e6N05aUIRrcjugU8BcTkQLLSJbkKJMDW','http://114.116.233.218:9000/topic/H.png',0,'1','2025-04-01 06:00:18',0,'2025-04-01 06:00:18','2025-04-01 06:00:18',NULL),(3,'测试用户','$2a$10$A4sSq1c/IfT0eU5BMCl9pORD2rIyPytV085sbO/QG2X/FvmWEWLFW','http://127.0.0.1:9000/topic/avatar/20250505/0185bb4a8342468a9ff6f60a477379a8',0,'3655271237@qq.com',NULL,0,NULL,'2025-05-05 05:03:19','八百'),(4,'asdasd','$2a$10$iDDE1ycgA4mIbFKE/1.9QuSA5oAZwTk9SgoTLdyOZP5SLOURnwEcK','http://114.116.233.218:9000/topic/avatar/20250421/5bb9fde4c8684eae8b43884447b16ebf',0,'36552712371@qq.com','2025-04-12 11:01:33',0,'2025-04-12 09:31:20','2025-04-21 12:39:18',NULL),(5,'测试添加会员','$2a$10$FTHvUL5Pa0s1iF5ur/ra/uqakVyQPlD/y5v3LsAy6BTYTWhJjkwZ.','http://127.0.0.1:9000/topic/20250412/e91a5091defa40f5b61efdee7e3d4cf8',0,'','2025-04-12 09:34:10',1,'2025-04-12 09:34:11','2025-04-12 09:34:10',NULL),(6,'十多分','$2a$10$WeZWY4cgSzQBdAmAnxSs3uyMQkNLP0LsI7jKjQ90.ivu5VRvrgpu6','http://127.0.0.1:9000/topic/20250412/333eee380c0f43088bfbc3f839766456',1,'11',NULL,0,'2025-04-12 12:25:32','2025-04-12 12:25:31',NULL),(7,'132','$2a$10$Hd/g0mt7Ue4ZQoa7/Q7SLOyKGydD4QyinzIyCe0Ozs4wWQLhCUqXC','http://127.0.0.1:9000/topic/20250412/b305ee96a3b94c97991def1b30119bf6',1,'123',NULL,0,'2025-04-12 12:26:24','2025-04-12 12:26:23',NULL),(8,'123','$2a$10$Vg3f/PV4rzJdsZL9d5I4HOFMHVfvfJ66CHTDCQIs73OA4LuRCJVXO','',0,'',NULL,0,'2025-04-12 13:54:57','2025-04-12 13:54:57',NULL),(9,'123123','$2a$10$KAQf8Yq4M0Yvu7dnFOP0AeiTvdG/yu3igcAUiVfZpzBjCSLmX63Ka','',0,'',NULL,0,'2025-04-12 13:55:04','2025-04-12 13:55:04',NULL),(13,'李四','$2a$10$gCcz/YBewaJJzXpg2bFxSe/36XtoGG.ckvA2tE/BDjnRr9VvJb7sa','http://114.116.233.218:9000/topic/avatar/20250421/c7516913082a469ebc7121724c8e7b40',0,'123123123',NULL,0,'2025-04-13 05:07:25','2025-04-13 05:54:16',NULL),(14,'王五','$2a$10$2lSQOjNs/lKMylrrYAN9kOD5q8K0iQj8AOFNadnFwc.jceIXQP/C6',NULL,0,'1231231231',NULL,0,'2025-04-13 05:07:25','2025-04-13 05:54:16',NULL),(15,'dsf','$2a$10$P92AR6P88sXlbfG41zKjcO19vdJYj1LWSsejWCLW1CEEFridM6POG',NULL,0,'12312',NULL,0,'2025-04-13 15:26:16','2025-04-13 15:31:23',NULL),(16,'HaoMember','$2a$10$lwj2J8xDcCgLre3am5S28u64lgf0xyH.s5/cOGv/6WzAwOPQBhrpa','http://114.116.233.218:9000/topic/avatar/20250508/e1ab194537094b51ab372fe503729fdd',0,'365527121137@qq.com','2025-04-25 14:03:05',0,'2025-04-25 14:03:06','2025-05-08 13:16:04','如果'),(17,'HaoHy','$2a$10$EPGVKuW/bK0SgSgwCcmI7.2d812bMl7pTGMqPRKv6gvgKWbc14.ny','http://114.116.233.218:9000/topic/avatar/20250508/e80c0eedbae74e1c91202d4a60111858',0,'3817445733@qq.com','2025-05-02 14:03:41',0,'2025-05-02 14:03:41','2025-05-08 13:01:36','HaoY');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `role_id` bigint DEFAULT NULL COMMENT '角色id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='用户角色关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1,1,'2025-04-01 03:50:44','2025-04-01 03:50:44',1),(2,2,2,'2025-04-01 06:00:33','2025-04-01 06:00:33',0),(3,3,2,'2025-05-05 04:56:35','2025-04-12 09:25:40',0),(4,4,1,'2025-04-12 09:31:20','2025-04-12 09:31:20',1),(5,5,3,'2025-04-12 09:34:11','2025-04-12 09:34:10',0),(9,4,1,'2025-04-12 10:59:22','2025-04-12 10:59:21',1),(10,4,2,'2025-04-12 11:01:28','2025-04-12 11:01:27',1),(11,4,3,'2025-04-12 11:01:34','2025-04-12 11:01:33',1),(12,4,1,'2025-04-12 11:01:42','2025-04-12 11:01:41',1),(13,4,1,'2025-04-12 11:01:46','2025-04-12 11:01:46',1),(14,4,1,'2025-04-12 11:02:11','2025-04-12 11:02:11',1),(15,4,1,'2025-04-12 11:02:17','2025-04-12 11:02:16',1),(16,4,1,'2025-04-12 11:02:25','2025-04-12 11:02:24',1),(17,4,1,'2025-04-12 11:10:57','2025-04-12 11:10:57',1),(18,6,1,'2025-04-12 12:25:32','2025-04-12 12:25:31',1),(19,7,1,'2025-04-12 12:26:24','2025-04-12 12:26:23',1),(20,4,1,'2025-04-12 12:35:18','2025-04-12 12:35:18',1),(21,4,1,'2025-04-12 12:35:29','2025-04-12 12:35:29',1),(22,4,1,'2025-04-12 12:38:16','2025-04-12 12:38:15',1),(23,1,1,'2025-04-12 12:38:22','2025-04-12 12:38:21',1),(24,4,1,'2025-04-12 12:50:19','2025-04-12 12:50:19',1),(25,1,1,'2025-04-12 12:50:25','2025-04-12 12:50:25',1),(26,1,1,'2025-04-12 12:50:44','2025-04-12 12:50:44',1),(27,1,1,'2025-04-12 12:50:58','2025-04-12 12:50:58',1),(28,1,1,'2025-04-12 12:51:19','2025-04-12 12:51:18',1),(29,1,1,'2025-04-12 12:51:45','2025-04-12 12:51:45',1),(30,8,2,'2025-04-12 13:54:57','2025-04-12 13:54:57',0),(31,9,2,'2025-04-12 13:55:04','2025-04-12 13:55:04',0),(32,13,2,'2025-04-13 05:07:25','2025-04-13 05:07:24',1),(33,14,2,'2025-04-13 05:07:25','2025-04-13 05:07:24',1),(34,13,2,'2025-04-13 05:16:49','2025-04-13 05:16:49',1),(35,14,2,'2025-04-13 05:16:49','2025-04-13 05:16:49',1),(36,13,2,'2025-04-13 05:17:27','2025-04-13 05:17:26',1),(37,14,2,'2025-04-13 05:17:27','2025-04-13 05:17:26',1),(38,13,2,'2025-04-13 05:54:16','2025-04-13 05:54:16',0),(39,14,2,'2025-04-13 05:54:16','2025-04-13 05:54:16',0),(42,15,2,'2025-04-13 15:26:16','2025-04-13 15:26:16',1),(43,15,2,'2025-04-13 15:27:36','2025-04-13 15:27:35',1),(44,15,2,'2025-04-13 15:31:23','2025-04-13 15:31:23',0),(45,4,1,'2025-04-21 12:30:51','2025-04-21 12:30:51',1),(46,4,1,'2025-04-21 12:39:18','2025-04-21 12:39:17',0),(47,1,1,'2025-04-21 12:39:59','2025-04-21 12:39:58',1),(48,1,1,'2025-04-24 10:41:23','2025-04-24 10:41:22',0),(49,16,3,'2025-04-25 14:03:06','2025-04-25 14:03:06',0),(50,17,2,'2025-05-02 14:03:41','2025-05-02 14:03:40',0);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic`
--

DROP TABLE IF EXISTS `topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '题目',
  `topic_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '题目名称',
  `answer` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '题目答案',
  `ai_answer` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'ai答案',
  `sorted` bigint DEFAULT '0' COMMENT '排序',
  `is_everyday` tinyint DEFAULT '0' COMMENT '是否为每日推荐暂定9题 0不是 1是',
  `is_member` tinyint DEFAULT '0' COMMENT '是否会员专享 0 不需要 1需要',
  `view_count` bigint DEFAULT '0' COMMENT '浏览数量',
  `create_by` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `status` tinyint DEFAULT '2' COMMENT '0正常 1停用 2待审核 3审核失败',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0',
  `fail_msg` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`),
  UNIQUE KEY `topic_name` (`topic_name`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='题目';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic`
--

LOCK TABLES `topic` WRITE;
/*!40000 ALTER TABLE `topic` DISABLE KEYS */;
INSERT INTO `topic` VALUES (35,'什么是MySQL？它有什么特点','<p><span style=\"color: rgb(44, 44, 54); background-color: rgb(255, 255, 255); font-size: 16px;\">请简述MySQL数据库的基本概念、主要特性及其适用场景</span></p>','MySQL是一种非常流行的开源关系型数据库管理系统，广泛用于各种应用程序的数据存储和管理。它的特点包括支持多种操作系统、具有高性能和可扩展性、支持多种编程语言、提供安全和稳定的数据存储、支持SQL标准等。同时，MySQL也是免费和开源的，这使得它成为许多开发者和企业的首选数据库管理系统。',0,0,0,0,'admin',0,'2025-05-08 11:19:22','2025-05-08 11:19:42',0,NULL),(36,'解释一下ACID属性在数据库管理系统中的意义','<p><span style=\"color: rgb(44, 44, 54); background-color: rgb(255, 255, 255); font-size: 16px;\">A代表原子性(Atomicity)、C代表一致性(Consistency)、I代表隔离性(Isolation)、D代表持久性(Durability)。请详细说明它们各自的意义及如何影响事务处理</span></p>','ACID属性是在数据库管理系统中确保数据库事务的可靠性和安全性的一个重要概念。它是由Atomicity、Consistency、Isolation和Durability四个英文单词的首字母组成的。\n\n首先，Atomicity，也就是原子性，保证了每个事务都是不可分割的，最小的执行单位。如果事务中任何一部分执行失败了，整个事务就要回滚，保证数据的一致性。\n\n其次，Consistency，也就是一致性，确保了数据库在事务执行前和执行后都保持一致的状态。也就是说，事务的执行必须遵守数据库的约束和规则，不能违反数据的完整性。\n\n第三，Isolation，也就是隔离性，保证了多个事务之间的相互独立。即使多个事务同时执行，也不会相互影响，好像每个事务都是单独执行的一样。\n\n最后，Durability，也就是持久性，保证了事务一旦提交后，其对数据库的修改是永久性的，即使出现系统故障或宕机，也不会丢失数据。\n\n综上所述，ACID属性为数据库事务的执行提供了一个可靠的保障，确保了数据的准确性、完整性和安全性，在数据库管理系统中起着至关重要的作用。',0,0,0,0,'admin',0,'2025-05-08 11:21:34','2025-05-08 11:22:01',0,NULL),(37,'什么是索引(Index)，以及索引的类型有哪些？请举例说明何时使用特定类型的索引。','<p style=\"text-align: left;\">解释索引的工作原理，并讨论B-Tree索引、哈希索引等的区别与应用场景。</p>','索引就像是书的目录，能快速找到数据的位置，不用一页一页翻。常见的索引类型有B树索引、哈希索引、全文索引和组合索引。B树索引适合范围查询，比如查某个时间段的订单；哈希索引适合精确匹配，比如通过用户ID直接找某个用户信息；全文索引用来处理大段文本搜索，像在文章中搜关键词；组合索引适合多条件查询，比如同时按姓名和年龄筛选记录。用哪种索引要看具体需求，选对了效率会高很多。',0,0,0,0,'admin',0,'2025-05-08 11:22:17','2025-05-08 11:27:32',0,NULL),(38,'Java 中的基本数据类型有哪些？','<p style=\"text-align: start;\">Java 有8种基本数据类型：</p><ul><li style=\"text-align: start;\">整型：<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>byte</code></span>（1字节）、<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>short</code></span>（2字节）、<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>int</code></span>（4字节）、<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>long</code></span>（8字节）</li><li style=\"text-align: start;\">浮点型：<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>float</code></span>（4字节）、<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>double</code></span>（8字节）</li><li style=\"text-align: start;\">字符型：<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>char</code></span>（2字节）</li><li style=\"text-align: start;\">布尔型：<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>boolean</code></span>（值只能是 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>true</code></span> 或 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>false</code></span>）</li></ul>','Java 中的基本数据类型有8种，分别是：整数类型的 byte、short、int、long，浮点类型的 float、double，字符类型的 char，以及布尔类型的 boolean。',0,0,0,0,'admin',0,'2025-05-08 11:24:52','2025-05-08 13:10:42',0,NULL),(39,' final、finally 和 finalize 的区别是什么？','<ul><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>final</code></span>：修饰类、方法或变量，表示不可继承、不可重写或不可修改。</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>finally</code></span>：在异常处理中使用，无论是否发生异常都会执行，常用于释放资源。</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>finalize()</code></span>：是 Object 类的方法，在对象被垃圾回收前由 JVM 调用，不推荐手动依赖此方法。</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 11:27:27','2025-05-08 11:27:26',0,NULL),(40,'什么是方法重载和方法重写？有什么区别？','<ul><li style=\"text-align: start;\"><strong>方法重载（Overload）</strong>：在同一个类中，方法名相同，参数不同（个数、类型或顺序），与返回值无关。</li><li style=\"text-align: start;\"><strong>方法重写（Override）</strong>：子类重新定义父类的方法，方法名、参数列表、返回值必须一致，访问权限不能更严格。</li></ul>','方法重载是指在同一个类中，可以有多个同名的方法，但它们的参数列表必须不同，比如参数的数量、类型或者顺序不一样。这种方法可以让代码更灵活，调用时根据传入的参数自动选择对应的方法。\n\n方法重写是指子类对父类中的方法进行重新定义，方法名和参数列表必须跟父类的一模一样，但方法的具体实现可以不同。这样可以让子类根据自己的需求改写父类的功能。\n\n它们的区别主要是：  \n1. 重载发生在同一个类里，重写发生在父子类之间。  \n2. 重载关注的是参数的不同，而重写的参数必须完全一样。  \n3. 重载是编译时决定的，重写是运行时才确定的。  \n4. 重载可以改变返回类型，但重写的返回类型一般要跟父类一致，或者符合协变返回类型规则。',0,0,0,0,'admin',0,'2025-05-08 11:27:43','2025-05-08 11:28:17',0,NULL),(41,'String、StringBuilder 和 StringBuffer 的区别？','<ul><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>String</code></span>：不可变字符串，每次操作都生成新对象。</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>StringBuilder</code></span>：可变字符串，非线程安全，性能高，适用于单线程。</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>StringBuffer</code></span>：可变字符串，线程安全，性能略低，适用于多线程。</li></ul>','String 是不可变的，每次修改都会创建新对象，适合少量字符串操作。StringBuilder 是可变的，性能高，但线程不安全，适合单线程场景。StringBuffer 也是可变的，线程安全，但性能比 StringBuilder 差一点，适合多线程环境。',0,0,1,0,'admin',0,'2025-05-08 11:28:03','2025-05-08 11:28:13',0,NULL),(42,'List、Set 和 Map 的常见实现类及其区别？','<ul><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>List</code></span>：有序、可重复。常用实现类：<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>ArrayList</code></span>、<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>LinkedList</code></span></li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>Set</code></span>：无序、不可重复。常用实现类：<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>HashSet</code></span>、<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>TreeSet</code></span></li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>Map</code></span>：键值对集合。常用实现类：<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>HashMap</code></span>、<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>TreeMap</code></span>、<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>LinkedHashMap</code></span></li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 11:28:41','2025-05-08 11:28:40',0,NULL),(43,'HashMap 是如何工作的？它是线程安全的吗？','<ul><li style=\"text-align: start;\">HashMap 基于哈希表实现，通过 key 的 hashCode 定位存储位置。</li><li style=\"text-align: start;\">不是线程安全的。可以使用 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>ConcurrentHashMap</code></span> 或 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>Collections.synchronizedMap()</code></span> 来实现线程安全。</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 11:28:59','2025-05-08 11:28:58',0,NULL),(44,' Java 中创建线程的方式有哪些？','<ul><li style=\"text-align: start;\">继承 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>Thread</code></span> 类并重写 run() 方法。</li><li style=\"text-align: start;\">实现 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>Runnable</code></span> 接口，并传入 Thread 构造器。</li><li style=\"text-align: start;\">使用 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>Callable</code></span> 和 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>FutureTask</code></span>（支持返回值）。</li><li style=\"text-align: start;\">使用线程池（如 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>ExecutorService</code></span>）管理线程。</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 11:29:12','2025-05-08 11:29:12',0,NULL),(45,' Java 中的线程有哪些状态？','<p style=\"text-align: start;\">Java 线程有6种状态：</p><ul><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>NEW</code></span>：新建</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>RUNNABLE</code></span>：运行/就绪</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>BLOCKED</code></span>：阻塞</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>WAITING</code></span>：等待</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>TIMED_WAITING</code></span>：定时等待</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>TERMINATED</code></span>：终止</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 11:29:30','2025-05-08 11:29:29',0,NULL),(46,' JVM 内存结构主要包括哪些部分？','<ul><li style=\"text-align: start;\"><strong>堆（Heap）</strong>：存放对象实例</li><li style=\"text-align: start;\"><strong>栈（Stack）</strong>：存放局部变量和方法调用</li><li style=\"text-align: start;\"><strong>方法区（Method Area）</strong>：存放类信息、静态变量等（Java 8 后改为元空间 Metaspace）</li><li style=\"text-align: start;\"><strong>程序计数器（PC Register）</strong>：记录当前线程执行的位置</li><li style=\"text-align: start;\"><strong>本地方法栈（Native Method Stack）</strong>：为 Native 方法服务<br></li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 11:29:44','2025-05-08 11:29:43',0,NULL),(47,'什么是自动装箱和拆箱？','<ul><li style=\"text-align: start;\"><strong>自动装箱（Autoboxing）</strong>：将基本类型自动转换为对应的包装类，如 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>Integer i = 10;</code></span></li><li style=\"text-align: start;\"><strong>自动拆箱（Unboxing）</strong>：将包装类自动转换为基本类型，如 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>int j = i;</code></span></li><li style=\"text-align: start;\">注意：<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>==</code></span> 比较时可能会因缓存机制（如 Integer 缓存 -128~127）导致结果不同。</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 11:29:56','2025-05-08 11:29:56',0,NULL),(48,' HTML5 有哪些新特性？','<p>HTML5 引入了新的语义化标签（如 &lt;header&gt;、&lt;footer&gt;、&lt;article&gt;）、表单控件（如 email、date）、多媒体支持（&lt;audio&gt;、&lt;video&gt;）、Canvas 绘图、本地存储（localStorage/sessionStorage）等</p>',NULL,0,0,1,0,'admin',0,'2025-05-08 11:31:43','2025-05-08 13:18:49',0,''),(49,'什么是 CSS 的层叠（Cascading）和优先级？','<p style=\"text-align: start;\">CSS 层叠是指多个规则应用在同一个元素时的冲突解决机制。优先级由选择器类型决定，从低到高依次为：</p><ul><li style=\"text-align: start;\">元素选择器</li><li style=\"text-align: start;\">类选择器</li><li style=\"text-align: start;\">ID 选择器</li><li style=\"text-align: start;\">行内样式</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>!important</code></span></li><li style=\"text-align: start;\">内联样式的优先级最高，但不建议滥用。</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 11:32:05','2025-05-08 11:32:05',0,NULL),(50,'什么是事件冒泡和事件捕获？如何阻止默认行为？','<ul><li style=\"text-align: start;\"><strong>事件冒泡</strong>：事件从子元素向父元素传播。</li><li style=\"text-align: start;\"><strong>事件捕获</strong>：事件从父元素向子元素传播。</li><li style=\"text-align: start;\">使用 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>event.stopPropagation()</code></span> 阻止传播，<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>event.preventDefault()</code></span> 阻止默认行为（如链接跳转）。</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 11:32:38','2025-05-08 11:32:37',0,NULL),(51,'解释一下 Promise 和 async/await 的关系。','<p><span style=\"color: rgb(44, 44, 54); background-color: rgb(255, 255, 255); font-size: 16px;\">Promise 是处理异步操作的对象，避免回调地狱；async/await 是基于 Promise 的语法糖，使异步代码看起来更像同步代码，提高可读性和可维护性。</span></p>',NULL,0,0,0,0,'admin',0,'2025-05-08 12:55:31','2025-05-08 12:55:30',0,NULL),(52,' 什么是事件冒泡和事件捕获？如何阻止默认行为？','<ul><li style=\"text-align: start;\"><strong>事件冒泡</strong>：事件从子元素向父元素传播。</li><li style=\"text-align: start;\"><strong>事件捕获</strong>：事件从父元素向子元素传播。</li><li style=\"text-align: start;\">使用 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>event.stopPropagation()</code></span> 阻止传播，<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>event.preventDefault()</code></span> 阻止默认行为（如链接跳转）。</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 12:56:55','2025-05-08 12:56:54',0,NULL),(53,'let、const 和 var 的区别是什么？','<ul><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>var</code></span>：函数作用域，存在变量提升。</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>let</code></span> 和 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>const</code></span>：块级作用域，不存在变量提升。</li><li style=\"text-align: start;\"><span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>const</code></span> 声明的是常量，值不能重新赋值（引用类型内部可变）。</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 12:57:09','2025-05-08 12:57:09',0,NULL),(54,'JVM 内存结构主要包括哪些部分？','<ul><li style=\"text-align: start;\"><strong>堆（Heap）</strong>：存放对象实例</li><li style=\"text-align: start;\"><strong>栈（Stack）</strong>：存放局部变量和方法调用</li><li style=\"text-align: start;\"><strong>方法区（Method Area）</strong>：存放类信息、静态变量等（Java 8 后改为元空间 Metaspace）</li><li style=\"text-align: start;\"><strong>程序计数器（PC Register）</strong>：记录当前线程执行的位置</li><li style=\"text-align: start;\"><strong>本地方法栈（Native Method Stack）</strong>：为 Native 方法服务</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 12:57:38','2025-05-08 12:57:38',0,NULL),(55,' 如何处理MySQL中的大数据量查询以提高效率？','<ul><li style=\"text-align: start;\">简述：可以通过创建合适的索引、优化查询语句、使用分区表、增加内存缓冲区大小等方式来提高大数据量查询的效率。</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 12:59:09','2025-05-08 12:59:09',0,NULL),(56,'在MySQL中，如何实现全文搜索功能？','<ul><li style=\"text-align: start;\">简述：MySQL支持全文索引（FULLTEXT index），可以在<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>CHAR</code></span>, <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>VARCHAR</code></span>, 或 <span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>TEXT</code></span>类型的列上创建。使用<span style=\"background-color: rgba(175, 184, 193, 0.2);\"><code>MATCH() AGAINST()</code></span>语法来进行全文搜索。</li></ul>',NULL,0,0,0,0,'admin',0,'2025-05-08 12:59:25','2025-05-08 12:59:25',0,NULL),(57,'解释一下MySQL中的外键约束及其作用。','<p><span style=\"color: rgb(44, 44, 54); background-color: rgb(255, 255, 255); font-size: 16px;\">外键用于保证数据的引用完整性，确保子表中的值必须存在于父表的相关列中。它可以防止非法数据进入子表，维护数据库的一致性</span></p>',NULL,0,0,0,0,'admin',0,'2025-05-08 12:59:48','2025-05-08 12:59:47',0,NULL),(58,'如何备份和恢复MySQL数据库？','<p><span style=\"color: rgb(44, 44, 54); background-color: rgb(255, 255, 255); font-size: 16px;\">可以使用</span>mysqldump<span style=\"color: rgb(44, 44, 54); background-color: rgb(255, 255, 255); font-size: 16px;\">工具进行备份，例如</span>mysqldump -u [user] -p [database_name] &gt; backup.sql<span style=\"color: rgb(44, 44, 54); background-color: rgb(255, 255, 255); font-size: 16px;\">；恢复时使用</span>mysql -u [user] -p [database_name] &lt; backup.sql</p>',NULL,0,0,0,0,'admin',0,'2025-05-08 13:00:03','2025-05-08 13:00:03',0,NULL),(59,'什么是死锁（Deadlock），如何检测和解决？','<p><span style=\"color: rgb(44, 44, 54); background-color: rgb(255, 255, 255); font-size: 16px;\">死锁是指两个或更多的事务都在等待对方释放资源而无法继续执行的情况。MySQL自动检测死锁并通过回滚其中一个事务来解决</span></p>',NULL,0,0,0,0,'admin',0,'2025-05-08 13:00:23','2025-05-08 13:00:22',0,NULL),(60,'描述一下MySQL复制（Replication）的工作原理。','<p><span style=\"color: rgb(44, 44, 54); background-color: rgb(255, 255, 255); font-size: 16px;\">主服务器记录所有更改的数据到二进制日志(binary log)，从服务器读取这些日志并应用到自己的数据副本上</span></p>',NULL,0,0,0,0,'admin',0,'2025-05-08 13:00:42','2025-05-08 13:00:41',0,NULL);
/*!40000 ALTER TABLE `topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_category`
--

DROP TABLE IF EXISTS `topic_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `category_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '分类名称',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint DEFAULT '2' COMMENT '0正常 1停用 2待审核 3审核失败 ',
  `create_by` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `is_deleted` tinyint DEFAULT '0',
  `subject_count` bigint DEFAULT '0' COMMENT '专题数量',
  `fail_msg` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`),
  UNIQUE KEY `category_name` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='题目分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_category`
--

LOCK TABLES `topic_category` WRITE;
/*!40000 ALTER TABLE `topic_category` DISABLE KEYS */;
INSERT INTO `topic_category` VALUES (47,'数据库','2025-05-08 11:17:23','2025-05-08 11:18:46',0,'admin',0,1,NULL),(48,'12312311','2025-05-08 11:17:30','2025-05-08 11:17:34',0,'admin',1,0,''),(49,'成色如','2025-05-08 11:18:19','2025-05-08 11:18:18',0,'admin',1,0,NULL),(50,'123123','2025-05-08 11:18:19','2025-05-08 11:18:18',0,'admin',1,0,NULL),(51,'后端','2025-05-08 11:23:04','2025-05-08 11:24:03',0,'admin',0,1,NULL),(52,'前端','2025-05-08 11:30:07','2025-05-08 11:31:17',0,'admin',0,1,NULL),(54,'前端开发','2025-05-08 11:49:28','2025-05-08 11:49:29',0,'HaoMember',0,0,''),(55,'Java色色','2025-05-08 11:49:44','2025-05-08 11:49:46',3,'HaoMember',0,0,'名称包含可能引发误解的词语，不符合公序良俗');
/*!40000 ALTER TABLE `topic_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_category_subject`
--

DROP TABLE IF EXISTS `topic_category_subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_category_subject` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` bigint NOT NULL COMMENT '分类id',
  `subject_id` bigint NOT NULL COMMENT '专题id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='分类专题关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_category_subject`
--

LOCK TABLES `topic_category_subject` WRITE;
/*!40000 ALTER TABLE `topic_category_subject` DISABLE KEYS */;
INSERT INTO `topic_category_subject` VALUES (65,47,38,'2025-05-08 11:18:46','2025-05-08 11:18:45',0),(66,51,39,'2025-05-08 11:24:03','2025-05-08 11:24:03',0),(67,52,40,'2025-05-08 11:31:17','2025-05-08 11:31:16',0);
/*!40000 ALTER TABLE `topic_category_subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_collection`
--

DROP TABLE IF EXISTS `topic_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_collection` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `topic_id` bigint NOT NULL COMMENT '题目id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='题目收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_collection`
--

LOCK TABLES `topic_collection` WRITE;
/*!40000 ALTER TABLE `topic_collection` DISABLE KEYS */;
INSERT INTO `topic_collection` VALUES (8,38,1,'2025-05-08 13:09:11'),(9,38,16,'2025-05-08 13:11:07'),(10,48,17,'2025-05-08 13:19:14');
/*!40000 ALTER TABLE `topic_collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_daily_brush`
--

DROP TABLE IF EXISTS `topic_daily_brush`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_daily_brush` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `daily_id` bigint DEFAULT NULL COMMENT '每日题目id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日题目和用户关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_daily_brush`
--

LOCK TABLES `topic_daily_brush` WRITE;
/*!40000 ALTER TABLE `topic_daily_brush` DISABLE KEYS */;
INSERT INTO `topic_daily_brush` VALUES (6,1191,1),(7,1191,16),(8,1194,16),(9,1194,1);
/*!40000 ALTER TABLE `topic_daily_brush` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_daily_staging`
--

DROP TABLE IF EXISTS `topic_daily_staging`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_daily_staging` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `topic_id` bigint DEFAULT NULL COMMENT '题目id',
  `subject_id` bigint DEFAULT NULL COMMENT '专题id',
  `is_public` tinyint DEFAULT NULL COMMENT '1.公共 2.用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1244 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目每日暂存表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_daily_staging`
--

LOCK TABLES `topic_daily_staging` WRITE;
/*!40000 ALTER TABLE `topic_daily_staging` DISABLE KEYS */;
INSERT INTO `topic_daily_staging` VALUES (1191,NULL,52,40,1),(1192,NULL,56,38,1),(1193,NULL,60,38,1),(1194,NULL,44,39,1),(1195,NULL,39,39,1),(1196,1,43,39,2),(1197,1,47,39,2),(1198,1,37,38,2),(1199,1,49,40,2),(1200,2,49,40,2),(1201,2,50,38,2),(1202,2,59,38,2),(1203,2,36,38,2),(1204,3,46,39,2),(1205,3,59,38,2),(1206,3,45,39,2),(1207,3,54,39,2),(1208,4,49,40,2),(1209,4,51,40,2),(1210,4,35,38,2),(1211,4,57,38,2),(1212,5,38,39,2),(1213,5,57,38,2),(1214,5,36,38,2),(1215,5,51,40,2),(1216,8,57,38,2),(1217,8,51,40,2),(1218,8,49,40,2),(1219,8,53,40,2),(1220,9,59,38,2),(1221,9,51,40,2),(1222,9,47,39,2),(1223,9,40,39,2),(1224,13,55,38,2),(1225,13,36,38,2),(1226,13,59,38,2),(1227,13,57,38,2),(1228,14,51,40,2),(1229,14,49,40,2),(1230,14,42,39,2),(1231,14,43,39,2),(1232,15,35,38,2),(1233,15,54,39,2),(1234,15,59,38,2),(1235,15,53,40,2),(1236,16,49,40,2),(1237,16,51,40,2),(1238,16,53,40,2),(1239,16,48,40,2),(1240,17,53,40,2),(1241,17,37,38,2),(1242,17,49,40,2),(1243,17,54,39,2);
/*!40000 ALTER TABLE `topic_daily_staging` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_favorite`
--

DROP TABLE IF EXISTS `topic_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '题目',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `topic_id` bigint DEFAULT NULL COMMENT '题目id',
  `status` tinyint DEFAULT '0' COMMENT '0收藏 1取消收藏',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='题目收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_favorite`
--

LOCK TABLES `topic_favorite` WRITE;
/*!40000 ALTER TABLE `topic_favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `topic_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_label`
--

DROP TABLE IF EXISTS `topic_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_label` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '题目标签',
  `label_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '标签名称',
  `use_count` bigint DEFAULT '0' COMMENT '使用数量',
  `create_by` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `status` tinyint DEFAULT '2' COMMENT '0正常 1停用 2待审核 3审核失败',
  `create_time` timestamp NULL DEFAULT (now()) COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT (now()) COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0',
  `fail_msg` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`),
  UNIQUE KEY `label_name` (`label_name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='题目标签';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_label`
--

LOCK TABLES `topic_label` WRITE;
/*!40000 ALTER TABLE `topic_label` DISABLE KEYS */;
INSERT INTO `topic_label` VALUES (14,'mysql',8,'admin',0,'2025-05-08 11:18:58','2025-05-08 13:00:42',0,NULL),(15,'数据库',7,'admin',0,'2025-05-08 11:21:46','2025-05-08 13:00:42',0,NULL),(16,'java',11,'admin',0,'2025-05-08 11:24:17','2025-05-08 12:57:38',0,NULL),(17,'Java基础',6,'admin',0,'2025-05-08 11:24:22','2025-05-08 12:57:38',0,NULL),(18,'html',7,'admin',0,'2025-05-08 11:30:23','2025-05-08 13:18:49',0,NULL),(19,'css',5,'admin',0,'2025-05-08 11:30:26','2025-05-08 12:57:09',0,NULL);
/*!40000 ALTER TABLE `topic_label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_label_topic`
--

DROP TABLE IF EXISTS `topic_label_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_label_topic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `label_id` bigint NOT NULL COMMENT '标签id',
  `topic_id` bigint NOT NULL COMMENT '题目id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='标签题目关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_label_topic`
--

LOCK TABLES `topic_label_topic` WRITE;
/*!40000 ALTER TABLE `topic_label_topic` DISABLE KEYS */;
INSERT INTO `topic_label_topic` VALUES (93,14,35,'2025-05-08 11:19:22','2025-05-08 11:19:22',0),(94,14,36,'2025-05-08 11:21:34','2025-05-08 11:21:33',0),(95,14,37,'2025-05-08 11:22:17','2025-05-08 11:22:16',0),(96,15,37,'2025-05-08 11:22:17','2025-05-08 11:22:16',0),(97,16,38,'2025-05-08 11:24:52','2025-05-08 11:24:51',0),(98,17,38,'2025-05-08 11:24:52','2025-05-08 11:24:51',0),(99,16,39,'2025-05-08 11:27:27','2025-05-08 11:27:26',0),(100,17,39,'2025-05-08 11:27:27','2025-05-08 11:27:26',0),(101,16,40,'2025-05-08 11:27:43','2025-05-08 11:27:43',0),(102,17,40,'2025-05-08 11:27:43','2025-05-08 11:27:43',0),(103,16,41,'2025-05-08 11:28:03','2025-05-08 11:28:02',0),(104,17,41,'2025-05-08 11:28:03','2025-05-08 11:28:02',0),(105,16,42,'2025-05-08 11:28:41','2025-05-08 11:28:40',0),(106,16,43,'2025-05-08 11:28:59','2025-05-08 11:28:58',0),(107,16,44,'2025-05-08 11:29:12','2025-05-08 11:29:12',0),(108,16,45,'2025-05-08 11:29:30','2025-05-08 11:29:29',0),(109,17,45,'2025-05-08 11:29:30','2025-05-08 11:29:29',0),(110,16,46,'2025-05-08 11:29:44','2025-05-08 11:29:43',0),(111,16,47,'2025-05-08 11:29:56','2025-05-08 11:29:56',0),(112,18,48,'2025-05-08 11:31:43','2025-05-08 13:18:49',1),(113,18,49,'2025-05-08 11:32:05','2025-05-08 11:32:05',0),(114,19,49,'2025-05-08 11:32:05','2025-05-08 11:32:05',0),(115,18,50,'2025-05-08 11:32:38','2025-05-08 11:32:37',0),(116,19,50,'2025-05-08 11:32:38','2025-05-08 11:32:37',0),(117,18,51,'2025-05-08 12:55:31','2025-05-08 12:55:30',0),(118,19,51,'2025-05-08 12:55:31','2025-05-08 12:55:30',0),(119,18,52,'2025-05-08 12:56:55','2025-05-08 12:56:54',0),(120,19,52,'2025-05-08 12:56:55','2025-05-08 12:56:54',0),(121,18,53,'2025-05-08 12:57:09','2025-05-08 12:57:09',0),(122,19,53,'2025-05-08 12:57:09','2025-05-08 12:57:09',0),(123,16,54,'2025-05-08 12:57:38','2025-05-08 12:57:38',0),(124,17,54,'2025-05-08 12:57:38','2025-05-08 12:57:38',0),(125,14,55,'2025-05-08 12:59:09','2025-05-08 12:59:09',0),(126,15,55,'2025-05-08 12:59:09','2025-05-08 12:59:09',0),(127,14,56,'2025-05-08 12:59:25','2025-05-08 12:59:25',0),(128,15,56,'2025-05-08 12:59:25','2025-05-08 12:59:25',0),(129,14,57,'2025-05-08 12:59:48','2025-05-08 12:59:47',0),(130,15,57,'2025-05-08 12:59:48','2025-05-08 12:59:47',0),(131,15,58,'2025-05-08 13:00:03','2025-05-08 13:00:03',0),(132,14,59,'2025-05-08 13:00:23','2025-05-08 13:00:22',0),(133,15,59,'2025-05-08 13:00:23','2025-05-08 13:00:22',0),(134,14,60,'2025-05-08 13:00:42','2025-05-08 13:00:41',0),(135,15,60,'2025-05-08 13:00:42','2025-05-08 13:00:41',0),(136,18,48,'2025-05-08 13:18:49','2025-05-08 13:18:48',0);
/*!40000 ALTER TABLE `topic_label_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_record`
--

DROP TABLE IF EXISTS `topic_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '昵称',
  `topic_id` bigint DEFAULT NULL COMMENT '题目id',
  `subject_id` bigint DEFAULT NULL COMMENT '专题id',
  `count` bigint DEFAULT NULL COMMENT '刷题次数',
  `topic_time` date DEFAULT (curdate()) COMMENT '刷题日期',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12357 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='题目刷题记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_record`
--

LOCK TABLES `topic_record` WRITE;
/*!40000 ALTER TABLE `topic_record` DISABLE KEYS */;
INSERT INTO `topic_record` VALUES (12336,16,'如果',48,40,8,'2025-05-08','2025-05-08 13:18:51'),(12337,1,'HaoHao',52,40,1,'2025-05-08','2025-05-08 13:08:08'),(12338,1,'HaoHao',38,39,3,'2025-05-08','2025-05-08 13:09:13'),(12339,16,'如果',38,39,6,'2025-05-08','2025-05-08 13:15:00'),(12340,16,'如果',39,39,3,'2025-05-08','2025-05-08 13:11:13'),(12341,16,'如果',40,39,2,'2025-05-08','2025-05-08 13:11:17'),(12342,16,'如果',41,39,2,'2025-05-08','2025-05-08 13:11:18'),(12343,16,'如果',42,39,1,'2025-05-08','2025-05-08 13:11:27'),(12344,16,'如果',43,39,1,'2025-05-08','2025-05-08 13:11:27'),(12345,16,'如果',44,39,12,'2025-05-08','2025-05-08 13:19:33'),(12346,16,'如果',45,39,1,'2025-05-08','2025-05-08 13:11:29'),(12347,16,'如果',46,39,1,'2025-05-08','2025-05-08 13:11:29'),(12348,16,'如果',47,39,1,'2025-05-08','2025-05-08 13:11:29'),(12349,16,'如果',54,39,1,'2025-05-08','2025-05-08 13:11:30'),(12350,16,'如果',52,40,3,'2025-05-08','2025-05-08 13:19:25'),(12351,16,'如果',53,40,1,'2025-05-08','2025-05-08 13:17:10'),(12352,16,'如果',51,40,1,'2025-05-08','2025-05-08 13:17:11'),(12353,17,'HaoY',49,40,1,'2025-05-08','2025-05-08 13:19:05'),(12354,17,'HaoY',48,40,1,'2025-05-08','2025-05-08 13:19:08'),(12355,16,'如果',36,38,1,'2025-05-08','2025-05-08 13:20:18'),(12356,1,'HaoHao',44,39,1,'2025-05-08','2025-05-08 15:30:54');
/*!40000 ALTER TABLE `topic_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_subject`
--

DROP TABLE IF EXISTS `topic_subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_subject` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '题目专题',
  `subject_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '专题名称',
  `subject_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '专题概述',
  `image_url` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '图像',
  `topic_count` bigint DEFAULT '0' COMMENT '收录数量',
  `create_by` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `view_count` bigint DEFAULT '0' COMMENT '浏览数量',
  `status` tinyint DEFAULT '2' COMMENT '0正常 1停用 2待审核 3审核失败',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0',
  `fail_msg` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`),
  UNIQUE KEY `subject_name` (`subject_name`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='题目专题';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_subject`
--

LOCK TABLES `topic_subject` WRITE;
/*!40000 ALTER TABLE `topic_subject` DISABLE KEYS */;
INSERT INTO `topic_subject` VALUES (38,'Mysql','关系性数据库','http://114.116.233.218:9000/topic/subject/20250508/47aff0a1e7f54e83bdf5e56609be3038',10,'admin',24,0,'2025-05-08 11:18:46','2025-05-08 13:20:48',0,NULL),(39,'Java基础','java入门','http://114.116.233.218:9000/topic/subject/20250508/8760350d02b1431592cde7ab6328e8c0',11,'admin',59,0,'2025-05-08 11:24:03','2025-05-08 15:30:54',0,NULL),(40,'前端三件套','网页','http://114.116.233.218:9000/topic/subject/20250508/60873266c9874e87b35c347d55d27e64',6,'admin',36,0,'2025-05-08 11:31:17','2025-05-08 13:20:48',0,NULL);
/*!40000 ALTER TABLE `topic_subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_subject_topic`
--

DROP TABLE IF EXISTS `topic_subject_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_subject_topic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `topic_id` bigint NOT NULL COMMENT '题目id',
  `subject_id` bigint NOT NULL COMMENT '专题id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='题目专题关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_subject_topic`
--

LOCK TABLES `topic_subject_topic` WRITE;
/*!40000 ALTER TABLE `topic_subject_topic` DISABLE KEYS */;
INSERT INTO `topic_subject_topic` VALUES (69,35,38,'2025-05-08 11:19:22','2025-05-08 11:19:22',0),(70,36,38,'2025-05-08 11:21:34','2025-05-08 11:21:33',0),(71,37,38,'2025-05-08 11:22:17','2025-05-08 11:22:16',0),(72,38,39,'2025-05-08 11:24:52','2025-05-08 11:24:51',0),(73,39,39,'2025-05-08 11:27:27','2025-05-08 11:27:26',0),(74,40,39,'2025-05-08 11:27:43','2025-05-08 11:27:43',0),(75,41,39,'2025-05-08 11:28:03','2025-05-08 11:28:02',0),(76,42,39,'2025-05-08 11:28:41','2025-05-08 11:28:40',0),(77,43,39,'2025-05-08 11:28:59','2025-05-08 11:28:58',0),(78,44,39,'2025-05-08 11:29:12','2025-05-08 11:29:12',0),(79,45,39,'2025-05-08 11:29:30','2025-05-08 11:29:29',0),(80,46,39,'2025-05-08 11:29:44','2025-05-08 11:29:43',0),(81,47,39,'2025-05-08 11:29:56','2025-05-08 11:29:56',0),(82,48,40,'2025-05-08 11:31:43','2025-05-08 13:18:49',1),(83,49,40,'2025-05-08 11:32:05','2025-05-08 11:32:05',0),(84,50,38,'2025-05-08 11:32:38','2025-05-08 11:32:37',0),(85,51,40,'2025-05-08 12:55:31','2025-05-08 12:55:30',0),(86,52,40,'2025-05-08 12:56:55','2025-05-08 12:56:54',0),(87,53,40,'2025-05-08 12:57:09','2025-05-08 12:57:09',0),(88,54,39,'2025-05-08 12:57:38','2025-05-08 12:57:38',0),(89,55,38,'2025-05-08 12:59:09','2025-05-08 12:59:09',0),(90,56,38,'2025-05-08 12:59:25','2025-05-08 12:59:25',0),(91,57,38,'2025-05-08 12:59:48','2025-05-08 12:59:47',0),(92,58,38,'2025-05-08 13:00:03','2025-05-08 13:00:03',0),(93,59,38,'2025-05-08 13:00:23','2025-05-08 13:00:22',0),(94,60,38,'2025-05-08 13:00:42','2025-05-08 13:00:41',0),(95,48,40,'2025-05-08 13:18:49','2025-05-08 13:18:48',0);
/*!40000 ALTER TABLE `topic_subject_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `web_config`
--

DROP TABLE IF EXISTS `web_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `web_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `content` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '内容',
  `price` decimal(10,0) DEFAULT NULL COMMENT '价格',
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片资源',
  `status` tinyint DEFAULT NULL COMMENT '1.我的支付',
  `remark` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='前端配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `web_config`
--

LOCK TABLES `web_config` WRITE;
/*!40000 ALTER TABLE `web_config` DISABLE KEYS */;
INSERT INTO `web_config` VALUES (1,'可后台自定义题目，最大限度AI陪练，所有题目免费刷',58,'http://127.0.0.1:9000/topic/pay58.jpg',1,'支付完成后请联系我们根据交易号申请');
/*!40000 ALTER TABLE `web_config` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-08 23:51:38
