/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.enumn.StatusEnum;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.io.ResourceUtils;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.RedisUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.*;
import com.langheng.modules.base.enumn.EventHandlingType;
import com.langheng.modules.base.enumn.StudentClassesState;
import com.langheng.modules.base.enumn.StudentOriginType;
import com.langheng.modules.base.event.TeachingClassEvent;
import com.langheng.modules.base.service.*;
import com.langheng.modules.base.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 班级Controller
 * @author xiaoxie
 * @version 2019-12-18
 */
@Api(description = "班级管理相关接口")
@RestController
@RequestMapping("admin/classes")
public class ClassesController extends BaseApiController {

	@Autowired
	private ClassesService classesService;

	@Autowired
	private UserHeadService userHeadService;

	@Autowired
	private AcademyService academyService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private StudentClassesService studentClassesService;

	@Autowired
	private ClassesApplyService classesApplyService;

	@Autowired
	private BaseUserService baseUserService;

	/**
	 * 获取班级数据
	 */
	@ModelAttribute
	public Classes get(String classesId, boolean isNewRecord) {
		return classesService.get(classesId, isNewRecord);
	}

	@ApiOperation("根据ID获取班级")
	@GetMapping("get")
	public ResultBean<Classes>  get(String classesId) {
		Classes classes = classesService.get(classesId);
		return success(classes);
	}

	@ApiOperation("查询该教师班级所属多少个二级机构")
	@PostMapping("findAcademyList")
	public ResultBean<List<Academy>>  findAcademyList() {
		Classes classes = new Classes();
		classes.setTeacher(TeacherUtils.getTeacher());
		List<String> academyIds = classesService.findAcademyIdsOrderByAcademyId(classes);
		List<Academy> academyList = ListUtils.newArrayList();
		if (!academyIds.isEmpty()){
			Academy academy = new Academy();
			academy.setId_in(academyIds.toArray(new String[]{}));
			academyList = academyService.findList(academy);
		}

		return success(academyList);
	}

	@ApiOperation(value = "保存班级")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "coverImg", value = "班级封面", required = true),
			@ApiImplicitParam(name = "className", value = "班级名称", required = true),
			@ApiImplicitParam(name = "academy.academyId", value = "学院id", required = true),
	})
	@PostMapping("save")
	public ResultBean save(@Validated Classes classes) {
		classesService.save(classes);
		return success(classes);
	}

	@ApiOperation("查询班级列表数据")
	@PostMapping("findList")
	public ResultBean<List<Classes>>  findList(Classes classes ) {
		if (!BaseUserUtils.isSuperAdmin()){
			classes.setTeacher(TeacherUtils.getTeacher());
		}
		List<Classes> classesList = classesService.findList(classes);
		return success(classesList);
	}

	@ApiOperation("随机封面")
	@PostMapping("/randomCoverImg")
	public ResultBean randomHeadImg() {
		UserHead userHead = userHeadService.getUserHead(6);
		return success(userHead.getHeadUrl());
	}

	@ApiOperation("开启邀请码  | 返回新的邀请码")
	@PostMapping("openInvite")
	public ResultBean<String> openInvite(String classesId) {
		Classes classes = classesService.get(classesId);
		classes.setOpenInvite(SysYesNoEnum.YES.value());
		classes.setInvitationCode(InvitationCodeUtils.getInvitationCode());
		// 设置4小时过期
		classes.setInviteCloseTime(DateUtils.addHours(new Date(),Integer.parseInt(Global.getConfig("sys.account.register.invitationCode.expiry.time","4"))));
		classesService.save(classes);
		return success(classes.getInvitationCode());
	}

	@ApiOperation("关闭邀请码")
	@PostMapping("closeInvite")
	public ResultBean closeInvite(String classesId) {
		Classes classes = classesService.get(classesId);
		classes.setOpenInvite(SysYesNoEnum.NO.value());
		classes.setInvitationCode("");
		classesService.save(classes);
		return success();
	}


	@ApiOperation("开启审核")
	@PostMapping("openAudit")
	public ResultBean openAudit(String classesId) {
		Classes classes = classesService.get(classesId);
		classes.setIsNeedAudit(SysYesNoEnum.YES.value());
		classesService.save(classes);
		return success();
	}

	@ApiOperation("关闭审核")
	@PostMapping("closeAudit")
	public ResultBean closeAudit(String classesId) {
		Classes classes = classesService.get(classesId);
		classes.setIsNeedAudit(SysYesNoEnum.NO.value());
		classesService.save(classes);
		return success();
	}

	@ApiOperation(value = "获取班级所有的学生信息")
	@PostMapping(value = "findStudentPage")
	public ResultBean<Page<Student>> findStudentPage(@RequestParam String classesId,
											  @RequestParam(defaultValue = "10") int pageSize,
											  @RequestParam(defaultValue = "1") int pageNo ,
											  Student student) {
		List<Student> students = studentService.findListByClasses(student,classesId);
		Page<Student> page = new Page<>();
        page.setCount(students.size());
		page.setList(students);
		if (!students.isEmpty()){
			Set<String> set = RedisUtils.keys("user:token:*");
			set.forEach(userTokenKey->{
				students.forEach(item->{
					item.setUserPassword(item.getPassword());
					if (userTokenKey.contains(item.getId())){
						item.setIsOnline(Global.YES);
					}
				});
			});
		}
		return success(page);
	}


	@ApiOperation(value = "班级新增学生（修改学生不在此）")
	@PostMapping(value = "addStudent")
	@Transactional(readOnly = false)
	public ResultBean<Student> addStudent(@RequestParam String classesId,@Validated Student student){

		BaseUser baseUser = baseUserService.getByLoginCode(student.getLoginCode());
		if (baseUser != null){
			student = studentService.get(baseUser.getId());
			if (student == null){
				return error("该登录账号不可用！");
			}
			if(classesService.isStudentExistClasses(student)){
				return error("该学生已经存在班级，不可再添加！");
			}
		}

		Classes classes = classesService.get(classesId);
		if (student.getIsNewRecord()){
			student.setOriginType(StudentOriginType.ADD.value());
		}
		student.setClassesId(classes.getClassesId());
		studentService.save(student);

		StudentClasses studentClasses = new StudentClasses();
		studentClasses.setClassesId(classes.getId());
		studentClasses.setStudentId(student.getId());
		studentClasses.setState(StudentClassesState.NORMAL.value());
		studentClassesService.save(studentClasses);

		// 调整班级数量
		classesService.adjustStudentNum(classes);

		student = studentService.get(student.getStudentId());
		student.setUserPassword(student.getPassword());
		return success(student);
	}


	@ApiOperation(value = "班级新增虚拟学生")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "classesId", value = "班级id", required = true),
			@ApiImplicitParam(name = "num", value = "数量", required = true),
	})
	@PostMapping(value = "addVirtualStudent")
	public ResultBean addVirtualStudent(@RequestParam String classesId,
												 @RequestParam Integer num){
		Classes classes = classesService.get(classesId);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String suffixStr = dateFormat.format(new Date());
		DecimalFormat decimalFormat = new DecimalFormat("00");
		// 获取最小后缀
        int maxSuffix  = studentService.getMaxLoginCode(suffixStr);
        List<Student> studentList = ListUtils.newArrayList();
		for (int index=0; index<num ;index++){
			Student student = new Student();
			student.setLoginCode(suffixStr + decimalFormat.format(index + maxSuffix + 1));
			student.setStudentName(suffixStr + decimalFormat.format(index + maxSuffix + 1));
			if (student.getIsNewRecord()){
				student.setOriginType(StudentOriginType.VIRTUAL.value());
			}
			student.setClassesId(classes.getClassesId());
			student.setPassword("666666");
			studentService.save(student);
			StudentClasses studentClasses = new StudentClasses();
			studentClasses.setClassesId(classes.getId());
			studentClasses.setStudentId(student.getId());
			studentClasses.setState(StudentClassesState.NORMAL.value());
			studentClassesService.save(studentClasses);

			student.setUserPassword(student.getPassword());
			studentList.add(student);
		}

		// 调整班级数量
		classesService.adjustStudentNum(classes);

		return success(studentList);
	}


	@ApiOperation(value = "班级移除该学生")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "classesId", value = "班级id", required = true),
			@ApiImplicitParam(name = "studentId", value = "学生id, 有多个则用逗号分隔", required = true),
	})
	@PostMapping(value = "removeStudent")
	public ResultBean removeStudent(@RequestParam String classesId,
									@RequestParam String studentId){
		Classes classes = classesService.get(classesId);

		StudentClasses studentClasses = new StudentClasses();
		studentClasses.setClassesId(classes.getId());
		studentClasses.setStudentId_in(studentId.split(","));

		List<StudentClasses> studentClassesList = studentClassesService.findList(studentClasses);
		if (!studentClassesList.isEmpty()){
			for (StudentClasses item : studentClassesList){
				Student student = studentService.get(item.getStudentId());
				studentClassesService.phyDelete(item);
				studentService.phyDelete(student);
				RegisterUtils.revokeUser(student,BaseUserUtils.getUser().getUserName(), EventHandlingType.MANUAL);
			}
		}

		// 调整班级数量
		classesService.adjustStudentNum(classes);
		return success();
	}

	@ApiOperation(value = "下载导入学生信息模板")
	@PostMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) throws IOException {
		String fileName = "学生信息模板.xlsx";
		InputStream inputStream = ResourceUtils.getResourceFileStream("classpath:/other/importStudentTlt.xlsx");
		response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");
		response.addHeader("Content-Disposition", "attachment; filename*=utf-8'zh_cn'"+ EncodeUtils.encodeUrl(fileName));
		OutputStream outputStream = response.getOutputStream();

		byte[] buffer = new byte[1024];
		int len = inputStream.read(buffer);
		while (len != -1) {
			outputStream.write(buffer, 0, len);
			len = inputStream.read(buffer);
		}
	}


	@ApiOperation(value = "导入学生信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "file", value = "Excel文件", required = true),
			@ApiImplicitParam(name = "classesId", value = "班级id", required = true),
			@ApiImplicitParam(name = "updateSupport", value = "是否更新",example = "true,false",required = false),
	})
	@PostMapping(value = "importData")
	@ResponseBody
	public ResultBean importData(MultipartFile file,
								 @RequestParam String classesId,
								 String updateSupport) {
		try {
			boolean isUpdateSupport = Global.YES.equals(updateSupport);
			List<Student> studentList = ListUtils.newArrayList();
			String message = studentService.importStudentToClass(file,classesId, isUpdateSupport,studentList);
			studentList.forEach(student -> {
				student.setUserPassword(student.getPassword());
			});
			return success(studentList,message);
		} catch (Exception ex) {
			return error(ex.getMessage());
		}
	}

	/**
	 * 导出学生信息
	 */
	@ApiOperation(value = "导出学生信息")
	@PostMapping(value = "exportData")
	public void exportData(@RequestParam String classesId,  HttpServletResponse response) {
		Classes classes = classesService.get(classesId);
		if (classes != null){
			List<Student> studentList = studentClassesService.findStudents(classesId);
			int index = 1;
			for (Student student: studentList){
				student.setSerialNumber(index++);
			}
			String fileName = "学生数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			try(ExcelExport ee = new ExcelExport(classes.getClassName(), Student.class)){
				ee.setDataList(studentList).write(response, fileName);
			}
		}
	}

	@ApiOperation(value = "查询加入班级申请列表")
	@PostMapping(value = "findApplyList")
	public ResultBean<List<ClassesApply>>  findApplyList(@RequestParam String classesId) {
		ClassesApply classesApply = new ClassesApply();
		classesApply.setClasses(new Classes(classesId));
		List<ClassesApply> classesApplyList = classesApplyService.findList(classesApply);
		return success(classesApplyList);
	}

	@ApiOperation(value = "获取回收站班级列表")
	@PostMapping(value = "findDeletedList")
	@Transactional(readOnly = false)
	public ResultBean  findDeletedList() {
		Classes classes = new Classes();
		classes.setTeacher(TeacherUtils.getTeacher());
		classes.disableStatus();
		classes.setStatus_in(new String[]{StatusEnum.DELETE.value()});
		List<Classes> classesList = classesService.findList(classes);
		return success(classesList);
	}

	@ApiOperation(value = "删除班级")
	@PostMapping(value = "delete")
	@Transactional(readOnly = false)
	public ResultBean  delete(@RequestParam String classesId) {
		Classes classes = classesService.get(classesId);
		classesService.delete(classes);
		// 删除班级关联的课堂
		SpringUtils.getApplicationContext()
				.publishEvent(new TeachingClassEvent(this,TeachingClassEvent.DELETE_TEACHING_CLASS,classes));
		return success();
	}

	@ApiOperation(value = "恢复班级")
	@PostMapping(value = "enable")
	@Transactional(readOnly = false)
	public ResultBean  enable(@RequestParam String classesId) {
		Classes classes = classesService.get(classesId);
		classes.setStatus(StatusEnum.NORMAL.value());
		classesService.updateStatus(classes);
		// 回复班级关联的课堂
		SpringUtils.getApplicationContext()
				.publishEvent(new TeachingClassEvent(this,TeachingClassEvent.ENABLE_TEACHING_CLASS,classes));
		return success();
	}

	@ApiOperation(value = "物理删除（清除班级）")
	@PostMapping(value = "phyDelete")
	@Transactional(readOnly = false)
	public ResultBean  phyDelete(@RequestParam String classesId) {
		Classes classes = classesService.get(classesId);
		classesService.phyDelete(classes);
		// 物理删除班级关联的课堂
		SpringUtils.getApplicationContext()
				.publishEvent(new TeachingClassEvent(this,TeachingClassEvent.PHY_DELETE_TEACHING_CLASS,classes));
		return success();
	}


	@ApiOperation(value = "上传班级封面")
	@PostMapping("/uploadCoverImg")
	public ResultBean uploadCoverImg(HttpServletRequest request, @RequestParam(value = "file", required = true) MultipartFile file) {
		String realPath = null;
		String executeUpload = null;
		try {
			realPath = Global.getConfig("web.upload-path") + Global.getConfig("uploadF.coverimg");
			FileUtils.createDirectory(realPath);
			executeUpload = uplocadUtil.executeUpload(realPath, file, Global.getConfig("uploadF.coverimg"));
		} catch (Exception e) {
			e.printStackTrace();
			return error("上传失败!");
		}
		return success(executeUpload);
	}

	@ApiOperation("强制下线")
	@PostMapping("tickOutStudent")
	public ResultBean<List<Classes>>  tickOutStudent(@RequestParam String classesId,
											  @RequestParam String studentIds ) {

		String [] studentIdArr = studentIds.split(",");
		BaseUserUtils.tickOut(studentIdArr);
		Classes classes = classesService.get(classesId);
		ClassesUtils.cleanCache(classes,studentIdArr);
		ClassesUtils.updateStuOnlineNum(classes);
		return success(null,"强制下线成功！");
	}
}