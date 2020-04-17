package com.wx.ptwo.controller;

import com.wx.ptwo.util.WxUtil;
import com.wx.ptwo.wxsec.WXBizMsgCrypt;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/main")
public class MainCtrl {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/event")
    @ResponseBody
    public String event(HttpServletRequest request, HttpServletResponse response) {
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String encrypt_type = request.getParameter("encrypt_type");
        String msg_signature = request.getParameter("msg_signature");
        System.out.println("timestamp"+timestamp);
        System.out.println("nonce"+nonce);
        System.out.println("encrypt_type"+encrypt_type);
        System.out.println("msg_signature"+msg_signature);
        String text = null;
        try {
            text = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8.name());

            System.out.println(text);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式
            String timeText = format.format(new Date());
            String sql = "INSERT INTO ticket(createtime,component_verify_ticket) VALUES(?,?)";
            int count = this.jdbcTemplate.update(sql, new Object[]{timeText, text});
            String appId = "wxf5d8d7427ebabd90";
            String encodingAesKey = "ef8dgq54regx9saol27z1bszubanp6knao09pg31h7l";
            String token = "hello";
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
            String result2 = pc.decryptMsg(msg_signature, timestamp, nonce, text);
            System.out.println("解密后明文: " + result2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
//        String AppId = request.getParameter("AppId");
//        String CreateTime = request.getParameter("CreateTime");
//        String InfoType = request.getParameter("InfoType");
//        String ComponentVerifyTicket = request.getParameter("ComponentVerifyTicket");

//        if (request.getMethod().toLowerCase().equals("get")) {
//            String timestamp = request.getParameter("timestamp");
//            String nonce = request.getParameter("nonce");
//            String signature = request.getParameter("signature");
//            String echostr = request.getParameter("echostr");
//            return WxUtil.checkConfig(wxToken, timestamp, nonce, signature) ? echostr : null;
//        } else {
//            return null;
//        }
    }
}
