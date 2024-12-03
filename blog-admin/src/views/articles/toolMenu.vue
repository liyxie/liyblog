<template>
  <div class="app-container">
    <el-card shadow="never" class="table-container">
      <template #header>
        <el-button type="success" @click="openDialog(0)" v-hasPerm="['system:tool:add']">
          <template #icon><i-ep-plus /></template>
          新增</el-button>
      </template>

      <el-table v-loading="loading" :data="menuList" highlight-current-row row-key="id" :expand-row-keys="['1']"
        @row-click="onRowClick" :tree-props="{
          children: 'childToolList',
          hasChildren: 'hasChildren',
        }">
        <el-table-column label="菜单名称" max-width="120">
          <template #default="scope">
            <svg-icon :icon-class="scope.row.icon" />
            {{ scope.row.name }}
          </template>
        </el-table-column>

        <el-table-column label="类型" align="center" width="80">
          <template #default="scope">
            <el-tag v-if="scope.row.type === 2" type="warning">分类</el-tag>
            <el-tag v-if="scope.row.type === 1" type="success">工具链接</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="url" align="left" width="250" prop="url" />

        <el-table-column label="描述" align="left" width="250" prop="describe" />

        <el-table-column label="状态" align="center" width="80">
          <template #default="scope">
            <el-tag v-if="scope.row.state === 1" type="success">显示</el-tag>
            <el-tag v-else type="info">隐藏</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="排序" align="center" width="80" prop="sort" />

        <el-table-column fixed="right" align="center" label="操作" width="220">
          <template #default="scope">
            <el-button v-if="scope.row.type == 2" type="success" link size="small" v-hasPerm="['system:tool:add']"
              @click.stop="openDialog(scope.row.id)">
              <i-ep-plus />新增
            </el-button>

            <el-button type="primary" link size="small" @click.stop="openDialog(undefined, scope.row)"
              v-hasPerm="['system:tool:update']">
              <i-ep-edit />编辑
            </el-button>

            <el-button type="danger" link size="small" @click.stop="handleDelete(scope.row.id)"
              v-hasPerm="['system:tool:delete']"><i-ep-delete />
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialog.visible" :title="dialog.title" destroy-on-close append-to-body width="1000px"
      @close="closeDialog" top="5vh">
      <el-form ref="menuFormRef" :model="formData" :rules="rules" label-width="160px" :validate-on-rule-change="false">
        <!-- 上级菜单 -->.
        <el-form-item label="父级菜单" prop="parentId">
          <el-tree-select v-model="formData.parentId" placeholder="选择上级菜单" :data="menuOptions" filterable check-strictly
            :render-after-expand="false" />
        </el-form-item>

        <el-form-item label="名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入名称" />
        </el-form-item>

        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="formData.type" @change="onMenuTypeChange">
            <el-radio :value="2">分类</el-radio>
            <el-radio :value="1">工具链接</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 路径 -->
        <el-form-item v-if="formData.type == 1" label="链接url" prop="url">
          <el-input v-model="formData.url" style="width: 95%" />
        </el-form-item>

        <!-- 状态 -->
        <el-form-item prop="state" label="显示状态">
          <el-radio-group v-model="formData.state">
            <el-radio :value="1">显示</el-radio>
            <el-radio :value="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 排序 -->
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" style="width: 100px" controls-position="right" :min="0" />
        </el-form-item>

        <!-- 描述 -->
        <el-form-item v-if="formData.type == '1'" label="链接描述" prop="describe">
          <el-input v-model="formData.describe" />
        </el-form-item>

        <!-- 图标 -->
        <el-form-item label="图标" prop="icon">
          <icon-select v-model="formData.icon" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="closeDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "ToolMenu",
  inheritAttrs: false,
});

import {
  getToolPage,
  getToolInfo,
  deleteTool,
  addTool,
  updateTool,
  getToolOptions,
} from "@/api/tool";

import { MenuTypeEnum } from "@/enums/MenuTypeEnum";

const menuFormRef = ref(ElForm);

const loading = ref(false);
const dialog = reactive({
  title: "",
  visible: false,
});

const menuList = ref<any[]>([]);

const menuOptions = ref<any[]>([]);

const formData = reactive<any>({
  parentId: 0,
  hidden: 0,
  sort: 1,
  type: 1,
  name: "",
  url: "",
});

// 新增规则
const rules = reactive({
  parentId: [{ required: true, message: "请选择顶级菜单", trigger: "blur" }],
  name: [{ required: true, message: "请输入名称", trigger: "blur" }],
  type: [{ required: true, message: "请选择类型", trigger: "blur" }],
  url: [{ required: false, message: "", trigger: "blur" }],
  hidden: [{ required: true, message: "请输入选择显示状态", trigger: "blur" }],
});

// 选择表格的行菜单ID
const selectedRowMenuId = ref<number | undefined>();

/**
 * 查询
 */
function handleQuery() {
  // 重置父组件
  loading.value = true;
  getToolPage()
    .then(({ data }) => {
      menuList.value = data;
    })
    .then(() => {
      loading.value = false;
    });
}

/**行点击事件 */
function onRowClick(row: any) {
  selectedRowMenuId.value = row.id;
}

/**
 * 打开表单弹窗
 *
 * @param parentId 父菜单ID
 * @param menu 菜单
 */
function openDialog(parentId?: number, menu?: Object) {
  getToolOptions()
    .then(({ data }) => {
      console.log(data);
      menuOptions.value = [{ value: 0, label: "顶级菜单", children: data }];
    })
    .then(() => {
      dialog.visible = true;
      if (menu) {
        dialog.title = "编辑菜单";
        // getMenuForm(menuId).then(({ data }) => {
        //   Object.assign(formData, data);
        //   menuCacheData.type = data.type;
        //   menuCacheData.path = data.path ?? "";
        // });
        Object.assign(formData, menu);
      } else {
        dialog.title = "新增菜单";
        formData.parentId = parentId;
      }
    });
}

/** 菜单类型切换事件处理 */
function onMenuTypeChange() {
  if (formData.type === 1) {
    rules.url = [{ required: true, message: "请输入链接", trigger: "blur" }];
  } else {
    rules.url = [{ required: false, message: "", trigger: "blur" }];
  }
}

/** 菜单保存提交 */
function submitForm() {
  menuFormRef.value.validate((isValid: boolean) => {
    if (isValid) {
      const menuId = formData.id;
      formData.path =
        formData.type === MenuTypeEnum.CATALOG
          ? "/" + formData.path
          : formData.path;
      if (menuId) {
        updateTool(formData).then(() => {
          ElMessage.success("修改成功");
          closeDialog();
          handleQuery();
        });
      } else {
        addTool(formData).then(() => {
          ElMessage.success("新增成功");
          closeDialog();
          handleQuery();
        });
      }
    }
  });
}

/** 删除菜单 */
function handleDelete(menuId: number) {
  if (!menuId) {
    ElMessage.warning("请勾选删除项");
    return false;
  }

  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      deleteTool(menuId).then(() => {
        ElMessage.success("删除成功");
        handleQuery();
      });
    })
    .catch(() => ElMessage.info("已取消删除"));
}

/** 关闭弹窗 */
function closeDialog() {
  dialog.visible = false;
  resetForm();
}

/** 重置表单 */
function resetForm() {
  menuFormRef.value.resetFields();
  menuFormRef.value.clearValidate();

  formData.id = undefined;
  formData.parentId = 0;
  formData.visible = 1;
  formData.sort = 1;
  formData.url = undefined;
  formData.name = undefined;
  formData.describe = undefined;
  formData.state = undefined;
  formData.icon = undefined;
  formData.keepAlive = undefined;
}

onMounted(() => {
  handleQuery();
});
</script>
