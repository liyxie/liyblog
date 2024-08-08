import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getCommentPage(
  queryParams?: any
): AxiosPromise<any> {
  return request({
    url: "/system/comment/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 删除
 *
 * @param data
 */
export function deleteComment(
  data?: any
): AxiosPromise<any> {
  return request({
    url: "/system/comment/delete",
    method: "delete",
    data
  });
}
