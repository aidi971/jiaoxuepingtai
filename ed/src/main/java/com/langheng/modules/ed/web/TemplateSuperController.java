/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.Template;
import com.langheng.modules.ed.enumn.TemplateState;
import com.langheng.modules.ed.service.CommonService;
import com.langheng.modules.ed.service.TemplateService;
import com.langheng.modules.ed.util.TemplateMsgUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "模板管理 | 管理员端")
@RestController
@RequestMapping(value = "admin/template")
public class TemplateSuperController extends BaseApiController {

	@Autowired
	private TemplateService templateService;
	@Autowired
	private CommonService commonService;

	@ModelAttribute
	public Template get(String templateId, boolean isNewRecord) {
		return templateService.get(templateId, isNewRecord);
	}

	@ApiOperation(value = "根据id查看模板详情")
	@PostMapping(value = "getDetail")
	public ResultBean<Map> getDetail(@RequestParam String templateId) {
		Map<String,Object> map = new HashMap<>();
		Template template = templateService.get(templateId);

		map.put("template",template);
		map.put("chapterList",commonService.expandByTemplate(template));
		return success(map);
	}

 	@ApiOperation(value = "分页查询模板列表数据")
	@PostMapping(value = "findPage")
	public ResultBean<Page<Template>>  findPage(Template template, HttpServletRequest request, HttpServletResponse response) {
		template.setPage(new Page<>(request, response));
		template.setIsCanSee(SysYesNoEnum.YES.value());
		Page<Template> page = templateService.findPage(template);
		return success(page);
	}

	@ApiOperation(value = "审核通过")
	@PostMapping(value = "agree")
	public ResultBean agree(@RequestParam String templateId) {
		Template template = templateService.get(templateId);
		template.setState(TemplateState.HAD_PUBLIC.value());
		template.setAuditTime(new Date());
		templateService.save(template);

		// 发送通知给教师
		TemplateMsgUtils.pushTemplateAuditMsg(template,template.getTeacher(),true);
		return success();
	}

	@ApiOperation(value = "审核拒绝")
	@PostMapping(value = "reject")
	public ResultBean reject(@RequestParam String templateId,
							 String  reason) {
		Template template = templateService.get(templateId);
		template.setState(TemplateState.HAD_REJECTED.value());
		template.setRemarks(reason);
		template.setAuditTime(new Date());
		templateService.save(template);

		// 发送通知给教师
		TemplateMsgUtils.pushTemplateAuditMsg(template,template.getTeacher(),false);
		return success();
	}

	@ApiOperation(value = "取消公开")
	@PostMapping(value = "cancel")
	public ResultBean cancel(@RequestParam String templateId) {
		Template template = templateService.get(templateId);
		template.setState(TemplateState.CANCEL.value());
		template.setCancelTime(new Date());
		templateService.save(template);
		return success();
	}

	@ApiOperation(value = "删除")
	@PostMapping(value = "delete")
	public ResultBean cancel1(@RequestParam String templateId) {
		Template template = templateService.get(templateId);
		template.setIsCanSee(SysYesNoEnum.NO.value());
		templateService.save(template);
		return success();
	}
}