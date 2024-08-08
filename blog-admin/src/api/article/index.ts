import request from "@/utils/request";

/**
 * 获取分页数据
 *
 * @param queryParams
 */
export function getArticlePage(queryParams?: any) {
  return request({
    url: "/system/article/list",
    method: "get",
    params: queryParams,
  });
}

/**
 * 获取详情
 *
 */
export function getArticleInfo(id?: any) {
  return request({
    url: "/system/article/info/" + id,
    method: "get",
  });
}

/**
 * 删除
 *
 * @param data
 */
export function deleteArticle(data: any) {
  return request({
    url: "/system/article/delete",
    method: "delete",
    data,
  });
}

/**
 * 添加
 *
 * @param data
 */
export function addArticle(data: any) {
  return request({
    url: "/system/article/add",
    method: "post",
    data,
  });
}

/**
 * 修改
 *
 * @param data
 */
export function updateArticle(data: any) {
  return request({
    url: "/system/article/update",
    method: "put",
    data,
  });
}

/**
 * 随机一张封面图
 *
 */
export function randomAvatar() {
  return request({
    url: "/system/article/randomImg",
    method: "get",
  });
}

/**
 * 置顶文章
 *
 */
export function topArticle(data: any) {
  return request({
    url: "/system/article/top",
    method: "put",
    data,
  });
}

/**
 * 发布或下架
 *
 */
export function toggleArticlePublication(data: any) {
  return request({
    url: "/system/article/toggleArticlePublication",
    method: "put",
    data,
  });
}

/**
 * SEO
 *
 */
export function seoArticle(data: any) {
  return request({
    url: "/system/article/seo",
    method: "post",
    data,
  });
}

/**
 * CSDN文章抓取
 *
 */
export function reptile(url: any) {
  return request({
    url: "/system/article/reptile",
    method: "get",
    params: {
      url: url,
    },
  });
}
