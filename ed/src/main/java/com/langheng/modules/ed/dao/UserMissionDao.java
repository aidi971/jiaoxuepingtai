/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.ed.entity.UserMission;

/**
 * 学生课程任务项DAO接口
 * @author xiaoxie
 * @version 2020-08-11
 */
@MyBatisDao
public interface UserMissionDao extends CrudDao<UserMission> {
	
}