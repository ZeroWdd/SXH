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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wdd
 * @Date: 2019/11/19 16:03
 * @Description:
 */
@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    static final String KEY_PREFIX = "leyou:admin:id:";

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

    @Transactional
    public void deleteRole(Long id) {
        // 先删除角色所拥有的权限(中间表tb_role_permission)
        roleMapper.deleteRolePermissionByRoleId(id);
        // 再删除管理员所拥有的角色
        roleMapper.deleteAdminRoleByRoleId(id);
        // 再删除角色
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
        //重新给角色分配权限
        int count = 0;
        for(Permission permission : role.getPermissions()) {
            count = roleMapper.insertRolePermission(role.getRoleId(),permission.getPermissionId());
            if(count != 1){
                throw new LyException(ExceptionEnum.ADMIN_NOT_FOUND);
            }
        }
    }

    /**
     * 获取该角色拥有的uri，由于频繁读取，放入redis缓存
     * @param adminId
     * @return
     */
    public List<String> queryUri(Long adminId) {
        List<String> uris = new ArrayList<>();
        // 首先看看redis里有没有缓存
        uris = (List<String>) redisTemplate.opsForValue().get(KEY_PREFIX + adminId);
        if(CollectionUtils.isEmpty(uris)){
            // 没有，则数据库查询
            uris = roleMapper.queryUriByAdminId(adminId);
            if(CollectionUtils.isEmpty(uris)){
                throw new LyException(ExceptionEnum.PERMISSION_NOT_FOUND);
            }
            // 并放入缓存,设置过期时间300秒
            redisTemplate.opsForValue().set(KEY_PREFIX + adminId,uris,300, TimeUnit.SECONDS);
        }
        return uris;
    }
}
