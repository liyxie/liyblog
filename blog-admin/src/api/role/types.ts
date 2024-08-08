/**
 * 角色查询参数
 */
export interface RoleQuery extends PageQuery {
  name?: string;
}

/**
 * 角色分页对象
 */
export interface RolePageVO {
  /**
   * 角色编码
   */
  code?: string;

  /**
   * 角色ID
   */
  id?: number;
  /**
   * 角色名称
   */
  name?: string;

  /**
  * 备注
  */
  remarks?: string;
  /**
   * 创建时间
   */
  createTime?: Date;
  /**
   * 修改时间
   */
  updateTime?: Date;
}

/**
 * 角色分页
 */
export type RolePageResult = PageResult<RolePageVO[]>;

/**
 * 角色表单对象
 */
export interface RoleForm {
  /**
   * 角色ID
   */
  id?: number;

  /**
   * 角色编码
   */
  code: string;

  /**
   * 角色名称
   */
  name: string;
  /**
   * 备注
   */
  remarks?: string;

}
