import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 获取数据
 *
 */
export function getSystemConfig(){
  return request({
    url: "/system/config/getConfig",
    method: "get",
  });
}


/**
 * 修改
 *
 * @param data
 */
export function updateSystemConfig(data:any): AxiosPromise<any> {
  return request({
    url: "/system/config/update",
    method: "put",
    data
  });
}