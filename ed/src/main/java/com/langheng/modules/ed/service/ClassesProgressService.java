/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.ed.dao.ClassesProgressDao;
import com.langheng.modules.ed.entity.ClassesProgress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 课程进度Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class ClassesProgressService extends CrudService<ClassesProgressDao, ClassesProgress> {
	
	/**
	 * 获取单条数据
	 * @param classesProgress
	 * @return
	 */
	@Override
	public ClassesProgress get(ClassesProgress classesProgress) {
		return super.get(classesProgress);
	}
	
	/**
	 * 查询分页数据
	 * @param classesProgress 查询条件
	 * @param classesProgress.page 分页对象
	 * @return
	 */
	@Override
	public Page<ClassesProgress> findPage(ClassesProgress classesProgress) {
		return super.findPage(classesProgress);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param classesProgress
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ClassesProgress classesProgress) {
		super.save(classesProgress);
	}
	
	/**
	 * 更新状态
	 * @param classesProgress
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ClassesProgress classesProgress) {
		super.updateStatus(classesProgress);
	}
	
	/**
	 * 删除数据
	 * @param classesProgress
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ClassesProgress classesProgress) {
		super.delete(classesProgress);
	}
	
}