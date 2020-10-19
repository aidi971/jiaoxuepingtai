/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.ed.entity.LessonTask;
import com.langheng.modules.ed.entity.UserTaskDto;

import java.util.List;

/**
 * 小节任务（文件）DAO接口
 * @author xiaoxie
 * @version 2019-12-17
 */
@MyBatisDao(dataSourceName="ds2")
public interface LessonTaskDao extends CrudDao<LessonTask> {

    List<LessonTask> findListHadPush(LessonTask lessonTask);

    Long findCountHadPush(LessonTask lessonTask);

    List<LessonTask> findListStuFinishDetail(LessonTask lessonTask);

    Float getTotalObjectiveDefaultScore(UserTaskDto userTaskDto);

    Float getTotalObjectiveReSetScore(UserTaskDto userTaskDto);
}