<template>
  <svg 
  :class="['svg-icon', { running: isRunning }]" 
  aria-hidden="true" 
  @mouseenter="handleMouseEnter"
  @mouseleave="handleMouseLeave"
  :style="{marginRight:`${mr}px`}">
    <use :xlink:href="iconName"/>
  </svg>
</template>

<script setup>
import { ref, watchEffect, onUnmounted } from 'vue'


const props = defineProps({
  name: {
    type: String,
    required: true,
     default: ''
  },
  color: {
    type: String,
    default: ''
  },
  mr:{
    type:String,
    default:'0'
  },
  animate: { // 新增动画控制属性
    type: Boolean,
    default: false
  },
  frameInterval: { // 帧切换间隔（毫秒）
    type: Number,
    default: 200
  }
})

const currentFrame = ref(1)
let animationInterval = null

watchEffect(() => {
  if (props.animate) {
    animationInterval = setInterval(() => {
      currentFrame.value = currentFrame.value % 3 + 1 // 循环1-3
    }, props.frameInterval)
  } else {
    clearInterval(animationInterval)
    currentFrame.value = 1 // 重置为第一帧
  }
})
onUnmounted(() => clearInterval(animationInterval))

// 动态计算图标名称
const iconName = computed(() => `#icon-${props.name}-${currentFrame.value}`)
// const iconName = computed(()=>`#icon-${props.name}`);

const mr = computed(()=>`#icon-${props.mr}`);
const svgClass = computed(()=> {
  if (props.name) {
    return `svg-icon icon-${props.name}`
  }
  return 'svg-icon'
});

// 保留之前代码，增加运行状态控制
const isRunning = ref(false)
// 监听悬停状态
const handleMouseEnter = () => {
  isRunning.value = true
  currentFrame.value = 1 // 重置动画起点
}
const handleMouseLeave = () => {
  isRunning.value = false
}


</script>

<style lang='scss'>
.svg-icon {
  width: 1em;
  height: 1em;
  fill: currentColor;
  vertical-align: middle;
}
</style>
