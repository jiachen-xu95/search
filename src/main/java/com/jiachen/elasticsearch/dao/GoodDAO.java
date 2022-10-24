package com.jiachen.elasticsearch.dao;

import com.jiachen.elasticsearch.dao.object.GoodDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodDAO {

    List<GoodDO> query(GoodDO goodDO);

    Integer count();
}
