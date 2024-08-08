import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getFeedBackPage(
  queryParams?: any
): AxiosPromise<any> {
  return request({
    url: "/system/feedback/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 删除
 *
 * @param data
 */
export function deleteFeedBack(
  data?: any
): AxiosPromise<any> {
  return request({
    url: "/system/feedback/delete",
    method: "delete",
    data
  });
}

/**
 * 修改
 *
 * @param data
 */
export function updateFeedBack(
  data?: any
): AxiosPromise<any> {
  return request({
    url: "/system/feedback/update",
    method: "put",
    data
  });
}