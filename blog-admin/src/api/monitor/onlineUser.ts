import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getOnlineUserPage(
  queryParams?: any
): AxiosPromise<any> {
  return request({
    url: "/system/user/online",
    method: "get",
    params: queryParams,
  });
}


/**
 * 删除
 *
 * @param data
 */
export function kickOnlineUser(tokenValue:any): AxiosPromise<any> {
  return request({
    url: "/system/user/kick",
    method: "get",
    params:{
      token:tokenValue
    }
  });
}
