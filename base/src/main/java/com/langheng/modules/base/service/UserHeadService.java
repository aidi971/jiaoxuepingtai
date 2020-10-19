package com.langheng.modules.base.service;

import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.UserHeadMapper;
import com.langheng.modules.base.entity.UserHead;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHeadService extends CrudService<UserHeadMapper,UserHead> {

    public UserHead getUserHead(Integer type) {
        return  dao.getHead(type);
    }

    public List<String> randomCover(Integer type) {
        return  dao.getVideo(type);
    }
    public String randomCoverUrl(Integer type) {
        return  dao.randVideoUrl(type);
    }
}
