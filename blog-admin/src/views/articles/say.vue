<script setup lang="ts">
import { getSayPage, deleteSay, addSay, updateSay } from "@/api/say";
import { upload } from "@/api/file";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { SayForm, SayPageVO } from "@/api/say/types";

defineOptions({
  name: "say",
  inheritAttrs: false,
});

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<any>({
  pageNo: 1,
  pageSize: 10,
});

const dialog = reactive({
  title: "",
  visible: false,
});

const formRef = ref(ElForm);
const formData = reactive<SayForm>({
  content: "",
  address: "",
  isPublic: 1,
  id: "",
});

const rules = reactive({
  isPublic: [{ required: true, message: "请选择公开类型", trigger: "blur" }],
  content: [{ required: true, message: "请输入内容", trigger: "blur" }],
});

const tabelData = ref<SayPageVO[]>();

const files = ref();
const mode = ref("default");
const editorRef = shallowRef();
const toolbarConfig = ref({});
const editorConfig = ref({
  placeholder: "请输入内容...",
  MENU_CONF: {
    // 配置上传图片
    uploadImage: {
      customUpload: contentUpload,
    },
    uploadVideo: {
      customUpload: uploadVideo,
    },
    codeSelectLang: {
      // 代码语言
      codeLangs: [
        { text: "CSS", value: "css" },
        { text: "HTML", value: "html" },
        { text: "XML", value: "xml" },
        { text: "Java", value: "java" },
        // 其他
      ],
    },
  },
});

//编辑器上传图片
function contentUpload(file: any, insertFn: any) {
  files.value = file;
  // FormData 对象
  var formData = new FormData();
  // 文件对象
  formData.append("multipartFile", files.value);
  upload(formData).then((res: any) => {
    insertFn(res.data, "", res.data);
    loading.value = false;
  });
}

//编辑器上传视频
function uploadVideo(file: any, insertFn: any) {
  files.value = file;
  //FormData 对象
  var formData = new FormData();
  // 文件对象
  formData.append("multipartFile", files.value);
  upload(formData).then((res: any) => {
    insertFn(res.data, "");
    loading.value = false;
  });
}

//创建富文本编辑框
function onCreated(editor: any) {
  editorRef.value = editor; // 一定要用 Object.seal() ，否则会报错
}

/** 查询 */
function handleQuery() {
  loading.value = true;
  getSayPage(queryParams)
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
  queryParams.pageNo = 1;
  handleQuery();
}

/** 行checkbox 选中事件 */
function handleSelectionChange(selection: any) {
  ids.value = selection.map((item: any) => item.id);
}

/** 打开表单弹窗 */
function openDialog(obj?: Object) {
  dialog.visible = true;
  if (obj) {
    dialog.title = "修改说说";
    Object.assign(formData, obj);
  } else {
    dialog.title = "新增说说";
  }
}

/** 保存提交 */
function handleSubmit() {
  formRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const roleId = formData.id;
      if (roleId) {
        updateSay(formData)
          .then(() => {
            ElMessage.success("修改成功");
            closeDialog();
            resetQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        addSay(formData)
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
  formData.id = "";
  formData.address = "";
  formData.content = "";
}

/** 删除 */
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
    deleteSay(formIds.value)
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
          type="success"
          @click="openDialog()"
          v-hasPerm="['system:say:add']"
          ><i-ep-plus />新增</el-button
        >

        <el-button
          type="danger"
          :disabled="ids.length === 0"
          @click="handleDelete"
          v-hasPerm="['system:say:delete']"
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
        <!-- <el-table-column
          prop="userId"
          align="center"
          width="180"
          label="用户id"
        /> -->
        <el-table-column
          prop="address"
          align="center"
          width="200"
          label="关联地址"
        />
        <el-table-column
          prop="content"
          align="center"
          width="600"
          label="内容"
        />
        <el-table-column
          prop="isPublic"
          align="center"
          width="180"
          label="是否公开"
        >
          <template #default="scope">
            <el-tag type="success" v-if="scope.row.isPublic === 1">公开</el-tag>
            <el-tag v-else type="info">隐藏</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" align="center" label="创建时间" />
        <el-table-column
          align="center"
          label="操作"
          width="180"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              v-if="!scope.row.status"
              type="primary"
              link
              icon="Edit"
              size="small"
              v-hasPerm="['system:say:update']"
              @click="openDialog(scope.row)"
              >修改</el-button
            >
            <el-button
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              icon="Delete"
              size="small"
              v-hasPerm="['system:say:delete']"
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

      <!-- 添加或修改 -->
      <el-dialog
        :title="dialog.title"
        v-model="dialog.visible"
        :close-on-click-modal="false"
        @close="closeDialog"
      >
        <el-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-width="100px"
        >
          <el-form-item label="关联地址" prop="address">
            <el-input v-model="formData.address"></el-input>
          </el-form-item>
          <el-form-item label="公开类型" prop="isPublic">
            <el-radio v-model="formData.isPublic" :label="0">隐藏</el-radio>
            <el-radio v-model="formData.isPublic" :label="1">公开</el-radio>
          </el-form-item>

          <el-form-item label="内容" prop="content">
            <div style="border: 1px solid #ccc">
              <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :defaultConfig="toolbarConfig"
                :mode="mode"
              />
              <Editor
                style="height: 500px; overflow-y: hidden"
                v-model="formData.content"
                :defaultConfig="editorConfig"
                :mode="mode"
                @onCreated="onCreated"
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
    </el-card>
  </div>
</template>

<style src="@wangeditor/editor/dist/css/style.css"></style>
