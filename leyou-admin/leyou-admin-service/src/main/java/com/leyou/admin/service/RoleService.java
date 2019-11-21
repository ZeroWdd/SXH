package com.leyou.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.CaseFormat;
import com.leyou.admin.mapper.RoleMapper;
import com.leyou.admin.pojo.Permission;
import com.leyou.admin.pojo.Role;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/11/19 16:03
 * @Description:
 */
@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public PageResult<Role> queryRolesByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();

        // 根据name模糊查询，或者根据电话模糊查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("roleName", "%" + key + "%");
        }

        // 添加分页条件
        PageHelper.startPage(page, rows);

        // 添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            sortBy = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortBy);
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));

        }

        List<Role> roles = roleMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(roles)){
            throw new LyException(ExceptionEnum.ROLE_NOT_FOUND);
        }
        // 包装成pageInfo
        PageInfo<Role> pageInfo = new PageInfo<>(roles);
        // 包装成分页结果集返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    public void addRole(Role role) {
        int count = roleMapper.insertSelective(role);
        if(count != 1){
            throw new LyException(ExceptionEnum.ROLE_SAVE_ERROR);
        }

    }

    public void deleteRole(Long id) {
        int count = roleMapper.deleteByPrimaryKey(id);
        if(count != 1){
            throw new LyException(ExceptionEnum.ROLE_DELETE_ERROR);
        }
    }

    public void updateRole(Role role) {
        int count = roleMapper.updateByPrimaryKeySelective(role);
        if(count != 1){
            throw new LyException(ExceptionEnum.ROLE_UPDATE_ERROR);
        }
    }

    public Role selectRole(Long id) {
        Role role = roleMapper.selectByPrimaryKey(id);
        List<Permission> permissions = roleMapper.queryPermissionsByRoleId(id);
        if(org.springframework.util.StringUtils.isEmpty(role)){
            throw new LyException(ExceptionEnum.ROLE_NOT_FOUND);
        }
        role.setPermissions(permissions);
        return role;
    }

    public List<Role> selectRoleList() {
        List<Role> roles = roleMapper.selectAll();
        if(CollectionUtils.isEmpty(roles)){
            throw new LyException(ExceptionEnum.ROLE_NOT_FOUND);
        }
        return roles;
    }

    @Transactional
    public void dealRolePermission(Role role) {
        //先将原有的分配权限删除
        roleMapper.deleteRolePermissionByRoleId(role.getRoleId());
        //重新分配角色
        int count = 0;
        for(Permission permission : role.getPermissions()) {
            count = roleMapper.insertRolePermission(role.getRoleId(),permission.getPermissionId());
            if(count != 1){
                throw new LyException(ExceptionEnum.ADMIN_NOT_FOUND);
            }
        }
    }
}
