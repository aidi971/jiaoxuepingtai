/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.base.entity.Classes;

import java.util.List;

/**
 * 班级DAO接口
 * @author xiaoxie
 * @version 2019-12-18
 */
@MyBatisDao(dataSourceName="ds2")
public interface ClassesDao extends CrudDao<Classes> {

    List<String> findAcademyIdsOrderByAcademyId(Classes classes);

    void updateStuOnlineNum(Classes classes);

}