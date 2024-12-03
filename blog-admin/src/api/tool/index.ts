import request from "@/utils/request";

/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getToolPage(queryParams?: any) {
  return request({
    url: "/system/tool/toolTree",
    method: "get",
    params: queryParams,
  });
}

/**
 * 获取下拉菜单
 *
 */
export function getToolOptions() {
  return request({
    url: "/system/tool/getToolOptions",
    method: "get",
  });
}

/**
 * 获取详情
 *
 */
export function getToolInfo(id?: any) {
  return request({
    url: "/system/tool/info/" + id,
    method: "get",
  });
}

/**
 * 删除
 *
 * @param data
 */
export function deleteTool(data: any) {
  return request({
    url: "/system/tool/delete",
    method: "delete",
    data,
  });
}

/**
 * 添加
 *
 * @param data
 */
export function addTool(data: any) {
  return request({
    url: "/system/tool/add",
    method: "post",
    data,
  });
}

/**
 * 修改
 *
 * @param data
 */
export function updateTool(data: any) {
  return request({
    url: "/system/tool/update",
    method: "put",
    data,
  });
}
