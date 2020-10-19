package com.langheng.modules.ed.util;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.entity.Teacher;
import com.langheng.modules.base.utils.TeacherUtils;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.Template;
import com.langheng.modules.ed.enumn.TemplateType;
import com.langheng.modules.ed.service.CourseService;
import com.langheng.modules.ed.service.TemplateService;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * @ClassName CourseUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-02-28 10:12
 * @Version 1.0
 */
public class CourseUtils {

    public static Map<Integer,String> UPPERCASE_MAP = MapUtils.newHashMap();

    private static CourseService courseService = SpringUtils.getBean(CourseService.class);
    private static TemplateService templateService = SpringUtils.getBean(TemplateService.class);

    static {
        UPPERCASE_MAP.put(1,"一");
        UPPERCASE_MAP.put(2,"二");
        UPPERCASE_MAP.put(3,"三");
        UPPERCASE_MAP.put(4,"四");
        UPPERCASE_MAP.put(5,"五");
        UPPERCASE_MAP.put(6,"六");
        UPPERCASE_MAP.put(7,"七");
        UPPERCASE_MAP.put(8,"八");
        UPPERCASE_MAP.put(9,"九");
        UPPERCASE_MAP.put(10,"十");
        UPPERCASE_MAP.put(11,"十一");
        UPPERCASE_MAP.put(12,"十二");
        UPPERCASE_MAP.put(13,"十三");
        UPPERCASE_MAP.put(14,"十四");
        UPPERCASE_MAP.put(15,"十五");
        UPPERCASE_MAP.put(16,"十六");
        UPPERCASE_MAP.put(17,"十七");
        UPPERCASE_MAP.put(18,"十八");
        UPPERCASE_MAP.put(19,"十九");
        UPPERCASE_MAP.put(20,"二十");
    }

    public static String getChapterSuffixName(Integer charterNum){
        return "第"+ UPPERCASE_MAP.get(charterNum.intValue()) + "章";
    }


    public static String getCourseFullName(Course course){
        // 格式为： teacher.userName-name-(iterations).version
        StringBuffer stringBuffer = new StringBuffer();
        Teacher teacher = course.getTeacher();
        if (course.getFromStandardTemplate() && course.getTemplate() != null){
            if (TemplateType.STANDARD.value().equals(course.getTemplate().getType())){
                stringBuffer.append("标准");
            }else {
                Template template = templateService.get(course.getTemplate());
                stringBuffer.append(template.getTeacher().getTeacherName());
            }
            course.setVersion(course.getTemplate().getVersion());
        }else {
            stringBuffer.append(teacher.getTeacherName());
        }

        stringBuffer.append("-");
        stringBuffer.append(course.getName());

        if (course.getFromStandardTemplate() && course.getTemplate() != null){
            if (StringUtils.isNotBlank(course.getIterations())
                    && !"0".equals(course.getIterations())){
                stringBuffer.append("-");
                stringBuffer.append("（");
                stringBuffer.append(course.getIterations());
                stringBuffer.append("）");
            }
        }

        stringBuffer.append(".");
        stringBuffer.append(course.getVersion());

        return stringBuffer.toString();
    }

    // 迭代的方法
    public static String getNextVersion(Course course){

        Course courseCriteria = new Course();
        courseCriteria.setName(course.getName());
        courseCriteria.setTeacher(new Teacher(course.getTeacher().getTeacherId()));
        courseCriteria.setIterations(course.getIterations());
        Long count = courseService.findCount(courseCriteria);

        Double baseVersion = 1d;
        baseVersion += count / 10d;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        if (baseVersion > 10){
            decimalFormat = new DecimalFormat("000.0");
        }
        String newVersion = "v" + decimalFormat.format(baseVersion);
        return newVersion;
    }

    public static String getNextVersion(String version){
        if (StringUtils.isBlank(version)
                || version.length() < 3){
            return "v1.0";
        }
        String mainVersion = version.substring(1,version.indexOf("."));
        Integer nextMainVersion = (Integer.parseInt(mainVersion) + 1);

        String newVersion = "v" + nextMainVersion.toString() + ".0";
        return newVersion;
    }

    /**
     * 获取新的迭代号
     * @param templateId
     * @return
     */
    public static String getNewIterations(String templateId){
        Course courseCriteria = new Course();
        courseCriteria.setTeacher(TeacherUtils.getTeacher());
        courseCriteria.setTemplate(new Template(templateId));
        courseCriteria.setIterations_not_eq("0");
        return new Long(courseService.findCount(courseCriteria) + 1).toString();
    }
}
