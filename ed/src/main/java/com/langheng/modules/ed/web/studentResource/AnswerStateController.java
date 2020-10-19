package com.langheng.modules.ed.web.studentResource;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.TeachClassAnswer;
import com.langheng.modules.ed.service.TeachClassAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "作业答案状态")
@RestController
@RequestMapping(value = "api/answerState")
public class AnswerStateController extends BaseApiController {
    @Autowired
    private TeachClassAnswerService teachClassAnswerService;

    @ApiOperation(value = "获取状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lessonTaskId", value = "小节任务id", required = true),
            @ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
    })
    @PostMapping(value = "getState")
    public ResultBean getState(TeachClassAnswer teachClassAnswer) {
        List<TeachClassAnswer> list = teachClassAnswerService.findList(teachClassAnswer);
        if (list.size()==0) {
            return success(0,"答案未推送");
        }
        return success(1,"答案已推送");
    }

}
