/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.ed.dao.UserLessonRecordDao;
import com.langheng.modules.ed.entity.UserLessonRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 小节任务记录Service
 * @author xiaoxie
 * @version 2020-04-29
 */
@Service
@Transactional(readOnly=true)
public class UserLessonRecordService extends CrudService<UserLessonRecordDao, UserLessonRecord> {

	@Autowired
	private LessonService lessonService;
	
	/**
	 * 获取单条数据
	 * @param userLessonRecord
	 * @return
	 */
	@Override
	public UserLessonRecord get(UserLessonRecord userLessonRecord) {
		return super.get(userLessonRecord);
	}
	
	/**
	 * 查询分页数据
	 * @param userLessonRecord 查询条件
	 * @return
	 */
	@Override
	public Page<UserLessonRecord> findPage(UserLessonRecord userLessonRecord) {
		return super.findPage(userLessonRecord);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param userLessonRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(UserLessonRecord userLessonRecord) {
		userLessonRecord.setStudent(BaseUserUtils.getUser());
		userLessonRecord.setFinishTime(new Date());
		super.save(userLessonRecord);
	}
	
	/**
	 * 更新状态
	 * @param userLessonRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(UserLessonRecord userLessonRecord) {
		super.updateStatus(userLessonRecord);
	}
	
	/**
	 * 删除数据
	 * @param userLessonRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(UserLessonRecord userLessonRecord) {
		super.delete(userLessonRecord);
	}
	
}