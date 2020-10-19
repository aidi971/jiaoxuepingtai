/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.Mission;
import com.langheng.modules.ed.service.MissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 课程任务Controller
 * @author xiaoxie
 * @version 2020-08-06
 */
@Api(description = "课程任务")
@RestController
@RequestMapping(value = "admin/mission")
public class MissionController extends BaseApiController {

	@Autowired
	private MissionService missionService;
	
	/**
	 * 获取课程任务数据
	 */
	@ModelAttribute
	public Mission get(String missionId, boolean isNewRecord) {
		return missionService.get(missionId, isNewRecord);
	}
	
 	@ApiOperation(value = "分页查询课程任务列表数据")
	@PostMapping(value = "findPage")
	public ResultBean<Page<Mission>>  findPage(Mission mission,
								@RequestParam(defaultValue = "10") int pageSize,
                                @RequestParam(defaultValue = "1") int pageNo ) {
		mission.setPage(new Page<>(pageNo, pageSize));
		Page<Mission> page = missionService.findPage(mission);
		return success(page);
	}

	@ApiOperation(value = "保存课程任务")
	@PostMapping(value = "save")
	public ResultBean save(@Validated Mission mission) {
		missionService.save(mission);
		return success();
	}


	@ApiOperation(value = "删除课程任务")
	@PostMapping(value = "delete")
	public ResultBean delete(Mission mission) {
		missionService.delete(mission);
		return success();
	}


}