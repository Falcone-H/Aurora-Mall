package com.besscroft.aurora.mall.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.besscroft.aurora.mall.common.entity.UserInfo;

import java.util.List;

public interface MallUserMapper extends BaseMapper<UserInfo> {

    /**
     * 查询所有用户
     *
     * @return
     */
    List<UserInfo> selectUserList();

}
