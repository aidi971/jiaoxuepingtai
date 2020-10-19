package com.jeesite.framework.uitls;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @ClassName EntityUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-02-24 14:30
 * @Version 1.0
 */
public class EntityUtils {

    /**
     * 列表对象  转id数组
     * @param entities
     * @return
     */
    public static List<String> getBaseEntityIdList(List<? extends BaseEntity> entities){
        List<String> idList = ListUtils.newArrayList();
        for (BaseEntity entity : entities){
            if (StringUtils.isNotBlank(entity.getId())){
                idList.add(entity.getId());
            }
        }
        return idList;
    }

    /**
     * 列表对象  转id数组
     * @param entities
     * @return
     */
    public static String [] getBaseEntityIds(List<? extends BaseEntity> entities){
        return getBaseEntityIdList(entities).toArray(new String[]{});
    }

    /**
     * 列表对象  转String属性为数组
     * @param entities
     * @return
     */
    public static String [] getBaseEntityStringFields(List<? extends BaseEntity> entities,String fieldName){
        List<String> fieldList = ListUtils.newArrayList();
        for (BaseEntity entity : entities){
            try{
                Field field = entity.getClass().getDeclaredField(fieldName);
                if (field.getType() == String.class){
                    field.setAccessible(true);
                    String value = (String)field.get(entity);
                    if (StringUtils.isNotBlank(value)){
                        fieldList.add(value);
                    }
                }
            }catch (Exception e){ }
        }
        return fieldList.toArray(new String[]{});
    }

    /**
     * 判断两个实体id，是否相等
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean equalsBaseEntityId(BaseEntity obj1,BaseEntity obj2) {
        if (obj1 == null || obj2 == null){
            return false;
        }
        if (StringUtils.isBlank(obj1.getId())
                || StringUtils.isBlank(obj2.getId())){
            return false;
        }
        return obj1.getId().equals(obj2.getId());
    }

    public static String  saveOrUpdate(BaseEntity entities){

        if (entities.getIsNewRecord()) {
            return "添加成功";
        }else {
            return "修改成功";
        }

    }
}
