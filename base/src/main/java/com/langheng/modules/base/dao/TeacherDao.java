package com.langheng.modules.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.base.entity.Teacher;

/**
 * @ClassName TeacherDao
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-18 8:49
 * @Version 1.0
 */
@MyBatisDao(dataSourceName="ds2")
public interface TeacherDao extends CrudDao<Teacher> {
}
