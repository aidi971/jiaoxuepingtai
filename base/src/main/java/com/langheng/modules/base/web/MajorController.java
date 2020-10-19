/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.base.entity.Academy;
import com.langheng.modules.base.entity.Major;
import com.langheng.modules.base.service.AcademyService;
import com.langheng.modules.base.service.MajorService;
import com.langheng.modules.base.webservice.Annotation.MyAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专业Controller
 * @author xiaoxie
 * @version 2020-02-15
 */
@Api(description = "专业")
@RestController
@RequestMapping(value = "admin/major")
public class MajorController extends BaseApiController {

	@Autowired
	private MajorService majorService;
	@Autowired
	private AcademyService academyService;
	
	/**
	 * 获取专业数据
	 */
	@ModelAttribute
	public Major get(String majorId, boolean isNewRecord) {
		return majorService.get(majorId, isNewRecord);
	}

	@MyAnnotation
 	@ApiOperation(value = "根据学院id查询专业列表数据")
	@PostMapping(value = "findList")
	public ResultBean<List<Major>>  findPage(@RequestParam String academyId) {
		Major major = new Major();
		major.setAcademyId(academyId);
		List<Major> list = majorService.findList(major);
		return success(list);
	}

	@MyAnnotation
	@ApiOperation(value = "保存专业")
	@PostMapping(value = "save")
	public ResultBean save(@Validated Major major) {
		Major judgeName = new Major();
		judgeName.setMajorName(major.getMajorName());
		judgeName.setAcademyId(major.getAcademyId());
		List<Major> list = majorService.findList(judgeName);
		if (list.size()!=0) {
			return success("同一院系下不可建立重复专业");
		}
		Major majorCount = new Major();
		majorCount.setAcademyId(major.getAcademyId());
		Integer count = (int) majorService.findCount(majorCount);
		if (count>=20) {
			return success("学院及同一学院下专业最多只能录20个");
		}
		String msg = EntityUtils.saveOrUpdate(major);
//		修改学院状态
		Academy academy = new Academy();
		academy.setAcademyId(major.getAcademyId());
		academy.setAcademyStatus("1");
		academyService.save(academy);
		majorService.save(major);
		return success(msg);
	}

	@MyAnnotation
	@ApiOperation(value = "修改专业")
	@PostMapping(value = "update")
	public ResultBean update(@Validated Major major) {
		Major judgeName = new Major();
		judgeName.setMajorName(major.getMajorName());
		judgeName.setAcademyId(major.getAcademyId());
		List<Major> list = majorService.findList(judgeName);
		if (list.size()!=0) {
			return success("同一院系下不可建立重复专业");
		}
//		Major majorCount = new Major();
//		majorCount.setAcademyId(major.getAcademyId());
//		Integer count = (int) majorService.findCount(majorCount);
//		if (count>=20) {
//			return success("学院及同一学院下专业最多只能录20个");
//		}
		majorService.save(major);
		return success("修改成功");
	}


	@MyAnnotation
	@ApiOperation(value = "删除专业")
	@PostMapping(value = "delete")
	public ResultBean delete(Major major) {
		Major major2 = majorService.get(major);
		majorService.delete(major);
		Major major1 = new Major();
		major1.setAcademyId(major2.getAcademyId());
		List<Major> list = majorService.findList(major1);
		if (list.size()==0) {
			Academy academy = new Academy();
			academy.setAcademyId(major.getAcademyId());
			academy.setAcademyStatus("0");
			academyService.save(academy);
		}
		return success("删除成功");
	}


}