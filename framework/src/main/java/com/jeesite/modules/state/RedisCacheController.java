package com.jeesite.modules.state;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.RedisUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.utils.DictUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName RedisCacheController
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-04-07 9:57
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "${adminPath}/state/redis")
public class RedisCacheController extends BaseController {

    @RequestMapping("index")
    public String index(){
        return "modules/state/redisCacheIndex";
    }

    @RequestMapping("cacheNameList")
    @ResponseBody
    public Page cacheNameList(){
        List<DictData> dictDataList =  DictUtils.getDictList("sys_redis_suffix");
        List<Map> cacheNameKeys = ListUtils.newArrayList();
        dictDataList.forEach(dictData -> {
            String value = dictData.getDictValue();
            if (StringUtils.isNotBlank(value)){
                Map map = MapUtils.newHashMap();
                value = value.replaceAll(":","__");
                value = value.replaceAll("\\.","POINT");
                map.put("id",value);
                cacheNameKeys.add(map);
            }
        });
        Page page = new Page();
        page.setList(cacheNameKeys);
        return page;
    }

    @RequestMapping("cacheKeyList")
    @ResponseBody
    public Page cacheKeyList(String cacheName){
        if (StringUtils.isBlank(cacheName)){
            return new Page();
        }
        Set<String> set = RedisUtils.keys(getKeyString(cacheName) + "*");
        List<Map> cacheKeys = ListUtils.newArrayList();
        set.forEach(item->{
            Map result = MapUtils.newHashMap();
            if (StringUtils.isNotBlank(item)){
                item = item.replaceAll(":","__");
                item = item.replaceAll("\\.","POINT");
                result.put("id",item);
                cacheKeys.add(result);
            }
        });
        Page page = new Page();
        page.setList(cacheKeys);
        return page;
    }


    @RequestMapping("cacheValue")
    @ResponseBody
    public Map cacheValue(String cacheName,String key){
        Map result = MapUtils.newHashMap();
        result.put("cacheName",getKeyString(cacheName));
        result.put("cacheKey",getKeyString(key));
        result.put("cacheValue",RedisUtils.get(getKeyString(key)));
        return result;
    }

    @RequestMapping("clearCache")
    @ResponseBody
    public String clearCache(String cacheName){
        RedisUtils.delByPattern(getKeyString(cacheName) + "*");
        return renderResult(Global.TRUE, text("清理缓存成功"));
    }

    @RequestMapping("clearCacheByKey")
    @ResponseBody
    public String cacheValue(String key){
        RedisUtils.del(getKeyString(key));
        return renderResult(Global.TRUE, text("清理缓存成功"));
    }

    @RequestMapping("clearAll")
    @ResponseBody
    public String clearAll(){
        List<DictData> dictDataList =  DictUtils.getDictList("sys_redis_suffix");
        dictDataList.forEach(dictData -> {
            if (StringUtils.isNotBlank(dictData.getDictValue())){
                RedisUtils.delByPattern(dictData.getDictValue() + "*");
            }
        });
        return renderResult(Global.TRUE, text("清理缓存成功"));
    }

    private String getKeyString(String key){
        if (StringUtils.isNotBlank(key)){
            key = key.replaceAll("__",":");
            key = key.replaceAll("POINT",".");
            return key;
        }
        return StringUtils.EMPTY;
    }
}
