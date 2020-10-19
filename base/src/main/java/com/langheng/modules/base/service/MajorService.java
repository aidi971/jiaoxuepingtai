/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.MajorDao;
import com.langheng.modules.base.entity.Academy;
import com.langheng.modules.base.entity.Major;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * sys_majorService
 * @author xiaoxie
 * @version 2020-02-15
 */
@Service
@Transactional(readOnly=true)
public class MajorService extends CrudService<MajorDao, Major> {

	@Autowired
	private AcademyService academyService;
	
	/**
	 * 获取单条数据
	 * @param major
	 * @return
	 */
	@Override
	public Major get(Major major) {
		return super.get(major);
	}
	
	/**
	 * 查询分页数据
	 * @param major 查询条件
	 * @param major.page 分页对象
	 * @return
	 */
	@Override
	public Page<Major> findPage(Major major) {
		return super.findPage(major);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param major
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Major major) {
		if (major.getIsNewRecord()){
			major.getAcademyId();
			Academy academy = academyService.get(major.getAcademyId());
			major.setFullName(academy.getAcademyName() + "/" + major.getMajorName());
		}
		super.save(major);
	}
	
	/**
	 * 更新状态
	 * @param major
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Major major) {
		super.updateStatus(major);
	}
	
	/**
	 * 删除数据
	 * @param major
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Major major) {
		super.delete(major);
	}
	
}