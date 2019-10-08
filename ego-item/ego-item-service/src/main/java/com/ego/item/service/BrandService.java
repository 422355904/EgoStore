package com.ego.item.service;

import com.ego.common.pojo.PageResult;
import com.ego.item.mapper.BrandMapper;
import com.ego.item.pojo.Brand;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * @Author TheKing
 * @Date 2019/9/24 22:44
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> getBrandByPage(Integer pageNo, Integer pageSize, String sortBy, Boolean descending, String key) {
        //分页
        PageHelper.startPage(pageNo, pageSize);
        //关键字
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name", "%" + key + "%");
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (descending ? "desc" : "asc"));
        }
        Page<Brand> page = (Page<Brand>) brandMapper.selectByExample(example);
        return new PageResult<>(page.getTotal(), page);
    }

    @Transactional
    public void saveBrand(Brand brand, Long[] cids) {
        //新增品牌
        brandMapper.insert(brand);
        //新增中间表，代码维护级联
        if (null != cids) {
            for (Long cid : cids) {
                brandMapper.saveBrandCategory(cid, brand.getId());
            }
        }
    }

    @Transactional
    public void updateBrand(Brand brand, Long[] cids) {
        /*Example example = new Example(Brand.class);
        example.createCriteria().andEqualTo("id", brand.getId());*/
        brandMapper.updateByPrimaryKeySelective(brand);
        if (null != cids) {
            //先删
            for (Long cid : cids) {
                brandMapper.deleteBrandCategory(brand.getId());
            }
            //再增
            for (Long cid : cids) {
                brandMapper.saveBrandCategory(cid, brand.getId());
            }
        }
    }

    @Transactional
    public void deleteBrand(Long bid) {
        brandMapper.deleteByPrimaryKey(bid);
        brandMapper.deleteBrandCategory(bid);
    }

    public List<Brand> findListByCid(Long cid) {
        return  brandMapper.getGoodsBrandByCid(cid);
    }

    public List<Brand> findListByIds(List<Long> ids) {
        return brandMapper.selectByIdList(ids);
    }
}
