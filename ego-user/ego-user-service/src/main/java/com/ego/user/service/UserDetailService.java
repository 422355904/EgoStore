package com.ego.user.service;

import com.ego.user.mapper.AddrsMapper;
import com.ego.user.pojo.Addrs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/10/16 17:58
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Service
public class UserDetailService {

    @Autowired
    private AddrsMapper addrsMapper;

    @Transactional
    public void createAddrs(Addrs addrs) {
        addrsMapper.insert(addrs);
    }

    public List<Addrs> queryAddrs() {
        return addrsMapper.selectAll();
    }

    @Transactional
    public void deleteAddrs(Integer id) {
        addrsMapper.deleteByPrimaryKey(id);
    }
}
