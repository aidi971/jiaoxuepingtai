/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.ed.entity.Lesson;

import java.util.List;
import java.util.Map;

/**
 * 课程小节DAO接口
 * @author xiaoxie
 * @version 2019-12-17
 */
@MyBatisDao(dataSourceName="ds2")
public interface LessonDao extends CrudDao<Lesson> {

    List<Map> findAllSysFile();

    List<Map> findAllTestGather();

    List<Lesson> findListHadPush(Lesson lesson);
}