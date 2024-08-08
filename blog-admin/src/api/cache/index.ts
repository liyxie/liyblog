import request from "@/utils/request";
import { AxiosPromise } from "axios";
import { S } from "vite/dist/node/types.d-aGj9QkWt";

/**
 * 获取缓存监控数据
 */
export function getCacheInfo() {
  return request({
    url: "/system/cache/getCacheInfo",
    method: "get",
  });
}

/**
 * 获取缓存监控key列表
 */
export function getCacheList(queryParams: any) {
  return request({
    url: "/system/cache/list",
    method: "get",
    params: queryParams,
  });
}

/**
 * 根据键获取值
 */
export function getValue(key: string) {
  return request({
    url: "/system/cache/getValue/" + key,
    method: "get",
  });
}

/**
 * 根据键删除缓存
 */
export function deleteCache(key: string) {
  return request({
    url: "/system/cache/delete/" + key,
    method: "delete",
  });
}
