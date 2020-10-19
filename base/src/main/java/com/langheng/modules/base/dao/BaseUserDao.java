package com.langheng.modules.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.base.entity.BaseUser;

@MyBatisDao(dataSourceName="ds2")
public interface BaseUserDao extends CrudDao<BaseUser> {
    BaseUser getByLoginCode(BaseUser var1);
}