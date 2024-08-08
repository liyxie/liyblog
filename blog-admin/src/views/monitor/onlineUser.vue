<script setup lang="ts">
import { getOnlineUserPage, kickOnlineUser } from "@/api/monitor/onlineUser";

defineOptions({
  name: "AdminLog",
  inheritAttrs: false,
});

const loading = ref(false);
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
  getOnlineUserPage(queryParams)
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

/** 删除标签 */
function handleDelete(token: any) {
  ElMessageBox.confirm("确认将该账号强制下线?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    kickOnlineUser(token)
      .then(() => {
        ElMessage.success("操作成功");
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
      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="tabelData"
        highlight-current-row
        stripe
        fit
      >
        <el-table-column align="center" prop="avatar" label="头像" width="180">
          <template #default="scope">
            <div class="block">
              <el-avatar :size="50" :src="scope.row.avatar"></el-avatar>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="nickname"
          label="昵称"
          width="180"
        />
        <el-table-column align="center" prop="ip" label="IP地址" />
        <el-table-column align="center" prop="city" label="登录地址" />
        <el-table-column align="center" prop="browser" label="浏览器" />
        <el-table-column align="center" prop="os" label="操作系统" />
        <el-table-column
          align="center"
          prop="loginTime"
          label="登录时间"
          width="180"
        />
        <el-table-column align="center" label="操作">
          <template #default="scope">
            <el-button
              type="danger"
              link
              @click="handleDelete(scope.row.tokenValue)"
              size="small"
              icon="Delete"
              v-hasPerm="['system:user:kick']"
              >强制下线</el-button
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
