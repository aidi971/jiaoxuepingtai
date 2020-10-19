/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.AcademyDao;
import com.langheng.modules.base.entity.Academy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学院Service
 * @author xiaoxie
 * @version 2019-12-20
 */
@Service
@Transactional(readOnly=true)
public class AcademyService extends CrudService<AcademyDao, Academy> {

	@Autowired
	private SchoolService schoolService;
	/**
	 * 获取单条数据
	 * @param academy
	 * @return
	 */
	@Override
	public Academy get(Academy academy) {
		return super.get(academy);
	}
	
	/**
	 * 查询分页数据
	 * @param academy 查询条件
	 * @param academy.page 分页对象
	 * @return
	 */
	@Override
	public Page<Academy> findPage(Academy academy) {
		return super.findPage(academy);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param academy
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Academy academy) {
		super.save(academy);
	}
	
	/**
	 * 更新状态
	 * @param academy
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Academy academy) {
		super.updateStatus(academy);
	}
	
	/**
	 * 删除数据
	 * @param academy
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Academy academy) {
		super.delete(academy);
	}

}