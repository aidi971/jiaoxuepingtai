/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.MsgUserDao;
import com.langheng.modules.base.entity.MsgUser;
import com.langheng.modules.base.enumn.MsgUserState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户消息Service
 * @author xiaoxie
 * @version 2020-05-08
 */
@Service
@Transactional(readOnly=true)
public class MsgUserService extends CrudService<MsgUserDao, MsgUser> {
	
	/**
	 * 获取单条数据
	 * @param msgUser
	 * @return
	 */
	@Override
	public MsgUser get(MsgUser msgUser) {
		return super.get(msgUser);
	}
	
	/**
	 * 查询分页数据
	 * @param msgUser 查询条件
	 * @return
	 */
	@Override
	public Page<MsgUser> findPage(MsgUser msgUser) {
		return super.findPage(msgUser);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param msgUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(MsgUser msgUser) {
		if (msgUser.getIsNewRecord()){
			msgUser.setState(MsgUserState.UN_READ.value());
		}
		super.save(msgUser);
	}

	@Transactional(readOnly=false)
	public void insertBatch(List<MsgUser> msgUserList) {
		msgUserList.forEach(msgUser -> {
			msgUser.setState(MsgUserState.UN_READ.value());
		});
		dao.insertBatch(msgUserList);
	}
	
	/**
	 * 更新状态
	 * @param msgUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(MsgUser msgUser) {
		super.updateStatus(msgUser);
	}
	
	/**
	 * 删除数据
	 * @param msgUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(MsgUser msgUser) {
		super.delete(msgUser);
	}

    public MsgUser getOne(MsgUser msgUser) {
		List<MsgUser> msgUserList = findList(msgUser);
		if (!msgUserList.isEmpty()){
			return msgUserList.get(0);
		}
		return null;
    }
}