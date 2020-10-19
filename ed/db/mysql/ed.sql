
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ed_chapter
-- ----------------------------
DROP TABLE IF EXISTS `ed_chapter`;
CREATE TABLE `ed_chapter` (
  `chapter_id` varchar(64) NOT NULL COMMENT '章节id',
  `course_id` varchar(64) NOT NULL COMMENT '课程id',
  `name` varchar(64) NOT NULL COMMENT '章节名称',
  `chapter_num` decimal(4,0)  NULL COMMENT '章节序号',
  `state` varchar(8) NOT NULL COMMENT '章节状态 | 字典集 ed_chapter_state',
  `status` varchar(32) NOT NULL COMMENT '状态 | 字典集 sys_status',
  `create_by` varchar(64)  NULL COMMENT '创建者',
  `update_by` varchar(64)  NULL COMMENT '更新者',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime  NULL COMMENT '更新日期',
  PRIMARY KEY (`chapter_id`)
) ENGINE=InnoDB  CHARSET=utf8 COMMENT='课程章节';

-- ----------------------------
-- Table structure for ed_classes_progress
-- ----------------------------
DROP TABLE IF EXISTS `ed_classes_progress`;
CREATE TABLE `ed_classes_progress` (
  `progress_id` varchar(64) NOT NULL COMMENT '班级进度id',
  `classes_id` varchar(64) NOT NULL COMMENT '班级',
  `biz_id` varchar(64) NOT NULL COMMENT '关联业务主键(可为: 课程id，章节id，小节id）',
  `type` varchar(16)  NULL COMMENT '类型 字典集 ed_classes_progress_type',
  `state` varchar(64) NOT NULL COMMENT '进度状态 | 字典集 ed_classes_progress_state',

  `status` varchar(32) NOT NULL COMMENT '状态 | 字典集 sys_status',
  `create_by` varchar(64)  NULL COMMENT '创建者',
  `update_by` varchar(64)  NULL COMMENT '更新者',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime  NULL COMMENT '更新日期',
  PRIMARY KEY (`lesson_classes_id`)
) ENGINE=InnoDB  CHARSET=utf8;

-- ----------------------------
-- Table structure for ed_course
-- ----------------------------
DROP TABLE IF EXISTS `ed_course`;
CREATE TABLE `ed_course` (
  `course_id` varchar(64) NOT NULL COMMENT '课程id',
  `template_id` varchar(64) NOT NULL COMMENT '关联模板id',
  `teacher_id` varchar(64) NOT NULL COMMENT '教师id',
  `type` varchar(16)  NULL COMMENT '类型',
  `is_template` varchar(16)  NULL COMMENT '是否为模板',
  `is_public` varchar(16)  NULL COMMENT '是否公开',
  `is_enabled` varchar(16)  NULL COMMENT '是否启用',
  `name` varchar(64) NOT NULL COMMENT '课程名称',
  `img_src` varchar(225)  NULL COMMENT '图片路径',
  `state` varchar(8) NOT NULL COMMENT '课程状态| 字典集 ed_course_state',

  `status` varchar(32) NOT NULL COMMENT '状态 | 字典集 sys_status',
  `create_by` varchar(64)  NULL COMMENT '创建者',
  `update_by` varchar(64)  NULL COMMENT '更新者',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime  NULL COMMENT '更新日期',
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB  CHARSET=utf8 COMMENT='课程';

-- ----------------------------
-- Table structure for ed_alter_record
-- ----------------------------
DROP TABLE IF EXISTS `ed_alter_record`;
CREATE TABLE `ed_alter_record` (
  `record_id` varchar(64) NOT NULL COMMENT '更变id',
  `lesson_id` varchar(64) NOT NULL COMMENT '关联小节id',
  `operation_type` varchar(16) NOT NULL COMMENT '操作类型（新增，删除）',
  `biz_id` varchar(64) NOT NULL COMMENT '操作的目标主键id',
  `type` varchar(64) NOT NULL COMMENT '目标类型 | 字典集 ed_alter_record_type',
  `status` varchar(32) NOT NULL COMMENT '状态 | 字典集 sys_status',
  `create_by` varchar(64)  NULL COMMENT '创建者',
  `update_by` varchar(64)  NULL COMMENT '更新者',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime  NULL COMMENT '更新日期',
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB  CHARSET=utf8 COMMENT='修改记录';

-- ----------------------------
-- Table structure for ed_department
-- ----------------------------
DROP TABLE IF EXISTS `ed_department`;
CREATE TABLE `ed_department` (
  `dept_id` varchar(64) NOT NULL COMMENT '部门id',
  `school_id` varchar(64) NOT NULL COMMENT '学校id',
  `dept_name` varchar(64) NOT NULL COMMENT '二级部门名字',
  `status` varchar(32) NOT NULL COMMENT '状态 | 字典集 sys_status , 2.暂停',
  `create_by` varchar(64)  NULL COMMENT '创建者',
  `update_by` varchar(64)  NULL COMMENT '更新者',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime  NULL COMMENT '更新日期',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB  CHARSET=utf8;

-- ----------------------------
-- Table structure for ed_lesson
-- ----------------------------
DROP TABLE IF EXISTS `ed_lesson`;
CREATE TABLE `ed_lesson` (
  `lesson_id` varchar(64) NOT NULL COMMENT '小节id',
  `chapter_id` varchar(64) NOT NULL COMMENT '章节id',
  `name` varchar(64) NOT NULL COMMENT '小节名称',
  `lesson_num` decimal(4)  NULL COMMENT '小节序号',
  `state` varchar(8) NOT NULL COMMENT '小节状态  参考字典集 ed_lesson_state',
  
  `status` varchar(32) NOT NULL COMMENT '状态 | 字典集 sys_status',
  `create_by` varchar(64)  NULL COMMENT '创建者',
  `update_by` varchar(64)  NULL COMMENT '更新者',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime  NULL COMMENT '更新日期',
  PRIMARY KEY (`lesson_id`)
) ENGINE=InnoDB  CHARSET=utf8 COMMENT='课程小节';


-- ----------------------------
-- Table structure for ed_user_lesson
-- ----------------------------
DROP TABLE IF EXISTS `ed_user_lesson`;
CREATE TABLE `ed_user_lesson` (
  `user_lesson_id` varchar(64) NOT NULL COMMENT '学生小节id',
  `lesson_id` varchar(64) NOT NULL COMMENT '小节id',
  `user_id` varchar(64) NOT NULL COMMENT '学生id',
  `student_name` varchar(64) NOT NULL COMMENT '学生姓名',
  `state` varchar(8) NOT NULL COMMENT '学生课程小节状态| 字典集 ed_user_lesson_state',
  `video_state` varchar(10)  NULL COMMENT '视频状态 | 字典集 ed_user_lesson_task_state',
  `job_state` varchar(10)  NULL COMMENT '作业状态 | 字典集 ed_user_lesson_task_state',
  `courseware_state` varchar(10)  NULL COMMENT '课件状态| 字典集 ed_user_lesson_task_state',
  `status` varchar(32) NOT NULL COMMENT '状态 | 字典集 sys_status',
  `create_by` varchar(64)  NULL COMMENT '创建者',
  `update_by` varchar(64)  NULL COMMENT '更新者',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime  NULL COMMENT '更新日期',
  PRIMARY KEY (`user_lesson_id`)
) ENGINE=InnoDB  CHARSET=utf8 COMMENT='学生小节';

-- ----------------------------
-- Table structure for ed_lesson_task
-- ----------------------------
DROP TABLE IF EXISTS `ed_lesson_task`;
CREATE TABLE `ed_lesson_task` (
  `lesson_task_id` varchar(64) NOT NULL COMMENT '小节文件id',
  `task_id` varchar(64) NOT NULL COMMENT '任务id',
  `lesson_id` varchar(64) NOT NULL COMMENT '小节id',
  `type` varchar(64) NOT NULL COMMENT '类型 |字典集 ed_lesson_task_type',

  `status` varchar(32) NOT NULL COMMENT '状态 | 字典集 sys_status',
  `create_by` varchar(64)  NULL COMMENT '创建者',
  `update_by` varchar(64)  NULL COMMENT '更新者',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime  NULL COMMENT '更新日期',
  PRIMARY KEY (`lesson_task_id`)
) ENGINE=InnoDB  CHARSET=utf8 COMMENT='小节任务（文件）';


-- ----------------------------
-- Table structure for ed_user_task
-- ----------------------------
DROP TABLE IF EXISTS `ed_user_task`;
CREATE TABLE `ed_user_task` (
  `user_task_id` varchar(64) NOT NULL COMMENT '学生任务id',
  `user_id` varchar(64) NOT NULL COMMENT '学生id',
  `student_name` varchar(64) NOT NULL COMMENT '学生姓名',
  `task_id` varchar(64) NOT NULL COMMENT '任务id',
  `lesson_id` varchar(64) NOT NULL COMMENT '小节id',
  `type` varchar(64) NOT NULL COMMENT '类型 |字典集 ed_lesson_task_type',
  `state` varchar(64) NOT NULL COMMENT '状态 |字典集 ed_user_task_state',

  `status` varchar(32) NOT NULL COMMENT '状态 | 字典集 sys_status',
  `create_by` varchar(64)  NULL COMMENT '创建者',
  `update_by` varchar(64)  NULL COMMENT '更新者',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime  NULL COMMENT '更新日期',
  PRIMARY KEY (`user_task_id`)
) ENGINE=InnoDB  CHARSET=utf8 COMMENT='学生任务';

-- ----------------------------
-- Table structure for ed_school
-- ----------------------------
DROP TABLE IF EXISTS `ed_school`;
CREATE TABLE `ed_school` (
  `school_id` varchar(64) NOT NULL COMMENT '学校id',
  `school_name` varchar(64) NOT NULL COMMENT '学校名字',
  `log` varchar(64) NOT NULL COMMENT '学校logo',
  `status` varchar(32) NOT NULL COMMENT '状态 | 字典集 sys_status',
  `create_by` varchar(64)  NULL COMMENT '创建者',
  `update_by` varchar(64)  NULL COMMENT '更新者',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime  NULL COMMENT '更新日期',
  PRIMARY KEY (`school_id`)
) ENGINE=InnoDB  CHARSET=utf8;

-- ----------------------------
-- Table structure for ed_user_chapter
-- ----------------------------
DROP TABLE IF EXISTS `ed_user_chapter`;
CREATE TABLE `ed_user_chapter` (
  `user_chapter_id` varchar(64) NOT NULL COMMENT '学生章节id',
  `chapter_id` varchar(64) NOT NULL COMMENT '章节id',
  `user_id` varchar(64) NOT NULL COMMENT '学生id',
  `state` varchar(8) NOT NULL COMMENT '学生章节状态 | 字典集 ed_user_chapter_state',

  `status` varchar(32) NOT NULL COMMENT '状态 | 字典集 sys_status',
  `create_by` varchar(64)  NULL COMMENT '创建者',
  `update_by` varchar(64)  NULL COMMENT '更新者',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime  NULL COMMENT '更新日期',
  PRIMARY KEY (`user_chapter_id`)
) ENGINE=InnoDB  CHARSET=utf8 COMMENT='学生章节';


