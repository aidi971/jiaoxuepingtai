/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.School;
import com.langheng.modules.base.service.SchoolService;
import com.langheng.modules.base.utils.uplocadUtil;
import com.langheng.modules.base.webservice.WebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 学校Controller
 * @author xiaoxie
 * @version 2019-12-20
 */
@Api(description = "学校")
@RestController
@RequestMapping(value = "/admin/school")
public class SchoolController extends BaseApiController {

	@Autowired
	private SchoolService schoolService;
	@Autowired
	private WebService webService;

	@Value("${web.upload-path}")
	private String filePath;  //获取上传地址
	@Value("${uploadF.logoPath}")
	public  String logo;
	/**
	 * 获取学校数据
	 */
	@ModelAttribute
	public School get(String schoolId, boolean isNewRecord) {
		return schoolService.get(schoolId, isNewRecord);
	}
	
	@ApiOperation(value = "更新学校信息")
	@PostMapping(value = "save")
	public ResultBean save(@Validated School school) {
		school.setSchoolId(schoolService.getDefaultSchool().getSchoolId());
		String msg="";
		if (school.getIsNewRecord()) {
			msg="添加成功";
		}else {
			msg="修改成功";
		}
		schoolService.save(school);
		return success(msg);
	}

//	@ApiOperation(value = "创建学校")
//	@PostMapping(value = "saveScool")
//	public ResultBean saveScool(@Validated School school) {
//		school.setLogo("static/coverHead/school/默认.png");
//		schoolService.save(school);
//		return success();
//	}

	@ApiOperation(value = "获取学校信息")
	@GetMapping(value = "get")
	public ResultBean<School> get() {
		return success(schoolService.getDefaultSchool());
	}
//	@ApiOperation(value = "获取学校信息")
//	@GetMapping(value = "get")
//	@MyAnnotation
//	public ResultBean<School> get() {
//		School defaultSchool = schoolService.getDefaultSchool();
//		Authorization authorization = webService.getWebService(true);
//		defaultSchool.setSchoolName(authorization.getSchoolName());
//		return success(defaultSchool);
//	}

	@ApiOperation(value ="上传logo")
	@PostMapping("uploadLogo")
	public ResultBean uploadLogo(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) {
		String executeUpload="";
		String realPath="";
		try {
			 realPath = filePath+logo;
			 FileUtils.createDirectory(realPath);
			 executeUpload = uplocadUtil.executeUpload(realPath, file,logo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success(executeUpload);
	}


}