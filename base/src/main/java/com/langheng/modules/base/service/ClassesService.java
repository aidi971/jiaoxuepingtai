/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.ClassesDao;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.entity.StudentClasses;
import com.langheng.modules.base.utils.TeacherUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 班级Service
 * @author xiaoxie
 * @version 2019-12-18
 */
@Service
@Transactional(readOnly=true)
public class ClassesService extends CrudService<ClassesDao, Classes> {
	@Autowired
	private StudentClassesService studentClassesService;

	/**
	 * 获取单条数据
	 * @param classes
	 * @return
	 */
	@Override
	public Classes get(Classes classes) {
		return super.get(classes);
	}
	
	/**
	 * 查询分页数据
	 * @param classes 查询条件
	 * @return
	 */
	@Override
	public Page<Classes> findPage(Classes classes) {
		return super.findPage(classes);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param classes
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Classes classes) {
		if (classes.getIsNewRecord()){
			classes.setStudentNum(0L);
			classes.setOpenInvite(SysYesNoEnum.NO.value());
			classes.setIsNeedAudit(SysYesNoEnum.NO.value());
			classes.setTeacher(TeacherUtils.getTeacher());
		}
		super.save(classes);
	}
	
	/**
	 * 更新状态
	 * @param classes
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Classes classes) {
		super.updateStatus(classes);
	}
	
	/**
	 * 删除数据
	 * @param classes
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Classes classes) {
		super.delete(classes);
	}

	/**
	 * 查询学院id  根据学院id分组
	 * @param classes
	 * @return
	 */
	public List<String> findAcademyIdsOrderByAcademyId(Classes classes){
		return dao.findAcademyIdsOrderByAcademyId(classes);
	}

	@Transactional(readOnly=false)
    public void adjustStudentNum(Classes classes) {
		StudentClasses studentClasses = new StudentClasses();
		studentClasses.setClassesId(classes.getId());
		long count = studentClassesService.findCount(studentClasses);
		classes.setStudentNum(count);
		save(classes);
    }


	/**
	 * 根据邀请码查找班级
	 * @param invitationCode
	 * @return
	 */
	public Classes getClassesByInvitationCode(String invitationCode){
		Classes classesCriteria = new Classes();
		classesCriteria.setInvitationCode(invitationCode);
		List<Classes> classesList = findList(classesCriteria);
		if (!classesList.isEmpty()){
			return  classesList.get(0);
		}
		return null;
	}

	/**
	 * 判断学生是否已经存在班级
	 * @param student
	 * @return
	 */
	public boolean isStudentExistClasses(Student student){
		StudentClasses studentClasses = new StudentClasses();
		studentClasses.setStudentId(student.getStudentId());
		return studentClassesService.findCount(studentClasses) > 0;
	}

	/**
	 * 更新在线学生人数
	 * @param classes
	 */
	@Transactional(readOnly=false)
	public void updateStuOnlineNum(Classes classes){
		dao.updateStuOnlineNum(classes);
	}

	@Transactional(readOnly=false)
    public void phyDelete(Classes classes) {
		dao.phyDelete(classes);
    }
}