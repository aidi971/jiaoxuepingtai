/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.controller;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.service.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户管理Controller
 * @author xiaoxie
 * @version 2020-04-08
 */
@Controller
@RequestMapping(value = "${adminPath}/base/baseUser")
public class BaseUserAdminController extends BaseController {

	@Autowired
	private BaseUserService baseUserService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BaseUser get(String userCode, boolean isNewRecord) {
		return baseUserService.get(userCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(BaseUser baseUser, Model model) {
		model.addAttribute("baseUser", baseUser);
		return "modules/base/baseUserList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BaseUser> listData(BaseUser baseUser, HttpServletRequest request, HttpServletResponse response) {
		baseUser.setPage(new Page<>(request, response));
		Page<BaseUser> page = baseUserService.findPage(baseUser);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(BaseUser baseUser, Model model) {
		model.addAttribute("baseUser", baseUser);
		return "modules/base/baseUserForm";
	}

	/**
	 * 保存用户管理
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BaseUser baseUser) {
		baseUserService.save(baseUser);
		return renderResult(Global.TRUE, text("保存用户管理成功！"));
	}
	
	/**
	 * 删除用户管理
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BaseUser baseUser) {
		baseUserService.delete(baseUser);
		return renderResult(Global.TRUE, text("删除用户管理成功！"));
	}
	
}