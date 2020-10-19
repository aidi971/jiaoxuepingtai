/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.utils.TeacherUtils;
import com.langheng.modules.ed.entity.Chapter;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.Template;
import com.langheng.modules.ed.enumn.TemplateState;
import com.langheng.modules.ed.enumn.TemplateType;
import com.langheng.modules.ed.service.CommonService;
import com.langheng.modules.ed.service.TemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模板Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "模板管理 | 教师端")
@RestController
@RequestMapping(value = "api/template")
public class TemplateApi extends BaseApiController {

	@Autowired
	private TemplateService templateService;
	@Autowired
	private CommonService commonService;

	/**
	 * 获取模板数据
	 */
	@ModelAttribute
	public Template get(String templateId, boolean isNewRecord) {
		return templateService.get(templateId, isNewRecord);
	}

	@ApiOperation(value = "展开模板，显示所有章节-节-小节-资源")
	@PostMapping(value = "expand")
	public ResultBean<List<Chapter>> expand(@RequestParam String templateId) {
		Template template = templateService.get(templateId);
		if (template != null){
			return success(commonService.expandByTemplate(template));
		}
		return error("模板参数错误！");
	}

	@ApiOperation(value = "获取标准模板列表")
	@PostMapping(value = "findStandardTemplateList")
	public ResultBean<List<Template>>  findStandardTemplateList() {
		Template template = new Template();
		template.setType(TemplateType.STANDARD.value());
		List<Template> templateList = templateService.findList(template);
		return success(templateList);
	}

	@ApiOperation(value = "获取公开模板列表")
	@PostMapping(value = "findPublicTemplateList")
	public ResultBean<List<Template>>  findPublicTemplateList() {
		Template template = new Template();
		template.setType(TemplateType.PERSONAL.value());
		template.setState(TemplateState.HAD_PUBLIC.value());
		List<Template> templateList = templateService.findList(template);
		return success(templateList);
	}

	@ApiOperation(value = "获取我的模板列表")
	@PostMapping(value = "findMyTemplateList")
	public ResultBean<List<Template>>  findMyTemplateList() {
		Template template = new Template();
		template.setTeacher(TeacherUtils.getTeacher());
		template.setType(TemplateType.PERSONAL.value());
		List<Template> templateList = templateService.findList(template);
		return success(templateList);
	}

    @ApiOperation(value = "模板引用（或者收藏）转存课程")
    @PostMapping(value = "templateSaveAsCourse")
    public ResultBean templateSaveAsCourse(String templateId) {
		Template template = templateService.get(templateId);
		Course newCourse = commonService.templateSaveAsCourse(template);
        return success(newCourse);
    }
}