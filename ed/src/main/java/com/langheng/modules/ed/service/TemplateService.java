/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.ed.dao.TemplateDao;
import com.langheng.modules.ed.entity.Template;
import com.langheng.modules.ed.enumn.LessonTaskType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 模板Service
 * @author xiaoxie
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class TemplateService extends CrudService<TemplateDao, Template> {
	
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
	 * @param template.page 分页对象
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

	@Transactional(readOnly=false)
	public void updateAllLessonTaskNum(String templateId) {
		if (StringUtils.isNotBlank(templateId)){
			dao.updateLessonTaskNum(templateId, LessonTaskType.COURSEWARE.value());
			dao.updateLessonTaskNum(templateId, LessonTaskType.VIDEO.value());
			dao.updateLessonTaskNum(templateId, LessonTaskType.OBJECTIVES.value());
		}
	}

}