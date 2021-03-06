package com.leyou.admin.mapper;

import com.leyou.admin.pojo.Admin;
import com.leyou.admin.pojo.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/11/19 08:43
 * @Description:
 */
public interface AdminMapper extends Mapper<Admin> {
    @Select("select tb_role.role_id as roleId,tb_role.role_name as roleName from tb_role, tb_admin_role where tb_role.role_id = tb_admin_role.role_id and tb_admin_role.admin_id = #{id}")
    List<Role> queryRolesByAdminId(@Param("id") Long id);

    @Delete("delete from tb_admin_role where admin_id = #{adminId}")
    void deleteAdminRoleByAdminId(@Param("adminId") Long adminId);

    @Insert("insert into tb_admin_role(admin_id,role_id) values(#{adminId}, #{roleId})")
    int insertAdminRole(@Param("adminId") Long adminId, @Param("roleId") Long roleId);
}
