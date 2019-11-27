package com.leyou.admin.mapper;

import com.leyou.admin.pojo.Permission;
import com.leyou.admin.pojo.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/11/19 16:04
 * @Description:
 */
public interface RoleMapper extends Mapper<Role> {
    @Select("select tb_permission.permission_id as permissionId,tb_permission.permission_name as permissionName,tb_permission.url from tb_permission, tb_role_permission where tb_permission.permission_id = tb_role_permission.permission_id and tb_role_permission.role_id = #{id}")
    List<Permission> queryPermissionsByRoleId(@Param("id") Long id);

    @Delete("delete from tb_role_permission where role_id = #{roleId}")
    void deleteRolePermissionByRoleId(@Param("roleId") Long roleId);

    @Insert("insert into tb_role_permission(role_id,permission_id) values(#{roleId},#{permissionId})")
    int insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    @Delete("delete from tb_admin_role where role_id = #{roleId}")
    void deleteAdminRoleByRoleId(Long id);

    @Select("select url " +
            "from tb_admin,tb_admin_role,tb_role,tb_role_permission,tb_permission " +
            "where tb_admin.admin_id = tb_admin_role.admin_id " +
            "and tb_admin_role.role_id = tb_role.role_id " +
            "and tb_role.role_id = tb_role_permission.role_id " +
            "and tb_role_permission.permission_id = tb_permission.permission_id " +
            "and tb_admin.admin_id = #{adminId}")
    List<String> queryUriByAdminId(@Param("adminId") Long adminId);
}
