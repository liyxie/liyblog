import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 获取异常日志分页数据
 *
 * @param queryParams
 */
export function getExceptionLogPage(
  queryParams?: any
): AxiosPromise<any> {
  return request({
    url: "/system/exceptionLog/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 删除异常日志
 *
 * @param data
 */
export function deleteExceptionrLog(
  data?: any
): AxiosPromise<any> {
  return request({
    url: "/system/exceptionLog/delete",
    method: "delete",
    data
  });
}
