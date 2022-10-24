package com.jiachen.elasticsearch.dao.object;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoodDO {

    private Long id;

    private String title;

    private BigDecimal price;

    private Double stock;

    private Double saleNum;

    private Date createTime;

    private String categoryName;

    private String brandName;

    private String spec;

    private Integer status;

    private Integer limit;
}
