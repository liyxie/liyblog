<template>
  <div class="calendar-container">
    <!-- 左侧事件详情区域 -->
    <div class="event-panel">
      <div v-if="selectedEvent" class="event-details">
        <h3>事件详情</h3>
        <el-form label-position="top">
          <el-form-item label="标题">
            <el-input v-model="selectedEvent.title" placeholder="事件标题" />
          </el-form-item>
          <el-form-item label="开始时间">
            <el-date-picker
              v-model="selectedEvent.start"
              type="datetime"
              placeholder="选择开始时间"
              format="YYYY-MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm" />
          </el-form-item>
          <el-form-item label="结束时间">
            <el-date-picker
              v-model="selectedEvent.end"
              type="datetime"
              placeholder="选择结束时间"
              format="YYYY-MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm" />
          </el-form-item>
          <el-form-item label="内容">
            <el-input
              v-model="selectedEvent.content"
              type="textarea"
              rows="4"
              placeholder="事件内容描述" />
          </el-form-item>
          <el-form-item label="背景颜色">
            <el-color-picker v-model="selectedEvent.bgcolor" />
          </el-form-item>
          <div class="form-actions">
            <el-button type="primary" @click="saveEvent">保存</el-button>
            <el-button v-if="!isNewEvent" type="danger" @click="deleteEvent">删除</el-button>
            <el-button @click="cancelEdit">取消</el-button>
          </div>
        </el-form>
      </div>
      <div v-else class="no-event-selected">
        <el-empty description="选择或创建一个事件">
          <el-button type="primary" @click="createNewEvent">创建新事件</el-button>
        </el-empty>
      </div>
    </div>

    <!-- 右侧日历区域 -->
    <div class="calendar-panel">
      <vue-cal
        ref="vuecal"
        v-bind="config"
        locale="zh-cn"
        :events="events"
      />
    </div>

  </div>
</template>

<script setup name="Calendar">
import { ref, computed, reactive } from 'vue'
import { useSiteStore } from "@/store/moudel/site.js";
import { VueCal, useLocale } from 'vue-cal';
import { ElMessage } from 'element-plus';
// 预加载中文
import zhCn from '/node_modules/vue-cal/dist/i18n/zh-cn.js'
useLocale(zhCn)
import 'vue-cal/style'
// 获取存储的主题
const siteStore = useSiteStore();
const vuecal = ref(null);

// 事件列表
const events = ref([
  {
    id: 1,
    start: '2025-04-13 10:00',
    end: '2025-04-13 12:00',
    title: '示例事件',
    content: '这是一个示例事件，你可以编辑或删除它',
    bgcolor: '#1976D2',
    class: 'sample-event'
  }
]);

// 当前选中的事件
const selectedEvent = ref(null);
// 是否为新事件
const isNewEvent = ref(false);

// 配置
const config = computed(() => ({
    // 是否隐藏周末
    hideWeekends: false,
    // 是否显示当前时间点
    time: true,
    // 是否显示月周数
    weekNumbers: true,
    // 显示时间视图选择范围
    views: ['year', 'month', 'week', 'day'],
    // 默认显示日历视图
    view: 'week',
    dark: siteStore.isDark,
    // 启用拖动创建事件
    dragToCreateEvent: true,
    // 启用事件拖动
    dragToCreateThreshold: 20,
  })
);


</script>

<style lang="scss" scoped>
.calendar-container {
  width: 97%;
  margin: 43px auto 0 auto;
  display: flex;
  gap: 20px;
  height: 700px;
}

.event-panel {
  flex: 1;
  max-width: 30%;
  min-width: 300px;
  background-color: var(--el-bg-color);
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow-y: auto;
}

.calendar-panel {
  flex: 2;
}

.event-details {
  h3 {
    margin-top: 0;
    margin-bottom: 20px;
    color: var(--el-text-color-primary);
  }
}

.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.no-event-selected {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.vuecal {
  // 主题色
  --vuecal-primary-color: var(--calendar-background-color);
}

</style>
