<!--字典类型-->
<script setup lang="ts">
import {
  getDictTypePage,
  getDictTypeForm,
  addDictType,
  updateDictType,
  deleteDictTypes,
} from "@/api/dict";

const dictItme = defineAsyncComponent(() => import("./dict-item.vue"));

defineOptions({
  name: "DictType",
  inheritAttrs: false,
});

const queryFormRef = ref(ElForm);
const dataFormRef = ref(ElForm);

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<any>({
  pageNum: 1,
  pageSize: 10,
});

const dictTypeList = ref<any[]>();

const dialog = reactive({
  title: "",
  visible: false,
});

const formData = reactive<any>({
  status: 1,
});

const rules = reactive({
  name: [{ required: true, message: "请输入字典类型名称", trigger: "blur" }],
  type: [{ required: true, message: "请输入字典类型编码", trigger: "blur" }],
});

/** 查询 */
function handleQuery() {
  loading.value = true;
  getDictTypePage(queryParams)
    .then(({ data }) => {
      dictTypeList.value = data.records;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

/**
 * 重置查询
 */
function resetQuery() {
  queryFormRef.value.resetFields();
  queryParams.pageNum = 1;
  handleQuery();
}

/** 行复选框选中  */
function handleSelectionChange(selection: any) {
  ids.value = selection.map((item: any) => item.id);
}

/**
 * 打开字典类型表单弹窗
 *
 * @param dicTypeId 字典类型ID
 */
function openDialog(dicType?: number) {
  dialog.visible = true;
  if (dicType) {
    dialog.title = "修改字典类型";
    Object.assign(formData, dicType);
  } else {
    dialog.title = "新增字典类型";
  }
}

/** 字典类型表单提交 */
function handleSubmit() {
  dataFormRef.value.validate((isValid: boolean) => {
    if (isValid) {
      loading.value = false;
      const dictTypeId = formData.id;
      if (dictTypeId) {
        updateDictType(formData)
          .then(() => {
            ElMessage.success("修改成功");
            closeDialog();
            handleQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        addDictType(formData)
          .then(() => {
            ElMessage.success("新增成功");
            closeDialog();
            handleQuery();
          })
          .finally(() => (loading.value = false));
      }
    }
  });
}

/** 关闭字典类型弹窗 */
function closeDialog() {
  dialog.visible = false;
  resetForm();
}

/**  重置字典类型表单 */
function resetForm() {
  dataFormRef.value.resetFields();
  dataFormRef.value.clearValidate();

  formData.id = undefined;
  formData.status = 1;
}

/** 删除字典类型 */
function handleDelete(id?: number) {
  const dictypeIds = ref<any>([]);
  if (id) {
    dictypeIds.value.push(id);
  }
  if (ids.value.length) {
    dictypeIds.value = ids.value;
  }
  if (!dictypeIds) {
    ElMessage.warning("请勾选删除项");
    return;
  }

  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    deleteDictTypes(dictypeIds.value).then(() => {
      ElMessage.success("删除成功");
      resetQuery();
    });
  });
}

const dictDataDialog = reactive({
  title: "",
  visible: false,
});

const selectedDictType = reactive({ dictId: null, typeName: "" }); // 当前选中的字典类型

/** 打开字典数据弹窗 */
function openDictDialog(row: any) {
  dictDataDialog.visible = true;
  dictDataDialog.title = "【" + row.name + "】字典数据";

  selectedDictType.dictId = row.id;
  selectedDictType.typeName = row.name;
}

/**  关闭字典数据弹窗 */
function closeDictDialog() {
  dictDataDialog.visible = false;
}

onMounted(() => {
  handleQuery();
});
</script>

<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item label="关键字" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="字典类型名称/编码"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="queryParams.status"
            placeholder="请选择状态"
            style="width: 150px"
            clearable
          >
            <el-option label="正常" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery()"
            ><i-ep-search />搜索</el-button
          >
          <el-button @click="resetQuery()"><i-ep-refresh />重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never" class="table-container">
      <template #header>
        <el-button
          type="success"
          @click="openDialog()"
          v-hasPerm="['system:dict:add']"
          ><i-ep-plus />新增</el-button
        >
        <el-button
          type="danger"
          :disabled="ids.length === 0"
          @click="handleDelete()"
          v-hasPerm="['system:dict:delete']"
          ><i-ep-delete />批量删除</el-button
        >
      </template>
      <el-table
        v-loading="loading"
        highlight-current-row
        :data="dictTypeList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          label="字典名称"
          align="center"
          prop="name"
          width="200"
        />
        <el-table-column
          label="字典类型"
          align="center"
          prop="type"
          width="200"
        >
          <template #default="scope">
            <el-tag type="info">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 1" type="success">正常</el-tag>
            <el-tag v-else type="danger">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="remark" align="center" />
        <el-table-column fixed="right" label="操作" align="center" width="240">
          <template #default="scope">
            <el-button
              type="success"
              link
              size="small"
              @click.stop="openDictDialog(scope.row)"
              ><i-ep-Collection />字典数据</el-button
            >
            <el-button
              type="primary"
              link
              size="small"
              v-hasPerm="['system:dict:update']"
              @click.stop="openDialog(scope.row)"
              ><i-ep-edit />编辑</el-button
            >
            <el-button
              type="danger"
              link
              size="small"
              @click.stop="handleDelete(scope.row.id)"
              v-hasPerm="['system:dict:delete']"
              ><i-ep-delete />删除</el-button
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

    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="500px"
      @close="closeDialog"
    >
      <el-form
        ref="dataFormRef"
        :model="formData"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="字典名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典编码" prop="type">
          <el-input v-model="formData.type" placeholder="请输入字典编码" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1" label="正常" />
            <el-radio :value="0" label="停用" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            placeholder="字典类型备注"
            :autosize="{ minRows: 2, maxRows: 4 }"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
          <el-button @click="closeDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!--字典数据弹窗-->
    <el-dialog
      v-model="dictDataDialog.visible"
      :title="dictDataDialog.title"
      width="1200px"
      @close="closeDictDialog"
    >
      <dictItme
        v-model:dictId="selectedDictType.dictId"
        v-model:typeName="selectedDictType.typeName"
      />
    </el-dialog>
  </div>
</template>
