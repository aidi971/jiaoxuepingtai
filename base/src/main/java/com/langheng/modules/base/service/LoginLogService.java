/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.LoginLogDao;
import com.langheng.modules.base.entity.LoginLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 系统访问记录Service
 * @author xiaoxie
 * @version 2020-02-17
 */
@Service
@Transactional(readOnly=true)
public class LoginLogService extends CrudService<LoginLogDao, LoginLog> {
	
	/**
	 * 获取单条数据
	 * @param loginLog
	 * @return
	 */
	@Override
	public LoginLog get(LoginLog loginLog) {
		return super.get(loginLog);
	}
	
	/**
	 * 查询分页数据
	 * @param loginLog 查询条件
	 * @param loginLog.page 分页对象
	 * @return
	 */
	@Override
	public Page<LoginLog> findPage(LoginLog loginLog) {
		return super.findPage(loginLog);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param loginLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(LoginLog loginLog) {
		super.save(loginLog);
	}
	
	/**
	 * 更新状态
	 * @param loginLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(LoginLog loginLog) {
		super.updateStatus(loginLog);
	}
	
	/**
	 * 删除数据
	 * @param loginLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(LoginLog loginLog) {
		super.delete(loginLog);
	}

    public Page<LoginLog> findPageJoinClasses(LoginLog loginLog) {
		Page page = loginLog.getPage();
		List<LoginLog> loginLogList = dao.findPageJoinClasses(loginLog);
		page.setList(loginLogList);
		page.setCount(loginLogList.size());
		return page;
    }

    public List<Map<String, Object>> everyMouthStatByClasses(String classesId) {
		return dao.everyMouthStatByClasses(classesId);
    }

	public List<Map<String, Object>> everyMouthStat() {
		return dao.everyMouthStat();
	}

	public List<Map<String, Object>> everyMouthStatByUser(String studentId) {
		return dao.everyMouthStatByUser(studentId);
	}
}