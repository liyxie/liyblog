import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getTablePage(
  queryParams?: any
): AxiosPromise<any> {
  return request({
    url: "/system/generate/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 预览
 *
 * @param tableName
 */
export function preview(tableName:string){
  return request({
    url: "/system/generate/preview/" + tableName,
    method: "get",
  });
}

/**
 * 下载
 *
 */
export function download(params:any) {
  return request({
    url: "/system/generate/download",
    method: "get",
    params:params
  });
}
