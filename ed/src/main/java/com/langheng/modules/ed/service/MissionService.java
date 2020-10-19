/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.ed.dao.MissionDao;
import com.langheng.modules.ed.entity.Mission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程任务Service
 * @author xiaoxie
 * @version 2020-08-06
 */
@Service
@Transactional(readOnly=true)
public class MissionService extends CrudService<MissionDao, Mission> {
	
	/**
	 * 获取单条数据
	 * @param mission
	 * @return
	 */
	@Override
	public Mission get(Mission mission) {
		return super.get(mission);
	}
	
	/**
	 * 查询分页数据
	 * @param mission 查询条件
	 * @param mission.page 分页对象
	 * @return
	 */
	@Override
	public Page<Mission> findPage(Mission mission) {
		return super.findPage(mission);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param mission
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Mission mission) {
		super.save(mission);
	}
	
	/**
	 * 更新状态
	 * @param mission
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Mission mission) {
		super.updateStatus(mission);
	}
	
	/**
	 * 删除数据
	 * @param mission
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Mission mission) {
		super.delete(mission);
	}

	public void insertBatch(List<Mission> newMissionList) {
		dao.insertBatch(newMissionList);
	}

    public List<Mission> findListHadPush(Mission missCriteria) {
		return null;
    }
}