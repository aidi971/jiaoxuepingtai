package com.langheng.modules.base.manager.factory;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.dao.LoginLogDao;
import com.langheng.modules.base.entity.*;
import com.langheng.modules.base.enumn.MsgReceiverType;
import com.langheng.modules.base.enumn.MsgUserState;
import com.langheng.modules.base.event.MsgEvent;
import com.langheng.modules.base.event.SystemEvent;
import com.langheng.modules.base.service.MsgService;
import com.langheng.modules.base.service.MsgUserService;
import com.langheng.modules.base.service.StudentClassesService;
import com.langheng.modules.base.utils.BaiduAiUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author liuhulu
 *
 */
public class AsyncFactory {

    private static MsgService msgService = SpringUtils.getBean(MsgService.class);
    private static MsgUserService msgUserService = SpringUtils.getBean(MsgUserService.class);
    private static StudentClassesService studentClassesService = SpringUtils.getBean(StudentClassesService.class);

    /**
     * 记录登陆信息
     *
     * @param isLogin 状态
     * @return 任务task
     */
    public static TimerTask recordLoginLog(BaseUser baseUser, Boolean isLogin, HttpServletRequest request)
    {
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String userAgentStr = request.getHeader("User-Agent");
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                LoginLog loginLog = new LoginLog();
                loginLog.setInfoId(IdGen.nextId());
                loginLog.setUserAgent(userAgentStr);
                loginLog.setUser(baseUser);
                loginLog.setIpaddr(baseUser.getHost());
                loginLog.setBrowser(browser);
                loginLog.setOs(os);
                loginLog.setMsg("登录成功");
                loginLog.setLocation(BaiduAiUtils.getAddressByIP(baseUser.getHost()));
                loginLog.setLoginTime(new Date());
                loginLog.setType(isLogin?"0":"1");
                // 插入数据
                SpringUtils.getBean(LoginLogDao.class).insertLog(loginLog);
            }
        };
    }


    /**
     * 推送系统消息
     * @return
     */
    public static TimerTask pushSysMsg(Msg msg){
        return new TimerTask() {
            @Override
            public void run() {
                List<MsgUser> msgUserList = ListUtils.newArrayList();
                List<String> receiveCodeList = ListUtils.newArrayList();

                if (MsgReceiverType.CLASSES.value().equals(msg.getReceiverType())){
                    receiveCodeList =  studentClassesService.findStudentIds(msg.getReceiverKey());
                    msg.setReceiveCodes(StringUtils.join(receiveCodeList,","));
                }else if (MsgReceiverType.PERSON.value().equals(msg.getReceiverType())){
                    receiveCodeList.add(msg.getReceiverKey());
                    msg.setReceiveCodes(msg.getReceiverKey());
                }

                if (msg.isSave()){

                    msgService.save(msg);

                    receiveCodeList.forEach(receiveCode->{
                        MsgUser msgUser = new MsgUser();
                        msgUser.setMsg(msg);
                        msgUser.setUserCode(receiveCode);
                        msgUser.setState(MsgUserState.UN_READ.value());
                        msgUserList.add(msgUser);
                    });
                    msgUserService.insertBatch(msgUserList);
                }

                MsgVo msgVo = new MsgVo();
                BeanUtils.copyProperties(msg,msgVo);
                // 通过事件监听器，发送系统消息
                SpringUtils.getApplicationContext()
                        .publishEvent(new MsgEvent(msg,receiveCodeList, JsonMapper.toJson(msgVo)));
            }
        };
    }

    /**
     * 踢掉用户
     * @return
     */
    public static TimerTask pushTickOutMsg(List<String> receiverIds){
        return new TimerTask() {
            @Override
            public void run() {
                // 通过事件监听器，发送系统消息
                SpringUtils.getApplicationContext()
                        .publishEvent(new SystemEvent(this,receiverIds,null));
            }
        };
    }
}
