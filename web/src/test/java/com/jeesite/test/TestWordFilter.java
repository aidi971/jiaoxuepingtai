//package com.jeesite.test;
//
//import com.jeesite.common.enumn.StatusEnum;
//import com.jeesite.modules.Application;
//import com.langheng.modules.ass.entity.BarrageSensitiveWord;
//import com.langheng.modules.ass.filter.WordFilter;
//import com.langheng.modules.ass.service.BarrageSensitiveWordService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes= Application.class)
//public class TestWordFilter {
//
//    @Autowired
//    BarrageSensitiveWordService sensitiveWordService;
//
//    @Test
//    public void testFilter(){
//
//        try{
//            sensitiveWordService.save(sensitiveWordService.findList(new BarrageSensitiveWord()).get(0));
//        }catch (Exception e){}
//
//        if (WordFilter.isContains("客服")){
//            System.out.println("客服" + "被屏蔽了  1");
//        }
//
//        String word = "略";
//        if (WordFilter.isContains(word)){
//            System.out.println(word + "被屏蔽了 1");
//        }
//
//        WordFilter.resetData();
//        if (WordFilter.isContains(word)){
//            System.out.println(word + "被屏蔽了  2");
//        }
//
//        if (WordFilter.isContains("客服")){
//            System.out.println("客服" + "被屏蔽了  2");
//        }
//
//        BarrageSensitiveWord sensitiveWord = new BarrageSensitiveWord();
//        sensitiveWord.setSensitiveWord(word);
//        sensitiveWord.setHitCount(0);
//        sensitiveWord.setState(StatusEnum.NORMAL.value());
//        sensitiveWordService.save(sensitiveWord);
//
//        if (WordFilter.isContains("客服")){
//            System.out.println("客服" + "被屏蔽了  3");
//        }
//
//        if (WordFilter.isContains(word)){
//            System.out.println(word + "被屏蔽了  3");
//        }
//
//        if (WordFilter.isContains("客服")){
//            System.out.println("客服" + "被屏蔽了  4");
//        }
//    }
//
//}
