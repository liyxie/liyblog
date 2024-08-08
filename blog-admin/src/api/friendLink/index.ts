import request from "@/utils/request";
import { AxiosPromise } from "axios";
import { FriendLinkForm, FriendLinkPageVO, FriendLinkQuery} from "./types";

/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getFriendPage(
  queryParams?: FriendLinkQuery
): AxiosPromise<FriendLinkPageVO> {
  return request({
    url: "/system/friend/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 添加
 *
 * @param data
 */
export function addFriend(data:FriendLinkForm): AxiosPromise<any> {
  return request({
    url: "/system/friend/add",
    method: "post",
    data
  });
}

/**
 * 修改
 *
 * @param data
 */
export function updateFriend(data:FriendLinkForm): AxiosPromise<any> {
  return request({
    url: "/system/friend/update",
    method: "put",
    data
  });
}

/**
 * 删除
 *
 * @param data
 */
export function deleteFriend(data:Number[]): AxiosPromise<any> {
  return request({
    url: "/system/friend/delete",
    method: "delete",
    data
  });
}

/**
 * 置顶友链
 *
 * @param id
 */
export function topFriend(id:Number): AxiosPromise<any> {
  return request({
    url: "/system/friend/top",
    method: "get",
    params:{
      id:id
    }
  });
}

