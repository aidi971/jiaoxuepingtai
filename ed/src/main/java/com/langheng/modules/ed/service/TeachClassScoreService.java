/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.ed.dao.TeachClassScoreDao;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.enumn.LessonTaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课堂赋分管理Service
 * @author xiaoxie
 * @version 2020-04-27
 */
@Service
@Transactional(readOnly=true)
public class TeachClassScoreService extends CrudService<TeachClassScoreDao, TeachClassScore> {

	@Autowired
	private TeachingClassService teachingClassService;
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
	 * @param teachClassScore
	 * @return
	 */
	@Override
	public TeachClassScore get(TeachClassScore teachClassScore) {
		return super.get(teachClassScore);
	}
	
	/**
	 * 查询分页数据
	 * @param teachClassScore 查询条件
	 * @return
	 */
	@Override
	public Page<TeachClassScore> findPage(TeachClassScore teachClassScore) {
		return super.findPage(teachClassScore);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param teachClassScore
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(TeachClassScore teachClassScore) {
		super.save(teachClassScore);
	}
	
	/**
	 * 更新状态
	 * @param teachClassScore
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TeachClassScore teachClassScore) {
		super.updateStatus(teachClassScore);
	}
	
	/**
	 * 删除数据
	 * @param teachClassScore
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(TeachClassScore teachClassScore) {
		super.delete(teachClassScore);
	}

    public List<Chapter> scoreStruct(Course course, String teachingClassId) {
		Chapter chapterCriteria = new Chapter();
		chapterCriteria.selectBarrageState(teachingClassId);
		chapterCriteria.setCourse(course);
		List<Chapter> chapterList = chapterService.findList(chapterCriteria);

		Lesson lessonCriteria = new Lesson();
		Chapter lcCriteria = new Chapter();
		lcCriteria.setId_in(EntityUtils.getBaseEntityIds(chapterList));
		lessonCriteria.setChapter(lcCriteria);
		lessonCriteria.selectLessonBarrageState(teachingClassId);
		List<Lesson> lessonList = lessonService.findList(lessonCriteria);

		Lesson subLessonCriteria = new Lesson();
		subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
		List<Lesson> subLessonList = lessonService.findList(subLessonCriteria);

		// 获取所有‘节’或者小节的资源
		List<Lesson> allLessons = ListUtils.newArrayList();
		allLessons.addAll(lessonList); allLessons.addAll(subLessonList);
		LessonTask lessonTaskCriteria = new LessonTask();
		// 关联上分数赋分
		lessonTaskCriteria.selectModifyScore(teachingClassId);
		// 只查询主观题，和客观题资源
		lessonTaskCriteria.setType_in(new String[]{LessonTaskType.OBJECTIVES.value(),
									LessonTaskType.SUBJECTIVITY.value(),
									LessonTaskType.PRACTICE.value()});
		lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
		List<LessonTask> lessonTaskList = lessonTaskService.findList(lessonTaskCriteria);

		commonService.buildStructChapter(chapterList,lessonList,subLessonList, lessonTaskList);

		return chapterList;
    }

	@Transactional(readOnly=false)
	public void deleteByEntity(TeachClassScore teachClassScore) {
		dao.deleteByEntity(teachClassScore);
	}

	@Transactional(readOnly=false)
	public void saveLessonTasksByTeachingClass(String teachingClassId,Float score) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		if (teachingClass.getCourse()!= null && StringUtils.isNotBlank(teachingClass.getCourse().getCourseId())){
			Chapter chapterCriteria = new Chapter();
			chapterCriteria.setCourse(teachingClass.getCourse());
			List<Chapter> chapterList = chapterService.findList(chapterCriteria);
			List<LessonTask> lessonTaskList = ListUtils.newArrayList();
			chapterList.forEach(chapter -> {
				List<LessonTask> taskList = commonService.findLessonTasksByChapter(chapter);
				lessonTaskList.addAll(taskList);
			});
			if (!lessonTaskList.isEmpty()){
				List<TeachClassScore> teachClassScoreList = ListUtils.newArrayList();
				lessonTaskList.forEach(lessonTask -> {
					if (LessonTaskType.SUBJECTIVITY.value().equals(lessonTask.getType())
						|| LessonTaskType.OBJECTIVES.value().equals(lessonTask.getType())){
						TeachClassScore teachClassScore = new TeachClassScore();
						teachClassScore.setTeachingClassId(teachingClassId);
						teachClassScore.setLessonTaskId(lessonTask.getLessonTaskId());
						teachClassScore.setScore(score);
						teachClassScoreList.add(teachClassScore);
					}
				});
				insertBatch(teachClassScoreList);
			}
		}
	}

	@Transactional(readOnly=false)
	public void insertBatch(List<TeachClassScore> teachClassScoreList) {
		if (!teachClassScoreList.isEmpty()){
			dao.insertBatch(teachClassScoreList);
		}
	}
}