/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.RegisterDao;
import com.langheng.modules.base.entity.Register;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 注册管理Service
 * @author xiaoxie
 * @version 2020-07-29
 */
@Service
@Transactional(readOnly=true)
public class RegisterService extends CrudService<RegisterDao, Register> {
	
	/**
	 * 获取单条数据
	 * @param register
	 * @return
	 */
	@Override
	public Register get(Register register) {
		return super.get(register);
	}
	
	/**
	 * 查询分页数据
	 * @param register 查询条件
	 * @return
	 */
	@Override
	public Page<Register> findPage(Register register) {
		return super.findPage(register);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param register
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Register register) {
		if (register.getIsNewRecord()){
			preDelete(register.getLoginCode());
		}
		super.save(register);
	}
	
	/**
	 * 更新状态
	 * @param register
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Register register) {
		super.updateStatus(register);
	}
	
	/**
	 * 删除数据
	 * @param register
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Register register) {
		super.delete(register);
	}

	@Transactional(readOnly=false)
	public void preDelete(String loginCode) {
		if (StringUtils.isNotBlank(loginCode)){
			Register register = new Register();
			register.setLoginCode(loginCode);
			dao.phyDeleteByEntity(register);
		}
	}

	public Page<Register> findInUseRegisterPage(Register register) {
		Page page = register.getPage();
		List<Register> registerList = dao.findInUseRegisterList(register);
		page.setList(registerList);
		page.setCount(registerList.size());
		return page;
	}
}