//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.langheng.modules.base.service;

import com.jeesite.common.config.Global;
import com.jeesite.common.enumn.StatusEnum;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.BaseUserDao;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.utils.BaseUserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;

@Service
public class BaseUserService extends CrudService<BaseUserDao, BaseUser> {
    public BaseUserService() {
    }

    public BaseUser getByLoginCode(BaseUser baseUser) {
        return ((BaseUserDao)super.dao).getByLoginCode(baseUser);
    }

    public BaseUser getByLoginCode(String loginCode) {
        BaseUser baseUser = new BaseUser();
        baseUser.setLoginCode(loginCode);
        return getByLoginCode(baseUser);
    }

    public void save(BaseUser baseUser) {
        boolean isNewRecord = baseUser.getIsNewRecord();
        if (isNewRecord) {
            this.checkLoginCode("", baseUser.getLoginCode());
            if (StringUtils.isBlank(baseUser.getPassword())) {
                baseUser.setPassword(Global.getConfig("sys.user.initPassword", "123456"));
            }
        } else {
            baseUser.setLoginCode((String)null);
        }
        super.save(baseUser);

    }

    public void checkLoginCode(String oldLoginCode, String loginCode) {
        if (StringUtils.isBlank(loginCode)) {
            throw new ValidationException("登录账号不能为空");
        } else {
            if (!loginCode.equals(oldLoginCode)) {
                BaseUser baseUserCriteria = new BaseUser();
                baseUserCriteria.disableStatus();
                baseUserCriteria.setLoginCode(loginCode);
                baseUserCriteria.setStatus_in(new String[]{StatusEnum.NORMAL.value(), StatusEnum.DELETE.value(), StatusEnum.DISABLE.value()});
                List<BaseUser> baseUserList = this.findList(baseUserCriteria);
                if (!baseUserList.isEmpty()) {
                    BaseUser baseUser = (BaseUser) baseUserList.get(0);
                    if (StatusEnum.DELETE.value().equals(baseUser.getStatus())) {
                        throw new ValidationException("该登录账号在历史记录已存在!");
                    }

                    throw new ValidationException("该登录账号已存在!");
                }
            }

        }
    }

    @Transactional(
            readOnly = false
    )
    public void disable(BaseUser baseUser) {
        if (baseUser.getId().equals(BaseUserUtils.getUser().getId())) {
            throw newValidationException("不能停用自己！");
        } else {
            baseUser.setStatus(StatusEnum.DISABLE.value());
            this.updateStatus(baseUser);
        }
    }

    @Transactional(
            readOnly = false
    )
    public void enable(BaseUser baseUser) {
        baseUser.setStatus(StatusEnum.NORMAL.value());
        this.updateStatus(baseUser);
    }

    public void delete(BaseUser baseUser) {
        if (BaseUser.SUPER_ADMIN_CODE.equals(baseUser.getId())) {
            throw newValidationException("不能删除系统用户！");
        } else if (baseUser.getId().equals(BaseUserUtils.getUser().getId())) {
            throw newValidationException("不能删除自己！");
        } else {
            super.delete(baseUser);
        }
    }

    @Transactional(readOnly = false)
    public void phyDelete(BaseUser baseUser){
        dao.phyDelete(baseUser);
    }
}
