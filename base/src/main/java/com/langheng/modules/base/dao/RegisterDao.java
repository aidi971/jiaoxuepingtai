/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.base.entity.Register;

import java.util.List;

/**
 * 注册管理DAO接口
 * @author xiaoxie
 * @version 2020-07-29
 */
@MyBatisDao(dataSourceName="ds2")
public interface RegisterDao extends CrudDao<Register> {

    List<Register> findInUseRegisterList(Register register);
}