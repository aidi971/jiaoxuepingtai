/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.ed.entity.TeachClassLesson;

/**
 * 课堂节或者'小节'管理DAO接口
 * @author xiaoxie
 * @version 2020-04-21
 */
@MyBatisDao(dataSourceName="ds2")
public interface TeachClassLessonDao extends CrudDao<TeachClassLesson> {
	
}