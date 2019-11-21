package com.leyou.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.CaseFormat;
import com.leyou.admin.mapper.PermissionMapper;
import com.leyou.admin.pojo.Permission;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/11/21 15:57
 * @Description:
 */
@Service
public class PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    public PageResult<Permission> queryPermissionsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        Example example = new Example(Permission.class);
        Example.Criteria criteria = example.createCriteria();

        // 根据name模糊查询，或者根据电话模糊查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("permissionName", "%" + key + "%");
        }

        // 添加分页条件
        PageHelper.startPage(page, rows);

        // 添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            sortBy = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortBy); //驼峰装换
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));

        }

        List<Permission> permissions = permissionMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(permissions)){
            throw new LyException(ExceptionEnum.PERMISSION_NOT_FOUND);
        }
        // 包装成pageInfo
        PageInfo<Permission> pageInfo = new PageInfo<>(permissions);
        // 包装成分页结果集返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    public void addPermission(Permission permission) {
        int count = permissionMapper.insertSelective(permission);
        if(count != 1){
            throw new LyException(ExceptionEnum.PERMISSION_SAVE_ERROR);
        }
    }

    public void deletePermission(Long id) {
        int count = permissionMapper.deleteByPrimaryKey(id);
        if(count != 1){
            throw new LyException(ExceptionEnum.PERMISSION_DELETE_ERROR);
        }
    }

    public void updatePermission(Permission permission) {
        int count = permissionMapper.updateByPrimaryKeySelective(permission);
        if(count != 1){
            throw new LyException(ExceptionEnum.PERMISSION_UPDATE_ERROR);
        }
    }

    public Permission selectPermission(Long id) {
        Permission permission = permissionMapper.selectByPrimaryKey(id);
        if(org.springframework.util.StringUtils.isEmpty(permission)){
            throw new LyException(ExceptionEnum.PERMISSION_NOT_FOUND);
        }
        return permission;
    }

    public List<Permission> selectPermissionList() {
        List<Permission> permissions = permissionMapper.selectAll();
        if(CollectionUtils.isEmpty(permissions)){
            throw new LyException(ExceptionEnum.PERMISSION_NOT_FOUND);
        }
        return permissions;
    }
}
