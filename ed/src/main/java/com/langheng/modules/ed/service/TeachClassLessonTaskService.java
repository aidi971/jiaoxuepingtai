/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.ed.dao.TeachClassLessonTaskDao;
import com.langheng.modules.ed.entity.LessonTask;
import com.langheng.modules.ed.entity.TeachClassLessonTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课堂节任务管理Service
 * @author xiaoxie
 * @version 2020-04-21
 */
@Service
@Transactional(readOnly=true)
public class TeachClassLessonTaskService extends CrudService<TeachClassLessonTaskDao, TeachClassLessonTask> {

	@Autowired
	private ChapterService chapterService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private LessonTaskService lessonTaskService;

	/**
	 * 获取单条数据
	 * @param teachClassLessonTask
	 * @return
	 */
	@Override
	public TeachClassLessonTask get(TeachClassLessonTask teachClassLessonTask) {
		return super.get(teachClassLessonTask);
	}
	
	/**
	 * 查询分页数据
	 * @param teachClassLessonTask 查询条件
	 * @return
	 */
	@Override
	public Page<TeachClassLessonTask> findPage(TeachClassLessonTask teachClassLessonTask) {
		return super.findPage(teachClassLessonTask);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param teachClassLessonTask
	 */
	@Transactional(readOnly=false)
	public void flushSave(TeachClassLessonTask teachClassLessonTask) {
		deleteByEntity(teachClassLessonTask);
		insertBatch(ListUtils.newArrayList(teachClassLessonTask));
	}
	
	/**
	 * 更新状态
	 * @param teachClassLessonTask
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TeachClassLessonTask teachClassLessonTask) {
		super.updateStatus(teachClassLessonTask);
	}
	

	@Transactional(readOnly = false)
	public void insertBatch(List<TeachClassLessonTask> teachClassLessonTasks){
		dao.insertBatch(teachClassLessonTasks);
	}

	@Transactional(readOnly = false)
	public void saveByLessonTasks(List<LessonTask> lessonTasks,String teachingClassId){
		List<TeachClassLessonTask> needAddTeachClassTasks = ListUtils.newArrayList();
		// 先删除原先的 再添加新的
		deleteByLessonTasks(lessonTasks,teachingClassId);
		for (LessonTask lessonTask: lessonTasks){
			TeachClassLessonTask tclk = new TeachClassLessonTask();
			tclk.setLessonTaskId(lessonTask.getLessonTaskId());
			tclk.setTeachingClassId(teachingClassId);
			needAddTeachClassTasks.add(tclk);
		}
		if (!needAddTeachClassTasks.isEmpty()){
			insertBatch(needAddTeachClassTasks);
		}
	}

	@Transactional(readOnly = false)
	public void deleteByLessonTasks(List<LessonTask> lessonTasks,String teachingClassId){
		if (!lessonTasks.isEmpty()){
			TeachClassLessonTask tclt = new TeachClassLessonTask();
			tclt.setLessonTaskId_in(EntityUtils.getBaseEntityIds(lessonTasks));
			tclt.setTeachingClassId(teachingClassId);
			deleteByEntity(tclt);
		}
	}

	@Transactional(readOnly = false)
	public void deleteByEntity(TeachClassLessonTask teachClassLessonTask){
		dao.deleteByEntity(teachClassLessonTask);
	}

}