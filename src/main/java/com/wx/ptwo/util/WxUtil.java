package com.wx.ptwo.util;

import com.google.gson.Gson;
import com.wx.ptwo.models.AccessToken;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class WxUtil {

    public static boolean checkConfig(String token, String timestamp, String noncestr, String signature) {
        String[] strArray = new String[]{token, timestamp, noncestr};
        Arrays.sort(strArray);
        String strResult = getSha1(strArray[0] + strArray[1] + strArray[2]);
        return strResult.equals(signature);
    }

    //    public static String getJsapiSignature(String jsapi_ticket, long timestamp, String noncestr, String url) {
//        String n = "noncestr=" + noncestr;
//        String j = "jsapi_ticket=" + jsapi_ticket;
//        String t = "timestamp=" + timestamp;
//        String u = "url=" + url;
//        String[] strArray = new String[]{n, j, t, u};
//        Arrays.sort(strArray);
//        String str = String.join("&", strArray);
//        return getSha1(str);
//    }
//
    public static String getAccesstToken(String appid, String appsecret) {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appid, appsecret);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String response = restTemplate.getForObject(url, String.class);
        Gson gson = new Gson();
        AccessToken accessToken = gson.fromJson(response, AccessToken.class);
        return accessToken.access_token;
    }

    //
//    public static String getJsapiTicket(String accesstoken) {
//        String url = String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi", accesstoken);
//        String response = HttpUtil.Get(url);
//        Gson gson = new Gson();
//        JsapiTicket ticket = gson.fromJson(response, JsapiTicket.class);
//        return ticket.ticket;
//    }
//
//    public static BaseCallback setMenu(String menujson, String accesstoken) {
//        String url = String.format("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s", accesstoken);
//        String response = HttpUtil.Post(url, menujson);
//        Gson gson = new Gson();
//        BaseCallback result = gson.fromJson(response, BaseCallback.class);
//        return result;
//    }
//
    private static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
