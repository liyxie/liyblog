<script setup lang="ts">
import { getCommentPage, deleteComment } from "@/api/message/comment";

defineOptions({
  name: "AdminLog",
  inheritAttrs: false,
});

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryFormRef = ref(ElForm);
const queryParams = reactive<any>({
  pageNo: 1,
  pageSize: 10,
});

const tabelData = ref<any[]>();

/** 查询 */
function handleQuery() {
  loading.value = true;
  getCommentPage(queryParams)
    .then(({ data }) => {
      tabelData.value = data.records;
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

/** 删除标签 */
function handleDelete(commentId: any) {
  const commentIds = ref<any>([]);
  if (commentId) {
    commentIds.value.push(commentId);
  }
  if (ids.value.length) {
    commentIds.value = ids.value;
  }
  if (!commentIds) {
    ElMessage.warning("请勾选删除项");
    return;
  }

  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    deleteComment(commentIds.value)
      .then(() => {
        ElMessage.success("删除成功");
        handleQuery();
      })
      .finally(() => (loading.value = false));
  });
}

onMounted(() => {
  handleQuery();
});
</script>

<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="keywords" label="用户名称">
          <el-input
            v-model="queryParams.keywords"
            placeholder="用户名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleQuery"
            ><i-ep-search />搜索</el-button
          >
          <el-button @click="resetQuery"><i-ep-refresh />重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never" class="table-container">
      <template #header>
        <el-button
          type="danger"
          :disabled="ids.length === 0"
          v-hasPerm="['system:comment:delete']"
          @click="handleDelete()"
          ><i-ep-delete />批量删除</el-button
        >
      </template>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="tabelData"
        highlight-current-row
        stripe
        fit
        max-height="600px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" align="center" />
        <el-table-column prop="avatar" align="center" width="150" label="头像">
          <template #default="scope">
            <el-avatar
              shape="square"
              :size="50"
              :src="scope.row.avatar"
            ></el-avatar>
          </template>
        </el-table-column>
        <el-table-column
          prop="nickname"
          align="center"
          width="200"
          label="评论用户"
        />
        <el-table-column
          prop="replyNickname"
          align="center"
          width="200"
          label="回复用户"
        />
        <el-table-column
          prop="articleTitle"
          align="center"
          width="250"
          label="所属文章"
        />
        <el-table-column prop="content" align="center" width="400" label="内容">
          <template #default="scope">
            <span v-html="scope.row.content" class="comment-content" />
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          width="250"
          align="center"
          label="评论时间"
        />
        <el-table-column
          align="center"
          label="操作"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              icon="Delete"
              size="small"
              v-hasPerm="['system:comment:delete']"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-if="total > 0"
        v-model:total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="handleQuery"
      />
    </el-card>
  </div>
</template>
