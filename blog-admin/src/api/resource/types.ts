/**
 * 分页对象
 */
export interface ResourcePageVO {
  /**
   * id
   */
  id?: Number;
  /**
   * 文件地址
   */
  url?: string;
  /**
   * 文件类型
   */
  type?: string;
  /**
   * 存储平台
   */
  platform?: string;
  /**
   * 创建时间
   */
  createTime?: Date;
}
