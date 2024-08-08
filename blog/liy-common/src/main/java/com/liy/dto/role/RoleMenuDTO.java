package com.liy.dto.role;

import lombok.Data;

import java.util.List;

@Data
public class RoleMenuDTO {

    private Integer roleId;

    private List<Integer> menuIds;
}
