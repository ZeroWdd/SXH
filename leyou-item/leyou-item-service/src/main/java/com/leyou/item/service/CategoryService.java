package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname CategoryService
 * @Description None
 * @Date 2019/8/3 12:33
 * @Created by WDD
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据parentId查询子类目
     * @param pid
     * @return
     */
    public List<Category> queryCategoriesByPid(Long pid) {
        Category record = new Category();
        record.setParentId(pid);
        List<Category> list = categoryMapper.select(record);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }

    public List<String> queryNamesByIds(List<Long> ids) {
        List<Category> list = categoryMapper.selectByIdList(ids);
        List<String> names = new ArrayList<>();
        for (Category category : list) {
            names.add(category.getName());
        }
        return names;
    }

    public void deleteCategory(Long id) {
        //查询是否有子节点
        Category record = new Category();
        record.setParentId(id);
        List<Category> categories = categoryMapper.select(record);
        if(!CollectionUtils.isEmpty(categories)){
            throw new LyException(ExceptionEnum.CATEGORY_CHILD_NODE);
        }
        //没有则删除
        categoryMapper.deleteByPrimaryKey(id);
    }

    public void addCategory(Category category) {
        category.setId(null);
        categoryMapper.insertSelective(category);
    }

    public void updateCategory(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    public List<Category> queryAllByCid3(Long id) {
        Category c3 = this.categoryMapper.selectByPrimaryKey(id);
        Category c2 = this.categoryMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.categoryMapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1,c2,c3);
    }
}
