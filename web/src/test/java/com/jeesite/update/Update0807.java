//package com.jeesite.update;
//
//import com.jeesite.modules.Application;
//import com.langheng.modules.ass.entity.Discuss;
//import com.langheng.modules.ass.service.DiscussService;
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
//public class Update0807 {
//
//    @Autowired
//    private DiscussService discussService;
//    @Test
//    public void updateReplyDiscuss(){
//
//        Discuss discussCriteria = new Discuss();
//        discussCriteria.setIsReply("1");
//        List<Discuss> discussList = discussService.findList(discussCriteria);
//        for (Discuss discuss: discussList){
//            try {
//                Discuss parentDiscuss = discussService.get(discuss.getParentDiscussId());
//                if ("1".equals(parentDiscuss.getIsReply())){
//                    discuss.setCourseId(parentDiscuss.getCourseId());
//                    discuss.setChapterId(parentDiscuss.getChapterId());
//                    discuss.setLessonId(parentDiscuss.getLessonId());
//                    discuss.setSubLessonId(parentDiscuss.getSubLessonId());
//                    discuss.setTargetKey(parentDiscuss.getTargetKey());
//                    discuss.setTargetType(parentDiscuss.getTargetType());
//                    discussService.save(discuss);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//}
