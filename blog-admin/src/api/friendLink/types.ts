/**
 * 分页对象
 */
export interface FriendLinkPageVO {
  /**
   * 头像
   */
  avatar?: string;
  /**
   * 网站名
   */
  name?: string;
  /**
   * 网站描述
   */
  info?: string;
  /**
   * 网站地址
   */
  url?: string;
  /**
   * 状态
   */
  status?: Number;
  /**
   * 排序
   */
  sort?: Number;
  /**
   * 创建时间
   */
  createTime?: Date;
  
}

/**
 * 查询对象类型
 */
export interface FriendLinkQuery extends PageQuery {
  name?: string;
  status?:Number;
}


/**
 * 表单请求参数
 */
export interface FriendLinkForm {
  /**
   * id
   */
  id:string;
  /**
     * 头像
     */
  avatar?: string;
  /**
   * 网站名
   */
  name?: string;
  /**
   * 网站描述
   */
  info?: string;
  /**
   * 网站地址
   */
  url?: string;
  /**
   * 状态
   */
  status?: Number;
  /**
   * 排序
   */
  sort?: Number;

  /**
   * 站长邮箱
   */
  email?: Number;

  /**
   * 下架原因
   */
  reason?:string;
}