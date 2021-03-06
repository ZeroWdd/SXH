package com.leyou.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.CaseFormat;
import com.leyou.admin.mapper.AdminMapper;
import com.leyou.admin.pojo.Admin;
import com.leyou.admin.pojo.Role;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.PageResult;
import com.leyou.common.util.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/11/19 08:43
 * @Description:
 */
@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public Admin queryAdmin(String name, String password) {
        //查询
        Admin admin = new Admin();
        admin.setName(name);
        Admin one = adminMapper.selectOne(admin);
        //校验用户名和校验密码
        if(one == null || !one.getPassword().equals(CodecUtils.md5Hex(password,one.getSalt()))){
            throw new LyException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return one;
    }

    public void addAdmin(Admin admin) {
        // 生成盐
        String salt = CodecUtils.generateSalt();
        admin.setSalt(salt);
        //设置管理员默认密码
        admin.setPassword("admin");
        // 对密码加密
        admin.setPassword(CodecUtils.md5Hex(admin.getPassword(), salt));

        int count = adminMapper.insertSelective(admin);
        if(count != 1){
            throw new LyException(ExceptionEnum.ADMIN_SAVE_ERROR);
        }

    }

    @Transactional
    public void deleteAdmin(Long id) {
        // 先删除管理员所拥有的角色(中间表)
        adminMapper.deleteAdminRoleByAdminId(id);
        // 再删除管理员
        int count = adminMapper.deleteByPrimaryKey(id);
        if(count != 1){
            throw new LyException(ExceptionEnum.ADMIN_DELETE_ERROR);
        }
    }

    public void updateAdmin(Admin admin) {
        int count = adminMapper.updateByPrimaryKeySelective(admin);
        if(count != 1){
            throw new LyException(ExceptionEnum.ADMIN_UPDATE_ERROR);
        }
    }

    public PageResult<Admin> queryAdminsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();

        // 根据name模糊查询，或者根据电话模糊查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orLike("phone","%" + key + "%")
                    .orLike("email","%" + key + "%");
        }

        // 添加分页条件
        PageHelper.startPage(page, rows);

        // 添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            sortBy = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortBy);
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));

        }

        List<Admin> admins = adminMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(admins)){
            throw new LyException(ExceptionEnum.ADMIN_NOT_FOUND);
        }
        // 包装成pageInfo
        PageInfo<Admin> pageInfo = new PageInfo<>(admins);
        // 包装成分页结果集返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    public Admin selectAdmin(Long id) {
        Admin admin = adminMapper.selectByPrimaryKey(id);
        //根据AdminId查询管理员角色中间表 即该管理员拥有的角色
        List<Role> roles = adminMapper.queryRolesByAdminId(id);
        if(org.springframework.util.StringUtils.isEmpty(admin)){
            throw new LyException(ExceptionEnum.ADMIN_NOT_FOUND);
        }
        admin.setRoles(roles);
        return admin;
    }

    @Transactional
    public void dealAdminRole(Admin admin) {
        //先将原有的分配角色删除
        adminMapper.deleteAdminRoleByAdminId(admin.getAdminId());
        //重新分配角色
        int count = 0;
        for(Role role : admin.getRoles()) {
            count = adminMapper.insertAdminRole(admin.getAdminId(),role.getRoleId());
            if(count != 1){
                throw new LyException(ExceptionEnum.ADMIN_NOT_FOUND);
            }
        }

    }
}
