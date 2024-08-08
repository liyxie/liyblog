import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getJobLogPage(
  queryParams?: any
): AxiosPromise<any> {
  return request({
    url: "/system/jobLog/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 删除
 *
 * @param data
 */
export function deleteJobLog(data:any): AxiosPromise<any> {
  return request({
    url: "/system/jobLog/delete",
    method: "delete",
    data
  });
}

/**
 * 清空日志
 *
 * @param data
 */
export function cleanJobLog(): AxiosPromise<any> {
  return request({
    url: "/system/jobLog/clean",
    method: "get"
  });
}
