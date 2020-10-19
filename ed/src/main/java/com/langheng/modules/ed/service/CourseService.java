/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.utils.CoverImgUtils;
import com.langheng.modules.base.utils.TeacherUtils;
import com.langheng.modules.ed.dao.CourseDao;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.enumn.CourseState;
import com.langheng.modules.ed.enumn.LessonTaskType;
import com.langheng.modules.ed.enumn.StructureType;
import com.langheng.modules.ed.util.CourseUtils;
import com.langheng.modules.ed.util.TeachingClassUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 课程Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class CourseService extends CrudService<CourseDao, Course> {
	
	/**
	 * 获取单条数据
	 * @param course
	 * @return
	 */
	@Override
	public Course get(Course course) {
		return super.get(course);
	}
	
	/**
	 * 查询分页数据
	 * @param course 查询条件
	 * @return
	 */
	@Override
	public Page<Course> findPage(Course course) {
		return super.findPage(course);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param course
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Course course) {
		if (course.getIsNewRecord()){
			course.setStructureType(StructureType.CHAPTER.value());
			course.setTeacher(TeacherUtils.getTeacher());
			course.setIsEnableBarrage(SysYesNoEnum.NO.value());
			course.setState(CourseState.HAD_NOT_STARTED.value());
			if (StringUtils.isBlank(course.getVersion())){
				course.setVersion("v1.0");
			}
			if (StringUtils.isBlank(course.getIterations())){
				course.setIterations("0");
			}
			course.setFullName(CourseUtils.getCourseFullName(course));
			course.setImgSrc(CoverImgUtils.getCoverImg("1"));
		}

		super.save(course);

		TeachingClassUtils.clearCacheByCourse(course.getCourseId());
	}
	
	/**
	 * 更新状态
	 * @param course
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Course course) {
		super.updateStatus(course);
	}
	
	/**
	 * 删除数据
	 * @param course
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Course course) {
		super.delete(course);
	}

	@Transactional(readOnly=false)
    public void updateLessonTaskNum(String courseId, String type) {
		if (StringUtils.isNotBlank(courseId)
			&& StringUtils.isNotBlank(type)){
			if(LessonTaskType.COURSEWARE.value().equals(type)
			|| LessonTaskType.VIDEO.value().equals(type)
			|| LessonTaskType.SUBJECTIVITY.value().equals(type)
			|| LessonTaskType.OBJECTIVES.value().equals(type)){
				dao.updateLessonTaskNum(courseId,type);
			}
		}
    }

	@Transactional(readOnly=false)
	public void updateAllLessonTaskNum(String courseId) {
		if (StringUtils.isNotBlank(courseId)){
			dao.updateLessonTaskNum(courseId, LessonTaskType.COURSEWARE.value());
			dao.updateLessonTaskNum(courseId, LessonTaskType.VIDEO.value());
			dao.updateLessonTaskNum(courseId, LessonTaskType.OBJECTIVES.value());
		}
	}
}