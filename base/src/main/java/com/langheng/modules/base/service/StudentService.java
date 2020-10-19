package com.langheng.modules.base.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.enumn.StatusEnum;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.utils.excel.ExcelImport;
import com.jeesite.common.validator.ValidatorUtils;
import com.langheng.modules.base.dao.StudentDao;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.entity.StudentClasses;
import com.langheng.modules.base.enumn.StudentClassesState;
import com.langheng.modules.base.enumn.StudentOriginType;
import com.langheng.modules.base.enumn.UserType;
import com.langheng.modules.base.utils.CoverImgUtils;
import com.langheng.modules.base.utils.SystemUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-16 17:07
 * @Version 1.0
 */
@Service
@Transactional(readOnly = true)
public class StudentService extends CrudService<StudentDao, Student> {

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private ClassesService classesService;

    @Autowired
    private StudentClassesService studentClassesService;

    public List<Student> findListByClasses(Student student, String classesId){
        return dao.findListByClasses(student,classesId);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(Student student) {
        BaseUser baseUser = new BaseUser();
        student.setUserType(UserType.STUDENT.value());
        BeanUtils.copyProperties(student, baseUser);
        boolean isNewRecord = student.getIsNewRecord();
        if (student.getIsNewRecord()){
            if (StringUtils.isBlank(baseUser.getUserName())){
                baseUser.setUserName(student.getStudentName());
            }
            if (StringUtils.isBlank(baseUser.getPassword())){
                baseUser.setPassword("666666");
            }
            if (StringUtils.isBlank(baseUser.getCoverImg())){
                baseUser.setCoverImg(CoverImgUtils.getCoverImg("0"));
                student.setCoverImg(baseUser.getCoverImg());
            }
        }
        if (StringUtils.isNotBlank(student.getStudentId())){
            baseUser.setUserCode(student.getStudentId());
        }
        baseUserService.save(baseUser);
        if (isNewRecord){
            student.setStudentId(baseUser.getId());
            student.setIsNewRecord(true);
        }
        super.save(student);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Student student) {
        BaseUser baseUser = new BaseUser();
        BeanUtils.copyProperties(student, baseUser);
        baseUserService.delete(baseUser);
        // 发送通知踢掉用户
        SystemUtils.tickOutUser(baseUser.getId());
        super.delete(student);
    }

    @Transactional(readOnly = false)
    public String importData(MultipartFile file, boolean isUpdateSupport) {
        if (file == null){
            throw new ServiceException("请选择导入的数据文件！");
        }
        int successNum = 0; int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        try(ExcelImport ei = new ExcelImport(file, 2, 0)){
            List<Student> list = ei.getDataList(Student.class);
            for (Student student : list) {
                try{
                    // 验证数据文件
                    ValidatorUtils.validateWithException(student);
                    // 验证是否存在这个学生
                    Student oldStudent = getByLoginCode(student);

                    if (oldStudent == null){
                        this.save(student);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、登录ID " + student.getUserName() + " 导入成功");
                    } else if (isUpdateSupport){
                        student.setId(oldStudent.getId());
                        this.save(student);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、登录ID " + student.getUserName() + " 更新成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、登录ID " + student.getUserName() + " 已存在");
                    }
                } catch (Exception e) {
                    failureNum++;
                    String msg = "<br/>" + failureNum + "、登录ID " + student.getUserName() + " 导入失败：";
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
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }else{
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();    
    }

    private Student getByLoginCode(Student student) {
        BaseUser baseUser = new BaseUser();
        BeanUtils.copyProperties(student, baseUser);
        baseUser = baseUserService.getByLoginCode(baseUser);
        if (baseUser != null){
            return get(baseUser.getId());
        }
        return null;
    }

    @Transactional(readOnly = false)
    public void disable(Student student) {
        BaseUser baseUser = new BaseUser(student.getId());
        baseUserService.disable(baseUser);

        // 同时更新 student
        student.setStatus(StatusEnum.DISABLE.value());
        updateStatus(student);
        // 发送通知踢掉用户
        SystemUtils.tickOutUser(student.getId());
    }

    @Transactional(readOnly = false)
    public void enable(Student student) {
        BaseUser baseUser = new BaseUser(student.getId());
        baseUserService.enable(baseUser);

        // 同时更新 student
        student.setStatus(StatusEnum.NORMAL.value());
        updateStatus(student);
    }

    public Integer getMaxLoginCode(String suffixLoginCode){
        int maxSuffix = 0;
        String loginCode =  dao.getMaxLoginCode(suffixLoginCode);
        if (!StringUtils.isBlank(loginCode)){
            loginCode = loginCode.substring(loginCode.length() - 3,loginCode.length());
            maxSuffix = Integer.parseInt(loginCode);
        }
        return maxSuffix;
    }

    @Transactional(readOnly = false)
    public String importStudentToClass(MultipartFile file,String classesId,
                                       boolean isUpdateSupport,List<Student> studentList) {
        if (file == null){
            throw new ServiceException("请选择导入的数据文件！");
        }
        Classes classes = classesService.get(classesId);
        int successNum = 0; int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        try(ExcelImport ei = new ExcelImport(file, 2, 0)){
            List<Student> list = ei.getDataList(Student.class);
            for (Student student : list) {
                try{
                    // 验证数据文件
                    ValidatorUtils.validateWithException(student);

                    if (student.getLoginCode() == null
                            || student.getLoginCode().length() < 6){
                        throw newValidationException("登录账号长度应为 6 到 20 个字符");
                    }

                    // 验证是否存在这个学生
                    Student oldStudent = getByLoginCode(student);

                    if (oldStudent == null){
                        student.setClassesId(classes.getClassesId());
                        student.setOriginType(StudentOriginType.IMPORT.value());
                        this.save(student);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、" + student.getLoginCode() + "  " +student.getStudentName() + " 导入成功");

                        // 关联班级和学生
                        StudentClasses studentClasses = new StudentClasses();
                        studentClasses.setClassesId(classes.getId());
                        studentClasses.setStudentId(student.getId());
                        studentClasses.setState(StudentClassesState.NORMAL.value());
                        studentClassesService.save(studentClasses);

                        student = get(student);
                        // 返回成功添加学生信息
                        studentList.add(student);

                    } else if (isUpdateSupport){
                        student.setId(oldStudent.getId());
                        this.save(student);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、" + student.getLoginCode() + "  " + student.getStudentName() + " 更新成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、" + student.getLoginCode() + "  " + student.getStudentName() + " 已存在");
                    }
                } catch (Exception e) {
                    failureNum++;
                    String msg = "<br/>" + failureNum  + "、" + student.getLoginCode() + "  " +student.getStudentName() + " 导入失败：";
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

            // 调整班级数量
            classesService.adjustStudentNum(classes);
        } catch (Exception e) {
            failureMsg.append(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "导入成功共 " + successNum + " 条数据, 导入失败共 " + failureNum + "条数据, 错误如下：");
            return failureMsg.toString();
            // throw new ServiceException(failureMsg.toString());
        }else{
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Transactional(readOnly = true)
    public void phyDelete(Student student){
        baseUserService.phyDelete(new BaseUser(student.getId()));
        dao.phyDelete(student);
        SystemUtils.tickOutUser(student.getId());
    }

    public List<String> selectUnJoinClassesStudent(Date limitTime){
        return dao.selectUnJoinClassesStudent(limitTime);
    }

    public List<Student> findVoList(Student stuCriteria) {
        List<Student> studentList = findList(stuCriteria);
        List<Student> studentVoList = ListUtils.newArrayList();
        for (Student student: studentList){
            Student studentVo = new Student();
            studentVo.setStudentId(student.getStudentId());
            studentVo.setStudentName(student.getStudentName());
            studentVoList.add(studentVo);
        }

        return studentVoList;
    }
}

