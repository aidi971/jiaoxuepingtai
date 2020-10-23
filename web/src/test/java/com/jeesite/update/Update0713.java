//package com.jeesite.update;
//
//import com.jeesite.common.lang.StringUtils;
//import com.jeesite.modules.Application;
//import com.langheng.modules.ed.entity.Template;
//import com.langheng.modules.ed.service.TemplateService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes= Application.class)
//@Rollback(false)
//public class Update0713 {
//
//    @Autowired
//    private TemplateService templateService;
//
//    @Test
//    public void fixTemplateVersion(){
//        List<Template> templateList = templateService.findList(new Template());
//        templateList.forEach(template -> {
//            try{
//                if (StringUtils.isNotBlank(template.getFullName())){
//                    Integer length = template.getFullName().length();
//                    template.setVersion(template.getFullName().substring(length-4,length));
//                    templateService.save(template);
//                }
//            }catch (Exception e){}
//        });
//    }
//
//}
