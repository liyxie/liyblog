<script setup lang="ts">
import {
  getFeedBackPage,
  deleteFeedBack,
  updateFeedBack,
} from "@/api/message/feedback";

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
  getFeedBackPage(queryParams)
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
    deleteFeedBack(formIds.value)
      .then(() => {
        ElMessage.success("删除成功");
        handleQuery();
      })
      .finally(() => (loading.value = false));
  });
}

//修改
function handleSolve(id: any) {
  let feedback = {
    id: id,
    status: 1,
  };
  updateFeedBack(feedback).then((res) => {
    ElMessage.success("操作成功");
    handleQuery();
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
        <el-form-item prop="keywords" label="反馈类型">
          <el-select
            v-model="queryParams.type"
            placeholder="选择反馈类型"
            style="width: 240px"
            @change="handleQuery"
            clearable
          >
            <el-option label="需求" :value="1" />
            <el-option label="反馈" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never" class="table-container">
      <template #header>
        <el-button
          type="danger"
          :disabled="ids.length === 0"
          @click="handleDelete"
          v-hasPerm="['system:feedback:delete']"
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
        <el-table-column
          prop="type"
          align="center"
          width="100"
          label="反馈类型"
        >
          <template #default="scope">
            <el-tag v-if="scope.row.type === 1" type="success">需求</el-tag>
            <el-tag v-else type="danger">缺陷</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="title"
          align="center"
          width="250"
          label="需求标题"
        />
        <el-table-column prop="content" align="center" label="详细内容" />
        <el-table-column
          prop="imgUrl"
          width="160"
          align="center"
          label="附加图片"
        />
        <el-table-column prop="status" align="center" width="160" label="状态">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 1" type="success">解决</el-tag>
            <el-tag v-else type="danger">未解决</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          width="160"
          align="center"
          label="反馈时间"
        />
        <el-table-column
          align="center"
          label="操作"
          width="160"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              v-if="scope.row.status == 0"
              link
              type="success"
              size="small"
              v-hasPerm="['system:feedback:update']"
              @click="handleSolve(scope.row.id)"
              icon="Check"
              >解决
            </el-button>

            <el-button
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              icon="Delete"
              size="small"
              v-hasPerm="['system:feedback:delete']"
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
