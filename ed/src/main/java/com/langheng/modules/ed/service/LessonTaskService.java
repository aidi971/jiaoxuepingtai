/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.ed.dao.LessonTaskDao;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.enumn.LessonTaskType;
import com.langheng.modules.ed.enumn.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小节任务（文件）Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class LessonTaskService extends CrudService<LessonTaskDao, LessonTask> {

	@Autowired
	private LessonService lessonService;
	@Autowired
	private CourseService courseService;
	
	/**
	 * 获取单条数据
	 * @param lessonTask
	 * @return
	 */
	@Override
	public LessonTask get(LessonTask lessonTask) {
		return super.get(lessonTask);
	}
	
	/**
	 * 查询分页数据
	 * @param lessonTask 查询条件
	 * @return
	 */
	@Override
	public Page<LessonTask> findPage(LessonTask lessonTask) {
		return super.findPage(lessonTask);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param lessonTask
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(LessonTask lessonTask) {
		boolean updateLessonTaskNum = false;
		String courseId = null;
		if (lessonTask.getIsNewRecord() && StringUtils.isNotBlank(lessonTask.getLessonId())){
			Lesson lesson = lessonService.get(lessonTask.getLessonId());
			Chapter chapter = lessonService.getChapterByLesson(lesson);
			if (chapter != null && StringUtils.isNotBlank(chapter.getChapterId())){
				lessonTask.setChapterId(chapter.getChapterId());
				if (StringUtils.isNotBlank(chapter.getCourse().getCourseId())){
					updateLessonTaskNum = true;
					courseId = chapter.getCourse().getCourseId();
				}
			}
		}
		super.save(lessonTask);

		// 更新资源个数
		if (updateLessonTaskNum){
			courseService.updateLessonTaskNum(courseId,lessonTask.getType());
		}
	}
	
	/**
	 * 更新状态
	 * @param lessonTask
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(LessonTask lessonTask) {
		super.updateStatus(lessonTask);
	}

	/**
	 * 删除数据
	 * @param lessonTask
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(LessonTask lessonTask) {
		super.delete(lessonTask);
		// 更新资源数
		courseService.updateLessonTaskNum(getCourseId(lessonTask),lessonTask.getType());
	}

	@Transactional(readOnly=false)
	public void deleteByEntity(LessonTask lessonTask) {
		if (dao.deleteByEntity(lessonTask) != 0){
			// 更新资源数
			courseService.updateAllLessonTaskNum(getCourseId(lessonTask));
		}
	}

	/**
	 * 批量插入
	 * @param lessonTaskList
	 */
	@Transactional(readOnly = false)
	public void insertBatch(List<LessonTask> lessonTaskList){
		super.dao.insertBatch(lessonTaskList);
	}

//	@Transactional(readOnly=false)
	public void saveTask(LessonTask lessonTask) {
		LessonTask taskInfo = new LessonTask();
		taskInfo.setTaskId(lessonTask.getTaskId());
		taskInfo.setLessonId(lessonTask.getLessonId());
		List<LessonTask> task = super.findList(taskInfo);
		if (task.size()==0) {
			save(lessonTask);
		}
	}

	@Transactional(readOnly = false)
	public Map findGroupCount(LessonTask lessonTask) {
		Map<String, Object> map = new HashMap<>();
		lessonTask.setType(LessonTaskType.COURSEWARE.value());
		long count0 = super.findCount(lessonTask);
		lessonTask.setType(LessonTaskType.VIDEO.value());
		long count1 = super.findCount(lessonTask);
		lessonTask.setType(LessonTaskType.SUBJECTIVITY.value());
		long count2 = super.findCount(lessonTask);
		lessonTask.setType(LessonTaskType.OBJECTIVES.value());
		long count3 = super.findCount(lessonTask);
		lessonTask.setType(LessonTaskType.CASE.value());
		long count4 = super.findCount(lessonTask);
		lessonTask.setType(LessonTaskType.CORRECT.value());
		long count5 = super.findCount(lessonTask);
		lessonTask.setType(LessonTaskType.PRACTICE.value());
		long count6 = super.findCount(lessonTask);

		map.put(LessonTaskType.COURSEWARE.label(),count0);
		map.put(LessonTaskType.VIDEO.label(),count1);
		map.put(LessonTaskType.SUBJECTIVITY.label(),count2);
		map.put(LessonTaskType.OBJECTIVES.label(),count3);
		map.put(LessonTaskType.CASE.label(),count4);
		map.put(LessonTaskType.CORRECT.label(),count5);
		map.put(LessonTaskType.PRACTICE.label(),count6);
		return map;
	}

	public List<LessonTask> findListHadPush(LessonTask lessonTask){
		return dao.findListHadPush(lessonTask);
	}

	public Long findCountHadPush(LessonTask lessonTask){
		return dao.findCountHadPush(lessonTask);
	}

	public List<LessonTask> findListStuFinishDetail(LessonTask lessonTask,String studentId) {
		lessonTask.setStudentId(studentId);
		return dao.findListStuFinishDetail(lessonTask);
	}

	public String getCourseId(LessonTask lessonTask){
		if (StringUtils.isNotBlank(lessonTask.getLessonId())){
			Lesson lesson = lessonService.get(lessonTask.getLessonId());
			Chapter chapter = lessonService.getChapterByLesson(lesson);
			if (chapter != null && chapter.getCourse() != null){
				return chapter.getCourse().getCourseId();
			}
		}
		return null;
	}

	public Float getTotalObjectiveScore(UserTaskDto userTaskDto) {
		Float defaultScore = dao.getTotalObjectiveDefaultScore(userTaskDto);
		Float reSetScore = dao.getTotalObjectiveReSetScore(userTaskDto);
		if (defaultScore == null){
			defaultScore = 0f;
		}
		if (reSetScore == null){
			reSetScore = 0f;
		}
		return defaultScore + reSetScore;
	}
	/**
	 * 批量修改
	 * @param
	 */
	@Transactional(readOnly = false)
	public void updateByEntity(LessonTask lessonTask, LessonTask whereLessonTask) {
		dao.updateByEntity(lessonTask,whereLessonTask);
	}

	public void updateLessonTaskName(String taskId, String taskName) {
		LessonTask where = new LessonTask();
		where.setTaskId(taskId);
		where.setTaskStatus(TaskStatus.PRIVATELY.value());
		LessonTask  lessonTask= new LessonTask();
		lessonTask.setTaskName(taskName);
		updateByEntity(lessonTask,where);
	}

	/**
	 * 获取课程所有推送的lessonTask
	 * @param course
	 * @param teachingClassId
	 * @return
	 */
	public List<LessonTask> findAllHadPushByCourse(Course course, String teachingClassId) {
		List<Lesson> lessonList = lessonService.findAllHadPushByCourse(course,teachingClassId);
		LessonTask lessonTask = new LessonTask();
		lessonTask.setTeachingClassId(teachingClassId);
		lessonTask.setLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
		List<LessonTask> lessonTaskList = findListHadPush(lessonTask);
		return lessonTaskList;
	}
}