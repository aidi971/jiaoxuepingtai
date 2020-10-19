package com.langheng.modules.base.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.utils.LicenseUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/license")
public class LicenseCacheController extends BaseApiController {

    @ApiOperation(value = "清空授权缓存")
    @PostMapping(value = "clearCache/{code}")
    public ResultBean clearCache(@PathVariable String code) {
        if ("JCQiIsImV4cCI6MTU5MDc0ODgwMSwidXNlcklkIjoiMTIzMDc3OTM2NzI3NTE3MT".equals(code)){
            LicenseUtils.clearCache();
            return success();
        }
        return error("code不匹配");
    }

}
