package com.liy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * Es的测试控制器
 */
@RestController
@RequiredArgsConstructor
public class EsTestController {
//
//    private final ApiArticleService apiArticleService;
//
//    private final EasyesMapper easyesMapper;
//
//    private final DataEventPublisherService dataEventPublisherService;
//
//
//    /**
//     * 在使用es前必须先执行这个方法
//     * @PostConstruct 会在服务器启动时执行该方法
//     */
//    @GetMapping("createIndex")
//    public void createIndex(){
//        easyesMapper.createIndex();
//    }
//
//
//    /**
//     * 根据关键词分页并分词查询
//     * @return
//     */
//    @GetMapping("search")
//    public ResponseResult test(String keyword){
//       return apiArticleService.searchArticle(keyword);
//    }
//
//
//    /**
//     * 根据id删除es中的数据
//     * @param id
//     */
//    @DeleteMapping("delete/{id}")
//    public void deleteData(@PathVariable Long id){
//        easyesMapper.deleteById(id);
//    }
//
//    /**
//     * 修改es中的数据
//     * @param articleElastic
//     */
//    @PutMapping("update")
//    public void update(@RequestBody ArticleElastic articleElastic){
//        easyesMapper.updateById(articleElastic);
//    }
//
//    /**
//     * 添加es中的数据
//     * @param articleElastic
//     * @return
//     */
//    @PostMapping ("add")
//    public String add(@RequestBody ArticleElastic articleElastic){
//        easyesMapper.insert(articleElastic);
//        dataEventPublisherService.publishData(DataEventEnum.ES_ADD,articleElastic);
//        return "执行完毕";
//    }
}
