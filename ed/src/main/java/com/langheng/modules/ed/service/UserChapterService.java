/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.ed.dao.UserChapterDao;
import com.langheng.modules.ed.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 学生章节Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class UserChapterService extends CrudService<UserChapterDao, UserChapter> {
	@Autowired
	private LessonService lessonService;
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserLessonService userLessonService;
	@Autowired
	private UserChapterService userChapterService;

    /**
	 * 获取单条数据
	 * @param userChapter
	 * @return
	 */
	@Override
	public UserChapter get(UserChapter userChapter) {
		return super.get(userChapter);
	}
	
	/**
	 * 查询分页数据
	 * @param userChapter 查询条件
	 * @return
	 */
	@Override
	public Page<UserChapter> findPage(UserChapter userChapter) {
		return super.findPage(userChapter);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param userChapter
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(UserChapter userChapter) {
		deleteByEntity(userChapter);
		insertBatch(userChapter);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param userChapter
	 */
	@Transactional(readOnly=false)
	public void insertBatch(UserChapter userChapter) {
		dao.insertBatch(ListUtils.newArrayList(userChapter));
	}


	/**
	 * 删除数据
	 * @param userChapter
	 */
	@Transactional(readOnly=false)
	public void deleteByEntity(UserChapter userChapter) {
		dao.deleteByEntity(userChapter);
	}

	/**
	 * 获取我正在进行中的章节
	 * @param teachingClassId
	 * @return
	 */
    public List<Chapter> findMyPendingChapterList(String teachingClassId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyPendingChapterList(userTaskDto);
    }

	public List<Chapter> findMyFinishChapterList(String teachingClassId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyFinishChapterList(userTaskDto);
	}

	public List<Chapter> findMyFinishingChapterList(String teachingClassId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyFinishingChapterList(userTaskDto);
	}

	public boolean checkIfFinishAllLesson(String teachingClassId, String chapterId){
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setChapterId(chapterId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.checkIfFinishAllLesson(userTaskDto) == 0 ;
	}

	public void checkAndSaveIfFinishAllLesson(String teachingClassId, String chapterId) {
    	if (checkIfFinishAllLesson(teachingClassId,chapterId)){
    		UserChapter userChapter = new UserChapter();
    		userChapter.setChapterId(chapterId);
    		userChapter.setUserId(BaseUserUtils.getUser().getId());
    		save(userChapter);
		}
	}

	public void flushUserTask(Chapter chapter, String teachingClassId) {
    	Lesson lessonCriteria = new Lesson();
		lessonCriteria.setChapter(chapter);
		lessonCriteria.setTeachingClassId(teachingClassId);
    	List<Lesson> lessonList = lessonService.findListHadPush(lessonCriteria);
    	boolean isFinishChapter = true;
    	for (Lesson lesson : lessonList){
			if (!userTaskService.checkIfFinishAllSubOrLesson(teachingClassId,lesson.getId())){
				isFinishChapter = false;

				// 清空小节  学生资源完成清空
				UserLesson userLesson = new UserLesson();
				userLesson.setLessonId(lesson.getId());
				userLessonService.deleteByEntity(userLesson);
			}
		}
    	if (!isFinishChapter){
    		// 清除章任务
			UserChapter userChapter = new UserChapter();
			userChapter.setChapterId(chapter.getChapterId());
			userChapterService.deleteByEntity(userChapter);
		}
	}

}