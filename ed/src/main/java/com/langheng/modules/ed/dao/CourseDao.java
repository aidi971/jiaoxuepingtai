/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.ed.entity.Course;

/**
 * 课程DAO接口
 * @author xiaoxie
 * @version 2019-12-17
 */
@MyBatisDao(dataSourceName="ds2")
public interface CourseDao extends CrudDao<Course> {

    void updateLessonTaskNum(String courseId,String type);
	
}