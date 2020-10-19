/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.ed.dao.TeachClassAnswerDao;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.enumn.LessonTaskType;
import com.langheng.modules.ed.enumn.LessonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课堂答案管理Service
 * @author xiaoxie
 * @version 2020-04-26
 */
@Service
@Transactional(readOnly=true)
public class TeachClassAnswerService extends CrudService<TeachClassAnswerDao, TeachClassAnswer> {

	@Autowired
	private CommonService commonService;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private LessonTaskService lessonTaskService;
	
	/**
	 * 获取单条数据
	 * @param teachClassAnswer
	 * @return
	 */
	@Override
	public TeachClassAnswer get(TeachClassAnswer teachClassAnswer) {
		return super.get(teachClassAnswer);
	}
	
	/**
	 * 查询分页数据
	 * @param teachClassAnswer 查询条件
	 * @return
	 */
	@Override
	public Page<TeachClassAnswer> findPage(TeachClassAnswer teachClassAnswer) {
		return super.findPage(teachClassAnswer);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param teachClassAnswer
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(TeachClassAnswer teachClassAnswer) {
		super.save(teachClassAnswer);
	}
	
	/**
	 * 更新状态
	 * @param teachClassAnswer
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TeachClassAnswer teachClassAnswer) {
		super.updateStatus(teachClassAnswer);
	}
	
	/**
	 * 删除数据
	 * @param teachClassAnswer
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(TeachClassAnswer teachClassAnswer) {
		super.delete(teachClassAnswer);
	}

	/**
	 * 查看答案树结构
	 * @param course
	 * @return
	 */
	public List<Chapter> answerStruct(Course course,String teachingClassId) {
		Chapter chapterCriteria = new Chapter();
		chapterCriteria.selectBarrageState(teachingClassId);
		chapterCriteria.setCourse(course);
		List<Chapter> chapterList = chapterService.findList(chapterCriteria);

		Lesson lessonCriteria = new Lesson();
		Chapter chapterCriteria2 = new Chapter();
		chapterCriteria2.setId_in(EntityUtils.getBaseEntityIds(chapterList));
		lessonCriteria.setChapter(chapterCriteria2);
		lessonCriteria.selectLessonBarrageState(teachingClassId);
		List<Lesson> lessonList = lessonService.findList(lessonCriteria);

		Lesson subLessonCriteria = new Lesson();
		subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
		List<Lesson> subLessonList = lessonService.findList(subLessonCriteria);

		// 获取所有‘节’或者小节的资源
		List<Lesson> allLessons = ListUtils.newArrayList();
		allLessons.addAll(lessonList); allLessons.addAll(subLessonList);
		LessonTask lessonTaskCriteria = new LessonTask();
		// 关联上答案推送状态
		lessonTaskCriteria.selectIsPushAnswer(teachingClassId);
		// 只查询主观题，和客观题资源
		lessonTaskCriteria.setType_in(new String[]{LessonTaskType.OBJECTIVES.value(),LessonTaskType.SUBJECTIVITY.value()});
		lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
		List<LessonTask> lessonTaskList = lessonTaskService.findList(lessonTaskCriteria);

		commonService.buildStructChapter(chapterList,lessonList,subLessonList, lessonTaskList);

		return chapterList;

	}

	@Transactional(readOnly = false)
	public void deleteByEntity(TeachClassAnswer teachClassAnswer) {
		dao.deleteByEntity(teachClassAnswer);
	}

	@Transactional(readOnly = false)
	public void insertBatch(List<TeachClassAnswer> teachClassAnswers) {
		if (!teachClassAnswers.isEmpty()){
			dao.insertBatch(teachClassAnswers);
		}
	}

	@Transactional(readOnly = false)
	public void saveByLessonTasks(List<LessonTask> lessonTaskList, String teachingClassId) {
		List<TeachClassAnswer> teachClassAnswerList = ListUtils.newArrayList();
		lessonTaskList.forEach(lessonTask -> {
			TeachClassAnswer teachClassAnswer = new TeachClassAnswer();
			teachClassAnswer.setTeachingClassId(teachingClassId);
			teachClassAnswer.setLessonTaskId(lessonTask.getLessonTaskId());
			Lesson lesson = lessonService.get(lessonTask.getLessonId());
			if (LessonType.LESSON.value().equals(lesson.getLessonType())){
				teachClassAnswer.setLessonId(lesson.getLessonId());
				teachClassAnswer.setChapterId(lesson.getChapter().getChapterId());
			}else if (LessonType.SUB_LESSON.value().equals(lesson.getLessonType())) {
				teachClassAnswer.setSubLessonId(lesson.getLessonId());
				if (lesson.getParentLesson() !=null){
					Lesson parentLesson = lessonService.get(lesson.getParentLesson());
					teachClassAnswer.setLessonId(parentLesson.getLessonId());
					teachClassAnswer.setChapterId(parentLesson.getChapter().getChapterId());
				}
			}
			teachClassAnswerList.add(teachClassAnswer);
		});

		insertBatch(teachClassAnswerList);
	}

	public List<Chapter> scoreStruct(Course course, String teachingClassId) {
		return null;
	}
}