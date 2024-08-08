import {createVNode, render, cloneVNode} from "vue"
import Loading from "./index.vue"

export default {
    install(app) {
        // 使用vue底层的createVNode方法将组件渲染为虚拟节点
        const VNode = createVNode(Loading)
        // 使用render函数将组件挂载到body中
        render(VNode, document.body)
        // 定义全局方法设置组件的显示和隐藏
        app.config.globalProperties.$showLoading = VNode.component?.exposed.showLoading
        app.config.globalProperties.$hideLoading = VNode.component?.exposed.hideLoading

        const weakMap = new WeakMap()

        // 自定义Loading指令
        app.directive("sy-loading", {
            mounted(el) {
                if (weakMap.get(el)) return
                //  记录当前绑定元素的position
                weakMap.set(el, window.getComputedStyle(el).position)
            },
            updated(el, binding) {
                const oldPosition = weakMap.get(el);
                // 如果不是position: relative或者absolute，就设置为relative
                // 这里的目的是确保loading组件正确覆盖当前绑定的元素
                if (oldPosition !== 'absolute' && oldPosition !== 'relative') {
                    el.style.position = 'relative'
                }
                // 克隆一份loading元素,
                // 作用是当页面上有多个zx-loading时，每个dom都维护一份属于自己的loading，不会冲突
                const newVNode = cloneVNode(VNode)
                // 挂载当前节点
                render(newVNode, el)
                // 判断绑定的值
                if (binding.value) {
                    newVNode.component?.exposed.showLoading()
                } else {
                    newVNode.component?.exposed.hideLoading(() => {
                        // 还原布局方式
                        el.style.position = oldPosition
                    })
                }
            }
        })
    }
}
