/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.ed.dao.TeachClassChapterDao;
import com.langheng.modules.ed.entity.TeachClassChapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程章节管理Service
 * @author xiaoxie
 * @version 2020-04-21
 */
@Service
@Transactional(readOnly=true)
public class TeachClassChapterService extends CrudService<TeachClassChapterDao, TeachClassChapter> {
	
	/**
	 * 获取单条数据
	 * @param teachClassChapter
	 * @return
	 */
	@Override
	public TeachClassChapter get(TeachClassChapter teachClassChapter) {
		return super.get(teachClassChapter);
	}
	
	/**
	 * 查询分页数据
	 * @param teachClassChapter 查询条件
	 * @return
	 */
	@Override
	public Page<TeachClassChapter> findPage(TeachClassChapter teachClassChapter) {
		return super.findPage(teachClassChapter);
	}

	/**
	 * 保存数据（插入或更新）
	 */
	@Transactional(readOnly=false)
	public void insertBatch(List<TeachClassChapter> teachClassChapters) {
		if (!teachClassChapters.isEmpty()){
			dao.insertBatch(teachClassChapters);
		}
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param teachClassChapter
	 */
	@Transactional(readOnly=false)
	public void flushSave(TeachClassChapter teachClassChapter) {
		deleteByEntity(teachClassChapter);
		insertBatch(ListUtils.newArrayList(teachClassChapter));
	}



	/**
	 * 删除数据
	 * @param teachClassChapter
	 */
	@Transactional(readOnly=false)
	public void deleteByEntity(TeachClassChapter teachClassChapter) {
		dao.deleteByEntity(teachClassChapter);
	}
	
}