import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getMessagePage(
  queryParams?: any
): AxiosPromise<any> {
  return request({
    url: "/system/message/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 删除
 *
 * @param data
 */
export function deleteMessage(
  data?: any
): AxiosPromise<any> {
  return request({
    url: "/system/message/delete",
    method: "delete",
    data
  });
}
