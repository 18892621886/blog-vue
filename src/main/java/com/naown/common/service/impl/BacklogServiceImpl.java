package com.naown.common.service.impl;

import com.naown.common.entity.Backlog;
import com.naown.common.mapper.BacklogMapper;
import com.naown.common.service.BacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenjian
 * @since 2021-03-20
 */
@Service
public class BacklogServiceImpl implements BacklogService {

    @Autowired
    private BacklogMapper backlogMapper;

    /**
     * 获取待办事件的所有记录
     * @return
     */
    @Override
    public List<Backlog> listBacklogs() {
        return backlogMapper.selectList(null);
    }

    /**
     * 更新BackLog
     * @param backlog
     * @return
     */
    @Override
    public Integer updateBackLogById(Backlog backlog) {
        return backlogMapper.updateById(backlog);
    }
}
