package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Classname BrandService
 * @Description None
 * @Date 2019/8/4 16:13
 * @Created by WDD
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        // 初始化example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        // 根据name模糊查询，或者根据首字母查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key.toUpperCase());
        }

        // 添加分页条件
        PageHelper.startPage(page, rows);

        // 添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }

        List<Brand> brands = this.brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        // 包装成pageInfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        // 包装成分页结果集返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if(count != 1){
            throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
        //新增中间表
        for(Long cid : cids){
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if(count != 1){
                throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
            }
        }
    }

    public List<Brand> queryBrandsByCid(Long cid) {
        List<Brand> brands = brandMapper.selectBrandByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }

    public Brand queryBrandById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if(org.springframework.util.StringUtils.isEmpty(brand)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    @Transactional
    public void updateBrand(Brand brand, List<Long> cids) {
        int count = brandMapper.updateByPrimaryKey(brand);
        if(count != 1){
            throw new LyException(ExceptionEnum.BRAND_UPDATE_ERROR);
        }
        //修改中间表
        //先删除中间表字段
        brandMapper.deleteCategoryBrandByBid(brand.getId());
        for(Long cid : cids){
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if(count != 1){
                throw new LyException(ExceptionEnum.BRAND_UPDATE_ERROR);
            }
        }
    }

    @Transactional
    public void deleteBrand(Long bid) {
        //先删除中间表
        brandMapper.deleteCategoryBrandByBid(bid);
        //删除品牌表
        Brand brand = new Brand();
        brand.setId(bid);
        brandMapper.delete(brand);

    }
}
