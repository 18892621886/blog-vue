package com.naown.common.service;

import com.naown.common.entity.Backlog;

import java.util.List;

/**
 * @author chenjian
 * @since 2021-03-20
 */
public interface BacklogService {

    /**
     * 获取待办事件的所有记录
     * @return
     */
    List<Backlog> listBacklogs();

    /**
     * 更新BackLog
     * @param backlog
     * @return
     */
    Integer updateBackLogById(Backlog backlog);

}
