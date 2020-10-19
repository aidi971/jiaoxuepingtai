package com.langheng.modules.base.service;

import com.jeesite.common.enumn.StatusEnum;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import com.jeesite.common.validator.ValidatorUtils;
import com.langheng.modules.base.dao.TeacherDao;
import com.langheng.modules.base.entity.Academy;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.entity.Major;
import com.langheng.modules.base.entity.Teacher;
import com.langheng.modules.base.enumn.UserType;
import com.langheng.modules.base.event.FloderEvent;
import com.langheng.modules.base.utils.CoverImgUtils;
import com.langheng.modules.base.utils.SystemUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-16 17:07
 * @Version 1.0
 */
@Service
public class TeacherService extends CrudService<TeacherDao, Teacher> {

    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private AcademyService academyService;
    @Autowired
    private MajorService majorService;

    @Override
    public void save(Teacher teacher) {
        BaseUser baseUser = new BaseUser();
        teacher.setUserType(UserType.TEACHER.value());
        BeanUtils.copyProperties(teacher, baseUser);
        boolean isNewRecord = teacher.getIsNewRecord();
        if (isNewRecord){
            if (StringUtils.isBlank(baseUser.getUserName())){
                baseUser.setUserName(teacher.getTeacherName());
            }
            if (StringUtils.isNotBlank(teacher.getTeacherId())){
                baseUser.setUserCode(teacher.getTeacherId());
            }
            if (StringUtils.isBlank(baseUser.getCoverImg())){
                baseUser.setCoverImg(CoverImgUtils.getCoverImg("1"));
            }
        }
        if (StringUtils.isNotBlank(teacher.getTeacherId())){
            baseUser.setUserCode(teacher.getTeacherId());
        }
        baseUserService.save(baseUser);
        if (isNewRecord){
            teacher.setTeacherId(baseUser.getId());
            teacher.setIsNewRecord(true);
            SpringUtils.getApplicationContext()
                    .publishEvent(new FloderEvent(this,FloderEvent.CREAT_USER_FIODER,baseUser.getId()));
        }
        super.save(teacher);
    }

    @Override
    public void delete(Teacher teacher) {
        BaseUser baseUser = new BaseUser();
        BeanUtils.copyProperties(teacher, baseUser);
        baseUserService.delete(baseUser);
        super.delete(teacher);
        // 发送通知踢掉用户
        SystemUtils.tickOutUser(baseUser.getId());
    }

    @Transactional(readOnly = false)
    public void disable(Teacher teacher) {
        BaseUser baseUser = new BaseUser(teacher.getId());
        baseUserService.disable(baseUser);
        // 同时更新 teacher
        teacher.setStatus(StatusEnum.DISABLE.value());
        updateStatus(teacher);
        // 发送通知踢掉用户
        SystemUtils.tickOutUser(baseUser.getId());
    }

    @Transactional(readOnly = false)
    public void enable(Teacher teacher) {
        BaseUser baseUser = new BaseUser(teacher.getId());
        baseUserService.enable(baseUser);
        // 同时更新 teacher
        teacher.setStatus(StatusEnum.NORMAL.value());
        updateStatus(teacher);
    }


    @Transactional(readOnly = false)
    public String importData(MultipartFile file,List<Teacher> teacherList) {
        if (file == null){
            throw new ServiceException("请选择导入的数据文件！");
        }
        int successNum = 0; int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        try(ExcelImport ei = new ExcelImport(file, 2, "数据导入")){
            List<Teacher> list = ei.getDataList(Teacher.class);

            List<Academy> academyList = academyService.findList(new Academy());
            List<Major> majorList = majorService.findList(new Major());

            for (Teacher teacher : list) {
                try{
                    // 验证数据文件
                    ValidatorUtils.validateWithException(teacher);
                    if (teacher.getLoginCode() == null
                            || teacher.getLoginCode().length() < 6){
                        throw newValidationException("登录账号长度应为 6 到 20 个字符");
                    }
                    // 验证是否存在这个教师
                    Teacher oldTeacher = getByLoginCode(teacher);
                    if (oldTeacher == null){
                        if (StringUtils.isBlank(teacher.getAcademyAndMajor())){
                            failureNum++;
                            failureMsg.append("<br/>" + failureNum + "、" + teacher.getLoginCode() + "  " + teacher.getTeacherName() + " 所属学院不能为空");
                            continue;
                        }
                        String [] strArr = teacher.getAcademyAndMajor().split("\\/");
                        if (strArr.length < 2){
                            failureNum++;
                            failureMsg.append("<br/>" + failureNum + "、" + teacher.getLoginCode() + "  " + teacher.getTeacherName() + " 所属学院填写错误");
                            continue;
                        }

                        Major teaMajor = null;
                        String academyName = strArr[0];
                        String majorName = strArr[1];
                        for (Academy academy: academyList){
                            if (teaMajor == null && academy.getAcademyName().equals(academyName)){
                                for (Major major: majorList){
                                    if (major.getAcademyId().equals(academy.getAcademyId())
                                        && major.getMajorName().equals(majorName)){
                                            teaMajor = major;
                                            break;
                                    }
                                }
                            }
                        }
                        if (teaMajor == null){
                            failureNum++;
                            failureMsg.append("<br/>" + failureNum + "、" + teacher.getLoginCode() + "  " + teacher.getTeacherName() + " 所属学院找不到");
                            continue;
                        }else {
                            teacher.setUserName(teacher.getTeacherName());
                            teacher.setMajor(teaMajor);
                            teacher.setIsNewRecord(true);
                            this.save(teacher);

                            teacherList.add(teacher);
                            successNum++;
                            successMsg.append("<br/>" + successNum + "、" + teacher.getLoginCode()  + "  " + teacher.getTeacherName() + " 导入成功");
                        }
                    }else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、" + teacher.getLoginCode()  + "  " + teacher.getTeacherName() + " 已存在");
                    }
                } catch (Exception e) {
                    failureNum++;
                    String msg = "<br/>" + failureNum + "、"  + teacher.getLoginCode() + "  " + teacher.getTeacherName() + " 导入失败：";
                    if (e instanceof ConstraintViolationException){
                        List<String> messageList = ValidatorUtils.extractPropertyAndMessageAsList((ConstraintViolationException)e, ": ");
                        for (String message : messageList) {
                            msg += message + "; ";
                        }
                    }else{
                        msg += e.getMessage();
                    }
                    failureMsg.append(msg);
                    logger.error(msg, e);
                }
            }
        } catch (Exception e) {
            failureMsg.append(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "导入成功共 " + successNum + " 条数据, 导入失败共 " + failureNum + "条数据, 错误如下：");
            return failureMsg.toString();
        }else{
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }


    private Teacher getByLoginCode(Teacher teacher) {
        BaseUser baseUser = new BaseUser();
        BeanUtils.copyProperties(teacher, baseUser);
        baseUser = baseUserService.getByLoginCode(baseUser);
        if (baseUser != null){
            return get(baseUser.getId());
        }
        return null;
    }

}

