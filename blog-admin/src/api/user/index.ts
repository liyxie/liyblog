import request from "@/utils/request";
import { AxiosPromise } from "axios";
import {
  UserForm,
  UserInfo,
  UserPageVO,
  UserQuery,
  PasswordData,
} from "./types";

/**
 * 登录成功后获取用户信息（昵称、头像、权限集合和角色集合）
 */
export function getUserInfoApi(): AxiosPromise<UserInfo> {
  return request({
    url: "/system/user/getCurrentUserInfo",
    method: "get",
  });
}

/**
 * 获取用户分页列表
 *
 * @param queryParams
 */
export function getUserPage(queryParams: any): AxiosPromise<any> {
  return request({
    url: "/system/user/list",
    method: "get",
    params: queryParams,
  });
}

/**
 * 获取用户详情
 *
 * @param id
 */
export function getUserInfo(id: any) {
  return request({
    url: "/system/user/info/" + id,
    method: "get",
  });
}

/**
 * 添加用户
 *
 * @param data
 */
export function addUser(data: any) {
  return request({
    url: "/system/user/add",
    method: "post",
    data: data,
  });
}

/**
 * 修改用户
 *
 * @param id
 * @param data
 */
export function updateUser(data: any) {
  return request({
    url: "/system/user/update",
    method: "put",
    data: data,
  });
}

/**
 * 修改用户密码
 *
 * @param id
 * @param password
 */
export function updateUserPassword(data: PasswordData) {
  return request({
    url: "/system/user/updatePassword",
    method: "post",
    data,
  });
}

/**
 * 删除用户
 *
 * @param ids
 */
export function deleteUsers(data: string) {
  return request({
    url: "/system/user/delete",
    method: "delete",
    data,
  });
}
