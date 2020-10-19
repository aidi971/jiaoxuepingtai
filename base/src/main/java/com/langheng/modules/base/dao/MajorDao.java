/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.base.entity.Major;

/**
 * sys_majorDAO接口
 * @author xiaoxie
 * @version 2020-02-15
 */
@MyBatisDao(dataSourceName="ds2")
public interface MajorDao extends CrudDao<Major> {
	
}