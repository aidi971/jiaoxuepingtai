/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.ed.dao.UserLessonDao;
import com.langheng.modules.ed.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 学生小节Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class UserLessonService extends CrudService<UserLessonDao, UserLesson> {

	@Autowired
	private LessonService lessonService;
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserChapterService userChapterService;
	/**
	 * 获取单条数据
	 * @param userLesson
	 * @return
	 */
	@Override
	public UserLesson get(UserLesson userLesson) {
		return super.get(userLesson);
	}
	
	/**
	 * 查询分页数据
	 * @param userLesson 查询条件
	 * @return
	 */
	@Override
	public Page<UserLesson> findPage(UserLesson userLesson) {
		return super.findPage(userLesson);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param userLesson
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(UserLesson userLesson) {
		deleteByEntity(userLesson);
		insertBatch(userLesson);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param userLesson
	 */
	@Transactional(readOnly=false)
	public void insertBatch(UserLesson userLesson) {
		dao.insertBatch(ListUtils.newArrayList(userLesson));
	}


	/**
	 * 删除数据
	 * @param userLesson
	 */
	@Transactional(readOnly=false)
	public void deleteByEntity(UserLesson userLesson) {
		dao.deleteByEntity(userLesson);
	}


	/**
	 * 获取我正在进行中的任务
	 * @param teachingClassId
	 * @return
	 */
	public List<Lesson> findMyPendingLessonList(String teachingClassId, String chapterId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setChapterId(chapterId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyPendingLessonList(userTaskDto);
	}

	/**
	 * 获取我正在进行中的任务
	 * @param teachingClassId
	 * @return
	 */
	public List<Lesson> findMyPendingLessonList(String teachingClassId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyPendingLessonList(userTaskDto);
	}

	/**
	 * 获取我正在进行中的任务
	 * @param teachingClassId
	 * @return
	 */
	public List<Lesson> findMyPendingSubLessonList(String teachingClassId, String lessonId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setLessonId(lessonId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyPendingSubLessonList(userTaskDto);
	}

	/**
	 * 获取我已经完成的 ’节‘
	 * @param teachingClassId
	 * @param chapterId
	 * @return
	 */
	public List<Lesson> findMyFinishingLessonList(String teachingClassId, String chapterId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setChapterId(chapterId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyFinishingLessonList(userTaskDto);
	}

	public List<Lesson> findMyFinishingLessonList(String teachingClassId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyFinishingLessonList(userTaskDto);
	}

	/**
	 * 获取我已经完成的 ’节‘
	 * @param teachingClassId
	 * @param chapterId
	 * @return
	 */
	public List<Lesson> findMyFinishLessonList(String teachingClassId, String chapterId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setChapterId(chapterId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyFinishLessonList(userTaskDto);
	}

	public List<Lesson> findMyFinishLessonList(String teachingClassId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyFinishLessonList(userTaskDto);
	}

	/**
	 * 获取我已经完成的 ’小节‘
	 * @param teachingClassId
	 * @param lessonId
	 * @return
	 */
	public List<Lesson> findMyFinishSubLessonList(String teachingClassId, String lessonId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setLessonId(lessonId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyFinishSubLessonList(userTaskDto);
	}

	/**
	 * 判断是否完成所有 ‘小节’
	 * @param teachingClassId
	 * @param lessonId
	 * @return
	 */
	public boolean checkIsFinishAllSubLesson(String teachingClassId, String lessonId){
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setLessonId(lessonId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.checkIsFinishAllSubLesson(userTaskDto) == 0 ;
	}

    public void flushUserTask(String teachingClassId, String lessonId) {
		Lesson lesson = lessonService.get(lessonId);
		if (!userTaskService.checkIfFinishAllSubOrLesson(teachingClassId,lesson.getId())){
			// 清空小节  学生资源完成清空
			UserLesson userLesson = new UserLesson();
			userLesson.setLessonId(lesson.getId());
			deleteByEntity(userLesson);

			Chapter chapter = null;

			if (lesson.isLesson()){
				chapter = lesson.getChapter();
			}else if (lesson.isSubLesson()){
				Lesson parentLesson = lesson.getParentLesson();
				if (parentLesson!= null && StringUtils.isNotBlank(parentLesson.getId())){
					parentLesson = lessonService.get(parentLesson);
					chapter = parentLesson.getChapter();
					// 清空小节  学生资源完成清空
					UserLesson uLesson = new UserLesson();
					uLesson.setLessonId(parentLesson.getId());
					deleteByEntity(uLesson);
				}
			}

			if (chapter!= null && StringUtils.isNotBlank(chapter.getChapterId())){
				// 清空小节  学生资源完成清空
				UserChapter userChapter = new UserChapter();
				userChapter.setChapterId(chapter.getChapterId());
				userChapterService.deleteByEntity(userChapter);
			}
		}
    }



}