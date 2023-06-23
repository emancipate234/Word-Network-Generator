package com.service.impl;

import com.entity.WordNetwork;
import com.mapper.WordNetworkMapper;
import com.service.WordNetworkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 单词网络表 服务实现类
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
@Service
public class WordNetworkServiceImpl extends ServiceImpl<WordNetworkMapper, WordNetwork> implements WordNetworkService {

    @Override
    public List<WordNetwork> getByAllNetwordByUserId(Long wordNetworkId, Long userId) {
        return baseMapper.getByAllNetwordByUserId(wordNetworkId, userId);
    }
}
