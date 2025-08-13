import request from '@/utils/request'

/**
 * 获取日历事件列表
 * @param {string} title 事件标题搜索条件
 */
export function getCalendarList(title) {
    return request({
        url: '/api/calendar/list',
        method: 'get',
        params: { title }
    })
}

/**
 * 获取当前用户的所有日历事件
 */
export function getUserCalendarList() {
    return request({
        url: '/api/calendar/user',
        method: 'get'
    })
}

/**
 * 按时间范围查询日历事件
 * @param {Date} startDate 开始时间
 * @param {Date} endDate 结束时间
 */
export function getCalendarByDateRange(startDate, endDate) {
    return request({
        url: '/api/calendar/range',
        method: 'get',
        params: {
            startDate: startDate,
            endDate: endDate
        }
    })
}

/**
 * 获取日历事件详情
 * @param {number} id 事件ID
 */
export function getCalendarById(id) {
    return request({
        url: '/api/calendar/info/' + id,
        method: 'get'
    })
}

/**
 * 新增日历事件
 * @param {object} data 事件数据
 */
export function addCalendar(data) {
    return request({
        url: '/api/calendar/add',
        method: 'post',
        data
    })
}

/**
 * 修改日历事件
 * @param {object} data 事件数据
 */
export function updateCalendar(data) {
    return request({
        url: '/api/calendar/update',
        method: 'put',
        data
    })
}

/**
 * 删除日历事件
 * @param {array} ids 事件ID数组
 */
export function deleteCalendar(ids) {
    return request({
        url: '/api/calendar/delete',
        method: 'delete',
        data: ids
    })
}