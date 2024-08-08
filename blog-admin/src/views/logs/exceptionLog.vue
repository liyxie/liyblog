<script setup lang="ts">
import {
  getExceptionLogPage,
  deleteExceptionrLog,
} from "@/api/logs/exceptionLog";

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

const dialogVisible = ref(false);
const form = ref<any>({});
const exceptionLogList = ref<any[]>();

/** 查询 */
function handleQuery() {
  loading.value = true;
  getExceptionLogPage(queryParams)
    .then(({ data }) => {
      exceptionLogList.value = data.records;
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
    deleteExceptionrLog(ids.value)
      .then(() => {
        ElMessage.success("删除成功");
        handleQuery();
      })
      .finally(() => (loading.value = false));
  });
}

//显示详情
function handleDetails(row: any) {
  form.value = row;
  dialogVisible.value = true;
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
          @click="handleDelete()"
          v-hasPerm="['system:exceptionLog:delete']"
          ><i-ep-delete />批量删除</el-button
        >
      </template>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="exceptionLogList"
        highlight-current-row
        stripe
        fit
        max-height="600px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection" width="55" />
        <el-table-column
          prop="exceptionMessage"
          :show-overflow-tooltip="true"
          align="center"
          width="600"
          label="异常内容"
        />
        <el-table-column
          prop="operation"
          align="center"
          width="200"
          label="接口名"
        />
        <el-table-column prop="ip" align="center" width="130" label="IP" />
        <el-table-column
          prop="ipSource"
          align="center"
          width="200"
          label="IP来源"
        />
        <el-table-column
          prop="username"
          align="center"
          width="130"
          label="操作人"
        />
        <el-table-column
          prop="createTime"
          align="center"
          width="200"
          label="创建时间"
        />
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button
              @click="handleDetails(scope.row)"
              link
              type="primary"
              icon="Document"
              >详情</el-button
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

      <el-dialog title="异常详情" v-model="dialogVisible">
        <h3>请求参数</h3>
        <span>{{ form.params }}</span>
        <h3>异常详情</h3>
        <el-scrollbar height="400px">
          <span>{{ form.exceptionJson }}}</span>
        </el-scrollbar>
      </el-dialog>
    </el-card>
  </div>
</template>
