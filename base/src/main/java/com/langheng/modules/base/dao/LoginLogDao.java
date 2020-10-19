/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.base.entity.LoginLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统访问记录DAO接口
 * @author xiaoxie
 * @version 2020-02-17
 */
@MyBatisDao(dataSourceName="ds2")
public interface LoginLogDao extends CrudDao<LoginLog> {

    void insertLog(LoginLog loginLog);

    List<LoginLog> findPageJoinClasses(LoginLog loginLog);

    List<Map<String, Object>> everyMouthStat();

    List<Map<String, Object>> everyMouthStatByClasses(@Param("classesId") String classesId);

    List<Map<String, Object>> everyMouthStatByUser(@Param("userId") String userId);
}