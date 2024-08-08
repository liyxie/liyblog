import request from "@/utils/request";
import { SayForm} from "./types";

/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getSayPage(queryParams?: any){
  return request({
    url: "/system/say/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 删除
 *
 * @param data
 */
export function deleteSay(data:Number[]) {
  return request({
    url: "/system/say/delete",
    method: "delete",
    data
  });
}


/**
 * 添加
 *
 * @param data
 */
export function addSay(data:SayForm) {
  return request({
    url: "/system/say/add",
    method: "post",
    data
  });
}

/**
 * 修改
 *
 * @param data
 */
export function updateSay(data:SayForm) {
  return request({
    url: "/system/say/update",
    method: "put",
    data
  });
}