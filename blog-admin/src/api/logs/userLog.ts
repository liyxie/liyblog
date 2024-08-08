import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 获取用户日志分页数据
 *
 * @param queryParams
 */
export function getUserLogPage(
  queryParams?: any
): AxiosPromise<any> {
  return request({
    url: "/system/userLog/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 删除用户日志
 *
 * @param data
 */
export function deleteUserLog(
  data?: any
): AxiosPromise<any> {
  return request({
    url: "/system/userLog/delete",
    method: "delete",
    data
  });
}
