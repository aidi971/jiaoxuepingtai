/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.Mission;
import com.langheng.modules.ed.entity.Template;
import com.langheng.modules.ed.service.CommonService;
import com.langheng.modules.ed.service.TemplateService;
import com.langheng.modules.ed.util.MissionCommonUtils;
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
@Api(description = "任务型课程-模板管理 | 教师端")
@RestController
@RequestMapping(value = "api/missionTemplate")
public class MissionTemplateApi extends BaseApiController {

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
	public ResultBean<List<Mission>> expand(@RequestParam String templateId) {
		Template template = templateService.get(templateId);
		if (template != null){
			return success(MissionCommonUtils.expandByTemplate(template));
		}
		return error("模板参数错误！");
	}

    @ApiOperation(value = "模板引用（或者收藏）转存课程")
    @PostMapping(value = "templateSaveAsCourse")
    public ResultBean templateSaveAsCourse(String templateId) {
		Template template = templateService.get(templateId);
		Course newCourse = MissionCommonUtils.templateSaveAsCourse(template);
        return success(newCourse);
    }
}