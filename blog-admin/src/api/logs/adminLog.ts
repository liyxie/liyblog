import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 获取操作日志分页数据
 *
 * @param queryParams
 */
export function getAdminLogPage(
  queryParams?: any
): AxiosPromise<any> {
  return request({
    url: "/system/adminLog/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 删除操作日志
 *
 * @param data
 */
export function deleteAdminLog(
  data?: any
): AxiosPromise<any> {
  return request({
    url: "/system/adminLog/delete",
    method: "delete",
    data
  });
}
