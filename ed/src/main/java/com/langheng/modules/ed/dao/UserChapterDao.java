/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.ed.entity.Chapter;
import com.langheng.modules.ed.entity.UserChapter;
import com.langheng.modules.ed.entity.UserTaskDto;

import java.util.List;

/**
 * 学生章节DAO接口
 * @author xiaoxie
 * @version 2019-12-17
 */
@MyBatisDao(dataSourceName="ds2")
public interface UserChapterDao extends CrudDao<UserChapter> {

    List<Chapter> findMyPendingChapterList(UserTaskDto userTaskDto);

    List<Chapter> findMyFinishChapterList(UserTaskDto userTaskDto);

    List<Chapter> findMyFinishingChapterList(UserTaskDto userTaskDto);

    Long checkIfFinishAllLesson(UserTaskDto userTaskDto);
}