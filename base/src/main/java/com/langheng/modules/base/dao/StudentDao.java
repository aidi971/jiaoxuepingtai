package com.langheng.modules.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.base.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @ClassName TeacherDao
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-18 8:49
 * @Version 1.0
 */
@MyBatisDao(dataSourceName="ds2")
public interface StudentDao extends CrudDao<Student> {

    List<Student> findListByClasses(@Param("student")Student student,@Param("classesId") String classesId);

    String getMaxLoginCode(String suffixLoginCode);

    List<String>  selectUnJoinClassesStudent(@Param("limitTime") Date limitTime);
}
