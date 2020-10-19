/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.controller;

import com.jeesite.common.entity.TokenInfo;
import com.jeesite.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ed_chapterController
 * @author xiaoxie
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/base/onlineUser")
public class OnlineUserController extends BaseController {


	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(TokenInfo tokenInfo, Model model) {
		model.addAttribute("tokenInfo", tokenInfo);
		return "modules/base/onlineUser";
	}
	


}