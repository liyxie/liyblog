<script setup lang="ts">
import { getAdminLogPage, deleteAdminLog } from "@/api/logs/adminLog";

defineOptions({
  name: "AdminLog",
  inheritAttrs: false,
});

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const methodStyle = ref([
  {
    name: "POST",
    type: "success",
  },
  {
    name: "GET",
    type: "primary",
  },
  {
    name: "DELETE",
    type: "danger",
  },
  {
    name: "PUT",
    type: "warning",
  },
]);

const queryParams = reactive<any>({
  pageNo: 1,
  pageSize: 20,
});

const adminLogList = ref<any[]>();

/** 查询 */
function handleQuery() {
  loading.value = true;
  getAdminLogPage(queryParams)
    .then(({ data }) => {
      adminLogList.value = data.records;
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
    deleteAdminLog(ids.value)
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
          v-hasPerm="['system:adminLog:delete']"
          @click="handleDelete()"
          ><i-ep-delete />批量删除</el-button
        >
      </template>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="adminLogList"
        highlight-current-row
        stripe
        fit
        max-height="600px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="expand">
          <template #default="scope">
            <el-scrollbar max-height="400px">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-row>
                  <el-form-item label="请求接口">
                    <span>{{
                      scope.row.classPath + scope.row.requestUrl
                    }}</span>
                  </el-form-item>
                </el-row>
                <el-row>
                  <el-form-item label="请求参数">
                    <span>{{ scope.row.paramsJson }}</span>
                  </el-form-item>
                </el-row>
              </el-form>
            </el-scrollbar>
          </template>
        </el-table-column>
        <el-table-column align="center" type="selection" width="55" />
        <el-table-column
          prop="username"
          align="center"
          width="100"
          label="操作人"
        />
        <el-table-column
          prop="requestUrl"
          align="center"
          width="250"
          label="请求接口"
        />
        <el-table-column
          prop="type"
          align="center"
          width="100"
          label="请求方式"
        >
          <template #default="scope">
            <span v-for="item in methodStyle" :key="item.name">
              <el-tag v-if="scope.row.type === item.name" :type="item.type">{{
                scope.row.type
              }}</el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="operationName" align="center" label="接口名" />
        <el-table-column prop="ip" width="200" align="center" label="IP" />
        <el-table-column prop="source" align="center" label="IP来源" />
        <el-table-column align="center" label="请求耗时" width="150">
          <template #default="scope">
            <span
              ><el-tag type="info">{{ scope.row.spendTime }} ms</el-tag></span
            >
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          align="center"
          width="200"
          label="创建时间"
        />
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
