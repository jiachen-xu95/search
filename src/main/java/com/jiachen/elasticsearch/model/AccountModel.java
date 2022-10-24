package com.jiachen.elasticsearch.model;

import lombok.Data;

import java.util.Date;

@Data
public class AccountModel {

    private String id;

    private String name;

    private Integer weight;

    private Date createTime;
}
