/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.ed.dao.UserTaskDao;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.enumn.LessonType;
import com.langheng.modules.ed.enumn.UserTaskStatue;
import com.langheng.modules.ed.util.CommonUtils;
import com.langheng.modules.ed.vo.ScoreRanking;
import com.langheng.modules.ed.vo.VideoRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 学生任务Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class UserTaskService extends CrudService<UserTaskDao, UserTask> {

	@Autowired
	private LessonService lessonService;
	@Autowired
	private LessonTaskService lessonTaskService;
	@Autowired
	private UserLessonService userLessonService;
	@Autowired
	private UserChapterService userChapterService;
	@Autowired
	private UserLessonRecordService userLessonRecordService;
	@Autowired
	private TeachingClassService teachingClassService;
	
	/**
	 * 获取单条数据
	 * @param userTask
	 * @return
	 */
	@Override
	public UserTask get(UserTask userTask) {
		return super.get(userTask);
	}
	
	/**
	 * 查询分页数据
	 * @param userTask 查询条件
	 * @return
	 */
	@Override
	public Page<UserTask> findPage(UserTask userTask) {
		return super.findPage(userTask);
	}

	@Override
	public List<UserTask> findList(UserTask entity) {
		entity.selectLoginCode();
		return super.findList(entity);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param userTask
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(UserTask userTask) {
		try{
			if (!userTask.getIsNewRecord()){
				UserTask oldUserTask = get(userTask.getId());
				if (UserTaskStatue.FINISHED.value().equals(userTask.getState())
						&& !userTask.getState().equals(oldUserTask.getState())){
					UserLessonRecord userLessonRecord = new UserLessonRecord();
					userLessonRecord.setTeachingClassId(oldUserTask.getTeachingClassId());
					userLessonRecord.setChapter(new Chapter(oldUserTask.getChapterId()));
					userLessonRecord.setLesson(new Lesson(oldUserTask.getLessonId()));
					LessonTask lessonTask = lessonTaskService.get(oldUserTask.getLessonTaskId());
					userLessonRecord.setLessonTaskName(lessonTask.getTaskName());
					userLessonRecord.setTaskNum(CommonUtils.getTaskNum(oldUserTask.getCourseId(),oldUserTask.getLessonId()));
					userLessonRecordService.save(userLessonRecord);
				}
			}
		}catch (Exception e){}

		super.save(userTask);
	}

	/**
	 * 物理删除
	 * @param userTask
	 */
	@Transactional(readOnly=false)
	public void phyDeleteByEntity(UserTask userTask) {
		dao.phyDeleteByEntity(userTask);
	}


	/**
	 * 删除数据
	 * @param userTask
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(UserTask userTask) {
		super.delete(userTask);
	}

	/**
	 * 删除数据
	 * @param userTask
	 */
	@Transactional(readOnly=false)
	public void deleteByEntity(UserTask userTask) {
		dao.deleteByEntity(userTask);
	}

	/**
	 * 批量插入
	 * @param userTasks
	 */
	public void batchInsert(List<UserTask> userTasks) {
		dao.insertBatch(userTasks);
	}

	/**
	 * 获取视频排行
	 * @param userTask
	 * @param classId
	 * @return
	 */
	public List<VideoRanking> getVideoRanking(UserTask userTask, String classId){
		return dao.videoRanking(userTask, classId);
	}

	/**
	 * 获取分数排行
	 * @param userTask
	 * @param classId
	 * @return
	 */
	public List<ScoreRanking> getScoreRanking(UserTask userTask, String classId) {
		return dao.scoreRanking(userTask, classId);
	}

	/**
	 * 获取我的任务
	 * @return
	 */
	public List<LessonTask> findMyLessonTaskList(String teachingClassId,String lessonId){
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setLessonId(lessonId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findMyLessonTaskList(userTaskDto);
	}

	/**
	 * 判断是否存在学生任务
	 * @param userTask
	 * @return
	 */
    public boolean isExist(UserTask userTask) {
		return findCount(userTask) > 0;
    }

	/**
	 * 检查 节或小节的资源   是否全都完成
	 * @param teachingClassId
	 * @param lessonId
	 * @return
	 */
	public boolean checkIsFinishAllLessonTask(String teachingClassId, String lessonId){
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setLessonId(lessonId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		return dao.findUnFinishLessonTaskCount(userTaskDto) == 0 ;
	}

	/**
	 * 检查 节或小节的资源   是否全都完成
	 * 如果全都完成则保存  节或小节完成记录
	 * @param teachingClassId
	 * @param lessonTaskId
	 */
	@Transactional(readOnly = false)
	public void checkAndSaveIfFinishAllLessonTask(String teachingClassId, String lessonTaskId) {
		LessonTask lessonTask = lessonTaskService.get(lessonTaskId);
		checkAndSaveIfFinishAllSubOrLesson(teachingClassId,lessonTask.getLessonId());
	}


	/**
	 * 判断 ’节‘或者 ’小节‘ 是否已经全部完成
	 * @param teachingClassId
	 * @param lessonId
	 */
	@Transactional(readOnly = false)
	public void checkAndSaveIfFinishAllSubOrLesson(String teachingClassId, String lessonId) {
		Lesson lesson = lessonService.get(lessonId);
		if (LessonType.SUB_LESSON.value().equals(lesson.getLessonType())){
			if (checkIsFinishAllLessonTask(teachingClassId,lesson.getLessonId())){
				UserLesson userLesson = new UserLesson();
				userLesson.setLessonId(lesson.getLessonId());
				userLesson.setUserId(BaseUserUtils.getUser().getId());
				userLessonService.save(userLesson);

				checkAndSaveIfFinishAllSubLesson(lesson.getParentLesson(),teachingClassId);
			}
		}else if (LessonType.LESSON.value().equals(lesson.getLessonType())){
			checkAndSaveIfFinishAllSubLesson(lesson,teachingClassId);
		}
	}

	/**
	 * 判断 ’节‘或者 ’小节‘ 是否已经全部完成
	 * @param teachingClassId
	 * @param lessonId
	 */
	@Transactional(readOnly = false)
	public boolean checkIfFinishAllSubOrLesson(String teachingClassId, String lessonId) {
		Lesson lesson = lessonService.get(lessonId);

		boolean isFinishAllLesson = true;

		if (LessonType.SUB_LESSON.value().equals(lesson.getLessonType())){
			if (!checkIsFinishAllLessonTask(teachingClassId,lesson.getLessonId())){
				isFinishAllLesson =  false;
			}
		}else if (LessonType.LESSON.value().equals(lesson.getLessonType())){
			if (checkIsFinishAllLessonTask(teachingClassId,lesson.getLessonId())){
				Lesson subLessonCriteria = new Lesson();
				subLessonCriteria.setParentLesson(lesson);
				subLessonCriteria.setTeachingClassId(teachingClassId);
				List<Lesson> subLessonList = lessonService.findListHadPush(subLessonCriteria);
				for (Lesson subLesson : subLessonList){
					// 判断小节资源 是否全都完成
					if (!checkIsFinishAllLessonTask(teachingClassId,subLesson.getId())){
						UserLesson userLesson = new UserLesson();
						userLesson.setLessonId(subLesson.getId());
						userLessonService.deleteByEntity(userLesson);

						isFinishAllLesson = false;
					}
				}
			} else {
				isFinishAllLesson = false;
			}
		}

		return isFinishAllLesson;
	}


	/**
	 * 校验是否完成所有资源  有则保存进度
	 * @param userTask
	 */
	@Transactional(readOnly = false)
	public void checkAndSaveIfFinishAllLessonTask(UserTask userTask) {
		checkAndSaveIfFinishAllLessonTask(userTask.getTeachingClassId(),userTask.getLessonTaskId());
	}

	/**
	 * 校验 节下面的小节 或者节下面的资源  是否全都完成
	 * 如果全都完成则保存  节完成记录
	 */
	@Transactional(readOnly = false)
	public void checkAndSaveIfFinishAllSubLesson(Lesson lesson,String teachingClassId) {
		lesson = lessonService.get(lesson.getLessonId());
		if (LessonType.LESSON.value().equals(lesson.getLessonType())){
			if (checkIsFinishAllLessonTask(teachingClassId,lesson.getLessonId())
			 && userLessonService.checkIsFinishAllSubLesson(teachingClassId,lesson.getLessonId())){
				UserLesson userLesson = new UserLesson();
				userLesson.setLessonId(lesson.getLessonId());
				userLesson.setUserId(BaseUserUtils.getUser().getId());
				userLessonService.save(userLesson);

				if (lesson.getChapter() != null){
					checkAndSaveIfFinishAllLesson(lesson.getChapter(),teachingClassId);
				}
			}
		}
	}

	/**
	 * 判断章节下面的 ’节‘ 是否全部完成
	 */
	@Transactional(readOnly = false)
	public void checkAndSaveIfFinishAllLesson(Chapter chapter,String teachingClassId) {
		if (userChapterService.checkIfFinishAllLesson(teachingClassId,chapter.getChapterId())){
			UserChapter userChapter = new UserChapter();
			userChapter.setChapterId(chapter.getChapterId());
			userChapter.setUserId(BaseUserUtils.getUser().getId());
			userChapterService.save(userChapter);
		}
	}

	/**
	 * 获取未开始的学生列表
	 * @param teachingClassId
	 * @param chapterId
	 */
	public List<StudentVo> findUnStartStudents(String teachingClassId, String chapterId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		userTaskDto.setClassesId(teachingClass.getClasses().getClassesId());
		userTaskDto.setChapterId(chapterId);
		userTaskDto.setTeachingClassId(teachingClassId);
		return dao.findUnStartStudents(userTaskDto);
	}

	public List<StudentVo> findHadFinishStudents(String teachingClassId, String chapterId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		userTaskDto.setClassesId(teachingClass.getClasses().getClassesId());
		userTaskDto.setChapterId(chapterId);
		return dao.findHadFinishStudents(userTaskDto);
	}

	/**
	 * 获取学生进度详情
	 * @return
	 */
	public List<StudentVo> findStudentProgress(UserTaskDto userTaskDto) {
		TeachingClass teachingClass = teachingClassService.get(userTaskDto.getTeachingClassId());
		userTaskDto.setClassesId(teachingClass.getClasses().getClassesId());
		return dao.findStudentProgress(userTaskDto);
	}

	public  Long findTotalLessonTaskCount(UserTaskDto userTaskDto){
		return dao.findTotalLessonTaskCount(userTaskDto);
	}

	public Long findUnFinishLessonTaskCount(UserTaskDto userTaskDto){
		return dao.findUnFinishLessonTaskCount(userTaskDto);
	}

	public Float getAverageScore(UserTaskDto userTaskDto) {
		return dao.getAverageScore(userTaskDto);
	}

    public List<QualityMatrixVo> qualityMatrix(UserTaskDto userTaskDto) {

		LessonTask lessonTask = new LessonTask();
		lessonTask.setTeachingClassId(userTaskDto.getTeachingClassId());
		if (StringUtils.isNotBlank(userTaskDto.getChapterId())){
			lessonTask.setChapterId(userTaskDto.getChapterId());
		}
		// 总共推送的资源
		Long hadPushCount = lessonTaskService.findCountHadPush(lessonTask);
		Float totalObjectiveScore = lessonTaskService.getTotalObjectiveScore(userTaskDto);
		List<QualityMatrixVo> qualityMatrixVoList = dao.qualityMatrix(userTaskDto);
		qualityMatrixVoList.forEach(qualityMatrixVo -> {
			Integer hadFinishLessonTaskNum = qualityMatrixVo.getHadFinishLessonTaskNum();
			Float grossObjectiveScore = qualityMatrixVo.getGrossObjectiveScore();
			if(hadFinishLessonTaskNum == null){
				hadFinishLessonTaskNum = 0;
			}
			if (grossObjectiveScore == null){
				grossObjectiveScore = 0f;
			}

			Float synthesisScore = 0f;
			if (hadPushCount != 0){
				synthesisScore = (hadFinishLessonTaskNum*100)/hadPushCount.floatValue();
			}
			if (totalObjectiveScore != 0){
				synthesisScore += (grossObjectiveScore*100)/totalObjectiveScore;
			}
			qualityMatrixVo.setSynthesisScore(synthesisScore);

		});
		qualityMatrixVoList.sort(new Comparator<QualityMatrixVo>() {
			@Override
			public int compare(QualityMatrixVo o1, QualityMatrixVo o2) {
				return o1.getSynthesisScore() < o2.getSynthesisScore() ? 1 : -1;
			}
		});
		return qualityMatrixVoList;
    }

	public List<Map> getGroupSouresByscore(String teachingClassId, String userId, String type) {
		return dao.getGroupSouresByscore(teachingClassId,userId,type);
	}

	public List<Map> getGroupSouresByAvgscore(String teachingClassId, String type) {
		return dao.getGroupSouresByAvgscore(teachingClassId,type);
	}
	public List<Map> CompletedResources(String teachingClassId, String userId) {
		return dao.CompletedResources(teachingClassId,userId);
	}

	public List<Map> getGroupTimeAndFinishCount(String teachingClassId, String userId) {
		return dao.getGroupTimeAndFinishCount(teachingClassId,userId);
	}
}