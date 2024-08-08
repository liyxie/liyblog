package com.liy.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liy.common.ResponseResult;
import com.liy.dto.ColumnInfo;
import com.liy.dto.Config;
import com.liy.dto.TableInfo;
import com.liy.dto.Util;
import com.liy.exception.BusinessException;
import com.liy.mapper.SystemConfigMapper;
import com.liy.utils.PageUtil;
import com.liy.vo.system.TableListVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenerateService {
    private final JdbcTemplate jdbcTemplate;

    private final SystemConfigMapper systemConfigMapper;

    private static String comPath = "com.liy";

    private static String schema = "blog";


    private String doEntity(GroupTemplate gt, TableInfo table, String comPath) {
        Template t = gt.getTemplate("entity.template");

        t.binding("entityName", Util.toEntityTableName(table.getTableName()));
        t.binding("package",  Util.addComSeparator(comPath,  Config.entityFolderName));
        t.binding("logicName", Config.logicName);
        t.binding("_root", table);
        return t.render();
    }

    private String doMapperXml(GroupTemplate gt, TableInfo table, String comPath) {
        Template t = gt.getTemplate("mapper-xml.template");

        String entityName = Util.toEntityTableName(table.getTableName());
        t.binding("namespace", Util.addComSeparator(comPath,  Config.mapperFolderName, entityName + Config.mapperSuffix));
        return t.render();
    }
    private String doMapperJava(GroupTemplate gt, TableInfo table, String comPath) {
        Template t = gt.getTemplate("mapper-java.template");
        String entityName = Util.toEntityTableName(table.getTableName());

        t.binding("entityName", entityName);
        t.binding("package", Util.addComSeparator(comPath,  Config.mapperFolderName));
        t.binding("impPo", Util.addComSeparator(comPath,Config.entityFolderName,entityName));
        t.binding("_root", table);
        return t.render();
    }

    private String doServiceImpl(GroupTemplate gt, TableInfo table, String comPath) {
        Template t = gt.getTemplate("serviceImpl.template");
        String entityName = Util.toEntityTableName(table.getTableName());

        t.binding("poSuffix", Config.poSuffix);
        t.binding("entityName", entityName);
        t.binding("varName", Util.toEntityName(table.getTableName()));
        t.binding("package", Util.addComSeparator(comPath, Config.serviceFolderName));
        t.binding("impEntity", Util.addComSeparator(comPath, Config.entityFolderName,  entityName));
        t.binding("impMapper", Util.addComSeparator(comPath,Config.mapperFolderName, entityName + "Mapper"));
        t.binding("idType", table.getIdType());
        t.binding("_root", table);
        return t.render();
    }
    private String doService(GroupTemplate gt, TableInfo table, String comPath) {
        Template t = gt.getTemplate("service.template");
        String entityName = Util.toEntityTableName(table.getTableName());
        t.binding("varName", Util.toEntityName(table.getTableName()));
        t.binding("entityName", entityName);
        t.binding("package", Util.addComSeparator(comPath, Config.serviceFolderName));
        t.binding("impEntity", Util.addComSeparator(comPath, Config.entityFolderName,  entityName));
        t.binding("idType", table.getIdType());
        t.binding("_root", table);
        return t.render();
    }

    private String doController(GroupTemplate gt, TableInfo table, String comPath) {
        Template t = gt.getTemplate("controller.template");
        String entityName = Util.toEntityTableName(table.getTableName());
        String tableFileName = Util.removeUnderlineAndLowerCase(table.getTableName());

        t.binding("poSuffix", Config.poSuffix);
        t.binding("voSuffix", Config.voSuffix);
        t.binding("entityName", entityName);
        t.binding("varName", Util.toEntityName(table.getTableName()));
        t.binding("package", Util.addComSeparator(comPath,  Config.controllerFolderName));
        t.binding("impPo", Util.addComSeparator(comPath, tableFileName, Config.entityFolderName, Config.poFolderName, entityName + Config.poSuffix));
        t.binding("impVo", Util.addComSeparator(comPath, tableFileName, Config.entityFolderName, Config.voFolderName, entityName + Config.voSuffix));
        t.binding("impService", Util.addComSeparator(comPath, tableFileName, Config.serviceFolderName, entityName + "Service"));
        t.binding("idType", table.getIdType());
        t.binding("tableComment", table.getTableComment());
        t.binding("_root", table);
        return t.render();
    }

    public TableInfo getTableInfoList(String schema, String tableName) {
        TableInfo tableInfo = new TableInfo();

        String sql = " select DISTINCT COLUMN_NAME as columnName, ORDINAL_POSITION as ordinalPosition, DATA_TYPE as dataType, COLUMN_KEY as columnKey, COLUMN_COMMENT as columnComment, TABLE_COMMENT AS tableComment " +
                " from information_schema.COLUMNS c" +
                " left join INFORMATION_SCHEMA.TABLES t" +
                " on c.TABLE_NAME = t.TABLE_NAME" +
                " and c.TABLE_SCHEMA = t.TABLE_SCHEMA" +
                " where c.TABLE_NAME = '" + tableName + "' and c.table_schema = '" + schema + "'";

        List<ColumnInfo> columns = jdbcTemplate.query(sql, (resultSet, i) -> {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setColumnName(resultSet.getString(1));
            columnInfo.setEntityColumnName(Util.toEntityName(resultSet.getString(1)));
            columnInfo.setEntityColumnMethodName(Util.toEntityTableName(resultSet.getString(1)));
            columnInfo.setOrdinalPosition(resultSet.getInt(2));
            columnInfo.setDataType(resultSet.getString(3));
            columnInfo.setFieldType(Util.dbTypeToJavaType(resultSet.getString(3)));
            columnInfo.setColumnKey(resultSet.getString(4));
            columnInfo.setColumnComment(resultSet.getString(5));
            tableInfo.setTableComment(resultSet.getString(6));
            if (StringUtils.isNotBlank(columnInfo.getColumnKey())) {
                tableInfo.setIdType(columnInfo.getFieldType());
            }
            return columnInfo;
        });
        tableInfo.setTableName(tableName);
        tableInfo.setColumns(columns);

        return tableInfo;
    }

    public ResponseResult preview(String tableName) throws IOException {
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("beetl-back-end");
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);

        TableInfo tableInfo = getTableInfoList(schema, tableName);
        Map<String, String> map = new HashMap<>();

        String entity = doEntity(gt, tableInfo, comPath);

        String mapper = doMapperJava(gt, tableInfo, comPath);
        String mapperXml = doMapperXml(gt, tableInfo, comPath);

        String serviceImpl = doServiceImpl(gt, tableInfo, comPath);
        String service = doService(gt, tableInfo, comPath);
        String controller = doController(gt, tableInfo, comPath);

        map.put("vm/java/domain.java.vm",entity);
        map.put("vm/java/mapper.java.vm",mapper);
        map.put("vm/xml/mapper.xml.vm",mapperXml);
        map.put("vm/java/serviceImpl.java.vm",serviceImpl);
        map.put("vm/java/service.java.vm",service);
        map.put("vm/java/controller.java.vm",controller);

        return ResponseResult.success(map);
    }

    public ResponseResult selectListTables() {
        Page<TableListVO> page =  systemConfigMapper.selectTables(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()));
        return ResponseResult.success(page);
    }

    public void download(String tableName,String downloadPath) throws IOException {
        if (StringUtils.isBlank(downloadPath)) {
            throw new BusinessException("下载路径不能为空");
        }
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("beetl-back-end");
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);

        TableInfo tableInfo = getTableInfoList(schema, tableName);

        String entity = doEntity(gt, tableInfo, comPath);

        String mapper = doMapperJava(gt, tableInfo, comPath);
        String mapperXml = doMapperXml(gt, tableInfo, comPath);

        String serviceImpl = doServiceImpl(gt, tableInfo, comPath);
        String service = doService(gt, tableInfo, comPath);
        String controller = doController(gt, tableInfo, comPath);

        String name = Util.toEntityTableName(tableInfo.getTableName());
        Map<String, ResponseEntity<Resource>> responseMap = new HashMap<>();

        // 将字符串写入文件
        writeFile(entity, name + ".java",downloadPath);
        writeFile(mapper,  name + "Mapper.java",downloadPath);
        writeFile(mapperXml,  name + "Mapper.xml",downloadPath);
        writeFile(controller, name + "Controller.java",downloadPath);
        writeFile(serviceImpl, name + "ServiceImpl.java",downloadPath);
        writeFile(service, name + "Service.java",downloadPath);
    }

    private static void writeFile(String content, String fileName,String path) throws IOException {
        String genPath = getGenPath(path, fileName);
        FileUtils.writeStringToFile(new File(genPath), content, "UTF-8");
    }

    /**
     * 获取代码生成地址
     *
     * @return 生成地址
     */
    public static String getGenPath(String path, String fileName)
    {
        return path + File.separator + fileName;
    }

}
