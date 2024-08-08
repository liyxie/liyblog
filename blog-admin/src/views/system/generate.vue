<script setup lang="ts">
import { getTablePage, preview, download } from "@/api/generate";

defineOptions({
  name: "Generate",
  inheritAttrs: false,
});

const { copy } = useClipboard();

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<any>({
  pageNo: 1,
  pageSize: 10,
});

const tabelData = ref<any[]>();

const dialog = reactive({
  title: "",
  visible: false,
  dVisible: false,
  data: {},
  activeName: "domain.java",
});

const formData = reactive<any>({});

/** 查询 */
function handleQuery() {
  loading.value = true;
  getTablePage(queryParams)
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

/** 复制代码 */
function handleClipboard(text: any) {
  copy(text)
    .then(() => {
      ElMessage.success("复制成功");
    })
    .catch(() => {
      ElMessage.warning("复制失败");
    });
}

//预览
function openDialog(name: any, type: any) {
  if (type == "download") {
    formData.downloadPath = "";
    formData.tableName = name;
    dialog.title = "生成代码";
    dialog.dVisible = true;
    return;
  }
  preview(name)
    .then((response) => {
      dialog.data = response.data;
      dialog.activeName = "domain.java";
      dialog.title = "代码预览";
      dialog.visible = true;
    })
    .catch((err) => {
      console.log(err);
    });
}

/**
 * 生成代码
 */
function handleDownload() {
  download(formData)
    .then((response) => {
      dialog.dVisible = false;
      ElMessage.success("下载成功！");
    })
    .catch((err) => {
      console.log(err);
    });
}

onMounted(() => {
  handleQuery();
});
</script>

<template>
  <div class="app-container">
    <el-card shadow="never" class="table-container">
      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="tabelData"
        highlight-current-row
        stripe
        fit
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection"></el-table-column>
        <el-table-column label="表名称" align="center">
          <template #default="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="表描述" align="center">
          <template #default="scope">
            <span>{{ scope.row.comment }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center">
          <template #default="scope">
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="修改时间" align="center">
          <template #default="scope">
            <span>{{ scope.row.updateTime }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button
              @click="openDialog(scope.row.name, 'preview')"
              type="primary"
              link
              icon="View"
              v-hasPerm="['system:generate:preview']"
            >
              预览
            </el-button>
            <el-button
              @click="openDialog(scope.row.name, 'download')"
              type="success"
              link
              icon="Download"
              v-hasPerm="['system:generate:download']"
            >
              生成
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
    <!-- 预览弹窗 -->
    <el-dialog
      center
      :title="dialog.title"
      v-model="dialog.visible"
      top="5vh"
      width="80%"
    >
      <el-tabs v-model="dialog.activeName">
        <el-tab-pane
          v-for="(value, key) in dialog.data"
          :label="key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))"
          :name="key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))"
          :key="value"
        >
          <el-scrollbar height="700px">
            <highlightjs language="Java" :autodetect="false" :code="value">
            </highlightjs>
            <el-link
              :underline="false"
              icon="DocumentCopy"
              type="success"
              @click="handleClipboard(value)"
              class="copy"
              >&nbsp;复制</el-link
            >
          </el-scrollbar>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <el-dialog :title="dialog.title" v-model="dialog.dVisible" width="30%">
      <el-form ref="form" :model="formData" label-width="80px">
        <el-form-item label="生成地址">
          <el-input v-model="formData.downloadPath"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleDownload">确 定</el-button>
        </el-form-item>
      </el-form>
      <el-alert
        title="地址示例：C:\blog"
        type="success"
        effect="dark"
        :closable="false"
      >
      </el-alert>
    </el-dialog>
  </div>
</template>

<style scoped>
.copy {
  position: absolute;
  top: 20px;
  right: 10px;
}
</style>
