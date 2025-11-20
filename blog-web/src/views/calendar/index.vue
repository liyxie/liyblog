<template>
  <div class="calendar-container">
    <!-- 左侧事件详情区域 -->
    <div class="event-panel">
      <div v-if="selectedEvent" class="event-details">
        <h3>{{ isNewEvent ? '创建新事件' : '编辑事件' }}</h3>
        <el-form 
          ref="eventFormRef" 
          :model="selectedEvent" 
          :rules="formRules" 
          label-position="top"
        >
          <el-form-item label="标题" prop="title">
            <el-input 
              v-model="selectedEvent.title" 
              placeholder="事件标题" 
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="开始时间" prop="start">
            <el-date-picker
              v-model="selectedEvent.start"
              type="datetime"
              placeholder="选择开始时间"
              format="YYYY-MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="结束时间" prop="end">
            <el-date-picker
              v-model="selectedEvent.end"
              type="datetime"
              placeholder="选择结束时间"
              format="YYYY-MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="内容">
            <el-input
              v-model="selectedEvent.content"
              type="textarea"
              rows="4"
              placeholder="事件内容描述"
              maxlength="1000"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="背景颜色">
            <el-color-picker 
              v-model="selectedEvent.bgcolor" 
              :predefine="predefinedColors"
            />
          </el-form-item>
          <el-form-item label="全天事件">
            <el-switch 
              v-model="selectedEvent.allDay" 
              active-value="1"
              inactive-value="0"
            />
          </el-form-item>
          <div class="form-actions">
            <el-button 
              type="primary" 
              @click="saveEvent"
              :loading="saveLoading"
              :disabled="saveLoading || deleteLoading"
            >
              保存
            </el-button>
            <el-button 
              v-if="!isNewEvent" 
              type="danger" 
              @click="deleteEvent"
              :loading="deleteLoading"
              :disabled="saveLoading || deleteLoading"
            >
              删除
            </el-button>
            <el-button 
              @click="cancelEdit"
              :disabled="saveLoading || deleteLoading"
            >
              取消
            </el-button>
          </div>
        </el-form>
      </div>
      <div v-else class="no-event-selected">
        <el-empty description="选择或创建一个事件" class="empty">
          <el-button type="primary" @click="createNewEvent">创建新事件</el-button>
        </el-empty>
        
        <!-- 快捷键帮助 -->
        <div class="keyboard-shortcuts">
          <h4>快捷键</h4>
          <div class="shortcut-list">
            <div class="shortcut-item">
              <kbd>Ctrl + N</kbd> <span>创建新事件</span>
            </div>
            <div class="shortcut-item">
              <kbd>Ctrl + S</kbd> <span>保存事件</span>
            </div>
            <div class="shortcut-item">
              <kbd>Esc</kbd> <span>取消编辑</span>
            </div>
            <div class="shortcut-item">
              <span>双击日期</span> <span>快速创建事件</span>
            </div>
            <div class="shortcut-item">
              <span>拖拽事件</span> <span>移动时间</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧日历区域 -->
    <div class="calendar-panel" style="position: relative;">
      <!-- 加载覆盖层 -->
      <div v-if="calendarLoading" class="loading-overlay">
        <!-- <el-loading-icon size="large" /> -->
        <span style="margin-left: 8px;">加载中...</span>
      </div>
      
      <vue-cal
        ref="vuecal"
        v-bind="calendarConfig"
        locale="zh-cn"
        :events="events"
        @event-click="handleEventClick"
        @cell-dblclick="handleCellDblclick"
        @event-drag-drop="handleEventDragDrop"
        @event-duration-change="handleEventDurationChange"
        @ready="onCalendarReady"
      />
    </div>

  </div>
</template>

<script setup name="Calendar">
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { useSiteStore } from "@/store/moudel/site.js"
import { VueCal, useLocale } from 'vue-cal'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  getUserCalendarList, 
  addCalendar, 
  updateCalendar, 
  deleteCalendar 
} from '@/api/calendar.js'

// 引入样式文件
import './index.scss' 

// 预加载中文
import zhCn from '/node_modules/vue-cal/dist/i18n/zh-cn.js'
useLocale(zhCn)
import 'vue-cal/style'
// 获取存储的主题
const siteStore = useSiteStore()
const vuecal = ref(null)
const eventFormRef = ref(null)

// 事件列表
const events = ref([])

// 当前选中的事件
const selectedEvent = ref(null)
// 是否为新事件
const isNewEvent = ref(false)
// 保存和删除按钮的加载状态
const saveLoading = ref(false)
const deleteLoading = ref(false)
// 日历数据加载状态
const calendarLoading = ref(false)

// 预定义颜色
const predefinedColors = ref([
  '#1976D2', '#42A5F5', '#66BB6A', '#FF7043', 
  '#AB47BC', '#26A69A', '#FFA726', '#EC407A'
])

// 表单验证规则
const formRules = {
  title: [
    { required: true, message: '请输入事件标题', trigger: 'blur' },
    { min: 1, max: 100, message: '标题长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  start: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  end: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ]
}

// Vue Cal配置
const calendarConfig = computed(() => ({
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
  // 主题
  // dark: siteStore.isDark,
  // 启用拖动创建事件
  dragToCreateEvent: true,
  // 启用事件拖动
  editable: true,
  // 启用事件调整大小
  resizable: true,
  // 拖动创建阈值
  dragToCreateThreshold: 20,
  // 时间格式 (使用 twelveHour: false 表示 24 小时制)
  twelveHour: false,
  // 不分割天数
  splitDays: []
}))

// 组件挂载后加载数据
onMounted(() => {
  loadCalendarEvents()
  // 添加键盘快捷键支持
  document.addEventListener('keydown', handleKeydown)
})

// 组件卸载时清理事件监听
onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown)
})

// 键盘快捷键处理
const handleKeydown = (event) => {
  // Ctrl/Cmd + N: 创建新事件
  if ((event.ctrlKey || event.metaKey) && event.key === 'n') {
    event.preventDefault()
    createNewEvent()
  }
  // ESC: 取消编辑
  else if (event.key === 'Escape' && selectedEvent.value) {
    cancelEdit()
  }
  // Ctrl/Cmd + S: 保存事件
  else if ((event.ctrlKey || event.metaKey) && event.key === 's' && selectedEvent.value) {
    event.preventDefault()
    saveEvent()
  }
}

// 加载日历事件
const loadCalendarEvents = async () => {
  try {
    calendarLoading.value = true
    const response = await getUserCalendarList()
    if (response.code === 200) {
      // 转换数据格式以适配Vue Cal
      events.value = response.data.map(item => ({
        id: item.id,
        start: item.start,
        end: item.end,
        title: item.title,
        content: item.content || '',
        bgcolor: item.bgcolor || '#1976D2',
        draggable: item.draggable === '1',
        resizable: item.resizable === '1',
        deletable: item.deletable === '1',
        allDay: item.allDay === '1',
        class: item.className || ''
      }))
    } else {
      ElMessage.error(response.message || '获取日历事件失败')
    }
  } catch (error) {
    console.error('加载日历事件失败:', error)
    if (error.response?.status === 401) {
      ElMessage.error('请先登录')
    } else if (error.response?.status === 403) {
      ElMessage.error('无权限访问')
    } else {
      ElMessage.error('网络错误，请检查网络连接')
    }
  } finally {
    calendarLoading.value = false
  }
}

// 创建新事件
const createNewEvent = () => {
  const now = new Date()
  const startTime = formatDateTime(now)
  const endTime = formatDateTime(new Date(now.getTime() + 60 * 60 * 1000)) // 1小时后

  selectedEvent.value = {
    title: '',
    start: startTime,
    end: endTime,
    content: '',
    bgcolor: generateRandomColor(),
    allDay: '0',
    draggable: '1',
    resizable: '1',
    deletable: '1'
  }
  isNewEvent.value = true
  
  // 自动聚焦到标题输入框
  nextTick(() => {
    const titleInput = document.querySelector('.event-details .el-input__inner')
    if (titleInput) {
      titleInput.focus()
    }
  })
}

// 事件点击处理
const handleEventClick = (event, e) => {
  // 从events数组中找到完整的事件数据
  const fullEvent = events.value.find(item => item.id === event.id)
  if (fullEvent) {
    selectedEvent.value = {
      id: fullEvent.id,
      title: fullEvent.title,
      start: fullEvent.start,
      end: fullEvent.end,
      content: fullEvent.content,
      bgcolor: fullEvent.bgcolor,
      allDay: fullEvent.allDay ? '1' : '0',
      draggable: '1',
      resizable: '1',
      deletable: '1'
    }
    isNewEvent.value = false
  }
}

// 双击日历单元格创建事件
const handleCellDblclick = (date, e) => {
  const startTime = formatDateTime(new Date(date))
  const endTime = formatDateTime(new Date(date.getTime() + 60 * 60 * 1000))
  
  selectedEvent.value = {
    title: '',
    start: startTime,
    end: endTime,
    content: '',
    bgcolor: '#1976D2',
    allDay: '0',
    draggable: '1',
    resizable: '1',
    deletable: '1'
  }
  isNewEvent.value = true
}

// 事件拖拽处理
const handleEventDragDrop = async (event, e) => {
  try {
    const updateData = {
      id: event.id,
      start: parseVueCalDate(event.start),
      end: parseVueCalDate(event.end),
      title: event.title,
      content: event.content || '',
      bgcolor: event.bgcolor || '#1976D2',
      allDay: event.allDay ? '1' : '0'
    }

    const response = await updateCalendar(updateData)
    if (response.code === 200) {
      ElMessage.success({
        message: '事件移动成功！',
        type: 'success',
        duration: 1500
      })
      await loadCalendarEvents()
    } else {
      ElMessage.error(response.message || '更新失败')
      // 恢复原始位置
      await loadCalendarEvents()
    }
  } catch (error) {
    console.error('拖拽更新事件失败:', error)
    ElMessage.error('更新事件失败，已恢复原位置')
    // 恢复原始位置
    await loadCalendarEvents()
  }
}

// 事件持续时间改变处理（调整大小）
const handleEventDurationChange = async (event, e) => {
  await handleEventDragDrop(event, e)
}

// 保存事件
const saveEvent = async () => {
  if (!eventFormRef.value) return
  
  try {
    const valid = await eventFormRef.value.validate()
    if (!valid) return

    saveLoading.value = true

    // 验证时间
    const startDate = new Date(selectedEvent.value.start)
    const endDate = new Date(selectedEvent.value.end)
    
    if (startDate >= endDate) {
      ElMessage.error('结束时间必须晚于开始时间')
      return
    }

    // 检查时间是否过于久远
    const now = new Date()
    const oneYearFromNow = new Date(now.getFullYear() + 1, now.getMonth(), now.getDate())
    if (startDate > oneYearFromNow) {
      const result = await ElMessageBox.confirm(
        '事件时间超过一年，确定要创建吗？',
        '时间提醒',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).catch(() => false)
      
      if (!result) return
    }

    const eventData = {
      title: selectedEvent.value.title.trim(),
      start: selectedEvent.value.start,
      end: selectedEvent.value.end,
      content: selectedEvent.value.content?.trim() || '',
      bgcolor: selectedEvent.value.bgcolor || '#1976D2',
      allDay: selectedEvent.value.allDay,
      draggable: '1',
      resizable: '1',
      deletable: '1'
    }

    let response
    if (isNewEvent.value) {
      response = await addCalendar(eventData)
    } else {
      response = await updateCalendar({
        id: selectedEvent.value.id,
        ...eventData
      })
    }

    if (response.code === 200) {
      ElMessage.success({
        message: isNewEvent.value ? '事件创建成功！' : '事件更新成功！',
        type: 'success',
        duration: 2000
      })
      selectedEvent.value = null
      await loadCalendarEvents()
    } else {
      ElMessage.error(response.message || '保存失败，请重试')
    }
  } catch (error) {
    console.error('保存事件失败:', error)
    if (error === 'cancel') return
    
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
    } else if (error.response?.status === 403) {
      ElMessage.error('无权限执行此操作')
    } else if (error.response?.status === 400) {
      ElMessage.error('数据格式错误，请检查输入内容')
    } else {
      ElMessage.error('保存失败，请检查网络连接后重试')
    }
  } finally {
    saveLoading.value = false
  }
}

// 删除事件
const deleteEvent = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除事件"${selectedEvent.value.title}"吗？此操作不可恢复！`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        customClass: 'delete-confirm-dialog'
      }
    )

    deleteLoading.value = true
    const response = await deleteCalendar([selectedEvent.value.id])
    
    if (response.code === 200) {
      ElMessage.success({
        message: '事件删除成功！',
        type: 'success',
        duration: 2000
      })
      selectedEvent.value = null
      await loadCalendarEvents()
    } else {
      ElMessage.error(response.message || '删除失败，请重试')
    }
  } catch (error) {
    if (error === 'cancel') return
    
    console.error('删除事件失败:', error)
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
    } else if (error.response?.status === 403) {
      ElMessage.error('无权限删除此事件')
    } else if (error.response?.status === 404) {
      ElMessage.error('事件不存在或已被删除')
      selectedEvent.value = null
      await loadCalendarEvents()
    } else {
      ElMessage.error('删除失败，请检查网络连接后重试')
    }
  } finally {
    deleteLoading.value = false
  }
}

// 取消编辑
const cancelEdit = () => {
  selectedEvent.value = null
  isNewEvent.value = false
}

// 日历准备就绪回调
const onCalendarReady = () => {
  console.log('Vue Cal 已准备就绪')
}

// 格式化日期时间
const formatDateTime = (date) => {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 解析Vue Cal的日期格式
const parseVueCalDate = (vueCalDate) => {
  if (!vueCalDate) return null
  // Vue Cal可能传递Date对象或字符串
  if (vueCalDate instanceof Date) {
    return formatDateTime(vueCalDate)
  }
  // 如果是字符串格式的日期
  return formatDateTime(new Date(vueCalDate))
}

// 防抖函数用于搜索
const debounce = (func, wait) => {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}

// 生成随机颜色
const generateRandomColor = () => {
  const colors = predefinedColors.value
  return colors[Math.floor(Math.random() * colors.length)]
}

</script>
<style lang="scss"> /* 这里不再需要 `scoped` 了 */
/* 组件内部的其他非模块化样式 */
</style>