<script setup lang="ts">
import { getJobLogPage, deleteJobLog, cleanJobLog } from "@/api/monitor/jobLog";

import { getDataByDictType } from "@/api/dict";

defineOptions({
  name: "JobLog",
  inheritAttrs: false,
});

const route = useRoute();
const router = useRouter();
const dialog = reactive({
  title: "",
  visible: false,
});

const formData = reactive<any>({});

const loading = ref(false);
const total = ref(0);
const ids = ref<number[]>([]);

const queryFormRef = ref(ElForm);
const queryParams = reactive<any>({
  pageNo: 1,
  pageSize: 20,
  startTime: null,
  endTime: null,
  jobId: route.query.jobId,
  jobName: null,
  jobGroup: null,
  status: null,
});

const tabelData = ref<any[]>();
const jobDictList = ref<any[]>();
const jobDictDefaultValue = ref<any[]>();
const dateRange = ref();

//获取字典数据
function getDictList() {
  let dictTypeList = ["sys_job_group"];
  getDataByDictType(dictTypeList).then((response) => {
    let dictMap = response.data;
    jobDictList.value = dictMap.sys_job_group.list;
    jobDictDefaultValue.value = dictMap.sys_job_group.defaultValue;
  });
}

/** 查询 */
function handleQuery() {
  loading.value = true;
  queryParams.startTime = null;
  queryParams.endTime = null;
  if (dateRange.value) {
    queryParams.startTime = dateRange.value[0];
    queryParams.endTime = dateRange.value[1];
  }
  getJobLogPage(queryParams)
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

/** 关闭表单弹窗 */
function closeDialog() {
  dialog.visible = false;
}

/** 打开表单弹窗 */
function openDialog(jobLog?: Object) {
  dialog.visible = true;
  dialog.title = "日志详情";
  Object.assign(formData, jobLog);
}

/** 清空 */
function handleClean() {
  ElMessageBox.confirm("确认要清空所有日志吗?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    cleanJobLog()
      .then(() => {
        ElMessage.success("操作成功");
        handleQuery();
      })
      .finally(() => (loading.value = false));
  });
}

/** 删除 */
function handleDelete(jobLogId: any) {
  const jobLogIds = ref<any>([]);
  if (jobLogId) {
    jobLogIds.value.push(jobLogId);
  }
  if (ids.value.length) {
    jobLogIds.value = ids.value;
  }
  if (!jobLogIds) {
    ElMessage.warning("请勾选删除项");
    return;
  }
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    deleteJobLog(jobLogIds.value)
      .then(() => {
        ElMessage.success("操作成功");
        handleQuery();
      })
      .finally(() => (loading.value = false));
  });
}
// 返回按钮
function handleBack() {
  router.push({ path: "job" });
}

/** 行checkbox 选中事件 */
function handleSelectionChange(selection: any) {
  ids.value = selection.map((item: any) => item.id);
}

onMounted(() => {
  handleQuery();
  getDictList();
});
</script>

<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item label="任务名称" prop="jobName">
          <el-input
            v-model="queryParams.jobName"
            placeholder="请输入任务名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="任务组名" prop="jobGroup">
          <el-select
            @change="handleQuery"
            v-model="queryParams.jobGroup"
            style="width: 200px"
            placeholder="请选择任务组名"
            clearable
          >
            <el-option
              v-for="dict in jobDictList"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="执行状态" prop="status">
          <el-select
            @change="handleQuery"
            clearable
            style="margin-left: 5px; width: 200px"
            v-model="queryParams.status"
            placeholder="请选择任务状态"
          >
            <el-option value="0" label="成功" />
            <el-option value="1" label="失败" />
          </el-select>
        </el-form-item>

        <el-form-item label="执行时间" prop="dateRange">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          >
          </el-date-picker>
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
          v-hasPerm="['system:jobLog:delete']"
          ><i-ep-delete />批量删除</el-button
        >
        <el-button
          type="warning"
          plain
          icon="Delete"
          v-hasPerm="['system:jobLog:clean']"
          @click="handleClean"
          >清空
        </el-button>
        <el-button type="primary" plain icon="DArrowLeft" @click="handleBack"
          >返回</el-button
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
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="日志编号" width="80" align="center" prop="id">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
          label="任务名称"
          align="center"
          prop="jobName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="任务组名"
          align="center"
          prop="jobGroup"
          :show-overflow-tooltip="true"
        >
          <template #default="scope">
            <span v-for="item in jobDictList" :key="item.value">
              <el-tag
                :type="item.style"
                v-if="scope.row.jobGroup === item.value"
              >
                {{ item.label }}
              </el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column
          label="调用目标字符串"
          align="center"
          prop="invokeTarget"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="日志信息"
          align="center"
          prop="jobMessage"
          :show-overflow-tooltip="true"
        />
        <el-table-column label="执行状态" align="center" prop="status">
          <template #default="scope">
            <el-tag v-if="scope.row.status === '0'" type="success">成功</el-tag>
            <el-tag v-if="scope.row.status === '1'" type="danger">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          label="执行时间"
          align="center"
          prop="createTime"
          width="180"
        />

        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              type="primary"
              link
              icon="View"
              @click="openDialog(scope.row)"
              size="small"
              >详细
            </el-button>

            <el-button
              type="danger"
              link
              icon="Delete"
              size="small"
              v-hasPerm="['system:jobLog:delete']"
              @click="handleDelete(scope.row.id)"
              >删除
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

      <!-- 调度日志详细 -->
      <el-dialog
        title="调度日志详细"
        v-model="dialog.visible"
        width="700px"
        append-to-body
      >
        <el-form ref="form" :model="formData" label-width="100px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="日志序号：">{{ formData.id }}</el-form-item>
              <el-form-item label="任务名称：">{{
                formData.jobName
              }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="任务分组：">{{
                formData.jobGroup
              }}</el-form-item>
              <el-form-item label="执行时间：">{{
                formData.startTime
              }}</el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="调用方法：">{{
                formData.invokeTarget
              }}</el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="日志信息：">{{
                formData.jobMessage
              }}</el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="执行状态：">
                <div v-if="formData.status === '0'">成功</div>
                <div v-else>失败</div>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="异常信息：" v-if="formData.status === 1">{{
                formData.exceptionInfo
              }}</el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <el-button @click="dialog.visible = false">关 闭</el-button>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>
<style lang="scss" scoped>
::v-deep(.el-form-item__label) {
  align-items: center !important;
}
</style>
