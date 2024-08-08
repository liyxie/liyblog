import request from "@/utils/request";
import { AxiosPromise } from "axios";
import { CategoryPageVO, CategoryForm, CategoryQuery} from "./types";

/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getCategoryPage(
  queryParams?: CategoryQuery
): AxiosPromise<CategoryPageVO> {
  return request({
    url: "/system/category/list",
    method: "get",
    params: queryParams,
  });
}


/**
 * 修改
 *
 * @param data
 */
export function updateCategory(
  data?: CategoryForm
): AxiosPromise<any> {
  return request({
    url: "/system/category/update",
    method: "put",
    data
  });
}

/**
 * 添加
 *
 * @param data
 */
export function addCategory(
  data?: CategoryForm
): AxiosPromise<any> {
  return request({
    url: "/system/category/add",
    method: "post",
    data
  });
}

/**
 * 删除标签
 *
 * @param data
 */
export function deleteCategory(
  data?: Number[]
): AxiosPromise<any> {
  return request({
    url: "/system/category/delete",
    method: "delete",
    data
  });
}
