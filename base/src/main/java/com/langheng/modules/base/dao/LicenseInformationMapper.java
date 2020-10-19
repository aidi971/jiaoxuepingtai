package com.langheng.modules.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.base.entity.LicenseInformation;

@MyBatisDao(dataSourceName="ds2")
public interface LicenseInformationMapper extends CrudDao<LicenseInformation> {
}
