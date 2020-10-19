ALTER TABLE `teaching_platform`.`ed_user_lesson_record`
ADD COLUMN `lesson_task_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称' AFTER `teaching_class_id`;


ALTER TABLE `teaching_platform`.`sys_classes`
MODIFY COLUMN `invitation_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '邀请码' AFTER `open_invite`;

ALTER TABLE `teaching_platform`.`ed_user_lesson_record`
ADD COLUMN `task_num` int(16) NULL COMMENT '任务序号' AFTER `lesson_task_name`;


==============================================   2020-05-19

ALTER TABLE `teaching_platform`.`sys_msg`
ADD COLUMN `biz_key` varchar(64) NULL COMMENT '业务主键' AFTER `receiver_key`;

ALTER TABLE `teaching_platform`.`ass_barrage`
ADD COLUMN `file_serialnumber` varchar(64) NULL COMMENT '文件编号' AFTER `sensitivity_type`;


======================================   2020-05-26
ALTER TABLE `teaching_platform`.`ed_lesson`
MODIFY COLUMN `introduce` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '‘节’或小节的介绍' AFTER `state`;

ALTER TABLE `teaching_platform`.`sys_classes`
DROP INDEX `invitation_code_index`;


ALTER TABLE `teaching_platform`.`ed_template`
MODIFY COLUMN `video_num` decimal(16, 0) NULL DEFAULT NULL COMMENT '视频数量' AFTER `img_src`,
MODIFY COLUMN `homework_num` decimal(16, 0) NULL DEFAULT NULL COMMENT '作业数量' AFTER `video_num`,
MODIFY COLUMN `courseware_num` decimal(16, 0) NULL DEFAULT NULL COMMENT '课件数量' AFTER `homework_num`;


ALTER TABLE `teaching_platform`.`ed_course`
ADD COLUMN `video_num` decimal(16, 0) NULL DEFAULT NULL COMMENT '视频数量' AFTER `remarks`,
ADD COLUMN `homework_num` decimal(16, 0) NULL DEFAULT NULL COMMENT '作业数量' AFTER `video_num`,
ADD COLUMN `courseware_num` decimal(16, 0) NULL DEFAULT NULL COMMENT '课件数量' AFTER `homework_num`;



=================================== 05/28

ALTER TABLE `teaching_platform`.`sys_user`
MODIFY COLUMN `cover_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像' AFTER `sex`;


====================== 06/03

ALTER TABLE `teaching_platform`.`msg_notice`
ADD COLUMN `theme` varchar(255) NULL COMMENT '主题' AFTER `title`;

ALTER TABLE `teaching_platform`.`sys_classes_apply`
ADD COLUMN `stu_name` varchar(255) NULL COMMENT '学生名' AFTER `teacher_id`,
ADD COLUMN `login_code` varchar(64) NULL COMMENT '登录名' AFTER `stu_name`;

ALTER TABLE `teaching_platform`.`ass_discuss`
ADD COLUMN `lesson_full_name` varchar(255) NULL COMMENT '小节全称' AFTER `content`;
ALTER TABLE `teaching_platform`.`ass_discuss`
CHANGE COLUMN `student_id` `user_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id' AFTER `discuss_id`;


ALTER TABLE `teaching_platform`.`ass_discuss`
MODIFY COLUMN `target_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '讨论对象类型(字典集 | sys_file_type)' AFTER `sub_lesson_id`,
MODIFY COLUMN `is_reply` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否是回复 （0不是，1是）' AFTER `parent_discuss_id`,
MODIFY COLUMN `pubdate` datetime(0) NULL COMMENT '发布时间' AFTER `is_reply`;

ALTER TABLE `teaching_platform`.`ass_give_like`
CHANGE COLUMN `student_id` `user_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id' AFTER `biz_key`;

ALTER TABLE `teaching_platform`.`ed_lesson_task`
ADD COLUMN `sort` int(32) NULL AUTO_INCREMENT COMMENT '排序' AFTER `is_modify`,
ADD UNIQUE INDEX `排序索引`(`sort`) USING BTREE;


===============================   06/05
ALTER TABLE `teaching_platform`.`sys_student`
MODIFY COLUMN `motto` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个性签名' AFTER `is_can_send_msg`;

ALTER TABLE `teaching_platform`.`ass_discuss`
ADD COLUMN `remarks` varchar(255) NULL COMMENT '备注' AFTER `lesson_full_name`;


===============================   06/22

ALTER TABLE `teaching_platform`.`ass_discuss`
ADD COLUMN `course_id` varchar(64) NULL COMMENT '课程id' AFTER `teaching_class_id`;


===============================   06/24

ALTER TABLE `teaching_platform`.`msg_notice`
ADD COLUMN `classes_id` varchar(64) NULL COMMENT '班级id' AFTER `biz_key`;

ALTER TABLE `teaching_platform`.`sys_msg_user`
ADD INDEX `msg_id_index`(`msg_id`) USING BTREE,
ADD INDEX `user_code_index`(`user_code`) USING BTREE;

ALTER TABLE `teaching_platform`.`sys_login_log`
ADD COLUMN `user_agent` varchar(255) NULL COMMENT '用户代理' AFTER `type`;


================================   07/02
ALTER TABLE `teaching_platform`.`sys_msg`
ADD COLUMN `teaching_class_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课堂id' AFTER `receive_names`;

ALTER TABLE `teaching_platform`.`msg_notice`
ADD COLUMN `teaching_class_id` varchar(64) NULL COMMENT '课堂id' AFTER `classes_id`;

===============================  07/03
ALTER TABLE `teaching_platform`.`sys_student`
ADD COLUMN `classes_id` varchar(64) NULL COMMENT '班级id' AFTER `motto`;

ALTER TABLE `teaching_platform`.`msg_notice`
ADD COLUMN `total_stu_num` int(16) NULL COMMENT '总人数' AFTER `teaching_class_id`;


================================  07/10

ALTER TABLE `teaching_platform`.`sys_user`
ADD COLUMN `motto` varchar(255) NULL COMMENT '个性签名' AFTER `sex`;

ALTER TABLE `teaching_platform`.`ed_template`
ADD COLUMN `version` varchar(32) NULL COMMENT '版本号' AFTER `full_name`;


================================ 07-16
ALTER TABLE `teaching_platform`.`ass_barrage_sensitive_word`
ADD COLUMN `is_show` char(1) NULL COMMENT '是否显示' AFTER `hit_count`;


================================  07-23
ALTER TABLE `teaching_platform`.`ass_nominate`
ADD COLUMN `user_name` varchar(64) NULL COMMENT '用户名' AFTER `login_code`;
