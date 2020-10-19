/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.vo.ScoreRanking;
import com.langheng.modules.ed.vo.VideoRanking;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 学生任务DAO接口
 * @author xiaoxie
 * @version 2019-12-17
 */
@MyBatisDao(dataSourceName="ds2")
public interface UserTaskDao extends CrudDao<UserTask> {

    @Select("SELECT a.user_id,b.user_name as student_name,b.login_code,b.cover_img,count(a.user_id) as count \n" +
            "from ed_user_task as a " +
            "LEFT JOIN sys_user as b ON a.user_id=b.user_code \n" +
            "WHERE user_id in \n" +
            "(SELECT student_id from sys_student_classes as u12 where u12.classes_id = #{classesId})\n" +
            "AND a.teaching_class_id=#{userTask.teachingClassId} AND a.course_id=#{userTask.courseId} AND a.type=#{userTask.type} AND a.state=#{userTask.state}\n" +
            "GROUP BY a.user_id,b.user_name \n" +
            "ORDER BY count DESC,a.update_date")
    List<VideoRanking> videoRanking(UserTask userTask, String classesId);


    @Select("SELECT a.user_id,b.user_name as student_name,b.login_code,b.cover_img,sum(a.score) as count \n \n" +
            "from ed_user_task  as a " +
            "LEFT JOIN sys_user as b ON a.user_id=b.user_code \n" +
            "WHERE user_id in \n" +
            "(SELECT student_id from sys_student_classes as u12 where u12.classes_id = #{classId})\n" +
            "AND a.teaching_class_id=#{userTask.teachingClassId} AND a.course_id=#{userTask.courseId} AND a.type in (2,3,6)\n" +
            "GROUP BY a.user_id\n" +
            "ORDER BY count DESC,a.update_date")
    List<ScoreRanking> scoreRanking(UserTask userTask, String classId);

    /**
     * 获取我的资源列表
     * @param userTaskDto
     * @return
     */
    List<LessonTask> findMyLessonTaskList(UserTaskDto userTaskDto);

    /**
     * 获取已经完成的资源数（已推送）
     * @param userTaskDto
     * @return
     */
    Long findUnFinishLessonTaskCount(UserTaskDto userTaskDto);

    /**
     * 获取总的的资源数 （已推送）
     * @param userTaskDto
     * @return
     */
    Long findTotalLessonTaskCount(UserTaskDto userTaskDto);

    List<StudentVo> findUnStartStudents(UserTaskDto userTaskDto);

    List<StudentVo> findHadFinishStudents(UserTaskDto userTaskDto);

    List<StudentVo> findStudentProgress(UserTaskDto userTaskDto);

    Float getAverageScore(UserTaskDto userTaskDto);

    List<QualityMatrixVo> qualityMatrix(UserTaskDto userTaskDto);

    @Select("select elt.lesson_task_id,elt.task_name,eut.`type`,eut.score,\n" +
            "(select avg(score) from ed_user_task \n" +
            "where state='2' and type=eut.`type` " +
            "and teaching_class_id=eut.teaching_class_id and lesson_task_id=eut.lesson_task_id) as avg\n" +
            "from ed_user_task eut\n" +
            "left join ed_lesson_task elt\n" +
            "on eut.lesson_task_id=elt.lesson_task_id\n" +
            "where eut.state='2' and eut.`type`=#{type} and eut.teaching_class_id=#{teachingClassId} and eut.user_id=#{userId}\n" +
            "group by eut.lesson_task_id\n" +
            "order by elt.sort")
    List<Map> getGroupSouresByscore(String teachingClassId, String userId, String type);

    @Select("select elt.lesson_task_id,elt.task_name,eut.`type`,avg(eut.score) as avg,max(eut.score) as max,min(eut.score) as min\n" +
            "from ed_user_task eut\n" +
            "left join ed_lesson_task elt\n" +
            "on eut.lesson_task_id=elt.lesson_task_id\n" +
            "where eut.state='2' and eut.`type`=#{type} and eut.teaching_class_id=#{teachingClassId}\n" +
            "group by eut.lesson_task_id\n" +
            "order by elt.sort")
    List<Map> getGroupSouresByAvgscore(String teachingClassId, String type);

    @Select("select type,count(1) as sum\n" +
            "from ed_user_task\n" +
            "where type !='5' and state='2' and user_id=#{userId} and teaching_class_id=#{teachingClassId}\n" +
            "group by type")
    List<Map> CompletedResources(String teachingClassId, String userId);

    @Select("SELECT DATE_FORMAT(finish_time, '%Y-%m-%d') AS time, COUNT(1) AS num\n" +
            "FROM ed_user_task\n" +
            "where user_id=#{userId} and teaching_class_id=#{teachingClassId}\n" +
            "and state = '2' and `status`='0'\n" +
            "GROUP BY time\n" +
            "ORDER BY time ASC;")
    List<Map> getGroupTimeAndFinishCount(String teachingClassId, String userId);
}