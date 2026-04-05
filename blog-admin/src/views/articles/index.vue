<script setup lang="ts">
import { getTagPage } from "@/api/tag";
import { getCategoryPage } from "@/api/category";

import {
  getArticlePage,
  deleteArticle,
  topArticle,
  toggleArticlePublication,
  seoArticle,
  reptile,
} from "@/api/article";

import { getDataByDictType } from "@/api/dict";
import ArticleForm from "./ArticleForm.vue";

defineOptions({
  name: "Article",
  inheritAttrs: false,
});

const queryFormRef = ref(ElForm);

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<any>({
  pageNo: 1,
  pageSize: 15,
});

const tableData = ref<any[]>();

const formDialogVisible = ref(false);
const currentArticleId = ref<number>();

const readTypeStyle = ref<any>(["info", "danger", "warning", "success"]);
const readTypeList = ref(["无需验证", "评论阅读", "点赞阅读", "扫码阅读"]);
const publishList = ref<any>([]);
const isOriginalList = ref(["转载", "原创"]);
const yesOrNoDefaultValue = ref();
const yesOrNoList = ref<any>([]);
const categoryList = ref<any>([]);
const tagList = ref<any>([]);
const homeUrl = ref(import.meta.env.VITE_APP_HOME_URL + "article/");

// csdn文章抓取弹窗控制
const dialogTableVisible = ref(false);
const reptileUrl = ref("");

/** 查询 */
function handleQuery() {
  loading.value = true;
  getArticlePage(queryParams)
    .then(({ data }) => {
      tableData.value = data.records;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}
/** 重置查询 */
function resetQuery() {
  queryFormRef.value.resetFields();
  queryParams.pageNo = 1;
  handleQuery();
}

/** 行checkbox 选中事件 */
function handleSelectionChange(selection: any) {
  ids.value = selection.map((item: any) => item.id);
}

/** 打开文章表单弹窗 */
function openDialog(row?: any) {
  currentArticleId.value = row?.id;
  formDialogVisible.value = true;
}


/** 删除 */
function handleDelete(id?: number) {
  const formIds = ref<any>([]);
  if (id) {
    formIds.value.push(id);
  }
  if (ids.value.length) {
    formIds.value = ids.value;
  }
  if (!formIds.value) {
    ElMessage.warning("请勾选删除项");
    return;
  }
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    deleteArticle(formIds.value)
      .then(() => {
        ElMessage.success("删除成功");
        resetQuery();
      })
      .finally(() => (loading.value = false));
  });
}

//置顶标签
function handleTop(row: any) {
  topArticle(row)
    .then(() => {
      ElMessage.success("置顶成功");
      resetQuery();
    })
    .finally(() => (loading.value = false));
}

function strSplit(item: any) {
  return item.split(",");
}


/**
 * 发布或下架文章
 */
function handleUpdateStatus(row: any, status: Number) {
  let article = { id: row.id, isPublish: status };
  toggleArticlePublication(article).then((res) => {
    ElMessage.success("修改成功");
    resetQuery();
  });
}

/**
 * SEO
 */
function handleSeo() {
  seoArticle(ids.value).then((res) => {
    ElMessage.success("推送成功");
  });
}

/**
 * 获取字典数据
 */
function getDictList() {
  let dictTypes = ["sys_publish_status", "sys_yes_no"];
  getDataByDictType(dictTypes).then((response) => {
    let dictMap = response.data;
    yesOrNoList.value = dictMap.sys_yes_no.list;
    yesOrNoDefaultValue.value = dictMap.sys_yes_no.defaultValue;
    publishList.value = dictMap.sys_publish_status.list;
  });
}

/**
 * 打开csdn抓取文章弹窗
 */
function openReptile() {
  dialogTableVisible.value = true;
  reptileUrl.value = "";
}

/**
 * 打卡随机图床弹窗
 */
function openCoverPlan() {
  // CoverPlanDialogTableVisible.value = true;
  // reptileUrl.value = "";
}

/**
 * 抓取文章
 */
function handleReptile() {
  reptile(reptileUrl.value).then(() => {
    ElMessage.success("抓取成功");
    dialogTableVisible.value = false;
  });
}

function getCategoryAndTag() {
  let params = { pageNo: 1, pageSize: 100 };
  getCategoryPage(params).then((res: any) => {
    categoryList.value = res.data.records;
  });
  getTagPage(params).then((res: any) => {
    tagList.value = res.data.records;
  });
}

onMounted(() => {
  handleQuery();
  getDictList();
  getCategoryAndTag();
});
</script>

<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="title" label="文章名称">
          <el-input v-model="queryParams.title" placeholder="文章名称" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="标签名" prop="tagId">
          <el-select style="width: 130px" v-model="queryParams.tagId" filterable clearable reserve-keyword
            @change="handleQuery" placeholder="请选择标签">
            <el-option v-for="item in tagList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类名" prop="categoryId">
          <el-select style="width: 130px" v-model="queryParams.categoryId" clearable reserve-keyword
            @change="handleQuery" placeholder="请选择分类">
            <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布状态" prop="isPublish">
          <el-select style="width: 130px" v-model="queryParams.isPublish" clearable reserve-keyword placeholder="是否发布"
            @change="handleQuery">
            <el-option v-for="(item, index) in publishList" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery"><i-ep-search />搜索</el-button>
          <el-button @click="resetQuery"><i-ep-refresh />重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never" class="table-container">
      <template #header>
        <el-button type="success" @click="openDialog()" v-hasPerm="['system:article:add']"><i-ep-plus />新增</el-button>
        <el-button type="primary" @click="openReptile" v-hasPerm="['system:article:reptile']"><el-icon>
            <MostlyCloudy />
          </el-icon>csdn文章抓取</el-button>
        <!-- <el-button
          type="primary"
          @click="openCoverPlan"
          v-hasPerm="['system:article:coverPlan']"
          ><el-icon> <MostlyCloudy /> </el-icon>随机图床</el-button
        > -->
        <el-button type="info" @click="handleSeo" v-hasPerm="['system:article:seo']"><el-icon>
            <MostlyCloudy />
          </el-icon>SEO</el-button>
        <el-button type="danger" :disabled="ids.length === 0" v-hasPerm="['system:article:delete']"
          @click="handleDelete()"><i-ep-delete />批量删除</el-button>
      </template>

      <!-- 列表 -->
      <el-table ref="dataTableRef" :data="tableData" highlight-current-row stripe fit
        @selection-change="handleSelectionChange" v-loading="loading" max-height="calc(100vh - 280px)">
        <el-table-column type="selection" width="45" align="center" />
        <el-table-column min-width="110" align="center" label="文章封面">
          <template #default="scope">
            <el-image class="article-cover" :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column prop="title" align="center" label="文章名称" min-width="160">
          <template #default="scope">
            <div class="cell-line-clamp">
              <el-link :underline="false" :href="homeUrl + scope.row.id" target="_blank">{{ scope.row.title }}</el-link>
            </div>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="70" label="状态">
          <template #default="scope">
            <span v-for="(item, index) in publishList" :key="index">
              <el-tag v-if="item.value == scope.row.isPublish" :type="item.style">{{ item.label }}</el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="80" label="分类">
          <template #default="scope">
            <el-tag style="margin-left: 3px" align="center" type="warning">{{ scope.row.categoryName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="120" label="标签">
          <template #default="scope">
            <div class="cell-tags">
              <el-tag style="margin-left: 3px" align="center" type="primary" v-for="item in strSplit(scope.row.tagNames)"
                :key="item">{{ item }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="isStick" align="center" width="65" label="置顶" v-hasPerm="['system:article:top']">
          <template #default="scope">
            <el-switch v-model="scope.row.isStick" :active-value="1" :inactive-value="0" @change="handleTop(scope.row)"
              active-color="#13ce66" />
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="80" label="阅读方式">
          <template #default="scope">
            <el-tag :type="readTypeStyle[scope.row.readType]">{{
              readTypeList[scope.row.readType]
              }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="nickname" min-width="80" align="center" label="文章作者" :show-overflow-tooltip="true" />
        <el-table-column min-width="140" align="center" prop="createTime" sortable label="添加时间" :show-overflow-tooltip="true" />
        <el-table-column align="center" min-width="60" label="类型">
          <template #default="scope">
            <span v-for="(item, index) in isOriginalList" :key="index">
              <el-tag :type="scope.row.isOriginal === 0 ? 'warning' : 'success'" :key="index"
                v-if="scope.row.isOriginal === index">{{ item }}
              </el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column width="180" fixed="right" align="center" label="操作">
          <template #default="scope">
            <el-button v-hasPerm="['system:article:toggleArticlePublication']" type="info" link size="small"
              v-if="scope.row.isPublish === 1" @click="handleUpdateStatus(scope.row, 0)" icon="Bottom">下架</el-button>
            <el-button v-hasPerm="['system:article:toggleArticlePublication']" v-if="scope.row.isPublish === 0"
              type="success" link size="small" @click="handleUpdateStatus(scope.row, 1)" icon="Position">
              发布
            </el-button>
            <el-button type="primary" size="small" link @click="openDialog(scope.row)" icon="Edit"
              v-hasPerm="['system:article:update']">编辑</el-button>
            <el-button size="small" link type="danger" @click="handleDelete(scope.row.id)" icon="Delete"
              v-hasPerm="['system:article:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-if="total > 0" :pager-count="3" :page-sizes="[15, 50, 100]" v-model:total="total"
        v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="handleQuery" />
    </el-card>

    <ArticleForm
      v-model="formDialogVisible"
      :article-id="currentArticleId"
      :category-list="categoryList"
      :tag-list="tagList"
      :publish-list="publishList"
      :yes-or-no-list="yesOrNoList"
      @success="resetQuery"
    />

    <el-dialog title="文章抓取" v-model="dialogTableVisible">
      <el-form>
        <el-form-item label="目标地址url">
          <el-input v-model="reptileUrl" autocomplete="off" />
        </el-form-item>
        <el-form-item label="tip">
          <span style="color: limegreen">
            请注意如下几点:<br />
            1.如果抓取成功则会自动保存到文章表中<br />
            2.CSDN的文章没有封面图片，所以会是随机图片<br />
            3.暂时只支持CSDN的文章抓取
          </span>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleReptile">确 定</el-button>
          <el-button @click="dialogTableVisible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style>
.article-cover {
  position: relative;
  width: 120px;
  height: 60px;
  border-radius: 4px;
}

.article-cover::after {
  content: "";
  background: rgba(0, 0, 0, 0.3);
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
}
</style>
