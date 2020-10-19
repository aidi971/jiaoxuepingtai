/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.base.entity.Academy;
import com.langheng.modules.base.entity.School;
import com.langheng.modules.base.service.AcademyService;
import com.langheng.modules.base.service.SchoolService;
import com.langheng.modules.base.webservice.Annotation.MyAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学院Controller
 * @author xiaoxie
 * @version 2019-12-20
 */
@Api(description = "学院")
@RestController
@RequestMapping(value = "admin/academy")
public class AcademyController extends BaseApiController {

	@Autowired
	private AcademyService academyService;
	@Autowired
	private SchoolService schoolService;
	
	/**
	 * 获取学院数据
	 */
	@ModelAttribute
	public Academy get(String academyId, boolean isNewRecord) {
		return academyService.get(academyId, isNewRecord);
	}

	@MyAnnotation
 	@ApiOperation(value = "查询学院列表数据")
	@PostMapping(value = "findList")
	public ResultBean<List<Academy>>  findList() {
		Academy academy = new Academy();
		academy.setAcademyStatus("1");
		List<Academy> academies = academyService.findList(academy);
		return success(academies);
	}

	@MyAnnotation
	@ApiOperation(value = "admin查询学院列表数据")
	@PostMapping(value = "adminFindList")
	public ResultBean<List<Academy>>  adminFindList() {
		Academy academy = new Academy();
		List<Academy> academies = academyService.findList(academy);
		return success(academies);
	}

	@MyAnnotation
	@ApiOperation(value = "保存或修改学院")
	@PostMapping(value = "save")
	public ResultBean save(@Validated Academy academy) {
		School defaultSchool = schoolService.getDefaultSchool();
		academy.setSchool(defaultSchool);
		Academy judgeName = new Academy();
		judgeName.setAcademyName(academy.getAcademyName());
		judgeName.setSchool(defaultSchool);
		List<Academy> list = academyService.findList(judgeName);
		if (list.size()!=0) {
			return success("不可建立重复学院");
		}
		Academy academyCount = new Academy();
		academyCount.setSchool(defaultSchool);
		Integer count = (int)academyService.findCount(academyCount);
		if (StringUtils.isBlank(academy.getAcademyId())) {
			if (count>=20) {
				return success("学院及同一学院下专业最多只能录20个");
			}
		}
		String msg = EntityUtils.saveOrUpdate(academy);

		academyService.save(academy);
		return success(msg);
	}

	@MyAnnotation
	@ApiOperation(value = "删除学院")
	@PostMapping(value = "delete")
	public ResultBean delete(Academy academy) {
		academyService.delete(academy);
		return success("删除成功");
	}


}