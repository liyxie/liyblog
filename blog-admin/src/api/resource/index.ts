import request from "@/utils/request";
import { AxiosPromise } from "axios";
import { ResourcePageVO } from "./types";

/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getResourcePage(
  queryParams?: any
): AxiosPromise<ResourcePageVO> {
  return request({
    url: "/system/resource/list",
    method: "get",
    params: queryParams,
  });
}

/**
 * 删除
 *
 * @param data
 */
export function deleteResource(data?: Number[]): AxiosPromise<any> {
  return request({
    url: "/system/resource/delete",
    method: "delete",
    data,
  });
}
