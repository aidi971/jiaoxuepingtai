 /**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

 import com.jeesite.common.entity.Page;
 import com.jeesite.common.enumn.SysYesNoEnum;
 import com.jeesite.common.lang.StringUtils;
 import com.jeesite.common.service.CrudService;
 import com.langheng.modules.ed.dao.ChapterDao;
 import com.langheng.modules.ed.entity.Chapter;
 import com.langheng.modules.ed.entity.Course;
 import com.langheng.modules.ed.enumn.ChapterState;
 import com.langheng.modules.ed.util.CommonUtils;
 import com.langheng.modules.ed.util.CourseUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;

 import java.util.List;

/**
 * 课程章节Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class ChapterService extends CrudService<ChapterDao, Chapter> {
	@Autowired
	private CourseService courseService;
	
	/**
	 * 获取单条数据
	 * @param chapter
	 * @return
	 */
	@Override
	public Chapter get(Chapter chapter) {
		return super.get(chapter);
	}
	
	/**
	 * 查询分页数据
	 * @param chapter 查询条件
	 * @return
	 */
	@Override
	public Page<Chapter> findPage(Chapter chapter) {
		return super.findPage(chapter);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param chapter
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Chapter chapter) {
		boolean isNewRecord = chapter.getIsNewRecord();
		if (isNewRecord){
			chapter.setState(ChapterState.HAD_NOT_STARTED.value());
			chapter.setChapterNum(Integer.MAX_VALUE);
		}

		super.save(chapter);

		if (isNewRecord && chapter.getCourse()!= null){
			adjustChapterNumByCourse(chapter.getCourse());
		}

	}
	
	/**
	 * 更新状态
	 * @param chapter
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Chapter chapter) {
		super.updateStatus(chapter);
	}
	
	/**
	 * 删除数据
	 * @param chapter
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Chapter chapter) {
		super.delete(chapter);
		if (chapter.getCourse()!= null){
			adjustChapterNumByCourse(chapter.getCourse());
		}
	}

	@Transactional(readOnly=false)
	public void adjustChapterNumByCourse(Course course){
		if (StringUtils.isBlank(course.getCourseId())){
			return;
		}
		Course courseCriteria = new Course(course.getCourseId());
		Chapter chapterCriteria = new Chapter();
		chapterCriteria.setCourse(courseCriteria);
		List<Chapter> chapterList = findList(chapterCriteria);
		if (!chapterList.isEmpty()){
			for (int index=0;index<chapterList.size();){
				Chapter chapter = chapterList.get(index);
				Chapter lastChapter = null;
				if (index + 1 < chapterList.size()){
					lastChapter = chapterList.get(index + 1);
				}
				if (lastChapter != null
					&& lastChapter.getChapterNum().equals(chapter.getChapterNum())
						&& lastChapter.getUpdateDate().getTime() > chapter.getUpdateDate().getTime()){

					if (SysYesNoEnum.YES.value().equals(lastChapter.getIsShiftDown())){
						lastChapter.setIsShiftDown(SysYesNoEnum.NO.value());
						save(lastChapter);
					}else {
						lastChapter.setChapterNum(index + 1);
						lastChapter.setSuffixName(CourseUtils.getChapterSuffixName(lastChapter.getChapterNum()));
						save(lastChapter);
						index ++;
					}
				}

				chapter.setChapterNum(index + 1);
				chapter.setSuffixName(CourseUtils.getChapterSuffixName(chapter.getChapterNum()));
				save(chapter);
				index++;
			}
		}

		// 清理缓存
		CommonUtils.clearCache(course);
	}

	/**
	 * 批量插入
	 * @param chapterList
	 */
	@Transactional(readOnly = false)
	public void insertBatch(List<Chapter> chapterList){
		super.dao.insertBatch(chapterList);
	}

    public List<Chapter> findListHadPush(Chapter chapter) {
		return dao.findListHadPush(chapter);
    }

    public List<Chapter> findListByCourseId(String courseId) {
		Chapter chapter = new Chapter();
		chapter.setCourse(new Course(courseId));
		return findList(chapter);
    }
}