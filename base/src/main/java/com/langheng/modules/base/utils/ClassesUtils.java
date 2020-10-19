package com.langheng.modules.base.utils;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.RedisUtils;
import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.service.ClassesService;
import com.langheng.modules.base.service.StudentService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TeacherUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-19 14:08
 * @Version 1.0
 */
public class ClassesUtils {

    private static StudentService studentService = SpringUtils.getBean(StudentService.class);
    private static ClassesService classesService = SpringUtils.getBean(ClassesService.class);

    /**
     * 获取班级学生
     * @param classesId
     * @return
     */
    public static List<Student> findListByClasses(String classesId){
        Student student = new Student();
        student.setClassesId(classesId);
        return studentService.findList(student);
    }

    public static Long findCountByClasses(String classesId){
        Student student = new Student();
        student.setClassesId(classesId);
        return studentService.findCount(student);
    }

    public static void updateStuOnlineNum(Classes classes) {
        Set set = RedisUtils.keys("user:classes:" + classes.getClassesId() + "*");
        classes.setStuOnlineNum(set.size());
        classesService.updateStuOnlineNum(classes);
    }

    public static void cleanCache(Classes classes,String [] studentIdArr) {
        for (String studentId: studentIdArr){
            RedisUtils.delByPattern("user:classes:" + classes.getClassesId() + ":" + studentId );
        }
    }

    public static void addOnlineStu(Classes classes,String studentId) {
        String classesStudentKey  = "user:classes:" + classes.getClassesId() + ":" + studentId;
        RedisUtils.set(classesStudentKey,"1");
        RedisUtils.expire(classesStudentKey,60, TimeUnit.MINUTES);
        updateStuOnlineNum(classes);
    }

    public static void cleanCache(Classes classes,String studentId) {
        RedisUtils.delByPattern("user:classes:" + classes.getClassesId() + ":" + studentId );
    }

    public static void cleanCacheAndUpdateOnlineNum(String userId) {
        Student student = studentService.get(userId);
        if (student!=null && StringUtils.isNotBlank(student.getClassesId())){
            Classes classes = new Classes(student.getClassesId());
            cleanCache(classes,student.getStudentId());
            updateStuOnlineNum(classes);
        }
    }

    public static void addCacheAndUpdateOnlineNum(String userId) {
        Student student = studentService.get(userId);
        if (student!=null && StringUtils.isNotBlank(student.getClassesId())){
            Classes classes = new Classes(student.getClassesId());
            addOnlineStu(classes,student.getStudentId());
        }
    }
}
