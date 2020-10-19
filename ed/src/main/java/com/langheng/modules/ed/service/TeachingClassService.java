/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.service.StudentClassesService;
import com.langheng.modules.base.utils.StudentUtils;
import com.langheng.modules.base.utils.TeacherUtils;
import com.langheng.modules.ed.dao.TeachingClassDao;
import com.langheng.modules.ed.entity.TeachingClass;
import com.langheng.modules.ed.enumn.TeachingClassState;
import com.langheng.modules.ed.util.TeachingClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * 课堂Service
 * @author xiaoxie
 * @version 2020-02-11
 */
@Service
@Transactional(readOnly=true)
public class TeachingClassService extends CrudService<TeachingClassDao, TeachingClass> {

	@Autowired
	private StudentClassesService studentClassesService;
	
	/**
	 * 获取单条数据
	 * @param teachingClass
	 * @return
	 */
	@Override
	public TeachingClass get(TeachingClass teachingClass) {
		return super.get(teachingClass);
	}
	
	/**
	 * 查询分页数据
	 * @param teachingClass 查询条件
	 * @return
	 */
	@Override
	public Page<TeachingClass> findPage(TeachingClass teachingClass) {
		return super.findPage(teachingClass);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param teachingClass
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(TeachingClass teachingClass) {
		if (teachingClass.getIsNewRecord()){
			if (new Date().before(teachingClass.getBeginTime())){
				teachingClass.setState(TeachingClassState.HAD_NOT_STARTED.value());
			} // 当前时间 大于开始时间  小于结束
			else if(new Date().after(teachingClass.getBeginTime()) && new Date().before(teachingClass.getEndTime())){
				teachingClass.setState(TeachingClassState.PROGRESSING.value());
			} // 当前时间 大于结束时间
			else if(new Date().after(teachingClass.getEndTime())){
				teachingClass.setState(TeachingClassState.FINISHED.value());
			}
			teachingClass.setTeacher(TeacherUtils.getTeacher());
			teachingClass.setIsEnableBarrage(SysYesNoEnum.YES.value());
			DecimalFormat decimalFormat = new DecimalFormat("000000");
			teachingClass.setClassCode(decimalFormat.format(findCountIgnoreStatus() + 1));
		}
		super.save(teachingClass);

		TeachingClassUtils.clearCache(teachingClass.getTeachingClassId());
	}
	
	/**
	 * 更新状态
	 * @param teachingClass
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TeachingClass teachingClass) {
		super.updateStatus(teachingClass);
	}
	
	/**
	 * 删除数据
	 * @param teachingClass
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(TeachingClass teachingClass) {
		super.delete(teachingClass);
	}

	public Integer findCountIgnoreStatus(){
		return dao.findCountIgnoreStatus();
	}

    public List<TeachingClass> findCurrentStudentList(TeachingClass teachingClassCriteria) {
		List<String> classesIds = studentClassesService.findCurrentStudentClassesIds(StudentUtils.getStudent());
		if (!classesIds.isEmpty()){
			teachingClassCriteria.setClassesIds_in(classesIds.toArray(new String[]{}));
			List<TeachingClass> teachingClassList =  findList(teachingClassCriteria);
			return teachingClassList;
		}
		return ListUtils.newArrayList();
    }
}