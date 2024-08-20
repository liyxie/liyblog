<template>
  <div class="app-container">
    <el-tabs type="border-card" @tab-click="handleClick" v-model="activeName">
      <el-tab-pane name="1">
        <template #label><svg-icon icon-class="system" />系统配置</template>
        <el-form
          style="margin-left: 20px"
          label-position="left"
          label-width="140px"
          :model="form"
          :rules="rules"
          ref="formRef"
        >
          <aside>通过开关选择博客的一些配置，以及文件显示方式<br /></aside>

          <el-form-item label="网站消息邮件通知">
            <el-radio-group v-model="form.startEmailNotification" class="ml-4">
              <el-radio
                v-for="item in openDictList"
                :key="item.value"
                :value="item.value"
                :label="item.label"
                border
              />
            </el-radio-group>
          </el-form-item>

          <el-form-item label="启用邮箱发送功能">
            <el-radio-group v-model="form.openEmail" class="ml-4">
              <el-radio
                v-for="item in yesNoDictList"
                :key="parseInt(item.value)"
                :value="parseInt(item.value)"
                :label="item.label"
                border
              />
            </el-radio-group>
          </el-form-item>

          <el-form-item label="注册用户邮件激活">
            <el-radio-group v-model="form.openEmailActivate" class="ml-4">
              <el-radio
                v-for="item in openDictList"
                :key="item.value"
                :value="item.value"
                :label="item.label"
                border
              />
            </el-radio-group>
          </el-form-item>

          <el-form-item label="图片上传方式">
            <el-radio-group v-model="form.fileUploadWay" class="ml-4">
              <el-radio
                v-for="item in form.systemFileConfigVOList"
                :key="item.id"
                :value="item.id"
                :label="item.typeName"
                border
              />
            </el-radio-group>
          </el-form-item>

          <el-form-item label="仪表盘弹框通知">
            <el-radio-group
              v-model="form.openDashboardNotification"
              class="ml-4"
            >
              <el-radio
                v-for="item in openDictList"
                :key="item.value"
                :value="item.value"
                :label="item.label"
                border
              />
            </el-radio-group>
          </el-form-item>

          <el-form-item label="文章搜索模式">
            <el-radio-group v-model="form.searchModel" class="ml-4">
              <el-radio
                v-for="item in searchModelDictList"
                :key="parseInt(item.value)"
                :value="parseInt(item.value)"
                :label="item.label"
                border
              />
            </el-radio-group>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              @click="submitForm()"
              v-hasPerm="['system:config:update']"
              >修 改</el-button
            >
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane name="2">
        <template #label> <svg-icon icon-class="publish" /> 邮箱发送 </template>

        <el-form
          style="margin-left: 20px"
          label-position="left"
          :model="form"
          label-width="120px"
          :rules="rules"
          ref="formRef"
        >
          <aside>使用 <a href="#">邮箱</a> 发送通知<br /></aside>

          <el-form-item label="邮箱地址" prop="emailHost">
            <el-input
              v-model="form.emailHost"
              auto-complete="new-password"
              style="width: 400px"
            />
          </el-form-item>

          <el-form-item label="邮箱发送者">
            <el-input
              v-model="form.emailUsername"
              auto-complete="new-password"
              style="width: 400px"
            />
          </el-form-item>

          <el-form-item label="邮箱授权码">
            <el-input
              type="emailUsername"
              v-model="form.emailPassword"
              auto-complete="new-password"
              style="width: 400px"
            />
          </el-form-item>

          <el-form-item label="邮箱端口">
            <el-input v-model="form.emailPort" style="width: 400px" />
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              @click="submitForm()"
              v-hasPerm="['system:config:update']"
              >修 改</el-button
            >
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane name="3">
        <template #label
          ><el-icon><BellFilled /></el-icon> 仪表盘通知</template
        >
        <el-form
          label-position="left"
          :model="form"
          :rules="rules"
          ref="formRef"
        >
          <el-form-item>
            <div class="editor-container" style="width: 100%">
              <mavon-editor
                placeholder="开始编辑...."
                :subfield="false"
                style="height: 500px"
                ref="mdRef"
                v-model="form.dashboardNotificationMd"
              />
            </div>
          </el-form-item>
        </el-form>

        <div
          style="margin-top: 5px; margin-left: 10px; text-align: center"
          v-hasPerm="['system:config:update']"
        >
          <el-button type="primary" @click="submitForm()">修 改</el-button>
        </div>
      </el-tab-pane>

      <el-tab-pane
        v-for="(item, index1) in form.systemFileConfigVOList"
        :key="item.id"
        :name="index1 + 4"
        :label="item.typeName"
      >
        <template #label>
          <svg-icon icon-class="download" /> {{ item.typeName }}
        </template>

        <el-form
          style="margin-left: 20px"
          label-position="left"
          :model="form"
          label-width="120px"
          :rules="rules"
          ref="formRef"
        >
          <aside>{{ item.typeName }}</aside>

          <el-form-item label="存储主路径" prop="path">
            <el-input v-model="item.path" style="width: 400px" />
          </el-form-item>

          <div v-if="index1 > 0">
            <el-form-item label="域名" prop="url">
              <el-input
                v-model="item.url"
                auto-complete="new-password"
                style="width: 400px"
              />
            </el-form-item>

            <el-form-item label="公钥">
              <el-input
                v-model="item.accessKey"
                auto-complete="new-password"
                style="width: 400px"
              />
            </el-form-item>

            <el-form-item label="私钥">
              <el-input
                type="password"
                v-model="item.secretKey"
                auto-complete="new-password"
                style="width: 400px"
              />
            </el-form-item>

            <el-form-item label="上传空间/桶">
              <el-input v-model="item.bucket" style="width: 400px" />
            </el-form-item>

            <el-form-item label="存储区域">
              <el-select
                v-model="item.area"
                placeholder="请选择存储区域"
                style="width: 200px"
                clearable
                v-if="item.id == '2'"
              >
                <el-option
                  v-for="item1 in QiNiuAreaDictList"
                  :key="item1.value"
                  :label="item1.name"
                  :value="item1.value"
                />
              </el-select>

              <el-select
                v-model="item.area"
                placeholder="请选择存储区域"
                style="width: 200px"
                clearable
                v-if="item.id == 3"
              >
                <el-option
                  v-for="item1 in txAreaDictList"
                  :key="item1.name"
                  :label="item1.name"
                  :value="item1.name"
                />
              </el-select>
            </el-form-item>
          </div>

          <el-form-item>
            <el-button
              type="primary"
              @click="submitForm()"
              v-hasPerm="['system:config:update']"
              >修 改</el-button
            >
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { getSystemConfig, updateSystemConfig } from "@/api/systemConfig";
import { getDataByDictType } from "@/api/dict";

defineOptions({
  name: "SystemConfig",
  inheritAttrs: false,
});

const index = ref("0");
const QiNiuAreaDictList = ref([
  { name: "华东", value: "z0" },
  { name: "华北", value: "z1" },
  { name: "华南", value: "z2" },
  { name: "北美", value: "na0" },
  { name: "东南亚", value: "as0" },
]);
const txAreaDictList = ref([
  { name: "北京一区" },
  { name: "北京" },
  { name: "南京" },
  { name: "上海" },
  { name: "广州" },
  { name: "成都" },
  { name: "重庆" },
]);
const yesNoDictList = ref([]);
const openDictList = ref([]);
const searchModelDictList = ref([]);
const openDictDefaultValue = ref(null);
const rules = ref({
  qiNiuPictureBaseUrl: [
    {
      pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/,
      message: "请输入正确的域名",
    },
  ],
  email: [
    {
      pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/,
      message: "请输入正确的邮箱",
    },
  ],
});
const activeName = ref("1");

const form = reactive<any>({});
const formRef = ref();
const mdRef = ref();

/**
 * 字典查询
 */
function getDictList() {
  let dictTypeList = ["sys_search_model", "sys_yes_no", "sys_normal_disable"];
  getDataByDictType(dictTypeList).then((response: any) => {
    let dictMap = response.data;
    yesNoDictList.value = dictMap.sys_yes_no.list;
    openDictList.value = dictMap.sys_normal_disable.list;
    openDictDefaultValue.value = dictMap.sys_normal_disable.defaultValue;
    searchModelDictList.value = dictMap.sys_search_model.list;
  });
}

/**
 * 点击tab
 */
function handleClick(tab: any, event: any) {
  index.value = tab.index;
}

/**
 * 获取系统配置
 */
function getSystemConfigList() {
  getSystemConfig().then((response: any) => {
    if (response.data) {
      Object.assign(form, response.data);
    }
    console.log(form);
  });
}
getSystemConfigList();

/**
 * 提交
 */
function submitForm() {
  formRef.value.validate((isValid: boolean) => {
    if (!isValid) {
      console.log("校验出错");
    } else {
      //获取文本编辑器中的内容【只有在切换到仪表盘通知的时候，才需要获取】
      if (index.value === "4") {
        form.dashboardNotification = mdRef.value.d_render;
      }
      updateSystemConfig(form).then(() => {
        ElMessage.success("修改成功");
      });
    }
  });
}

onMounted(() => {
  getDictList();
});
</script>

<style lang="scss">
aside {
  background: #eef1f6;
  padding: 8px 24px;
  margin-bottom: 20px;
  border-radius: 2px;
  display: block;
  line-height: 32px;
  font-size: 16px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, "Fira Sans", "Droid Sans", "Helvetica Neue", sans-serif;
  color: #2c3e50;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;

  a {
    color: #337ab7;
    cursor: pointer;

    &:hover {
      color: rgb(32, 160, 255);
    }
  }
}
</style>
