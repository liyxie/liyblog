import request from "@/utils/request";
import { AxiosPromise } from "axios";
import { TagForm, TagPageVO, TagQuery} from "./types";

/**
 * 获取标签分页数据
 *
 * @param queryParams
 */
export function getTagPage(
  queryParams?: TagQuery
): AxiosPromise<TagPageVO> {
  return request({
    url: "/system/tags/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 修改标签
 *
 * @param data
 */
export function updateTag(
  data?: TagForm
): AxiosPromise<any> {
  return request({
    url: "/system/tags/update",
    method: "put",
    data
  });
}

/**
 * 添加标签
 *
 * @param data
 */
export function addTag(
  data?: TagForm
): AxiosPromise<any> {
  return request({
    url: "/system/tags/add",
    method: "post",
    data
  });
}

/**
 * 删除标签
 *
 * @param data
 */
export function deleteTag(
  data?: Number[]
): AxiosPromise<any> {
  return request({
    url: "/system/tags/delete",
    method: "delete",
    data
  });
}

/**
 * 置顶标签
 *
 * @param data
 */
export function topTag(
  tagId?: Number
): AxiosPromise<any> {
  return request({
    url: "/system/tags/top",
    method: "get",
    params:{
      id:tagId
    }
  });
}