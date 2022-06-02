package com.besscroft.aurora.mall.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.besscroft.aurora.mall.common.entity.ProductSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductSkuMapper extends BaseMapper<ProductSku> {

    /**
     * 根据条件查询所有商品
     *
     * @param keyword 关键字
     * @return 分页商品列表
     */
    List<ProductSku> selectProductSkuListByPage(@Param("productId") String productId,
                                                @Param("keyword") String keyword);

}
