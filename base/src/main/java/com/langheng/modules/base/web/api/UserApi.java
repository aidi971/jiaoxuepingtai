package com.langheng.modules.base.web.api;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.service.BaseUserService;
import com.langheng.modules.base.utils.BaseUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName UserApi
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-18 16:07
 * @Version 1.0
 */
@RestController
@RequestMapping("api/user")
@Api(description = "用户信息相关接口")
public class UserApi extends BaseApiController {

    @Autowired
    private BaseUserService baseUserService;

    @ApiOperation(value = "获取用户自己的详细的信息")
    @GetMapping("/get")
    public ResultBean<BaseUser> get(HttpServletRequest request){
        BaseUser baseUser = BaseUserUtils.getUser();
        String id = request.getSession().getId();
        request.getSession().setAttribute(id,"verify");
        return success(baseUser);
    }


    @ApiOperation(value = "更新用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "originalPassword",value = "原密码"),
            @ApiImplicitParam(name = "newPassword",value = "新密码"),
            @ApiImplicitParam(name = "confirmPassword",value = "重复确认密码"),
    })
    @PostMapping("/updatePassword")
    public ResultBean updatePassword(@RequestParam String originalPassword,
                                     @RequestParam String newPassword,
                                     @RequestParam String confirmPassword){
        BaseUser baseUser = BaseUserUtils.getUser();
        if (originalPassword.equals(newPassword)){
            return error("新密码不能和旧密码不能一样！");
        }
        if (!originalPassword.equals(baseUser.getPassword())){
            return error("原密码错误！");
        }
        if (StringUtils.isBlank(newPassword)
                || StringUtils.isBlank(confirmPassword)){
            return error("密码不能为空！");
        }
        if (!confirmPassword.equals(newPassword)){
            return error("两次密码不一致！");
        }
        baseUser.setPassword(newPassword);
        baseUserService.save(baseUser);
        return success();
    }
}
