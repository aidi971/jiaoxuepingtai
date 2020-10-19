/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.ed.dao.LessonDao;
import com.langheng.modules.ed.entity.Chapter;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.Lesson;
import com.langheng.modules.ed.entity.LessonTask;
import com.langheng.modules.ed.enumn.ChapterState;
import com.langheng.modules.ed.enumn.LessonType;
import com.langheng.modules.ed.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 课程小节Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class LessonService extends CrudService<LessonDao, Lesson> {

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private LessonTaskService lessonTaskService;
	
	/**
	 * 获取单条数据
	 * @param lesson
	 * @return
	 */
	@Override
	public Lesson get(Lesson lesson) {
		return super.get(lesson);
	}
	
	/**
	 * 查询分页数据
	 * @param lesson 查询条件
	 * @return
	 */
	@Override
	public Page<Lesson> findPage(Lesson lesson) {
		return super.findPage(lesson);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param lesson
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Lesson lesson) {
		boolean isNewRecord = lesson.getIsNewRecord();
		if (isNewRecord){
			lesson.setState(ChapterState.HAD_NOT_STARTED.value());
			lesson.setLessonNum(Integer.MAX_VALUE);
		}
		super.save(lesson);

		if(isNewRecord){
			if (LessonType.LESSON.value().equals(lesson.getLessonType())){
				adjustLessonNumByChapter(lesson.getChapter());
			}else if (LessonType.SUB_LESSON.value().equals(lesson.getLessonType())){
				adjustLessonNumByParentLesson(lesson.getParentLesson());
			}
		}

	}

	/**
	 * 更新状态
	 * @param lesson
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Lesson lesson) {
		super.updateStatus(lesson);
	}
	
	/**
	 * 删除数据
	 * @param lesson
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Lesson lesson) {
		super.delete(lesson);
		if (lesson.isLesson()){
			adjustLessonNumByChapter(lesson.getChapter());
		}else if (lesson.isSubLesson()){
			adjustLessonNumByParentLesson(lesson.getParentLesson());
		}

		if (StringUtils.isNotBlank(lesson.getId())){
			Lesson lessonCriteria = new Lesson();
			lessonCriteria.setParentLesson(lesson);
			deleteByEntity(lessonCriteria);

			LessonTask lessonTask = new LessonTask();
			lessonTask.setLessonId(lesson.getId());
			lessonTaskService.deleteByEntity(lessonTask);
		}

	}

	@Transactional(readOnly=false)
	public void deleteByEntity(Lesson lessonCriteria){
		dao.deleteByEntity(lessonCriteria);
	}

	@Transactional(readOnly=false)
    public void adjustLessonNumByChapter(Chapter chapter) {
		if (StringUtils.isNotBlank((chapter.getChapterId()))){
			Lesson lessonCriteria = new Lesson();
			lessonCriteria.setChapter(new Chapter(chapter.getChapterId()));
			lessonCriteria.setLessonType(LessonType.LESSON.value());
			List<Lesson> lessonList = findList(lessonCriteria);
			adjustLessonNum(lessonList);
		}
    }


	@Transactional(readOnly=false)
	public void adjustLessonNumByParentLesson(Lesson parentLesson) {
		if (StringUtils.isNotBlank(parentLesson.getId())){
			Lesson lessonCriteria = new Lesson();
			lessonCriteria.setParentLesson(new Lesson(parentLesson.getId()));
			lessonCriteria.setLessonType(LessonType.SUB_LESSON.value());
			List<Lesson> lessonList = findList(lessonCriteria);
			adjustLessonNum(lessonList);
		}
	}

	@Transactional(readOnly=false)
	public void adjustLessonNum(List<Lesson> lessonList){
		if (!lessonList.isEmpty()){
			for (int index=0;index<lessonList.size();index++){
				Lesson lesson = lessonList.get(index);
				Lesson lastLesson = null;
				if (index + 1 < lessonList.size()){
					lastLesson = lessonList.get(index + 1);
				}
				if (lastLesson != null
						&& lastLesson.getLessonNum().equals(lesson.getLessonNum())
						&& lastLesson.getUpdateDate().getTime() > lesson.getUpdateDate().getTime()){

					if (SysYesNoEnum.YES.value().equals(lastLesson.getIsShiftDown())){
						lastLesson.setIsShiftDown(SysYesNoEnum.NO.value());
						save(lastLesson);
					}else {
						lastLesson.setLessonNum(index + 1);
						save(lastLesson);
						index ++;
					}
				}

				lesson.setLessonNum(index+1);
				save(lesson);
			}

			// 清理缓存
			CommonUtils.clearCache(lessonList.get(0));
		}
	}

	/**
	 *批量插入
 	 * @param lessonList
	 */
	@Transactional(readOnly = false)
	public void insertBatch(List<Lesson> lessonList){
		super.dao.insertBatch(lessonList);
	}

	public  List<Map> findAllSysFile(){
		return dao.findAllSysFile();
	}

	public List<Map> findAllTestGather() {
		return dao.findAllTestGather();
	}

	public List<Lesson> findListHadPush(Lesson lesson){
		return dao.findListHadPush(lesson);
	}

	public Chapter getChapterByLesson(Lesson lesson){
		if (lesson!= null && lesson.isLesson()){
			return  lesson.getChapter();
		}else if (lesson!= null &&  lesson.isSubLesson()){
			Lesson parentLesson = get(lesson.getParentLesson());
			if (parentLesson.getChapter() != null){
				return parentLesson.getChapter();
			}
		}
		return null;
	}


	/**
	 * 获取课程已推送的所有章节
	 * @param course
	 * @param teachingClassId
	 * @return
	 */
	public List<Lesson> findAllHadPushByCourse(Course course,String teachingClassId) {
		Chapter chapterCriteria = new Chapter();
		chapterCriteria.setCourse(course);
		chapterCriteria.setTeachingClassId(teachingClassId);
		List<Chapter> chapterList = chapterService.findListHadPush(chapterCriteria);

		Lesson lessonCriteria = new Lesson();
		Chapter lcCriteria = new Chapter();
		lcCriteria.setId_in(EntityUtils.getBaseEntityIds(chapterList));
		lessonCriteria.setChapter(lcCriteria);
		lessonCriteria.setTeachingClassId(teachingClassId);
		List<Lesson> lessonList = findListHadPush(lessonCriteria);

		Lesson subLessonCriteria = new Lesson();
		subLessonCriteria.setTeachingClassId(teachingClassId);
		subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
		List<Lesson> subLessonList = findListHadPush(subLessonCriteria);

		// 获取所有‘节’或者小节的资源
		List<Lesson> allLessons = ListUtils.newArrayList();
		allLessons.addAll(lessonList); allLessons.addAll(subLessonList);

		return allLessons;
	}
}