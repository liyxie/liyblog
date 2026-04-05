<script setup lang="ts">
import {
  addArticle,
  updateArticle,
  getArticleInfo,
} from "@/api/article";
import { upload } from "@/api/file";

defineOptions({
  name: "ArticleForm",
  inheritAttrs: false,
});

const props = defineProps<{
  modelValue: boolean;
  articleId?: number;
  categoryList: any[];
  tagList: any[];
  publishList: any[];
  yesOrNoList: any[];
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "success"): void;
}>();

const formRef = ref(ElForm);
const loading = ref(false);
const mdRef = ref();

const readTypeList = ref(["无需验证", "评论阅读", "点赞阅读", "扫码阅读"]);
const isOriginalList = ref(["转载", "原创"]);

const visible = ref(false);
const categoryName = ref();
const tagName = ref();
const dialogVisible = ref(false);
const videoInput = ref("");
const files = ref();
const uploadPictureHost = ref(
  import.meta.env.VITE_APP_API_URL + "/file/upload"
);

const dialogTitle = computed(() => (props.articleId ? "修改文章" : "新增文章"));

const formData = reactive<any>({
  name: "",
  tags: [],
  readType: 0,
  isStick: 0,
  isOriginal: 1,
  isRecommend: 0,
  isCarousel: 0,
  isPublish: 3,
});

const rules = reactive({
  title: [{ required: true, message: "必填字段", trigger: "blur" }],
  summary: [{ required: true, message: "必填字段", trigger: "blur" }],
  tags: [{ required: true, message: "必填字段", trigger: "blur" }],
  categoryName: [{ required: true, message: "必填字段", trigger: "blur" }],
  readType: [{ required: true, message: "必填字段", trigger: "change" }],
  isStick: [{ required: true, message: "必填字段", trigger: "change" }],
  isPublish: [{ required: true, message: "必填字段", trigger: "change" }],
  isOriginal: [{ required: true, message: "必填字段", trigger: "change" }],
  originalUrl: [{ required: true, message: "必填字段", trigger: "blur" }],
  contentMd: [{ required: true, message: "必填字段", trigger: "change" }],
  isRecommend: [{ required: true, message: "必填字段", trigger: "change" }],
  isCarousel: [{ required: true, message: "必填字段", trigger: "change" }],
});

watch(
  () => props.modelValue,
  (val) => {
    if (val && props.articleId) {
      getArticleInfo(props.articleId).then((res) => {
        Object.assign(formData, res.data);
      });
    }
  }
);

/** 表单保存提交 */
function handleSubmit() {
  formRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const id = formData.id;
      formData.content = mdRef.value.d_render;
      if (id) {
        updateArticle(formData)
          .then(() => {
            ElMessage.success("修改成功");
            closeDialog();
            emit("success");
          })
          .finally(() => (loading.value = false));
      } else {
        addArticle(formData)
          .then(() => {
            ElMessage.success("新增成功");
            closeDialog();
            emit("success");
          })
          .finally(() => (loading.value = false));
      }
    }
  });
}

/** 关闭弹窗 */
function closeDialog() {
  emit("update:modelValue", false);
  resetForm();
}

/** 重置表单 */
function resetForm() {
  formRef.value?.resetFields();
  formRef.value?.clearValidate();
  formData.id = undefined;
  formData.avatar = undefined;
  formData.createTime = undefined;
  formData.keywords = undefined;
}

//上传之前的操作
function uploadBefore() {
  loading.value = true;
}

//上传文件
function uploadSectionFile(param: any) {
  files.value = param.file;
  var form = new FormData();
  form.append("multipartFile", files.value);
  upload(form)
    .then((res) => {
      formData.avatar = res.data.url;
    })
    .finally(() => {
      loading.value = false;
    });
}

//上传视频
function uploadVideo(param: any) {
  files.value = param.file;
  // FormData 对象
  var formdata = new FormData();
  // 文件对象
  formdata.append("multipartFile", files.value);
  upload(formdata)
    .then((res) => {
      const $vm = mdRef.value;
      // 将文件名与文件路径插入当前光标位置，这是mavon-editor 内置的方法
      $vm.insertText($vm.getTextareaDom(), {
        prefix: `<video height=100% width=100% controls autoplay src="${res.data.url}"></video>`,
        subfix: "",
        str: "",
      });
    })
    .finally(() => {
      loading.value = false;
    });
}
/**
 * 添加网络视频地址
 */
function addVideo() {
  // 这里获取到的是mavon编辑器实例，上面挂载着很多方法
  const $vm = mdRef.value;
  // 将文件名与文件路径插入当前光标位置，这是mavon-editor 内置的方法
  $vm.insertText($vm.getTextareaDom(), {
    prefix: `<video height=100% width=100% src="${videoInput.value}"></video>`,
    subfix: "",
    str: "",
  });

  dialogVisible.value = false;
  videoInput.value = "";
}

//删除图片
function imgDel(filename: any) {
  // delBatchFile(filename[0].split(img)[1]);
}

function randomImg() {
  formData.avatar =
    "https://picsum.photos/500/300?random=" + Math.round(Date.now() / 1000);
}

//添加图片
function imgAdd(pos: any, $file: any) {
  var formdata = new FormData();
  formdata.append("multipartFile", $file);
  upload(formdata).then((res) => {
    mdRef.value.$img2Url(pos, res.data.url);
  });
}

//移除分类
function removeCategory() {
  formData.categoryName = null;
}

function addCategory(item: any) {
  formData.categoryName = item.name;
}
//保存分类
function saveCategory() {
  if (categoryName.value.trim() !== "") {
    formData.categoryName = categoryName;
  }
}

/**
 * 添加标签
 */
function addTag(item: any) {
  if (formData.tags.length > 2) {
    ElMessage.error("最多添加三个标签,如需继续添加,请先删除一个!");
    return;
  }
  if (formData.tags.indexOf(item.name) === -1) {
    formData.tags.push(item.name);
  }
}

/**
 * 保存标签
 */
function saveTag() {
  if (tagName.value.trim() !== "") {
    addTag({
      name: tagName.value,
    });
    tagName.value = "";
  }
}

/**
 * 删除标签
 */
function removeTag(item: any) {
  const index = formData.tags.indexOf(item);
  formData.tags.splice(index, 1);
}
</script>

<template>
  <!--添加or修改区域-->
  <el-dialog center :title="dialogTitle" @close="closeDialog" :model-value="modelValue" fullscreen>
    <el-form :rules="rules" ref="formRef" :model="formData" style="margin-top: 10px">
      <el-row>
        <el-col :span="10">
          <el-form-item label="文章名称" prop="title">
            <el-input v-model="formData.title" auto-complete="off" />
          </el-form-item>
          <el-form-item label="文章简介" prop="summary">
            <el-input auto-complete="off" type="textarea" v-model="formData.summary" />
          </el-form-item>
        </el-col>
        <el-col :span="1" />
        <el-col :span="12">
          <el-form-item>
            <template #label>
              <div style="display: flex; align-items: center">
                <span>标题图:</span>
                <el-popover placement="top" width="160" trigger="hover" v-model="visible">
                  <p>随机获取一张图片</p>
                  <div style="text-align: right; margin: 0">
                    <el-button type="info" link @click="visible = false">取消</el-button>
                    <el-button type="primary" @click="randomImg()">确定</el-button>
                  </div>
                  <template #reference>
                    <el-icon>
                      <QuestionFilled />
                    </el-icon>
                  </template>
                </el-popover>
              </div>
            </template>

            <el-upload v-loading="loading" style="width: 80px; height: 80px" class="avatar-uploader"
              :show-file-list="false" name="filedatas" :action="uploadPictureHost" :before-upload="uploadBefore"
              :http-request="uploadSectionFile" multiple>
              <img v-if="formData.avatar" :src="formData.avatar" class="imgAvatar" />
              <el-icon v-else class="avatar-uploader-icon">
                <Plus />
              </el-icon>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row style="margin-top: 20px">
        <el-col :span="5">
          <el-form-item label="标签" prop="tags">
            <el-tag v-for="(item, index) of formData.tags" :key="index" style="margin: 0 1rem 0 0" :closable="true"
              @close="removeTag(item)">
              {{ item }}
            </el-tag>
            <!-- 标签选项 -->
            <el-popover placement="bottom-start" width="460" trigger="click"
              v-if="formData.tags && formData.tags.length < 3">
              <div class="popover-title">标签</div>
              <!-- 搜索框 -->
              <el-input style="width: 100%" v-model="tagName" placeholder="请输入标签名,enter添加自定义标签"
                @keyup.enter="saveTag" />
              <!-- 标签 -->
              <div class="popover-container">
                <div>添加标签</div>
                <el-tag v-for="(item, index) of tagList" :key="index" style="margin-left: 3px; margin-top: 2px"
                  @click="addTag(item)">
                  {{ item.name }}
                </el-tag>
              </div>
              <template #reference>
                <el-button type="primary" plain> 添加标签 </el-button>
              </template>
            </el-popover>
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-form-item label="分类" prop="categoryName">
            <el-tag type="success" v-show="formData.categoryName" style="margin: 0 1rem 0 0" :closable="true"
              @close="removeCategory">
              {{ formData.categoryName }}
            </el-tag>
            <!-- 分类选项 -->
            <el-popover placement="bottom-start" width="460" trigger="click" v-if="!formData.categoryName">
              <div class="popover-title">分类</div>
              <!-- 输入框 -->
              <el-input style="width: 100%" v-model="categoryName" placeholder="请输入分类名,enter添加自定义分类"
                @keyup.enter="saveCategory" />
              <!-- 分类 -->
              <div class="popover-container">
                <div>添加分类</div>
                <el-tag v-for="(item, index) of categoryList" :key="index" style="margin-left: 3px; margin-top: 2px"
                  class="category-item" @click="addCategory(item)">
                  {{ item.name }}
                </el-tag>
              </div>
              <template #reference>
                <el-button type="success" plain> 添加分类 </el-button>
              </template>
            </el-popover>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="是否置顶" prop="isStick">
            <el-radio-group v-model="formData.isStick">
              <el-radio v-for="(item, index) in yesOrNoList" :value="parseInt(item.value)" :label="item.label" border
                :key="index" />
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="是否发布" prop="isPublish">
            <el-radio-group v-model="formData.isPublish">
              <el-radio v-for="(item, index) in publishList" :key="index" :value="parseInt(item.value)"
                :label="item.label" border />
            </el-radio-group>
          </el-form-item>
        </el-col>

        <el-col :span="3">
          <el-form-item label="设置时间" prop="time">
            <el-date-picker v-model="formData.createTime" type="datetime" placeholder="选择创建时间"
              value-format="YYYY-MM-DD HH:mm:ss" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row style="margin-top: 20px">
        <el-col :span="4">
          <el-form-item label="阅读方式" prop="readType">
            <el-select style="width: 150px" v-model="formData.readType" placeholder="请选择阅读方式">
              <el-option v-for="(item, index) in readTypeList" :key="index" :label="item" :value="index" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="创作类型" prop="isOriginal">
            <el-radio-group v-model="formData.isOriginal">
              <el-radio v-for="(item, index) in isOriginalList" :label="item" :value="index" :key="index" border />
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col v-if="formData.isOriginal === 0" :span="5">
          <el-form-item label="原文链接" prop="originalUrl">
            <el-input v-model="formData.originalUrl" />
          </el-form-item>
        </el-col>

        <el-col :span="5">
          <el-form-item label="是否推荐" prop="isRecommend">
            <el-radio-group v-model="formData.isRecommend">
              <el-radio v-for="(item, index) in yesOrNoList" :value="parseInt(item.value)" :label="item.label" border
                :key="index" />
            </el-radio-group>
          </el-form-item>
        </el-col>

        <el-col :span="5">
          <el-form-item label="是否首页轮播" prop="isCarousel">
            <el-radio-group v-model="formData.isCarousel">
              <el-radio v-for="(item, index) in yesOrNoList" :value="parseInt(item.value)" :label="item.label" border
                :key="index" />
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="SEO关键词">
            <el-input v-model="formData.keywords" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row style="margin-top: 20px">
        <el-col :spam="24">
          <el-form-item label="内容" prop="contentMd">
            <mavon-editor placeholder="输入文章内容..." style="height: 500px; width: 100%" ref="mdRef"
              v-model="formData.contentMd" @img-del="imgDel" @img-add="imgAdd" codeStyle="dark" :ishljs="true"
              toolbarsBackground="#3d3c3c">
              <template #left-toolbar-after>
                <el-dropdown>
                  <span class="el-dropdown-link">
                    <i title="上传视频"></i>
                    <el-icon class="op-icon fa el-icon-video-camera">
                      <VideoPlay />
                    </el-icon>
                  </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item>
                        <el-upload style="display: inline-block" :show-file-list="false" name="filedatas" action=""
                          :http-request="uploadVideo" multiple>
                          <span>上传视频</span>
                        </el-upload>
                      </el-dropdown-item>
                      <el-dropdown-item>
                        <div @click="dialogVisible = true">添加视频地址</div>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
            </mavon-editor>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
        <el-button @click="closeDialog">取 消</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog center title="添加视频" v-model="dialogVisible" width="30%">
    <el-input v-model="videoInput" placeholder="视频地址" />

    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="addVideo">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 200px;
  height: 110px;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-img-icon {
  font-size: 28px;
  color: #8c939d;
  line-height: 110px;
  text-align: center;
}

.imgAvatar {
  width: 200px;
  height: 110px;
  display: block;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  margin: 0, 0, 0, 10px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 195px;
  height: 105px;
  line-height: 105px;
  text-align: center;
}
</style>
