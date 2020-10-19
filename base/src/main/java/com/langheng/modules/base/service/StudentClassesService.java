/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.StudentClassesDao;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.entity.StudentClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 学生班级Service
 * @author xiaoxie
 * @version 2019-12-18
 */
@Service
@Transactional(readOnly=true)
public class StudentClassesService extends CrudService<StudentClassesDao, StudentClasses> {

	@Autowired
	private ClassesService classesService;
	@Autowired
	private StudentService studentService;

	/**
	 * 获取单条数据
	 * @param studentClasses
	 * @return
	 */
	@Override
	public StudentClasses get(StudentClasses studentClasses) {
		return super.get(studentClasses);
	}
	
	/**
	 * 查询分页数据
	 * @param studentClasses 查询条件
	 * @return
	 */
	@Override
	public Page<StudentClasses> findPage(StudentClasses studentClasses) {
		return super.findPage(studentClasses);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param studentClasses
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(StudentClasses studentClasses) {
		if (studentClasses.getIsNewRecord()){
			List<StudentClasses> studentClassesList = findList(studentClasses);
			if (!studentClassesList.isEmpty()){
				studentClasses = studentClassesList.get(0);
			}
		}
		super.save(studentClasses);
	}
	
	/**
	 * 更新状态
	 * @param studentClasses
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(StudentClasses studentClasses) {
		super.updateStatus(studentClasses);
	}
	
	/**
	 * 物理删除数据
	 * @param studentClasses
	 */
	@Transactional(readOnly=false)
	public void phyDelete(StudentClasses studentClasses) {
		super.dao.phyDelete(studentClasses);
	}

	public List<String> findStudentIds(String classesId){
		StudentClasses studentClasses = new StudentClasses();
		studentClasses.setClassesId(classesId);
		return findStudentIds(studentClasses);
	}

	public List<String> findStudentIds(StudentClasses studentClasses){
		List<StudentClasses> studentClassesList = findList(studentClasses);
		List<String> studentIds = ListUtils.newArrayList();
		for (StudentClasses item : studentClassesList){
			studentIds.add(item.getStudentId());
		}
		return studentIds;
	}

	/**
	 * 获取当前学生的所有班级
	 * @param student
	 * @return
	 */
	public List<String> findCurrentStudentClassesIds(Student student){
		List<String> classesIds = ListUtils.newArrayList();
		if (StringUtils.isNotBlank(student.getStudentId())){
			StudentClasses studentClassesCriteria = new StudentClasses();
			studentClassesCriteria.setStudentId(student.getStudentId());

			List<StudentClasses> studentClassesList = findList(studentClassesCriteria);
			studentClassesList.forEach(item -> {
				classesIds.add(item.getClassesId());
			});
		}
		return classesIds;
	}

	/**
	 * 获取学生的班级
	 */
	public Classes getCurrentStudentClasses(Student student){
		StudentClasses studentClasses = new StudentClasses();
		studentClasses.setStudentId(student.getStudentId());
		List<StudentClasses> studentClassesList  = findList(studentClasses);

		if (!studentClassesList.isEmpty()){
			return classesService.get(studentClassesList.get(0).getClassesId());
		}
		return null;
	}

	/**
	 * 获取班级人数
	 * @param classesId
	 * @return
	 */
	public Long getStudentNum(String classesId){
		StudentClasses studentClasses = new StudentClasses();
		studentClasses.setClassesId(classesId);
		long count = findCount(studentClasses);
		return count;
	}

	/**
	 * 查看班级学生
	 * @param classesId
	 * @return
	 */
    public List<Student> findStudents(String classesId) {
		List<String> studentIds = findStudentIds(classesId);
		Student student = new Student();
		student.setStudentId_in(studentIds.toArray(new String[]{}));
		return studentService.findList(student);
    }

}