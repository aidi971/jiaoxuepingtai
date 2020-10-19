/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.ed.dao.TeachClassLessonDao;
import com.langheng.modules.ed.entity.Lesson;
import com.langheng.modules.ed.entity.TeachClassLesson;
import com.langheng.modules.ed.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课堂节或者'小节'管理Service
 * @author xiaoxie
 * @version 2020-04-21
 */
@Service
@Transactional(readOnly=true)
public class TeachClassLessonService extends CrudService<TeachClassLessonDao, TeachClassLesson> {

	@Autowired
	private LessonService lessonService;

	/**
	 * 获取单条数据
	 * @param teachClassLesson
	 * @return
	 */
	@Override
	public TeachClassLesson get(TeachClassLesson teachClassLesson) {
		return super.get(teachClassLesson);
	}
	
	/**
	 * 查询分页数据
	 * @param teachClassLesson 查询条件
	 * @param teachClassLesson.page 分页对象
	 * @return
	 */
	@Override
	public Page<TeachClassLesson> findPage(TeachClassLesson teachClassLesson) {
		return super.findPage(teachClassLesson);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param teachClassLesson
	 */
	@Transactional(readOnly=false)
	public void flushSave(TeachClassLesson teachClassLesson) {
		deleteByEntity(teachClassLesson);
		insertBatch(ListUtils.newArrayList(teachClassLesson));
	}

	@Transactional(readOnly=false)
	public void insertBatch(List<TeachClassLesson> teachClassLessons) {
		if (!teachClassLessons.isEmpty()){
			dao.insertBatch(teachClassLessons);
		}
	}

	/**
	 * 更新状态
	 * @param teachClassLesson
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TeachClassLesson teachClassLesson) {
		super.updateStatus(teachClassLesson);
	}
	
	/**
	 * 删除数据
	 * @param teachClassLesson
	 */
	@Transactional(readOnly=false)
	public void deleteByEntity(TeachClassLesson teachClassLesson) {
		dao.deleteByEntity(teachClassLesson);
	}

	@Transactional(readOnly=false)
	public void deleteByLessonList(List<Lesson> lessonList, String teachingClassId){
		if (!lessonList.isEmpty()) {
			TeachClassLesson teachClassLesson = new TeachClassLesson();
			teachClassLesson.setLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
			teachClassLesson.setTeachingClassId(teachingClassId);

			deleteByEntity(teachClassLesson);
		}
	}

	public void saveByLessonList(List<Lesson> lessonList, String teachingClassId) {
		List<TeachClassLesson> teachClassLessonList = ListUtils.newArrayList();
		deleteByLessonList(lessonList,teachingClassId);

		lessonList.forEach(lesson -> {
			TeachClassLesson tcl = new TeachClassLesson();
			tcl.setTeachingClassId(teachingClassId);
			tcl.setLessonId(lesson.getLessonId());
			teachClassLessonList.add(tcl);
		});
		insertBatch(teachClassLessonList);
    }

    public String findLastPush(String teachingClassId) {
		TeachClassLesson tcLesson = new TeachClassLesson();
		tcLesson.setTeachingClassId(teachingClassId);
		List<TeachClassLesson> tcLessonList = findList(tcLesson);

		if (!tcLessonList.isEmpty()){
			tcLesson = tcLessonList.get(tcLessonList.size() - 1);
			Lesson lesson = lessonService.get(tcLesson.getLessonId());
			return CommonUtils.getFullLessonName(lesson);
		}
		return StringUtils.EMPTY;
    }
}