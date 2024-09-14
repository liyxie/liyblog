<script setup lang="ts">
import {
  getCategoryPage,
  updateCategory,
  addCategory,
  deleteCategory,
} from "@/api/category";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import {
  CategoryForm,
  CategoryPageVO,
  CategoryQuery,
} from "@/api/category/types";
import { number } from "echarts";

defineOptions({
  name: "category",
  inheritAttrs: false,
});

const icons = ref(ElementPlusIconsVue);
const queryFormRef = ref(ElForm);
const categoryFormRef = ref(ElForm);

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<CategoryQuery>({
  pageNo: 1,
  pageSize: 10,
});

const categoryList = ref<CategoryPageVO[]>();

const dialog = reactive({
  title: "",
  visible: false,
});
const iconDialog = ref(false);

const formData = reactive<CategoryForm>({
  id: "",
  name: "",
  icon: "",
  sort: 1,
});

const rules = reactive({
  name: [{ required: true, message: "请输入分类名称", trigger: "blur" }],
  icon: [{ required: true, message: "请选择图标", trigger: "blur" }],
  sort: [{ required: true, message: "请选择排序", trigger: "blur" }],
});

/** 查询 */
function handleQuery() {
  loading.value = true;
  getCategoryPage(queryParams)
    .then(({ data }: any) => {
      categoryList.value = data.records;
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

/** 打开分类表单弹窗 */
function openDialog(category?: Object) {
  dialog.visible = true;
  if (category) {
    dialog.title = "修改分类";
    Object.assign(formData, category);
  } else {
    dialog.title = "新增分类";
  }
}

/** 分类保存提交 */
function handleSubmit() {
  categoryFormRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const categoryId = formData.id;
      if (categoryId) {
        updateCategory(formData)
          .then(() => {
            ElMessage.success("修改成功");
            closeDialog();
            resetQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        addCategory(formData)
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
  categoryFormRef.value.resetFields();
  categoryFormRef.value.clearValidate();
  formData.id = "";
  formData.name = "";
  formData.sort = 1;
  formData.icon = "";
}

/** 删除分类 */
function handleDelete(categoryId?: number) {
  const categoryIds = ref<any>([]);
  if (categoryId) {
    categoryIds.value.push(categoryId);
  }
  if (ids.value.length) {
    categoryIds.value = ids.value;
  }
  if (!categoryIds.value) {
    ElMessage.warning("请勾选删除项");
    return;
  }
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    deleteCategory(categoryIds.value)
      .then(() => {
        ElMessage.success("删除成功");
        resetQuery();
      })
      .finally(() => (loading.value = false));
  });
}

const handleChange = (value: any) => {
  formData.sort = value;
};

//选择图标
function handleSelectIcon(icon: any) {
  iconDialog.value = false;
  console.log();
  formData.icon = icon.name;
}

onMounted(() => {
  handleQuery();
});
</script>

<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="name" label="分类名">
          <el-input
            v-model="queryParams.name"
            placeholder="分类名称"
            clearable
            @keyup.enter="handleQuery"
          />
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
          v-hasPerm="['system:category:add']"
          ><i-ep-plus />新增</el-button
        >
        <el-button
          type="danger"
          :disabled="ids.length === 0"
          v-hasPerm="['system:category:delete']"
          @click="handleDelete()"
          ><i-ep-delete />批量删除</el-button
        >
      </template>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="categoryList"
        highlight-current-row
        stripe
        fit
        max-height="600px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="图标" align="center">
          <template #default="scope">
            <el-icon :size="20">
              <component :is="scope.row.icon" />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column label="分类名" align="center" prop="name" />
        <el-table-column label="文章量" align="center" prop="articleCount" />
        <el-table-column label="排序" align="center" prop="sort" />
        <el-table-column label="创建时间" align="center" prop="createTime" />
        <el-table-column fixed="right" align="center" label="操作" width="220">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              v-hasPerm="['system:category:update']"
              @click="openDialog(scope.row)"
            >
              <i-ep-edit />编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              link
              v-hasPerm="['system:category:delete']"
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

    <!-- 分类表单弹窗 -->
    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="500px"
      @close="closeDialog"
    >
      <el-form
        ref="categoryFormRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="分类图标" prop="icon">
          <el-input
            v-model="formData.icon"
            style="max-width: 600px"
            placeholder="请选择图标名称"
          >
            <template #append>
              <el-button @click="iconDialog = true" icon="Setting"
                >选择</el-button
              >
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="分类名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入分类名称" />
        </el-form-item>

        <el-form-item label="排序" prop="sort">
          <el-input-number
            v-model="formData.sort"
            :min="1"
            :max="10"
            @change="handleChange"
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

    <!-- 图标按钮弹窗 -->
    <el-dialog v-model="iconDialog" title="选择图标" width="50%">
      <el-scrollbar height="400px">
        <div class="grid">
          <div v-for="(icon, name) of icons" :key="name">
            <div class="icon-item" @click="handleSelectIcon(icon)">
              <el-icon :size="20">
                <component :is="icon" />
              </el-icon>
              <span>{{ name }}</span>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.grid {
  position: relative;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
}

.icon-item {
  float: left;
  width: 100px;
  height: 85px;
  margin: 20px;
  font-size: 30px;
  color: var(--el-text-color-regular);
  text-align: center;
  cursor: pointer;
  &:hover {
    background-color: #eee;
  }
}
span {
  display: block;
  margin-top: 10px;
  font-size: 16px;
}
</style>
