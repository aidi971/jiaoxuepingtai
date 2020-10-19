/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.MsgDao;
import com.langheng.modules.base.entity.Msg;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统消息管理Service
 * @author xiaoxie
 * @version 2020-05-08
 */
@Service
@Transactional(readOnly=true)
public class MsgService extends CrudService<MsgDao, Msg> {
	
	/**
	 * 获取单条数据
	 * @param msg
	 * @return
	 */
	@Override
	public Msg get(Msg msg) {
		return super.get(msg);
	}
	
	/**
	 * 查询分页数据
	 * @param msg 查询条件
	 * @param msg.page 分页对象
	 * @return
	 */
	@Override
	public Page<Msg> findPage(Msg msg) {
		return super.findPage(msg);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param msg
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Msg msg) {
		super.save(msg);
	}
	
	/**
	 * 更新状态
	 * @param msg
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Msg msg) {
		super.updateStatus(msg);
	}
	
	/**
	 * 删除数据
	 * @param msg
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Msg msg) {
		super.delete(msg);
	}
	
}