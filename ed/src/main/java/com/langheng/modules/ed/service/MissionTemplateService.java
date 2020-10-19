/**
 * Copyright (c) 2019-Now http://langheng.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.ed.dao.MissionTemplateDao;
import com.langheng.modules.ed.entity.Template;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 模板Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class MissionTemplateService extends CrudService<MissionTemplateDao, Template> {

	/**
	 * 获取单条数据
	 * @param template
	 * @return
	 */
	@Override
	public Template get(Template template) {
		return super.get(template);
	}

	/**
	 * 查询分页数据
	 * @param template 查询条件
	 * @return
	 */
	@Override
	public Page<Template> findPage(Template template) {
		return super.findPage(template);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param template
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Template template) {
		super.save(template);
	}

	/**
	 * 更新状态
	 * @param template
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Template template) {
		super.updateStatus(template);
	}

	/**
	 * 删除数据
	 * @param template
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Template template) {
		super.delete(template);
	}

}