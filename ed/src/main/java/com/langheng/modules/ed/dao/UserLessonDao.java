/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.ed.entity.Lesson;
import com.langheng.modules.ed.entity.UserLesson;
import com.langheng.modules.ed.entity.UserTaskDto;

import java.util.List;

/**
 * 学生小节DAO接口
 * @author xiaoxie
 * @version 2019-12-17
 */
@MyBatisDao(dataSourceName="ds2")
public interface UserLessonDao extends CrudDao<UserLesson> {

    List<Lesson> findMyPendingLessonList(UserTaskDto userTaskDto);

    List<Lesson> findMyPendingSubLessonList(UserTaskDto userTaskDto);

    List<Lesson> findMyFinishingLessonList(UserTaskDto userTaskDto);

    List<Lesson> findMyFinishLessonList(UserTaskDto userTaskDto);

    List<Lesson> findMyFinishSubLessonList(UserTaskDto userTaskDto);

    Long checkIsFinishAllSubLesson(UserTaskDto userTaskDto);
}