Linux



```


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

```shell
docker run -d -p 8800:8800 --network="host" -v /root/blog/data:/root/blog --name blog1.0 blog:1.0

blog前端远行


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

# Git

## 分支操作 branch

```shell
git branch -v // 查看分支列表

git branch -m main // 将当前分支命名main，已有名将创建新分支

git branch -d master // 删除分支

git checkout main // 切换分支

git merge master // 合并master分支到当前分支
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

## 赋予新用户权限失败

mysql8.4；Dearver23.1；

数据库在云主机，本地用Dearver连接，要为db_warehouse数据库创建一个用户；

```sql
create user 'db_warehouse'@'%' identified by 'liy_warehouse'; //创建用户
// 用户创建是成功的

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

