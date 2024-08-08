/**
 * 分页对象
 */
export interface CategoryPageVO {
  /**
   * 名称
   */
  name?: string;
  /**
   * 文章量
   */
  articleCount?: Number;
  /**
   * 排序
   */
  sort?: string;
  /**
   * 创建时间
   */
  createTime?: Date;
  
}

/**
 * 查询对象类型
 */
export interface CategoryQuery extends PageQuery {
  name?: string;
}


/**
 * 表单请求参数
 */
export interface CategoryForm {
  /**
   * id
   */
  id:string;
  /**
   * 名称
   */
  name: string;
  /**
   * 图标
   */
  icon:string;
  /**
   * 排序
   */
  sort: Number;
}