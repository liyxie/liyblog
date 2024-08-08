<script setup lang="ts">
import {
  getFriendPage,
  addFriend,
  updateFriend,
  topFriend,
  deleteFriend,
} from "@/api/friendLink";
import {
  FriendLinkForm,
  FriendLinkPageVO,
  FriendLinkQuery,
} from "@/api/friendLink/types";

defineOptions({
  name: "FriendLink",
  inheritAttrs: false,
});

const formRef = ref(ElForm);
const rules = reactive({
  avatar: [{ required: true, message: "网站头像不能为空", trigger: "blur" }],
  name: [
    { required: true, message: "网站名称不能为空", trigger: "blur" },
    { min: 1, max: 20, message: "长度在1到20个字符" },
  ],
  url: [
    { required: true, message: "网站地址不能为空", trigger: "blur" },
    {
      pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/,
      message: "请输入有效的网站地址",
    },
  ],
  info: [{ required: true, message: "网站简介不能为空", trigger: "blur" }],
  // email: [
  //   {required: true,pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/, message: '请输入正确的邮箱'},
  // ],
  status: [{ required: true, message: "网站状态不能为空", trigger: "blur" }],
  reason: [{ required: true, message: "下架原因不能为空", trigger: "blur" }],
  sort: [
    { required: true, message: "排序字段不能为空", trigger: "blur" },
    { pattern: /^[0-9]\d*$/, message: "排序字段只能为自然数" },
  ],
});

const dialog = reactive({
  title: "",
  visible: false,
});

const formData = reactive<FriendLinkForm>({
  name: "",
  info: "",
  url: "",
  avatar: "",
  status: 1,
  sort: 1,
  id: "",
});

const loading = ref(false);
const total = ref(0);
const ids = ref<number[]>([]);
const statusOptions = ref(["下架", "申请", "上架"]);
const statusTypes = ref(["warning", "danger", "success"]);

const queryFormRef = ref(ElForm);
const queryParams = reactive<FriendLinkQuery>({
  pageNo: 1,
  pageSize: 10,
});

const tabelData = ref<FriendLinkPageVO[]>();

/** 查询 */
function handleQuery() {
  loading.value = true;
  getFriendPage(queryParams)
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

/** 重置表单 */
function resetForm() {
  formRef.value.resetFields();
  formRef.value.clearValidate();
  formData.id = "";
  formData.name = "";
  formData.info = "";
  formData.url = "";
  formData.avatar = "";
  formData.email = "";
  formData.status = 1;
  formData.sort = 1;
  formData.reason = "";
}

/** 关闭表单弹窗 */
function closeDialog() {
  dialog.visible = false;
  resetForm();
}

/** 打开表单弹窗 */
function openDialog(job?: Object) {
  dialog.visible = true;
  if (job) {
    dialog.title = "修改友链";
    Object.assign(formData, job);
  } else {
    dialog.title = "新增友链";
  }
}

/** 保存提交 */
function handleSubmit() {
  formRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const id = formData.id;
      if (id) {
        updateFriend(formData)
          .then(() => {
            ElMessage.success("修改成功");
            closeDialog();
            resetQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        addFriend(formData)
          .then(() => {
            ElMessage.success("新增成功");
            closeDialog();
            resetQuery();
          })
          .finally(() => (loading.value = false));
      }
    }
  });
}

//置顶友链
function handleTop(row: any) {
  topFriend(row.id).then(() => {
    ElMessage.success("置顶成功");
    handleQuery();
  });
}

/** 删除 */
function handleDelete(friendLinkId: any) {
  const friendLinkIds = ref<any>([]);
  if (friendLinkId) {
    friendLinkIds.value.push(friendLinkId);
  }
  if (ids.value.length) {
    friendLinkIds.value = ids.value;
  }
  if (!friendLinkIds) {
    ElMessage.warning("请勾选删除项");
    return;
  }
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    deleteFriend(friendLinkIds.value)
      .then(() => {
        ElMessage.success("操作成功");
        handleQuery();
      })
      .finally(() => (loading.value = false));
  });
}

/** 行checkbox 选中事件 */
function handleSelectionChange(selection: any) {
  ids.value = selection.map((item: any) => item.id);
}

onMounted(() => {
  handleQuery();
});
</script>

<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item label="友链名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入友链名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="友链状态" prop="status">
          <el-select
            @change="handleQuery"
            v-model="queryParams.status"
            style="width: 200px"
            placeholder="请选择友链状态"
            clearable
          >
            <el-option
              v-for="(item, index) in statusOptions"
              :key="index"
              :label="item"
              :value="index"
            ></el-option>
          </el-select>
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
          type="success"
          @click="openDialog()"
          v-hasPerm="['system:friendLink:add']"
          ><i-ep-plus />新增</el-button
        >
        <el-button
          type="danger"
          :disabled="ids.length === 0"
          @click="handleDelete"
          v-hasPerm="['system:friendLink:delete']"
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
        @selection-change="handleSelectionChange"
        max-height="600px"
      >
        <el-table-column type="selection" align="center" />
        <el-table-column label="网站图标" width="120" align="center">
          <template #default="scope">
            <img
              v-if="scope.row.avatar"
              :src="[scope.row.avatar]"
              style="width: 50px; height: 50px"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          align="center"
          label="网站名称"
          width="200"
        />
        <el-table-column
          prop="info"
          align="center"
          width="200"
          label="网站描述"
        />
        <el-table-column align="center" width="350" label="网站地址">
          <template #default="scope">
            <el-link :underline="false" :href="scope.row.url" target="_blank">{{
              scope.row.url
            }}</el-link>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="status" label="状态">
          <template #default="scope">
            <span>
              <el-tag :type="statusTypes[scope.row.status]">
                {{ statusOptions[scope.row.status] }}
              </el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="sort" sortable label="排序">
          <template #default="scope">
            <span>
              <el-tag type="warning">{{ scope.row.sort }}</el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="createTime"
          width="200"
          label="创建时间"
        >
          <template #default="scope">
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="操作"
          width="230"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              @click="handleTop(scope.row)"
              type="info"
              size="small"
              link
              icon="Top"
              v-hasPerm="['system:friendLink:top']"
              >置顶
            </el-button>
            <el-button
              @click="openDialog(scope.row)"
              type="primary"
              size="small"
              link
              icon="Edit"
              v-hasPerm="['system:friendLink:update']"
              >编辑
            </el-button>
            <el-button
              type="danger"
              link
              @click="handleDelete(scope.row.id)"
              size="small"
              icon="Delete"
              v-hasPerm="['system:friendLink:delete']"
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

      <!-- 添加或修改对话框 -->
      <el-dialog
        :title="dialog.title"
        v-model="dialog.visible"
        width="800px"
        append-to-body
        @close="closeDialog"
      >
        <el-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-width="120px"
        >
          <el-form-item label="网站头像" prop="avatar">
            <el-input v-model="formData.avatar" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="网站名称" prop="name">
            <el-input v-model="formData.name" auto-complete="off"></el-input>
          </el-form-item>

          <el-form-item label="网站简介" prop="info">
            <el-input v-model="formData.info" auto-complete="off"></el-input>
          </el-form-item>

          <el-form-item label="网站地址" prop="url">
            <el-input v-model="formData.url" auto-complete="off"></el-input>
          </el-form-item>

          <el-form-item label="站长邮箱" prop="email">
            <el-input v-model="formData.email" auto-complete="off"></el-input>
          </el-form-item>

          <el-form-item label="网站状态" prop="status">
            <el-select v-model="formData.status" placeholder="请选择">
              <el-option
                v-for="(item, index) in statusOptions"
                :key="index"
                :label="item"
                :value="index"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item
            v-if="formData.status === 0"
            label="下架原因"
            prop="reason"
          >
            <el-input v-model="formData.reason" auto-complete="off"></el-input>
          </el-form-item>

          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="formData.sort"></el-input-number>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialog.visible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
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
