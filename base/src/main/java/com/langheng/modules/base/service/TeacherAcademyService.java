/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.TeacherAcademyDao;
import com.langheng.modules.base.entity.TeacherAcademy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 教师学院Service
 * @author xiaoxie
 * @version 2019-12-20
 */
@Service
@Transactional(readOnly=true)
public class TeacherAcademyService extends CrudService<TeacherAcademyDao, TeacherAcademy> {
	
	/**
	 * 获取单条数据
	 * @param teacherAcademy
	 * @return
	 */
	@Override
	public TeacherAcademy get(TeacherAcademy teacherAcademy) {
		return super.get(teacherAcademy);
	}
	
	/**
	 * 查询分页数据
	 * @param teacherAcademy 查询条件
	 * @param teacherAcademy.page 分页对象
	 * @return
	 */
	@Override
	public Page<TeacherAcademy> findPage(TeacherAcademy teacherAcademy) {
		return super.findPage(teacherAcademy);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param teacherAcademy
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(TeacherAcademy teacherAcademy) {
		super.save(teacherAcademy);
	}
	
	/**
	 * 更新状态
	 * @param teacherAcademy
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TeacherAcademy teacherAcademy) {
		super.updateStatus(teacherAcademy);
	}
	
	/**
	 * 删除数据
	 * @param teacherAcademy
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(TeacherAcademy teacherAcademy) {
		super.delete(teacherAcademy);
	}
	
}