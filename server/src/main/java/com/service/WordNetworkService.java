package com.service;

import com.entity.WordNetwork;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 单词网络表 服务类
 * </p>
 *
 * @author
 * @since 2023-04-21
 */
public interface WordNetworkService extends IService<WordNetwork> {

    List<WordNetwork> getByAllNetwordByUserId(Long wordNetworkId, Long id);
}
