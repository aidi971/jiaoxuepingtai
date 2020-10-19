/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.SchoolDao;
import com.langheng.modules.base.entity.School;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学校Service
 * @author xiaoxie
 * @version 2019-12-20
 */
@Service
@Transactional(readOnly=true)
public class SchoolService extends CrudService<SchoolDao, School> {
	
	/**
	 * 获取单条数据
	 * @param school
	 * @return
	 */
	@Override
	public School get(School school) {
		return super.get(school);
	}
	
	/**
	 * 查询分页数据
	 * @param school 查询条件
	 * @param school.page 分页对象
	 * @return
	 */
	@Override
	public Page<School> findPage(School school) {
		return super.findPage(school);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param school
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(School school) {
		super.save(school);
	}
	
	/**
	 * 更新状态
	 * @param school
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(School school) {
		super.updateStatus(school);
	}
	
	/**
	 * 删除数据
	 * @param school
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(School school) {
		super.delete(school);
	}

	public School getDefaultSchool(){
		return get("1207934534334636032");
	}
	
}