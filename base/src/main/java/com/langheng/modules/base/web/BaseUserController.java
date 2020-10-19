package com.langheng.modules.base.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.enumn.UserType;
import com.langheng.modules.base.service.BaseUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-14 10:37
 * @Version 1.0
 */
@ApiIgnore
@RestController
@RequestMapping("/admin/user")
public class BaseUserController extends BaseApiController {
    
    @Autowired
    private BaseUserService baseUserService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public BaseUser get(String id, boolean isNewRecord) {
        return baseUserService.get(id, isNewRecord);
    }

    @GetMapping("get")
    public ResultBean<BaseUser> get(BaseUser baseUser){
        return success(baseUser);
    }

    @ApiOperation(value = "分页获取用户信息")
    @PostMapping(value = "findPage")
    public ResultBean<Page<BaseUser>> findPage(BaseUser baseUser,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               @RequestParam(defaultValue = "1") int pageNo ) {
        baseUser.setPage(new Page(pageNo,pageSize));
        baseUser.disableStatus();
        baseUser.setStatus_in(new String[]{"0","2"});
        Page<BaseUser> page = baseUserService.findPage(baseUser);
        return success(page);
    }

    @ApiOperation(value = "保存用户")
    @PostMapping("save")
    public ResultBean save(BaseUser baseUser){

        if (BaseUser.SUPER_ADMIN_CODE.equals(baseUser.getId())){
            return error("不能操作超级管理员！");
        }
        if (UserType.ADMIN.value().equals(baseUser.getUserType())){
            baseUser.setCoverImg("static/userHead/admin/管理员1.png");
        }
        baseUserService.save(baseUser);
        return success();
    }

    @ApiOperation(value = "重置密码")
    @PostMapping("/repeatPassword")
    public ResultBean repeatPassword(@RequestParam String userCode){
        if (BaseUser.SUPER_ADMIN_CODE.equals(userCode)){
            return error("不能操作超级管理员！");
        }

        BaseUser baseUser = baseUserService.get(userCode);
        baseUser.setPassword(Global.getConfig("sys.user.initPassword","123456"));
        baseUserService.save(baseUser);
        return success();
    }

    @ApiOperation(value = "停用用户")
    @PostMapping("/disable")
    public ResultBean disable(BaseUser baseUser){
        if (BaseUser.SUPER_ADMIN_CODE.equals(baseUser.getId())){
            return error("不能操作超级管理员！");
        }
        baseUserService.disable(baseUser);
        return success();
    }

    @ApiOperation(value = "启用用户")
    @PostMapping("/enable")
    public ResultBean enable(BaseUser baseUser){
        if (BaseUser.SUPER_ADMIN_CODE.equals(baseUser.getId())){
            return error("不能操作超级管理员！");
        }
        baseUserService.enable(baseUser);
        return success();
    }

    @ApiOperation(value = "删除用户")
    @PostMapping(value = "delete")
    public ResultBean delete(BaseUser baseUser){
        if (BaseUser.SUPER_ADMIN_CODE.equals(baseUser.getId())){
            return error("不能操作超级管理员！");
        }
        baseUserService.delete(baseUser);
        return success();
    }


}
