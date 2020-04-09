package com.wx.ptwo.controller;

import com.google.gson.Gson;
import com.wx.ptwo.models.AccessToken;
import com.wx.ptwo.models.OAuthUserAccessToken;
import com.wx.ptwo.models.OAuthUserInfo;
import com.wx.ptwo.models.UserInfo;
import com.wx.ptwo.util.HttpUtil;
import com.wx.ptwo.util.WxUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/oauth2")
public class OAuth2Ctrl {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("${wx2.token}")
    private String wxToken;

    @Value("${wx2.appid}")
    private String wxAppId;

    @Value("${wx2.appsecret}")
    private String wxAppSecret;

    @Value("${wx2.id}")
    private String wxId;

    private static final Logger logger = LogManager.getLogger(OAuth1Ctrl.class.getName());

    @RequestMapping("/config")
    @ResponseBody
    public String config(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().toLowerCase().equals("get")) {
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String signature = request.getParameter("signature");
            String echostr = request.getParameter("echostr");
            return WxUtil.checkConfig(wxToken, timestamp, nonce, signature) ? echostr : null;
        } else {
            try {
                SAXReader reader = new SAXReader();
                Document document = reader.read(request.getInputStream());
                Element root = document.getRootElement();
                logger.info("自动回复-接收：" + document.asXML());
                String msgType = root.elementText("MsgType");
                String event = root.elementText("Event");
                String openId = root.elementText("FromUserName");
                if (msgType.equals("event") && event.equals("subscribe")) {
                    String accessToken = WxUtil.getAccesstToken(this.wxAppId, this.wxAppSecret);
                    String url = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN", accessToken, openId);
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
                    String rsp = restTemplate.getForObject(url, String.class);
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(rsp, UserInfo.class);

                    String sql = "update t_user set w_openid2=? where w_unionid=?";
                    int count = this.jdbcTemplate.update(sql, new Object[]{userInfo.openid, userInfo.unionid});
                    sql = "update t_event set subscribe=1 where member=?";
                    count = this.jdbcTemplate.update(sql, new Object[]{userInfo.unionid});

                    Map r1 = new HashMap();
                    Map r11 = new HashMap();
                    r11.put("content", "http://wx.fenglingtime.com/oauth1/requestcode/" + userInfo.unionid);
                    r1.put("touser", userInfo.openid);
                    r1.put("msgtype", "text");
                    r1.put("text", r11);
                    rsp = restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken, r1, String.class);
                    System.out.println("客服消息返回1");

                    sql = "select count(*) from t_event where organizer=? and subscribe=1";
                    count = this.jdbcTemplate.queryForObject(sql, new Object[]{userInfo.unionid}, Integer.class);

                    Map r2 = new HashMap();
                    Map r21 = new HashMap();
                    r21.put("content", "当前助力用户：" + count + "人");
                    r2.put("touser", userInfo.openid);
                    r2.put("msgtype", "text");
                    r2.put("text", r21);
                    rsp = restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken, r2, String.class);
                    System.out.println("客服消息返回2");


                    sql = "select * from t_event where member=?";
                    List<Map<String, Object>> userList = this.jdbcTemplate.queryForList(sql, new Object[]{userInfo.unionid});

                    sql = "select * from t_user where w_unionid=?";
                    List<Map<String, Object>> userList2 = this.jdbcTemplate.queryForList(sql, new Object[]{userList.get(0).get("organizer")});


                    Map r3 = new HashMap();
                    Map r31 = new HashMap();
                    r31.put("content", userInfo.nickname + ":为你助力");
                    r3.put("touser", userList2.get(0).get("w_openid2"));
                    r3.put("msgtype", "text");
                    r3.put("text", r31);
                    rsp = restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken, r3, String.class);
                    System.out.println("客服消息返回2");

//                    Document document2 = DocumentHelper.createDocument();
//                    Element root2 = document2.addElement("xml");
//                    Element toUserName = root2.addElement("ToUserName").addText(openId);
//                    Element fromUserName = root2.addElement("FromUserName").addText(wxId);
//                    Element createTime = root2.addElement("CreateTime").addText(String.valueOf(new Date().getTime()));
//                    Element msgType2 = root2.addElement("MsgType").addText("text");
//                    Element Content = root2.addElement("Content").addText("http://wx.fenglingtime.com/oauth1/requestcode/" + userInfo.unionid);
//                    String responseXml = document2.asXML();
//                    logger.info("自动回复-回复：" + responseXml);
//                    PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
//                    out.print(responseXml);
//                    out.flush();
////                    out.close();
//                    Document document2_1 = DocumentHelper.createDocument();
//                    Element root2_1 = document2_1.addElement("xml");
//                    Element toUserName_1 = root2_1.addElement("ToUserName").addText(openId);
//                    Element fromUserName_1 = root2_1.addElement("FromUserName").addText(wxId);
//                    Element createTime_1 = root2_1.addElement("CreateTime").addText(String.valueOf(new Date().getTime()));
//                    Element msgType2_1 = root2_1.addElement("MsgType").addText("text");
//                    Element Content_1 = root2_1.addElement("Content").addText("http://wx.fenglingtime.com/oauth1/requestcode/" + userInfo.unionid);
//                    responseXml = document2_1.asXML();
//                    logger.info("自动回复-回复：" + responseXml);
//                    out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
//                    out.print(responseXml);
//                    out.flush();
//                    out.close();
                } else if (msgType.equals("event") && event.equals("unsubscribe")) {
                    String sql = "update t_event left join t_user on t_user.w_unionid=t_event.member set subscribe=2 where t_user.w_openid2=?";
                    int count = this.jdbcTemplate.update(sql, new Object[]{openId});
                }
            } catch (Exception e) {
                logger.error("自动回复异常：" + e.getMessage());
            }
            return null;
        }
    }

    @RequestMapping(value = "/requestcode", method = RequestMethod.GET)
    public String auth(HttpServletRequest request) throws UnsupportedEncodingException {
        String baseUrl = HttpUtil.getBaseUrl(request);
        String encodeUrl = URLEncoder.encode(baseUrl + "/oauth2/getcode", "utf-8");
        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect", this.wxAppId, encodeUrl, "snsapi_userinfo", "ptwo");
        return "redirect:" + url;
    }

    @RequestMapping(value = "/getcode", method = RequestMethod.GET)
    public String getcode(HttpServletRequest request, HttpServletResponse response) {
        String state = request.getParameter("state");
        String code = request.getParameter("code");
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", this.wxAppId, this.wxAppSecret, code);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String rsp = restTemplate.getForObject(url, String.class);
        logger.info("微信授权，code换取accesstoken：" + rsp);
        Gson gson = new Gson();
        OAuthUserAccessToken oAuthUserAccessToken = gson.fromJson(rsp, OAuthUserAccessToken.class);
        String sql = "select * from t_user where w_openid=?";
        List<Map<String, Object>> userList = this.jdbcTemplate.queryForList(sql, new Object[]{oAuthUserAccessToken.openid});
        if (userList.size() == 1) {
            System.out.println("已经存在");
//            //统计登陆次数
//            try {
//                long current = System.currentTimeMillis();
//                long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
//                sql = "select count(*) from t_scan where t_time=?";
//                int count = this.jdbcTemplate.queryForObject(sql, Integer.class, new Object[]{zero});
//                if (count == 0) {
//                    sql = "insert into t_scan(t_time,t_scan) values(" + zero + ",1)";
//                } else {
//                    sql = "update t_scan set t_scan=t_scan+1 where t_time=" + zero;
//                }
//                count = this.jdbcTemplate.update(sql);
//            } catch (Exception e) {
//                logger.error("统计登陆次数异常：" + e.getMessage());
//            }
//            //登陆
//            Cookie cookie = new Cookie("userid", userList.get(0).get("t_id").toString());
//            cookie.setDomain(request.getServerName());
//            cookie.setPath("/");
//            response.addCookie(cookie);
//            return "redirect:/home/index";
        } else {
            url = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN", oAuthUserAccessToken.access_token, oAuthUserAccessToken.openid);
            rsp = restTemplate.getForObject(url, String.class);
            logger.info("微信授权，accesstoken换取userinfo（授权专用accesstoken）：" + rsp);
            OAuthUserInfo oAuthUserInfo = gson.fromJson(rsp, OAuthUserInfo.class);
            sql = "insert into t_user(w_openid,w_nickname,w_sex,w_province,w_city,w_country,w_headimgurl,w_unionid,t_time) values (?,?,?,?,?,?,?,?,?)";
            int count = this.jdbcTemplate.update(sql, new Object[]{oAuthUserInfo.openid,
                    oAuthUserInfo.nickname,
                    oAuthUserInfo.sex,
                    oAuthUserInfo.province,
                    oAuthUserInfo.city,
                    oAuthUserInfo.country,
                    oAuthUserInfo.headimgurl,
                    oAuthUserInfo.unionid,
                    new Date().getTime()});
            System.out.println("信息存储完成" + count);
            //return "redirect:/oauth/requestcode";
        }
        return null;
    }

//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public String login(HttpServletRequest request, HttpServletResponse response) {
//        Cookie cookie = new Cookie("userid", "14");
//        cookie.setDomain(request.getServerName());
//        cookie.setPath("/");
//        response.addCookie(cookie);
//        return "redirect:/home/index";
//    }
//
//    @RequestMapping("/jssdkconfig")
//    @ResponseBody
//    public Map<String, Object> jssdkconfig(HttpServletRequest request) {
//        long timestamp = new Date().getTime();
//        String nonceStr = global.wxToken;
//        String accesstoken = WxUtil.getAccesstToken(global.wxAppid, global.wxAppsecret);
//        String jsapiticket = WxUtil.getJsapiTicket(accesstoken);
//        String url = request.getParameter("url");
//        String signature = WxUtil.getJsapiSignature(jsapiticket, timestamp, nonceStr, url);
//        Map<String, Object> map = new HashMap<>();
//        map.put("appId", global.wxAppid);
//        map.put("timestamp", timestamp);
//        map.put("nonceStr", nonceStr);
//        map.put("signature", signature);
//        return map;
//    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirect() {
        String url = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzI3ODQ4MzIwNg==&scene=124#wechat_redirect";
        return "redirect:" + url;
    }

}
