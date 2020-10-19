package com.langheng.modules.base.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.service.DictDataService;
import com.jeesite.modules.sys.utils.DictUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DictDataController
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-10-24 15:53
 * @Version 1.0
 */
@RestController(value = "BaseDictDataController")
@RequestMapping("/admin/dictData")
@Api(description = "字典数据管理")
public class DictDataController extends BaseApiController {

    @Autowired
    private DictDataService dictDataService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public DictData get(String id, boolean isNewRecord) {
        return dictDataService.get(id, isNewRecord);
    }


    @GetMapping("/get")
    public ResultBean get(DictData dictData){
        return success(dictDataService.get(dictData));
    }


    @ApiOperation("根据字典类型获取字典数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictTypeStr",value = "字典类型，如有多个，则用逗号分隔"),
    })
    @PostMapping("/findList")
    public ResultBean<Map<String,List<DictData>>> findList(String dictTypeStr){
        Map<String,List<DictData>> result = new HashMap<>();
        String [] dictTypeArr = dictTypeStr.split(",");
        for (String dictType : dictTypeArr){
           List<DictData> dictDataList = DictUtils.getDictList(dictType);
           result.put(dictType,dictDataList);
        }
        return success(result);
    }




}
