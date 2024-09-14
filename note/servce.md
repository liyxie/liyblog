# Activiti

## 程序启动自动创建数据库表失败

```shell
Caused by: java.sql.SQLSyntaxErrorException: Table 'activiti.act_ge_property' doesn't exist
```

处理：

数据库配置链接加

```shell
nullCatalogMeansCurrent = true 
```

原因：同一数据源下其他库的表会影响activiti表创建，activiti表创建前会检索数据源全部库，当检索到act_开头表，会判断为已存在activiti工作表，即使

```shell
databaseSchemaUpdate=true
```

activiti检测是否已存在工资表

```java
public boolean isTablePresent(String tableName) {
        if (!this.dbSqlSessionFactory.isTablePrefixIsSchema()) {
            tableName = this.prependDatabaseTablePrefix(tableName);
        }

        Connection connection = null;

        try {
            connection = this.sqlSession.getConnection();
            // 使用了 JDBC 的 `DatabaseMetaData` 接口来获取数据库中表的元数据
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet tables = null;
            String catalog = this.connectionMetadataDefaultCatalog;
            if (this.dbSqlSessionFactory.getDatabaseCatalog() != null && this.dbSqlSessionFactory.getDatabaseCatalog().length() > 0) {
                catalog = this.dbSqlSessionFactory.getDatabaseCatalog();
            }

            String schema = this.connectionMetadataDefaultSchema;
            if (this.dbSqlSessionFactory.getDatabaseSchema() != null && this.dbSqlSessionFactory.getDatabaseSchema().length() > 0) {
                schema = this.dbSqlSessionFactory.getDatabaseSchema();
            }

            String databaseType = this.dbSqlSessionFactory.getDatabaseType();
            if ("postgres".equals(databaseType)) {
                tableName = tableName.toLowerCase();
            }

            if (schema != null && "oracle".equals(databaseType)) {
                schema = schema.toUpperCase();
            }

            if (catalog != null && catalog.length() == 0) {
                catalog = null;
            }

            boolean var8;
            try {
                // 调用 `DatabaseMetaData.getTables` 方法来检索数据库中的表信息。(数据库的 catalog（目录）、schema（模式/命名空间）、表名，包含表类型的数组 `JDBC_METADATA_TABLE_TYPES`)
                tables = databaseMetaData.getTables(catalog, schema, tableName, JDBC_METADATA_TABLE_TYPES);
                var8 = tables.next();
            } finally {
                try {
                    tables.close();
                } catch (Exception var16) {
                    log.error("Error closing meta data tables", var16);
                }

            }

            return var8;
        } catch (Exception var18) {
            throw new ActivitiException("couldn't check if tables are already present using metadata: " + var18.getMessage(), var18);
        }
    }
```

理论上catalog和schema指定情况下不会检索到其他库，但不知activiti任何设置它们的值；

当catalog和schema为null时，理论上因为mysql连接已经指定了数据库，应当使用配置链接的数据库检索；

这与链接配置的nullCatalogMeansCurrent参数有关，这个参数的主要作用是指定当 catalog 为 null 时，JDBC 驱动程序的行为。

- 当设置为 `true` 时，如果 catalog 为 null，JDBC 驱动将只在当前数据库（schema）中查找表，而不是在整个服务器上搜索
- 从 `mysql-connector-java` 5.x 版本开始，`nullCatalogMeansCurrent` 的默认值由 `true` 改为了 `false`。在 6.x 版本中，它又变回了 `true`，但在 8.x 版本中再次变为了 `false` 
- 因此，在使用 MySQL 8.x 或更高版本时，建议在数据库连接字符串中显式设置 `nullCatalogMeansCurrent=true`，以确保 JDBC 驱动程序在当前数据库中查找表，避免因扫描整个服务器而找到其他数据库中的相同名称的表，这可能会导致应用程序错误地认为所需的表已经存在

参考

[Spring Java 注解配置之 数据源配置 - 简书 (jianshu.com)](https://www.jianshu.com/p/8d23f7483297)

[MySQL 连接字符串中加入 nullCatalogMeansCurrent = true 的含义 - 快乐随行 - 博客园 (cnblogs.com)](https://www.cnblogs.com/jddreams/p/17864815.html)

[Activiti6集成springboot后不执行创建语句，直接报查询的表不存在_spring整合activiti6 pg数据库表不新建-CSDN博客](https://blog.csdn.net/sinat_34163739/article/details/103142013)



# Hutool的DateUtil注意事项

hutool：5.8.18

在做打卡程序时出现错误，早上8点时大部分人反馈打不了卡，经过检查发现是时区设置问题；

### 问题

打卡业务中在查询记录时用了

```sql
date = CURRENT_DATE
```

在新插入记录时采用了hutool的api

```java
String date = DateUtil.today();
```

结果today();得到的时间是UTC（协调世界时），就是以下的格式

2024-08-27T23:59:39；2024-08-28T00:00:50

而Mysql的CURRENT_DATE用的是Mysql服务器时区，国内一般会是UTC+8；

```sql
-- 查询mysql服务器全局和会话级别的时区设置，SYSTEM代表使用系统时区
SELECT @@global.time_zone, @@session.time_zone; 
```

UTC+8会比UTC少8小时，因此8点前today();得出的是前一天，CURRENT_DATE是当天，造成新打卡数据错误；

### 解决

设置DateUtil时区

# 待做

响应速度提升

升级SpringBoot3.x

架构改模块化或微服务

https://www.shiyit.com/shiyi/oauth/callback/gitee





http://localhost:8000/liy/oauth/callback/gitee?code=1d874b3860998a8f2a224d921fc0c8e12257db0817f3b77630b993755d8703f3&state=58c847f22d181dadbaa72baec426e384

