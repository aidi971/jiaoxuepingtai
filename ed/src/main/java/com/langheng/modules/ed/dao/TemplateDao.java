/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.ed.entity.Template;

/**
 * 模板DAO接口
 * @author xiaoxie
 * @version 2019-12-17
 */
@MyBatisDao(dataSourceName="ds2")
public interface TemplateDao extends CrudDao<Template> {

    void updateLessonTaskNum(String templateId,String type);

}