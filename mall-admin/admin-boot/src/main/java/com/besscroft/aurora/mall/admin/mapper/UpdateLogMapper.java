package com.besscroft.aurora.mall.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.besscroft.aurora.mall.common.entity.UpdateLog;

import java.util.List;

/**
 * 日志持久化
 */
public interface UpdateLogMapper extends BaseMapper<UpdateLog> {

    /**
     * 查询版本日志列表
     *
     * @return 版本日志列表
     */
    List<UpdateLog> selectAllList();

}
