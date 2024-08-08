<script setup lang="ts">
import { getUserLogPage, deleteUserLog } from "@/api/logs/userLog";

defineOptions({
  name: "AdminLog",
  inheritAttrs: false,
});

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<any>({
  pageNo: 1,
  pageSize: 20,
});

const userLogList = ref<any[]>();

/** 查询 */
function handleQuery() {
  loading.value = true;
  getUserLogPage(queryParams)
    .then(({ data }) => {
      userLogList.value = data.records;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

/** 行checkbox 选中事件 */
function handleSelectionChange(selection: any) {
  ids.value = selection.map((item: any) => item.id);
}

/** 删除标签 */
function handleDelete() {
  if (!ids.value) {
    ElMessage.warning("请勾选删除项");
    return;
  }
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    deleteUserLog(ids.value)
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
    <el-card shadow="never" class="table-container">
      <template #header>
        <el-button
          type="danger"
          :disabled="ids.length === 0"
          v-hasPerm="['system:userLog:delete']"
          @click="handleDelete()"
          ><i-ep-delete />批量删除</el-button
        >
      </template>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="userLogList"
        highlight-current-row
        stripe
        fit
        max-height="600px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection" width="55" />
        <el-table-column prop="ip" align="center" width="130" label="IP" />
        <el-table-column
          prop="address"
          align="center"
          width="200"
          label="IP来源"
        />
        <el-table-column prop="accessOs" align="center" label="平台" />
        <el-table-column prop="browser" align="center" label="浏览器" />
        <el-table-column prop="type" align="center" label="操作类型" />
        <el-table-column prop="description" align="center" label="操作日志" />
        <el-table-column prop="model" align="center" label="操作模块" />
        <el-table-column prop="result" align="center" label="操作结果" />
        <el-table-column
          prop="createTime"
          align="center"
          width="200"
          sortable
          label="操作时间"
        >
          <template #default="scope">
            <span>{{ scope.row.createTime }}</span>
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
