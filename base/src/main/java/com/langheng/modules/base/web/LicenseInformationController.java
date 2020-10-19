package com.langheng.modules.base.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.utils.RedisUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.LicenseInformation;
import com.langheng.modules.base.service.LicenseInformationService;
import com.langheng.modules.base.webservice.WebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(description = "授权信息")
@RequestMapping("license")
public class LicenseInformationController extends BaseApiController {

    @Autowired
    private LicenseInformationService licenseInformationService;
    @Autowired
    private WebService webService;

//    @ApiOperation(value = "修改信息")
//    @PostMapping("save")
//    public ResultBean save(LicenseInformation licenseInformation) {
//        licenseInformationService.save(licenseInformation);
//        return success();
//    }

    @ApiOperation(value = "获取信息")
    @PostMapping("get")
    public ResultBean get(LicenseInformation licenseInformationCriteria) {

        List<LicenseInformation> licenseInformations
                =  licenseInformationService.findList(licenseInformationCriteria);
        LicenseInformation licenseInformation = new LicenseInformation();
        if (!licenseInformations.isEmpty()){
            licenseInformation = licenseInformations.get(0);
        }

        return success(licenseInformation);
    }

//    @ApiOperation(value = "获取信息")
//    @PostMapping("get")
//    @MyAnnotation
//    public ResultBean get() {
//        LicenseInformation licenseInformation = new LicenseInformation();
//        Authorization authorization = webService.getWebService(true);
//        licenseInformation.setLicenseId("1");
//        licenseInformation.setManufacturer("RPA财务机器人实践教学平台");
//        licenseInformation.setLicenseCount(authorization.getPoints());
//        licenseInformation.setSchoolName(authorization.getSchoolName());
//        licenseInformation.setExpirationTime(webService.endTimeDate(authorization));
//
//        return success(licenseInformation);
//    }

    @PostMapping("remove")
    public ResultBean remove(@RequestParam Integer code) {
        if (code==20405) {
            RedisUtils.del("sys:jiaoxuepingtai:verify");
            return success();
        }
        return error("请求异常");
    }
}
