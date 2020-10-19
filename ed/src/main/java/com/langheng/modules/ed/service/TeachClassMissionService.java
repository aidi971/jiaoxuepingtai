/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.ed.dao.TeachClassMissionDao;
import com.langheng.modules.ed.entity.TeachClassMission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 课程任务关联Service
 * @author xiaoxie
 * @version 2020-08-11
 */
@Service
@Transactional(readOnly=true)
public class TeachClassMissionService extends CrudService<TeachClassMissionDao, TeachClassMission> {
	
	/**
	 * 获取单条数据
	 * @param teachClassMission
	 * @return
	 */
	@Override
	public TeachClassMission get(TeachClassMission teachClassMission) {
		return super.get(teachClassMission);
	}
	
	/**
	 * 查询分页数据
	 * @param teachClassMission 查询条件
	 * @param teachClassMission.page 分页对象
	 * @return
	 */
	@Override
	public Page<TeachClassMission> findPage(TeachClassMission teachClassMission) {
		return super.findPage(teachClassMission);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param teachClassMission
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(TeachClassMission teachClassMission) {
		super.save(teachClassMission);
	}
	
	/**
	 * 更新状态
	 * @param teachClassMission
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TeachClassMission teachClassMission) {
		super.updateStatus(teachClassMission);
	}
	
	/**
	 * 删除数据
	 * @param teachClassMission
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(TeachClassMission teachClassMission) {
		super.delete(teachClassMission);
	}
	
}