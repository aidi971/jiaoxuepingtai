/**
 * Copyright (c) 2019-Now http://langheng.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.ed.dao.MissionLessonTaskDao;
import com.langheng.modules.ed.entity.LessonTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 小节任务（文件）Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class MissionLessonTaskService extends CrudService<MissionLessonTaskDao, LessonTask> {

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
		super.save(lessonTask);
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
	}

    public void insertBatch(List<LessonTask> newLessonTaskList) {
		dao.insertBatch(newLessonTaskList);
    }
}