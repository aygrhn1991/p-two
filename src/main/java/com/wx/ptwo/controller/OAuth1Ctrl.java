package com.wx.ptwo.controller;

import com.google.gson.Gson;
import com.wx.ptwo.models.OAuthUserAccessToken;
import com.wx.ptwo.models.OAuthUserInfo;
import com.wx.ptwo.util.HttpUtil;
import com.wx.ptwo.util.WxUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/oauth1")
public class OAuth1Ctrl {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("${wx1.token}")
    private String wxToken;

    @Value("${wx1.appid}")
    private String wxAppId;

    @Value("${wx1.appsecret}")
    private String wxAppSecret;

    @Value("${wx1.id}")
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
            return null;
        }
    }

    @RequestMapping(value = "/requestcode/{unionid}", method = RequestMethod.GET)
    public String auth(HttpServletRequest request, @PathVariable("unionid") String unionid) throws UnsupportedEncodingException {
        String baseUrl = HttpUtil.getBaseUrl(request);
        String encodeUrl = URLEncoder.encode(baseUrl + "/oauth1/getcode", "utf-8");
        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect", this.wxAppId, encodeUrl, "snsapi_userinfo", unionid);
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
        String sql = "select * from t_user where w_openid1=?";
        List<Map<String, Object>> userList = this.jdbcTemplate.queryForList(sql, new Object[]{oAuthUserAccessToken.openid});
        if (userList.size() == 1) {
            String unionid = userList.get(0).get("w_unionid").toString();
            if (!state.equals(unionid)) {
                sql = "select * from t_event where member=?";
                List<Map<String, Object>> eventList = this.jdbcTemplate.queryForList(sql, new Object[]{unionid});
                //尚未扫过别人的码
                if (eventList.size() == 0) {
                    sql = "insert into t_event(organizer,member,subscribe) values(?,?,0)";
                    int count = this.jdbcTemplate.update(sql, new Object[]{state, unionid});
                } else {
                    int subscribe = Integer.parseInt(eventList.get(0).get("subscribe").toString());
                    //已经扫码，但还未关注
                    if (subscribe == 0) {
                        sql = "delete from t_event where member=?";
                        int count = this.jdbcTemplate.update(sql, new Object[]{unionid});
                        sql = "insert into t_event(organizer,member,subscribe) values(?,?,0)";
                        count = this.jdbcTemplate.update(sql, new Object[]{state, unionid});
                    }
                    //已经扫码并关注
                    if (subscribe == 1) {
                        //啥也不用干
                    }
                    //扫码关注后，又取消关注
                    if (subscribe == 2) {
                        //啥也不用干
                    }
                }
            }
            return "redirect:/home/index";
        } else {
            url = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN", oAuthUserAccessToken.access_token, oAuthUserAccessToken.openid);
            rsp = restTemplate.getForObject(url, String.class);
            logger.info("微信授权，accesstoken换取userinfo（授权专用accesstoken）：" + rsp);
            OAuthUserInfo oAuthUserInfo = gson.fromJson(rsp, OAuthUserInfo.class);
            sql = "insert into t_user(w_openid1,w_nickname,w_sex,w_province,w_city,w_country,w_headimgurl,w_unionid,t_time) values (?,?,?,?,?,?,?,?,?)";
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
            return "redirect:/oauth1/requestcode/" + state;
        }
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
