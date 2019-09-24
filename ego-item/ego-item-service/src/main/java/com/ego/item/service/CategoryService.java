package com.ego.item.service;

import com.ego.item.pojo.Category;
import com.ego.item.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/24 15:31
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> findCategoryByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    @Transactional
    public void saveCategory(Category category) {
        //判断是否为父节点
        Category parentCategory = categoryMapper.selectByPrimaryKey(category.getParentId());
        if (null!=parentCategory) {
            if (parentCategory.getIsParent() != 1) {
                parentCategory.setIsParent(1L);
                categoryMapper.updateByPrimaryKeySelective(parentCategory);
            }
        }
        //保存新增种类
        categoryMapper.insertSelective(category);
    }

    @Transactional
    public void updateCategoryById(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        //是否为父节点
        if (category.getIsParent()==1){//是
            List<Category> categoryList = findCategoryByPid(id);
            for (Category cg : categoryList) {
                deleteCategoryById(cg.getId());
            }
        }
        categoryMapper.delete(category);
    }
}
