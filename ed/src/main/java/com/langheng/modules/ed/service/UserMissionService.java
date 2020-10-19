/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.ed.dao.UserMissionDao;
import com.langheng.modules.ed.entity.UserMission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生课程任务项Service
 * @author xiaoxie
 * @version 2020-08-11
 */
@Service
@Transactional(readOnly=true)
public class UserMissionService extends CrudService<UserMissionDao, UserMission> {
	
	/**
	 * 获取单条数据
	 * @param userMission
	 * @return
	 */
	@Override
	public UserMission get(UserMission userMission) {
		return super.get(userMission);
	}
	
	/**
	 * 查询分页数据
	 * @param userMission 查询条件
	 * @param userMission.page 分页对象
	 * @return
	 */
	@Override
	public Page<UserMission> findPage(UserMission userMission) {
		return super.findPage(userMission);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param userMission
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(UserMission userMission) {
		super.save(userMission);
	}
	
	/**
	 * 更新状态
	 * @param userMission
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(UserMission userMission) {
		super.updateStatus(userMission);
	}
	
	/**
	 * 删除数据
	 * @param userMission
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(UserMission userMission) {
		super.delete(userMission);
	}
	
}