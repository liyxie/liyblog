<script setup lang="ts">
import { getTagPage, updateTag, addTag, deleteTag } from "@/api/tag";
import { TagForm, TagPageVO, TagQuery } from "@/api/tag/types";

defineOptions({
  name: "Tag",
  inheritAttrs: false,
});

const queryFormRef = ref(ElForm);
const tagFormRef = ref(ElForm);

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<TagQuery>({
  pageNo: 1,
  pageSize: 10,
});

const tagList = ref<TagPageVO[]>();

// Tag Cloud variables
const viewMode = ref('list');
const cloudSortMode = ref('random');
const allTagsList = ref<any[]>([]);

const fetchAllTags = () => {
  loading.value = true;
  getTagPage({ pageNo: 1, pageSize: 1000 })
    .then(({ data }: any) => {
      allTagsList.value = data.records.map((tag: any) => ({
        ...tag,
        hue: Math.floor(Math.random() * 360),
        sat: Math.floor(Math.random() * 40) + 50,
        delay: Math.random() * 2 + 's',
        duration: (Math.random() * 2 + 3) + 's'
      }));
    })
    .finally(() => {
      loading.value = false;
    });
};

const handleViewModeChange = (val: any) => {
  if (val === 'cloud' && allTagsList.value.length === 0) {
    fetchAllTags();
  }
};

const sortedCloudTags = computed(() => {
  const list = [...allTagsList.value];
  if (cloudSortMode.value === 'count') {
    list.sort((a, b) => (b.articleCount || 0) - (a.articleCount || 0));
  } else if (cloudSortMode.value === 'random') {
    list.sort(() => Math.random() - 0.5);
  }
  return list;
});

const handleCloudTagClick = (tag: any) => {
  openDialog(tag);
};

const dialog = reactive({
  title: "",
  visible: false,
});

const formData = reactive<TagForm>({
  id: "",
  name: "",
  sort: 1,
});

const rules = reactive({
  name: [{ required: true, message: "请输入标签名称", trigger: "blur" }],
  sort: [{ required: true, message: "请选择排序", trigger: "blur" }],
});

/** 查询 */
function handleQuery() {
  loading.value = true;
  getTagPage(queryParams)
    .then(({ data }: any) => {
      tagList.value = data.records;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });

  if (viewMode.value === 'cloud') {
    fetchAllTags();
  }
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

/** 打开表单弹窗 */
function openDialog(tag?: Object) {
  dialog.visible = true;
  if (tag) {
    dialog.title = "修改标签";
    Object.assign(formData, tag);
  } else {
    dialog.title = "新增标签";
  }
}

/** 保存提交 */
function handleSubmit() {
  tagFormRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const id = formData.id;
      if (id) {
        updateTag(formData)
          .then(() => {
            ElMessage.success("修改成功");
            closeDialog();
            resetQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        addTag(formData)
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
  tagFormRef.value.resetFields();
  tagFormRef.value.clearValidate();
  formData.id = "";
  formData.name = "";
  formData.sort = 1;
}

/** 删除 */
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
    deleteTag(formIds.value)
      .then(() => {
        ElMessage.success("删除成功");
        resetQuery();
      })
      .finally(() => (loading.value = false));
  });
}

const handleChange = (value: Number) => {
  formData.sort = value;
};

onMounted(() => {
  handleQuery();
});
</script>

<template>
  <div class="app-container">
    <div class="view-switcher" style="margin-bottom: 20px;">
      <el-radio-group v-model="viewMode" @change="handleViewModeChange" size="large">
        <el-radio-button label="list"><i-ep-list style="margin-right: 4px;" />列表视图</el-radio-button>
        <el-radio-button label="cloud"><i-ep-element-plus style="margin-right: 4px;" />标签云视图</el-radio-button>
      </el-radio-group>
    </div>

    <div class="search-container" v-show="viewMode === 'list'">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="name" label="标签名">
          <el-input
            v-model="queryParams.name"
            placeholder="标签名称"
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

    <el-card shadow="never" class="table-container" v-show="viewMode === 'list'">
      <template #header>
        <el-button
          type="success"
          @click="openDialog()"
          v-hasPerm="['system:tags:add']"
          ><i-ep-plus />新增</el-button
        >
        <el-button
          type="danger"
          :disabled="ids.length === 0"
          v-hasPerm="['system:tags:delete']"
          @click="handleDelete()"
          ><i-ep-delete />批量删除</el-button
        >
      </template>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="tagList"
        highlight-current-row
        stripe
        fit
        max-height="calc(100vh - 280px)"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="标签名" align="center" prop="name" />
        <el-table-column label="文章量" align="center" prop="articleCount" />
        <el-table-column label="排序" align="center" prop="sort" />
        <el-table-column label="创建时间" align="center" prop="createTime" />
        <el-table-column fixed="right" align="center" label="操作" width="220">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              v-hasPerm="['system:tags:update']"
              @click="openDialog(scope.row)"
            >
              <i-ep-edit />编辑
            </el-button>
            <el-button
              type="danger"
              link
              size="small"
              v-hasPerm="['system:tags:delete']"
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

    <!-- 标签云视图 -->
    <el-card shadow="never" class="cloud-container" v-show="viewMode === 'cloud'" v-loading="loading">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <el-button
            type="success"
            @click="openDialog()"
            v-hasPerm="['system:tags:add']"
            ><i-ep-plus />新增</el-button
          >
          <div class="cloud-controls">
            <span style="margin-right: 10px; font-size: 14px; color: #606266;">排序方式:</span>
            <el-radio-group v-model="cloudSortMode" size="small">
              <el-radio-button label="random">随机排序</el-radio-button>
              <el-radio-button label="count">文章量排行</el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </template>

      <div class="tag-cloud-wrapper">
        <el-tooltip
          v-for="tag in sortedCloudTags"
          :key="tag.id"
          effect="dark"
          :content="`文章数：${tag.articleCount || 0}`"
          placement="top"
        >
          <div 
            class="floating-tag" 
            :style="{ 
              '--tag-h': tag.hue,
              '--tag-s': tag.sat + '%',
              animationDelay: tag.delay,
              animationDuration: tag.duration
            }"
            @click="handleCloudTagClick(tag)"
          >
            {{ tag.name }}
          </div>
        </el-tooltip>
      </div>
    </el-card>

    <!-- 标签表单弹窗 -->
    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="500px"
      @close="closeDialog"
    >
      <el-form
        ref="tagFormRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="标签名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入标签名称" />
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
  </div>
</template>

<style lang="scss" scoped>
.view-switcher {
  display: flex;
  justify-content: flex-start;
}

.tag-cloud-wrapper {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  padding: 20px;
  min-height: 400px;
  justify-content: center;
  align-items: center;
}

.floating-tag {
  background-color: hsl(var(--tag-h), var(--tag-s), 65%);
  padding: 10px 20px;
  border-radius: 8px;
  color: #fff;
  font-weight: bold;
  font-size: 14px;
  cursor: pointer;
  user-select: none;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  animation: float ease-in-out infinite;

  &:hover {
    transform: scale(1.15) translateY(-8px) rotate(-2deg);
    box-shadow: 0 10px 20px rgba(0,0,0,0.2);
    filter: brightness(1.05);
    z-index: 10;
    // pause animation on hover
    animation-play-state: paused;
  }
}

@keyframes float {
  0% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-15px);
  }
  100% {
    transform: translateY(0px);
  }
}
</style>

<style lang="scss">
html.dark .floating-tag {
  background-color: hsl(var(--tag-h), var(--tag-s), 30%) !important;
  color: #e5eaf3 !important;
  box-shadow: 0 4px 6px rgba(0,0,0,0.4) !important;

  &:hover {
    box-shadow: 0 10px 20px rgba(0,0,0,0.6) !important;
    filter: brightness(1.25) !important;
  }
}
</style>
