package com.langheng.modules.base.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class uplocadUtil {
    /**
     * 提取上传方法为公共方法
     *
     * @param uploadDir 上传文件目录
     * @param file      上传对象
     * @param filepath
     * @throws Exception
     */
    public static String executeUpload(String uploadDir, MultipartFile file, String filepath) throws Exception {
        //文件后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        int size = (int) file.getSize();
        //上传文件名
        //uuid
        String uuidInOrderId = NoteUtil.createID();
        String filename = uuidInOrderId + suffix;
        //服务器端保存的文件对象
        File serverFile = new File(uploadDir + filename);
        //将上传的文件写入到服务器端文件内
        file.transferTo(serverFile);
        return filepath+filename;
    }

    public static String UploadDir(HttpServletRequest request,String uploadDir) throws Exception {
//        String projectPath = request.getServletContext().getRealPath("/");
//        String tomcatPath = new File(projectPath).getParentFile().getParentFile().getAbsolutePath();

//        System.out.println(request.getClass().getResource("/").getPath().replaceAll("^\\/", ""));
        String projectPath=request.getClass().getClassLoader().getResource("").getPath();
//        String tomcatPath = new File(request.getClass().getClassLoader().getResource("").getPath()).getParentFile().getParentFile().getAbsolutePath();
//        System.out.println(tomcatPath);
//        String realPath = projectPath+"/webapps/"+ uploadDir;
        System.out.println("projectPath = " + projectPath);
        String realPath = projectPath+ uploadDir;
        return realPath;
    }



}
