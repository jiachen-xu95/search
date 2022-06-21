package com.jiachen.elasticsearch.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Version 1.0
 * @ClassName UserModel
 * @Author jiachenXu
 * @Date 2020/4/12
 * @Description
 */
@Data
public class UserModel implements Serializable {

    private static final long serialVersionUID = 3669797251638034839L;

    private String name;

    private String age;

    private String salary;

    private String address;

    private String remark;

    private Date createDate;

    private String birthDate;
}
