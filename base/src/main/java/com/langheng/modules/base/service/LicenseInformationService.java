package com.langheng.modules.base.service;

import com.jeesite.common.service.CrudService;
import com.langheng.modules.base.dao.LicenseInformationMapper;
import com.langheng.modules.base.entity.LicenseInformation;
import org.springframework.stereotype.Service;

@Service
public class LicenseInformationService extends CrudService<LicenseInformationMapper, LicenseInformation> {
}
