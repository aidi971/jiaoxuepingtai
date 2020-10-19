/**
 * Copyright (c) 2019-Now http://langheng.com All rights reserved.
 */
package com.langheng.modules.ed.service;


import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.ed.dao.MissionUserTaskDao;
import com.langheng.modules.ed.entity.UserTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生任务Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class MissionUserTaskService extends CrudService<MissionUserTaskDao, UserTask> {

	/**
	 * 获取单条数据
	 * @param userTask
	 * @return
	 */
	@Override
	public UserTask get(UserTask userTask) {
		return super.get(userTask);
	}

	/**
	 * 查询分页数据
	 * @param userTask 查询条件
	 * @return
	 */
	@Override
	public Page<UserTask> findPage(UserTask userTask) {
		return super.findPage(userTask);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param userTask
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(UserTask userTask) {
		super.save(userTask);
	}

	/**
	 * 更新状态
	 * @param userTask
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(UserTask userTask) {
		super.updateStatus(userTask);
	}

	/**
	 * 删除数据
	 * @param userTask
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(UserTask userTask) {
		super.delete(userTask);
	}

    public void phyDeleteByEntity(UserTask userTask) {
    }
}