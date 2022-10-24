package com.jiachen.elasticsearch.service;

import com.jiachen.elasticsearch.dao.object.GoodDO;

import java.io.IOException;
import java.util.List;

public interface GoodService {

    List<GoodDO> searchName(String name) throws IOException;

    void init();
}
