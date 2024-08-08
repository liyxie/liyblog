

/**
 * 分页对象
 */
export interface SayPageVO {
  /**
   * 用户id
   */
  userId?: string;
  /**
   * 创建时间
   */
  createTime?: Date;
  /**
   * 关联地址
   */
  address?: string;
  /**
   * 内容
   */
  content?: string;
  /**
   * 是否公开
   */
  isPublic?: Number;
 
}

/**
 * 表单类型
 */
export interface SayForm {
  /**
   * id
   */
    id:string;
    /**
   * 关联地址
   */
    address?: string;
    /**
     * 内容
     */
    content?: string;
    /**
     * 是否公开
     */
    isPublic?: Number;
}

