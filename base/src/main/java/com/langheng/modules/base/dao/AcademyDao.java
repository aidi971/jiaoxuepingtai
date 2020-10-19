/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.base.entity.Academy;

/**
 * 学院DAO接口
 * @author xiaoxie
 * @version 2019-12-20
 */
@MyBatisDao(dataSourceName="ds2")
public interface AcademyDao extends CrudDao<Academy> {
	
}