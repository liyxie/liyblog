<template>
  <div class="dashboard-container">
    <!-- github角标 -->
    <github-corner class="github-corner" />

    <el-card shadow="never">
      <el-row justify="space-between">
        <el-col :span="18" :xs="24">
          <div class="flex h-full items-center">
            <img class="w-20 h-20 mr-5 rounded-full" :src="userStore.user.avatar + '?imageView2/1/w/80/h/80'" />
            <div>
              <p>{{ greetings }}</p>
              <p class="text-sm text-gray">
                {{
                  userStore.user.intro
                    ? userStore.user.intro
                    : "以后的你会感谢现在努力的你"
                }}
              </p>
            </div>
          </div>
        </el-col>

        <el-col :span="6" :xs="24">
          <div class="flex h-full items-center justify-around">
            <el-statistic :value="topData.article">
              <template #title>
                <div class="flex items-center">
                  <svg-icon icon-class="todolist" size="20px" />
                  <span class="text-[16px] ml-1">文章数</span>
                </div>
              </template>
            </el-statistic>

            <el-statistic :value="topData.viewsCount">
              <template #title>
                <div class="flex items-center">
                  <el-icon size="20px" color="red">
                    <View />
                  </el-icon>
                  <span class="text-[16px] ml-1">总访客数</span>
                </div>
              </template>
            </el-statistic>

            <el-statistic :value="topData.user">
              <template #title>
                <div class="flex items-center">
                  <svg-icon icon-class="visit" size="20px" />
                  <span class="text-[16px] ml-1">用户量</span>
                </div>
              </template>
            </el-statistic>

            <el-statistic :value="topData.message">
              <template #title>
                <div class="flex items-center">
                  <svg-icon icon-class="message" size="20px" />
                  <span class="text-[16px] ml-1">留言数</span>
                </div>
              </template>
            </el-statistic>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-row class="mt-3">
      <el-card style="padding: 16px 16px 0; width: 100%">
        <div id="container" style="width: 100%; height: 300px"></div>
      </el-card>
    </el-row>

    <!-- Echarts 图表 -->
    <!-- 文章阅读量排行 -->
    <el-row :gutter="10" class="mt-3">
      <el-col :sm="24" :lg="8" class="mb-2">
        <el-card>
          <div class="e-title">文章阅读量排行</div>
          <el-table :data="articles" style="padding-top: 25px" fit>
            <el-table-column label="标题" width="400">
              <template #default="scope">
                <el-link style="
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                  " :underline="false" :href="url + 'article/' + scope.row.id" target="_blank">{{ scope.row.title
                  }}</el-link>
              </template>
            </el-table-column>
            <el-table-column label="阅读量" prop="quantity" align="center" />
          </el-table>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="8" class="mb-2">
        <el-card>
          <div class="e-title">文章分类统计</div>
          <div class="chart-wrapper">
            <div id="categoryChart" class="chart" style="height: 310px; width: 100%"></div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="8" class="mb-2">
        <el-card>
          <div class="e-title">文章标签</div>
          <tag-cloud :data="tagsList" />
        </el-card>
      </el-col>
    </el-row>

    <el-row class="mt-3">
      <el-card style="width: 100%">
        <div class="e-title">一周访问量</div>
        <div id="access" class="chart" style="height: 350px; width: 100%"></div>
      </el-card>
    </el-row>

    <el-dialog title="通知" v-model="centerDialogVisible" center>
      <span v-html="dashboard"></span>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { getDashboardBottomStatistics, getDashboardTopStatistics } from "@/api";
import * as echarts from "echarts";

defineOptions({
  name: "Dashboard",
  inheritAttrs: false,
});

import { useUserStore } from "@/store/modules/user";
import { useTransition, TransitionPresets } from "@vueuse/core";

const userStore = useUserStore();
const date: Date = new Date();

const categoryChart = ref();
const chart = ref();
const articles = ref();
const dashboard = ref();
const tagsList = ref<any>([]);
const topData = ref<any>({});
const url = ref(import.meta.env.VITE_APP_HOME_URL);
// 显示弹窗
const centerDialogVisible = ref(false);

const greetings = computed(() => {
  const hours = date.getHours();
  if (hours >= 6 && hours < 8) {
    return "晨起披衣出草堂，轩窗已自喜微凉🌅！";
  } else if (hours >= 8 && hours < 12) {
    return "上午好，" + userStore.user.nickname + "！";
  } else if (hours >= 12 && hours < 18) {
    return "下午好，" + userStore.user.nickname + "！";
  } else if (hours >= 18 && hours < 24) {
    return "晚上好，" + userStore.user.nickname + "！";
  } else {
    return "偷偷向银河要了一把碎星，只等你闭上眼睛撒入你的梦中，晚安🌛！";
  }
});

const duration = 5000;

//初始化文章贡献度
function initContributeDate(contributeDate: any, blogContributeCount: any) {
  let chart = markRaw(echarts.init(document.getElementById("container")));
  let option = {
    //设置背景
    // backgroundColor: '#d0d0d0',
    title: {
      top: 30,
      text: "文章贡献度",
      subtext: "一年内博客提交的数量",
      left: "center",
      color: "#29a348d1",
    },
    tooltip: {
      trigger: "item",
      formatter: function (params: any) {
        return params.data[0] + "<br>文章数：" + params.data[1];
      },
    },
    legend: {
      top: "30",
      left: "100",
      data: ["文章数"],
      // 设置字体颜色
      color: "#29a348d1",
    },
    calendar: [
      {
        top: 100,
        left: "center",
        range: contributeDate,
        splitLine: {
          show: true,
          lineStyle: {
            // 设置月份分割线的颜色
            color: "#D3D3D3",
            width: 4,
            type: "solid",
          },
        },
        yearLabel: { show: false },
        dayLabel: {
          nameMap: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"], // 设置中文显示
          // 设置周显示颜色
          color: "#29a348d1",
          firstDay: 1, // 从周一开始
        },
        monthLabel: {
          nameMap: "cn", // 设置中文显示
          // 设置月显示颜色
          color: "#29a348d1",
        },
        itemStyle: {
          // 设置背景颜色
          color: "#ffffff",
          borderWidth: 1,
          // 设置方块分割线段颜色
          borderColor: "#D3D3D3",
        },
      },
    ],
    series: [
      {
        name: "文章数",
        type: "scatter",
        coordinateSystem: "calendar",
        data: blogContributeCount,
        // 根据值设置原点大小
        symbolSize: function (val: any) {
          if (val[1] === 0) {
            return val[1];
          } else {
            let size = 8 + val[1] * 2;
            if (size > 18) {
              size = 18;
            }
            return size;
          }
        },
        itemStyle: {
          // 设置圆点颜色
          color: "#2ec7c9",
        },
      },
    ],
  };
  chart.setOption(option);
  window.addEventListener("resize", function () {
    chart.resize();
  });
}

//初始化分类统计
function initCategoryChart(categoryList: any, result: any) {
  categoryChart.value = markRaw(
    echarts.init(document.getElementById("categoryChart"))
  );
  categoryChart.value.setOption({
    tooltip: {
      trigger: "item",
      formatter: "{a} <br/>{b} : {c} ({d}%)",
    },
    legend: {
      left: "center",
      bottom: "10",
      data: categoryList,
    },
    series: [
      {
        name: "WEEKLY WRITE ARTICLES",
        type: "pie",
        roseType: "radius",
        radius: [15, 95],
        center: ["50%", "38%"],
        data: result,
        animationEasing: "cubicInOut",
        animationDuration: 2600,
      },
    ],
  });
  let chart = categoryChart.value;
  window.addEventListener("resize", function () {
    chart.resize();
  });
}

//初始化访问线状图
function initUserAccessChart(datas: any) {
  chart.value = markRaw(echarts.init(document.getElementById("access")));
  let data = ref<any>({
    createData: 0,
    countData: 0,
    accessData: 0,
  });
  let createData = <any>[];
  let countData = <any>[];
  let accessData = <any>[];
  datas.forEach((item: any) => {
    createData.push(item.createTime);
    countData.push(item.ip);
    accessData.push(item.access);
  });
  data.value.createData = createData;
  data.value.countData = countData;
  data.value.accessData = accessData;
  setOptions(data.value);
}

function setOptions(data: any) {
  let createData = data.createData;
  let countData = data.countData;
  let accessData = data.accessData;
  chart.value.setOption({
    xAxis: {
      data: createData,
      boundaryGap: false,
      axisTick: {
        show: false,
      },
    },
    grid: {
      left: 10,
      right: 10,
      bottom: 20,
      top: 30,
      containLabel: true,
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "cross",
      },
      padding: [5, 10],
    },
    yAxis: {
      axisTick: {
        show: false,
      },
    },
    legend: {
      data: ["独立用户(UV)", "访问量(PV)"],
    },
    series: [
      {
        name: "独立用户(UV)",
        smooth: true,
        type: "line",
        itemStyle: {
          color: "#FF005A",
          lineStyle: {
            color: "#FF005A",
            width: 2,
          },
        },
        data: countData,
        animationDuration: 2800,
        animationEasing: "quadraticOut",
      },
      {
        name: "访问量(PV)",
        smooth: true,
        type: "line",
        itemStyle: {
          color: "#3888fa",
          lineStyle: {
            color: "#3888fa",
            width: 2,
          },
          areaStyle: {
            color: "#f3f8ff",
          },
        },
        data: accessData,
        animationDuration: 2800,
        animationEasing: "quadraticOut",
      },
    ],
  });
  let c = chart.value;
  window.addEventListener("resize", function () {
    c.resize();
  });
}

onMounted(() => {
  getDashboardBottomStatistics().then((res) => {
    console.log(res.data);
    dashboard.value = res.data.dashboard;
    tagsList.value = res.data.tagsList;
    articles.value = res.data.articles;
    initContributeDate(
      res.data.contribute.contributeDate,
      res.data.contribute.blogContributeCount
    );
    initCategoryChart(
      res.data.categoryList.categoryList,
      res.data.categoryList.result
    );
    initUserAccessChart(res.data.userAccess);
    centerDialogVisible.value = res.data.openDashboardNotification == 1;
  });
  getDashboardTopStatistics().then((res) => {
    topData.value = res.data;
  });
});
</script>

<style lang="scss" scoped>
.dashboard-container {
  position: relative;
  padding: 24px;

  .user-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
  }

  .github-corner {
    position: absolute;
    top: 0;
    right: 0;
    z-index: 1;
    border: 0;
  }

  .data-box {
    display: flex;
    justify-content: space-between;
    padding: 20px;
    font-weight: bold;
    color: var(--el-text-color-regular);
    background: var(--el-bg-color-overlay);
    border-color: var(--el-border-color);
    box-shadow: var(--el-box-shadow-dark);
  }

  .svg-icon {
    fill: currentcolor !important;
  }
}
</style>
