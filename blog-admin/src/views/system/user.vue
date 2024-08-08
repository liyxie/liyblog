<script setup lang="ts">
import {
  getUserPage,
  addUser,
  updateUser,
  deleteUsers,
  getUserInfo,
} from "@/api/user";
import { getDataByDictType } from "@/api/dict";
import { getRolePage } from "@/api/role";
import { upload } from "@/api/file";

defineOptions({
  name: "User",
  inheritAttrs: false,
});

const dictLoginTypeList = ref<any>([]);
const loginTypeDefaultValue = ref([]);
const statusOptions = ref(["禁用", "正常"]);

const uploadPictureHost = ref(
  import.meta.env.VITE_APP_API_URL + "/file/upload"
);
const files = ref();

const queryFormRef = ref(ElForm);
const formRef = ref(ElForm);

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<any>({
  pageNo: 1,
  pageSize: 10,
});

const tableData = ref<any[]>();
const roleList = ref<any[]>();

const dialog = reactive({
  title: "",
  visible: false,
  type: "",
});

const formData = reactive<any>({
  name: "",
  sort: 1,
});

const rules = reactive({
  username: [
    { required: true, message: "请输入账号", trigger: "blur" },
    { min: 1, max: 50, message: "长度在1到50个字符" },
  ],
  nickname: [
    { required: true, message: "请输入昵称", trigger: "blur" },
    { min: 1, max: 50, message: "长度在1到50个字符" },
  ],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  status: [{ required: true, message: "请选择状态", trigger: "change" }],
  roleId: [{ required: true, message: "请选择角色", trigger: "change" }],
});

/** 查询 */
function handleQuery() {
  loading.value = true;
  getUserPage(queryParams)
    .then(({ data }) => {
      tableData.value = data.records;
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

/** 打开标签表单弹窗 */
function openDialog(user?: any) {
  dialog.visible = true;
  if (user) {
    dialog.title = "修改用户";
    getUserInfo(user.id).then((res) => {
      Object.assign(formData, res.data);
      dialog.type = "update";
    });
  } else {
    dialog.title = "新增用户";
    dialog.type = "add";
    Object.assign(formData, { status: 1 });
  }
}

/** 保存提交 */
function handleSubmit() {
  formRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const userId = formData.id;
      if (userId) {
        updateUser(formData)
          .then(() => {
            ElMessage.success("修改成功");
            closeDialog();
            resetQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        addUser(formData)
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

/** 关闭表单弹窗 */
function closeDialog() {
  dialog.visible = false;
  resetForm();
}

/** 重置表单 */
function resetForm() {
  formRef.value.resetFields();
  formRef.value.clearValidate();
  formData.id = undefined;
}

/** 删除标签 */
function handleDelete(id?: number) {
  const userIds = ref<any>([]);
  if (id) {
    userIds.value.push(id);
  }
  if (ids.value.length) {
    userIds.value = ids.value;
  }
  if (!userIds) {
    ElMessage.warning("请勾选删除项");
    return;
  }
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    deleteUsers(userIds.value)
      .then(() => {
        ElMessage.success("删除成功");
        resetQuery();
      })
      .finally(() => (loading.value = false));
  });
}

//上传之前的操作
function uploadBefore() {
  loading.value = true;
}

// 上传头像
function uploadSectionFile(param: any) {
  let file = param.file;
  files.value = file;
  // FormData 对象
  var form = new FormData();
  // 文件对象
  form.append("multipartFile", files.value);
  upload(form).then((res: any) => {
    formData.avatar = res.data;
    loading.value = false;
  });
}

/**
 * 获取字典数据
 */
function getDictList() {
  let dictTypes = ["sys_login_method"];
  getDataByDictType(dictTypes).then((response: any) => {
    let dictMap = response.data;
    dictLoginTypeList.value = dictMap.sys_login_method.list;
    loginTypeDefaultValue.value = dictMap.sys_login_method.defaultValue;
  });
}

//获取角色
function getRoleList() {
  getRolePage({ pageNo: 1, pageSize: 10 }).then((res) => {
    roleList.value = res.data.records;
  });
}

onMounted(() => {
  handleQuery();
  getDictList();
  getRoleList();
});
</script>

<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="username" label="用户名称">
          <el-input
            v-model="queryParams.username"
            placeholder="用户名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item label="登录方式" prop="loginType">
          <el-select
            style="width: 150px"
            v-model="queryParams.loginType"
            clearable
            reserve-keyword
            @change="handleQuery"
            placeholder="请选择登录方式"
          >
            <el-option
              v-for="item in dictLoginTypeList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
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
          v-hasPerm="['system:user:add']"
          ><i-ep-plus />新增</el-button
        >
        <el-button
          type="danger"
          :disabled="ids.length === 0"
          @click="handleDelete()"
          v-hasPerm="['system:user:delete']"
          ><i-ep-delete />批量删除</el-button
        >
      </template>

      <el-table
        ref="dataTableRef"
        :data="tableData"
        highlight-current-row
        v-loading="loading"
        stripe
        fit
        max-height="600px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="avatar" align="center" width="100" label="头像">
          <template #default="scope">
            <img :src="scope.row.avatar" width="60" height="60" />
          </template>
        </el-table-column>
        <el-table-column
          prop="nickname"
          width="200px"
          align="center"
          label="昵称"
        />
        <el-table-column
          prop="loginType"
          align="center"
          width="150px"
          label="登录方式"
        >
          <template #default="scope">
            <span v-for="item in dictLoginTypeList" :key="item">
              <el-tag
                v-if="scope.row.loginType === parseInt(item.value)"
                :type="item.style"
              >
                {{ item.label }}
              </el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="用户角色">
          <template #default="scope">
            <span v-for="item in roleList" :key="item">
              <el-tag v-if="scope.row.roleId === item.id">
                {{ item.name }}
              </el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="ipAddress"
          width="200px"
          align="center"
          label="登录IP"
        />
        <el-table-column
          prop="ipSource"
          width="200px"
          align="center"
          label="登录地址"
        />
        <el-table-column
          prop="createTime"
          align="center"
          width="180"
          label="创建时间"
        />
        <el-table-column
          align="center"
          width="180"
          label="最后登录时间"
          prop="lastLoginTime"
        />

        <!--        <el-table-column align="center" label="状态">
          <template #default="scope">
            <span>{{statusOptions[scope.row.status]}}</span>
          </template>
        </el-table-column>-->
        <el-table-column
          align="center"
          label="操作"
          width="200"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              @click="openDialog(scope.row)"
              icon="Edit"
              v-hasPerm="['system:user:update']"
              >编辑</el-button
            >
            <el-button
              size="small"
              type="danger"
              link
              @click="handleDelete(scope.row.id)"
              icon="Delete"
              v-hasPerm="['system:user:delete']"
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
    </el-card>

    <!-- 标签表单弹窗 -->
    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="800px"
      @close="closeDialog"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
        label-position="left"
      >
        <el-form-item prop="avatar" label="头像">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :action="uploadPictureHost"
            :before-upload="uploadBefore"
            :http-request="uploadSectionFile"
          >
            <img v-if="formData.avatar" :src="formData.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item prop="username" label="用户名">
          <el-input v-model="formData.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item v-if="dialog.type == 'add'" prop="password" label="密码">
          <el-input v-model="formData.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="nickname" label="昵称">
          <el-input v-model="formData.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="status" label="状态">
          <div>
            <el-radio
              v-for="(item, index) in statusOptions"
              v-model="formData.status"
              :label="item"
              :value="index"
              :key="index"
              border
            />
          </div>
        </el-form-item>
        <el-form-item prop="roleId" label="角色">
          <div>
            <el-radio
              v-for="(item, index) in roleList"
              v-model="formData.roleId"
              :label="item.name"
              :value="item.id"
              :key="index"
              border
            />
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
          <el-button @click="closeDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
}
</style>
