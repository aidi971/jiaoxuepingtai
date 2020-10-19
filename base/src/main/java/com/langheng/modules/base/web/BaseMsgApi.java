/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.Msg;
import com.langheng.modules.base.entity.MsgUser;
import com.langheng.modules.base.enumn.MsgUserState;
import com.langheng.modules.base.service.MsgService;
import com.langheng.modules.base.service.MsgUserService;
import com.langheng.modules.base.utils.BaseUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统消息管理Controller
 * @author xiaoxie
 * @version 2020-05-08
 */
@RestController
@RequestMapping("/api/msg")
@Api(description = "系统消息管理")
public class BaseMsgApi extends BaseApiController {

	@Autowired
	private MsgService msgService;
	@Autowired
	private MsgUserService msgUserService;
	
	/**
	 * 获取系统消息数据
	 */
	@ModelAttribute
	public Msg get(String msgId, boolean isNewRecord) {
		return msgService.get(msgId, isNewRecord);
	}
	
 	@ApiOperation(value = "分页查询系统消息列表数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "state", value = "是否已读（0否，1是）", required = false),
			@ApiImplicitParam(name = "msg.type", value = "查询的类型 sys_msg_type", required = false),
			@ApiImplicitParam(name = "filterType", value = "指定需要过滤掉的类型", required = false),
			@ApiImplicitParam(name = "msg.teachingClassId", value = "课堂id", required = false),
	})
	@PostMapping(value = "findPage")
	public ResultBean<Page<MsgUser>>  findPage(MsgUser msgUser, String filterType,
												@RequestParam(defaultValue = "10") int pageSize,
												@RequestParam(defaultValue = "1") int pageNo ) {
		if (StringUtils.isNotBlank(filterType)){
			Msg msg = msgUser.getMsg();
			if (msg == null){
				msg = new Msg();
			}
			msg.setType_not_in(new String[]{filterType});
			msgUser.setMsg(msg);
		}
		msgUser.setPage(new Page<>(pageNo, pageSize));
		msgUser.setUserCode(BaseUserUtils.getUser().getUserCode());
		Page<MsgUser> page = msgUserService.findPage(msgUser);
		return success(page);
	}

	@ApiOperation(value = "获取未读消息数量")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = false),
	})
	@PostMapping(value = "findUnReadCount")
	public ResultBean  findUnReadCount(String teachingClassId) {
		MsgUser msgUser = new MsgUser();
		Msg msg = new Msg();
		msg.setTeachingClassId(teachingClassId);
		msgUser.setMsg(msg);
		msgUser.setUserCode(BaseUserUtils.getUser().getUserCode());
		msgUser.setState(MsgUserState.UN_READ.value());
		return success(msgUserService.findCount(msgUser));
	}

	@ApiOperation(value = "置为已读")
	@PostMapping(value = "hadRead")
	public ResultBean get(String userMsgId,String msgId) {
		if (StringUtils.isNotBlank(userMsgId)){
			MsgUser msgUser = msgUserService.get(userMsgId);
			msgUser.setState(MsgUserState.HAD_READ.value());
			msgUserService.save(msgUser);
		}else if (StringUtils.isNotBlank(msgId)){
			MsgUser msgUserCriteria = new MsgUser();
			msgUserCriteria.setMsg(new Msg(msgId));
			msgUserCriteria.setUserCode(BaseUserUtils.getUser().getUserCode());
			MsgUser msgUser = msgUserService.getOne(msgUserCriteria);
			if (msgUser!= null){
				msgUser.setState(MsgUserState.HAD_READ.value());
				msgUserService.save(msgUser);
			}
		}
		return success();
	}

}