<script setup lang="ts">
import { getResourcePage, deleteResource } from "@/api/resource";
import { ResourcePageVO } from "@/api/resource/types";

defineOptions({
  name: "Resource",
  inheritAttrs: false,
});

const queryFormRef = ref(ElForm);

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<any>({
  pageNo: 1,
  pageSize: 10,
});

const resourceList = ref<ResourcePageVO[]>();

/** 查询 */
function handleQuery() {
  loading.value = true;
  getResourcePage(queryParams)
    .then(({ data }: any) => {
      resourceList.value = data.records;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}
/** 重置查询 */
function resetQuery() {
  queryParams.pageNo = 1;
  handleQuery();
}

/** 行checkbox 选中事件 */
function handleSelectionChange(selection: any) {
  ids.value = selection.map((item: any) => item.id);
}

function handleDelete(id?: number) {
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
    deleteResource(formIds.value)
      .then(() => {
        ElMessage.success("删除成功");
        resetQuery();
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
          v-hasPerm="['system:resource:delete']"
          @click="handleDelete()"
          ><i-ep-delete />批量删除</el-button
        >
      </template>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="resourceList"
        highlight-current-row
        stripe
        fit
        max-height="600px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          label="文件地址"
          align="center"
          prop="url"
          :show-overflow-tooltip="true"
        />
        <el-table-column label="文件类型" align="center" prop="type" />
        <el-table-column label="存储平台" align="center" prop="platform" />
        <el-table-column label="创建时间" align="center" prop="createTime" />
        <el-table-column fixed="right" align="center" label="操作" width="220">
          <template #default="scope">
            <el-button
              type="danger"
              link
              size="small"
              v-hasPerm="['system:resource:delete']"
              @click="handleDelete(scope.row.id)"
            >
              <i-ep-delete />删除
            </el-button>
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
