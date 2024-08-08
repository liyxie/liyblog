<script setup lang="ts">
import { getMessagePage, deleteMessage } from "@/api/message/message";

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
  getMessagePage(queryParams)
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
function handleDelete(id: any) {
  const formIds = ref<any>([]);
  if (id) {
    formIds.value.push(id);
  }
  if (ids.value.length) {
    formIds.value = ids.value;
  }
  if (!formIds) {
    ElMessage.warning("请勾选删除项");
    return;
  }

  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    deleteMessage(formIds.value)
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
        <el-form-item prop="name" label="用户名称">
          <el-input
            v-model="queryParams.name"
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
          @click="handleDelete"
          v-hasPerm="['system:message:delete']"
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
        <el-table-column type="selection" align="center"> </el-table-column>
        <el-table-column prop="avatar" align="center" width="100" label="头像">
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
          width="130"
          label="昵称"
        />
        <el-table-column
          prop="content"
          align="center"
          width="300"
          label="内容"
        />
        <el-table-column
          prop="ipAddress"
          align="center"
          width="150"
          label="ip地址"
        />
        <el-table-column
          prop="ipSource"
          align="center"
          width="200"
          label="ip来源"
        />
        <el-table-column prop="status" align="center" width="180" label="状态">
          <template #default="scope">
            <el-tag type="success" v-if="scope.row.status === 1">正常</el-tag>
            <el-tag v-else type="info">审核中</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" align="center" label="留言时间" />
        <el-table-column
          align="center"
          label="操作"
          width="160"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              size="small"
              icon="Delete"
              v-hasPerm="['system:message:delete']"
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
