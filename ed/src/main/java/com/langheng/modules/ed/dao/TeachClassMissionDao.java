/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.ed.entity.TeachClassMission;

/**
 * 课程任务关联DAO接口
 * @author xiaoxie
 * @version 2020-08-11
 */
@MyBatisDao
public interface TeachClassMissionDao extends CrudDao<TeachClassMission> {
	
}