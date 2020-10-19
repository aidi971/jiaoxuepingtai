package com.langheng.modules.base.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.langheng.modules.base.entity.UserHead;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@MyBatisDao(dataSourceName="ds2")
public interface UserHeadMapper extends CrudDao<UserHead> {
    @Select("select * from sys_userhead where head_type=#{type} order by rand() limit 1; ")
    UserHead getHead(Integer type);

    @Select("select head_url from sys_userhead where head_type=#{type} order by head_id; ")
    List<String> getVideo(Integer type);

    @Select("select head_url from sys_userhead where head_type=#{type} order by rand() limit 1; ")
    String randVideoUrl(Integer type);
}
