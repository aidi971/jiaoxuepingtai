package com.langheng.modules.base.web.api;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.service.StudentClassesService;
import com.langheng.modules.base.utils.BaseUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@Api(description = "用户取消授权接口")
public class CancelAuthorizeApi extends BaseApiController {

    @Autowired
    private StudentClassesService studentClassesService;

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public ResultBean logout(HttpServletRequest request, HttpServletResponse response) {
         BaseUserUtils.logout(request, response);
        return success();
    }

}