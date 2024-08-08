import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 修改
 *
 * @param data
 */
export function updateWebConfig(data:any): AxiosPromise<any> {
  return request({
    url: "/system/webConfig/update",
    method: "put",
    data
  });
}

/**
 * 网站信息
 *
 */
export function getWebConfig(): AxiosPromise<any> {
  return request({
    url: "/system/webConfig/",
    method: "get"
  });
}



