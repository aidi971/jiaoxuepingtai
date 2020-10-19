/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.ClassesApplyDao;
import com.langheng.modules.base.entity.ClassesApply;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 加入班级申请Service
 * @author xiaoxie
 * @version 2020-03-30
 */
@Service
@Transactional(readOnly=true)
public class ClassesApplyService extends CrudService<ClassesApplyDao, ClassesApply> {
	
	/**
	 * 获取单条数据
	 * @param classesApply
	 * @return
	 */
	@Override
	public ClassesApply get(ClassesApply classesApply) {
		return super.get(classesApply);
	}
	
	/**
	 * 查询分页数据
	 * @param classesApply 查询条件
	 * @param classesApply.page 分页对象
	 * @return
	 */
	@Override
	public Page<ClassesApply> findPage(ClassesApply classesApply) {
		return super.findPage(classesApply);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param classesApply
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ClassesApply classesApply) {
		super.save(classesApply);
	}
	
	/**
	 * 更新状态
	 * @param classesApply
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ClassesApply classesApply) {
		super.updateStatus(classesApply);
	}
	
	/**
	 * 删除数据
	 * @param classesApply
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ClassesApply classesApply) {
		super.delete(classesApply);
	}

	@Transactional(readOnly=false)
	public void deleteByEntity(ClassesApply classesApply) {
		dao.deleteByEntity(classesApply);
	}
	
}