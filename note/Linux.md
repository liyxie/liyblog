# Linux



```bash


sudo systemctl status // Linux查看全部运行程序

free -h // 查看内存使用
mpstat //查看CPU情况



# 
```

# docker

```bash
docker ps --no-trunc //不省略

docker build -t 名 . // Dockerfile文件创建镜像

docker exec -it id /bin/bash  // 进入容器
```

### 日志

```bash
docker logs [OPTIONS] CONTAINER # 查看日志
```

| code             | 默认 | 描述                     |
| ---------------- | ---- | ------------------------ |
| --details        |      | 显示更多信息             |
| --follow，-f     |      | 实时输出                 |
| --since          |      | 某个时间戳后，或相对时间 |
| --tail,-n        | all  | 从末尾开始显示           |
| --timestamps, -t |      | 时间戳                   |
| --until          |      | 某个时间戳前，或相对时间 |

```bash
docker logs -f -t --since="2018-02-08" --tail=100 id #时间之后，显示最后100行

docker logs --since 30m id #最近30分钟

docker logs -t --since="" --until "" id #时间段
```

grep过滤日志

```bash
docker logs id | grep -C 10 'error' # 打印error前后10行
docker logs -t id | grep '' >> log.txt # 输出到文件
```

[Docker — 从入门到实践 | Docker 从入门到实践 (docker-practice.com)](https://vuepress.mirror.docker-practice.com/)

### blog后端运行

```bash
docker run -d -p 8800:8800 -v /root/project/blog/service/data:/root/blog -v /root/project/blog/service/log:/logs --name blog1.0 blog:1.0

# blog前端远行
docker run -d -p 3000:80 --network=blogNetWork -v /root/project/blog/adminWeb/log:/var/log/nginx --name adminBlog admin_blog:2.0



```



### 容器只映射端口ipv6，没有ipv4

```bash
docker run -d -p 8800:8800 --network="host" -v /root/blog/data:/root/blog --name blog1.0 blog:1.0
```

使用host模式运行后无法访问8800

docker ps没有显示端口映射

![image-20240909090822299](C:\Users\贤\AppData\Roaming\Typora\typora-user-images\image-20240909090822299.png)

主机访问失败 curl http://127.0.0.1:8800/liy/

![image-20240909090946431](C:\Users\贤\AppData\Roaming\Typora\typora-user-images\image-20240909090946431.png)

检测主机端口监听情况，只有tcp6

![image-20240909091129972](C:\Users\贤\AppData\Roaming\Typora\typora-user-images\image-20240909091129972.png)

```bash
sysctl net.ipv4.ip_forward # 查看主机系统是否开启ipv4
```

[docker 启用ipv4，默认只有ipv6 - 咿呀哒喏 - 博客园 (cnblogs.com)](https://www.cnblogs.com/santia-god/p/17929794.html)

[Docker映射端口时仅有IPv6无法到IPv4解决方法使用阿里云源安装Docker后，使用docker-compose - 掘金 (juejin.cn)](https://juejin.cn/post/6844903953793024014)



### docker-compose

同目录下，创建文件

Dockerfile

docker-compose.yml

```yaml
# jar包运行
services:
  warehouse-backend:
    build: .
    image: warehouse-backend:latest
    container_name: warehouse-backend
    ports:
      - "5580:9999" # 映射主机5580端口到容器9999端口
    volumes:
      - ./logs:/app/logs # 将容器内的日志目录挂载到主机的 logs 目录
      - ./data:/app/image # 将容器内的数据目录挂载到主机的 data 目录
    environment:
      - JAVA_OPTS=-Xmx512M
    restart: always
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
```

```yaml
# 前端dist和nginx打包运行例子
services:
  warehouse-frontend:
    image: nginx
    container_name: warehouse-frontend
    ports:
      - "5590:3000"
    privileged: true
    restart: always
    volumes:
      - ./conf/nginx.conf:/etc/nginx/nginx.conf
      - ./conf/conf.d:/etc/nginx/conf.d
      - ./html:/usr/share/nginx/html
      - ./log:/var/log/nginx
```

```bash
docker-compose up -d # 目录下运行
```



# Git

## 分支操作 branch

```shell
git branch -v // 查看分支列表

git branch -m main // 将当前分支命名main，已有名将创建新分支

git branch -d master // 删除分支

git checkout main // 切换分支

git merge master // 合并master分支到当前分支
```



# Redis

```bash
docker run -p 6379:6379 --name redis6.2.6 -v /data/dockerData/redis/conf/redis.config:/etc/redis/redis.conf -v /data/dockerData/redis/data:/var/lib/redis -v /data/dockerData/redis/logs:/logs -d redis:6.2.6 redis-server /etc/redis/redis.conf
```



# Mysql

## 查询相同记录

当使用 `GROUP BY` 子句结合 `HAVING count(*) > 1` 时，你实际上是在查询那些在分组后具有多条记录的组。然而，当你使用 `SELECT *` 时，对于每个分组，MySQL 默认只返回一条记录。这是因为在没有明确指定聚合函数的情况下，对于分组的每个字段，MySQL 会随机选择一个值（这可能不是重复记录中的任何一条）

```sql
SELECT t1.*
FROM db_warehouse.checkin t1
INNER JOIN (
    SELECT user_id, `date`
    FROM db_warehouse.checkin
    GROUP BY user_id, `date`
    HAVING COUNT(*) > 1
) t2 ON t1.user_id = t2.user_id AND t1.`date` = t2.`date`;
```

## 赋予新用户权限

mysql8.4；Dearver23.1；

数据库在云主机，本地用Dearver连接，要为db_warehouse数据库创建一个用户；

```sql
create user 'db_warehouse'@'%' identified by '密码'; //创建用户

GRANT privileges ON databasename.tablename TO 'username'@'host'
-- privileges：用户的操作权限，如SELECT，INSERT，UPDATE等，如果要授予所的权限则使用ALL
-- databasename：数据库名
-- tablename：表名，如果要授予该用户对所有数据库和表的相应操作权限则可用表示，如.*

-- 以上命令授权的用户不能给其它用户授权，如果想让该用户可以授权
GRANT privileges ON databasename.tablename TO 'username'@'host' WITH GRANT OPTION;

-- 撤销权限
REVOKE privilege ON databasename.tablename FROM 'username'@'host';

-- 删除用户
DROP USER 'username'@'host';
```

## 安装Mysql8.4

CentOS Stream 9平台安装MySQL8.4.0

官方下载，RedHatLinux版本， 下载tar包

或者服务器直接下载：wget https://dev.mysql.com/get/Downloads/MySQL-8.4/mysql-8.4.0-1.el9.x86_64.rpm-bundle.tar

随便找个目录解压，

```bash
tar -xvf mysql-8.4.0-1.el9.x86_64.rpm-bundle.tar # 解压
yum install -y libaio # 一些工具

rpm -ivh --nodeps mysql*rpm # 批量安装

vim /etc/my.cnf # 配置文件，建议跳过，之后运行成功再考虑修改配置

systemctl start mysqld # 启动

systemctl enable mysqld # 设置开机自启

grep -i root /var/log/mysqld.log #日志，查看初始密码

set password for root@localhost = '123456'; # 修改密码，初始密码策略会要求复杂密码

show variables like 'validate_password%'; # 查看密码策略
set global validate_password.policy=0; # 修改密码验证策略低要求(0或LOW代表低级)

ALTER user 'root'@'localhost' IDENTIFIED BY '123456' # 修改密码

# 配置远程连接
use mysql;			# 使用mysql库
update user set host="%" where user="root";		# 将host修改为所有ip均可以访问
flush privileges;	# 刷新权限
```

```bash
# my.cnf
# 官方文档
# http://dev.mysql.com/doc/refman/8.4/en/server-configuration-defaults.html

[client]
# 设置默认端口
port = 3306
# 设置默认字符集
default-character-set = utf8mb4

[mysql]
# 设置mysql命令行客户端默认字符集
default-character-set = utf8mb4
# 设置命令行客户端提示符格式（用户名、主机名、数据库、时、分、秒）
prompt="\\u@\\h :\\d \\R:\\m:\\s>" 

[mysqld]
# 根据服务器内存调整 InnoDB 缓冲池大小。对于专用服务器，建议设置为总内存的 70%，对于其他服务器建议为 10%。此配置项用于优化 InnoDB 的性能。
innodb_buffer_pool_size = 512M
# 调整缓冲区大小优化报表服务器性能。
join_buffer_size = 128M
sort_buffer_size = 2M
read_rnd_buffer_size = 2M

# MySQL 数据库文件存储的目录。
datadir=/var/lib/mysql
# MySQL 进程的套接字文件位置，用于本地连接。
socket=/var/lib/mysql/mysql.sock
# 错误日志文件的位置
log-error=/var/log/mysqld.log
# MySQL 服务的 PID 文件位置，用于记录当前 MySQL 进程的 ID。
pid-file=/var/run/mysqld/mysqld.pid

# 解决 MySQL 登录时出现的认证插件问题
# default_authentication_plugin = mysql_native_password
# 解决ERROR 2002 (HY000): Can't connect to MySQL server on '192.168.31.16'
mysql_native_password=ON
# 密码验证策略为 LOW，表示较低的密码强度要求。
validate_password.policy=LOW
# 密码最小长度未为1
validate_password.length=1


# MySQL 服务的系统用户
#user=mysql
# 服务器默认字符集
character-set-server = utf8mb4
# 字符集排序规则
collation-server = utf8mb4_general_ci
# 客户端连接MySQL的字符集
init_connect='SET NAMES utf8mb4' 

# 二进制mysql binlog日志文件保存的过期时间7天，单位秒
binlog_expire_logs_seconds=604800
# 限制单个二进制日志文件大小
max_binlog_size = 1G 

# 把未使用到索引的sql记录到慢查询日志
log_queries_not_using_indexes = 1
# 启用慢查询sql日志记录
slow_query_log = 1
# 慢查询的阈值为 3 秒，超过这个时间的查询将被记录到慢查询日志
long_query_time = 3

# 设置临时表空间文件的路径及大小，初始大小为 12MB，自动扩展，最大大小为 1GB。
innodb_temp_data_file_path = ibtmp1:12M:autoextend:max:1G
# 设置服务器默认时区
default_time_zone="+8:00"
# 日志时间戳为系统时间
log_timestamps = system 

# MySQL 进程可以打开的最大文件数量为 65535
open_files_limit = 65535
# 连接数达到 max_connections 时的等待连接数，超过此数将不被授予连接资源。
back_log=600
# 避免Too many connections错误，最大并发连接数为 1000，过小会影响连接数量，此参数过大会导致资源用完。
max_connections = 1000
# 单个用户的最大连接数为 1000，设置为 0 表示不限制。
max_user_connections=1000
# 同时打开的表数量为 1024
table_open_cache = 1024
# 缓存数量
table_definition_cache = 1024
# 线程栈大小
thread_stack = 512K
# 线程缓存
thread_cache_size = 1500
# 排序缓冲区
sort_buffer_size = 12M
# 连接缓冲区
join_buffer_size = 12M
# 顺序扫描缓冲区
read_buffer_size = 24M
# 随机读缓冲区
read_rnd_buffer_size = 8M
# 大批量插入缓冲区
bulk_insert_buffer_size = 4M
# 交互式和非交互式会话的超时时间为 600 秒
interactive_timeout = 600
wait_timeout = 600
# 临时表大小
tmp_table_size = 48M
max_heap_table_size = 32M
# 二进制日志缓存的大小为 12MB 和最大 50MB。
binlog_cache_size = 12M
max_binlog_cache_size = 50M
# 索引缓冲区大小
key_buffer_size=384M

# 事务提交时将日志缓冲区中的重做日志写入日志文件，但不会立即写入磁盘，值为 2 表示每秒写入一次。
innodb_flush_log_at_trx_commit = 2 
# 设置 InnoDB 日志缓冲区大小为 64MB。
innodb_log_buffer_size=64M
innodb_redo_log_capacity=52428800

# 最大允许的包
max_allowed_packet = 48M

# 连接空闲超过一定时间后的超时时间为 300 秒
interactive_timeout = 300
# 与interactive_timeout参数同时配置生效
wait_timeout = 300

# 禁用域名解析以提高性能，值为 1 表示启用
skip_name_resolve = 1

# 禁用符号链接以防止安全风险，值为 no 表示不禁用。
skip-symbolic-links=no

# 设置 InnoDB 为每个表使用独立的表空间文件，值为 1 表示启用
innodb_file_per_table = 1

# 设置 InnoDB 缓冲池块大小为 256MB，缓冲池总大小为 512M。
innodb_buffer_pool_chunk_size= 256M
innodb_buffer_pool_size = 512M

# 设置不显示的警告列表
log_error_suppression_list='MY-013712'


```

### 远程连接错误

DBeaver连接时报错：Public Key Retrieval is not allowed Public Key Retrieval is not allowed

需要检索公钥，与AllowPublicKeyRetrieval值相关，mysql默认为false，需要将其设为true

点击驱动属性，AllowPublicKeyRetrieval设为true；

JDBC连接加allowPublicKeyRetrieval=true。



### 报错

```bash
systemctl start mysqld

# 启动失败
Job for mysqld.service failed because the control process exited with error code.
See "systemctl status mysqld.service" and "journalctl -xeu mysqld.service" for details.

# 查看systemctl status mysqld.service，无可用信息
systemctl status mysqld.service
mysqld.service - MySQL Server
     Loaded: loaded (/usr/lib/systemd/system/mysqld.service; enabled; vendor preset: disabled)
     Active: failed (Result: exit-code) since Thu 2024-09-19 09:21:14 CST; 59s ago
       Docs: man:mysqld(8)
             http://dev.mysql.com/doc/refman/en/using-systemd.html
    Process: 539765 ExecStartPre=/usr/bin/mysqld_pre_systemd (code=exited, status=0/SUCCESS)
    Process: 539844 ExecStart=/usr/sbin/mysqld $MYSQLD_OPTS (code=exited, status=1/FAILURE)
   Main PID: 539844 (code=exited, status=1/FAILURE)
     Status: "Server shutdown complete (with return value = 1)"
      Error: 22 (Invalid argument)
        CPU: 1.659s

# 查看mysql系统日志
cat /var/log/mysqld.log 
```

![image-20240919094626608](C:\Users\贤\AppData\Roaming\Typora\typora-user-images\image-20240919094626608.png)

注释/etc/my.cnf  validate_password.policy=LOW

没解决，猜测是之前安装过mysql没卸载完全，或者my.cnf文件问题

重新卸载干净mysql重装解决。



## 数据库转储sql

使用mysqldump工具，MySQL自带

```bash
mysqldump - u 用户名 -p 库名 表名 > '/root/test.sql'
mysqldump -u root -p ku > '/root/test/sql' # 导出整个数据库sql
```



# 漏洞

```
/Department/departmentId-all1
```



合同上传 图片、pdf

采购员 加添加供应商



采购员   分车队采购 车队信息，全部车牌， 不分车牌拉多少，

结算时算车费，单价，路程，总价

出库

# 其他

软仓：https://ruancang.net/

MacWK：https://macwk.cn/

positiverecords：https://positiverecords.ru/

appstorrent：https://appstorrent.ru/

三顿导航：https://www.sandunppt.com/

MacBl：https://www.macbl.com/
