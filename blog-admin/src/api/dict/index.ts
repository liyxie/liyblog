import request from "@/utils/request";
import { AxiosPromise } from "axios";


/**
 * 字典类型分页列表
 *
 * @param queryParams
 */
export function getDictTypePage(
  queryParams: any
): AxiosPromise<any> {
  return request({
    url: "/system/dict/list",
    method: "get",
    params: queryParams,
  });
}

/**
 * 字典类型表单数据
 *
 * @param id
 */
export function getDictTypeForm(id: number): AxiosPromise<any> {
  return request({
    url: "/api/v1/dict/types/" + id + "/form",
    method: "get",
  });
}

/**
 * 新增字典类型
 *
 * @param data
 */
export function addDictType(data: any) {
  return request({
    url: "/system/dict/add",
    method: "post",
    data: data,
  });
}

/**
 * 修改字典类型
 *
 * @param id
 * @param data
 */
export function updateDictType(data: any) {
  return request({
    url: "/system/dict/update",
    method: "put",
    data
  });
}

/**
 * 删除字典类型
 */
export function deleteDictTypes(data: any) {
  return request({
    url: "/system/dict/delete",
    method: "delete",
    data
  });
}

/**
 * 字典分页列表
 */
export function getDictDataPage(
  queryParams: any
): AxiosPromise<any> {
  return request({
    url: "/system/dictData/list",
    method: "get",
    params: queryParams,
  });
}

/**
 * 新增字典
 *
 * @param data
 */
export function addDict(data: any) {
  return request({
    url: "/system/dictData/add",
    method: "post",
    data: data,
  });
}

/**
 * 修改字典项
 *
 * @param data
 */
export function updateDict( data: any) {
  return request({
    url: "/system/dictData/update",
    method: "put",
    data: data,
  });
}

/**
 * 删除字典
 *
 */
export function deleteDict(data: any) {
  return request({
    url: "/system/dictData/deleteBatch",
    method: "delete",
    data
  });
}


/**
 * 获取字典类型的数据项
 *
 * @param data
 */
export function getDataByDictType(data:any){
  return request({
    url: "/system/dictData/getDataByDictType",
    method: "post",
    data
  });
}