package com.langheng.modules.base.web.api;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.jeesite.common.web.response.GlobalStatusCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @ClassName GlobalApi
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-17 10:03
 * @Version 1.0
 */
@ApiIgnore
@RestController
public class GlobalApi extends BaseApiController {

    @RequestMapping("error/403")
    public ResultBean error403() {
        return error(GlobalStatusCode.CODE_AUTH_ERROR);
    }

    @RequestMapping("error/500")
    public ResultBean error500() {
        return error(GlobalStatusCode.CODE_ERROR);
    }

    @RequestMapping("error/404")
    public ResultBean error404() {
        return error(GlobalStatusCode.CODE_NOT_FOUND);
    }

    @RequestMapping("error/400")
    public ResultBean error400() {
        return error(GlobalStatusCode.CODE_INVALID_PARAMETER);
    }
}
