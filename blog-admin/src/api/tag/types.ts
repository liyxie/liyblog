/**
 * 分页对象
 */
export interface TagPageVO {
  /**
   * 标签名
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
export interface TagQuery extends PageQuery {
  name?: string;
}


/**
 * 表单请求参数
 */
export interface TagForm {
  /**
   * id
   */
  id:string;
  /**
   * 标签名
   */
  name: string;
  /**
   * 排序
   */
  sort: Number;
}