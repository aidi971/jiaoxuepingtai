//package com.jeesite.update;
//
//import com.jeesite.common.lang.StringUtils;
//import com.jeesite.modules.Application;
//import com.langheng.modules.ass.entity.BarrageSensitiveWord;
//import com.langheng.modules.ass.filter.WordFilter;
//import com.langheng.modules.ass.service.BarrageSensitiveWordService;
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
//public class Update0716 {
//
//    @Autowired
//    BarrageSensitiveWordService sensitiveWordService ;
//
//    @Test
//    public void saveSensitiveWord(){
//        List<String> senWordList = WordFilter.readSensitiveWordFromFile("classpath:/sensitiveWord/words/色情词库.txt");
//            senWordList.forEach(word -> {
//            try{
//                if (StringUtils.isNotBlank(word)){
//                    BarrageSensitiveWord sensitiveWord = new BarrageSensitiveWord();
//                    sensitiveWord.setSensitivityType("0007");
//
//                    sensitiveWord.setSensitiveWord(word);
//                    sensitiveWord.setHitCount(0);
//                    sensitiveWord.setState("0");
//                    sensitiveWordService.save(sensitiveWord);
//                }
//            }catch (Exception e){}
//        });
//    }
//
//}
