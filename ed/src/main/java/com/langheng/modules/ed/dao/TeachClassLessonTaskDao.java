/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.ed.entity.TeachClassLessonTask;

/**
 * 课堂节任务管理DAO接口
 * @author xiaoxie
 * @version 2020-04-21
 */
@MyBatisDao(dataSourceName="ds2")
public interface TeachClassLessonTaskDao extends CrudDao<TeachClassLessonTask> {
	
}