package com.wx.ptwo.util;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {

    //    public static String Get(String url) {
//        try {
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            HttpGet httpGet = new HttpGet(url);
//            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            String result = EntityUtils.toString(httpEntity, "UTF-8");
//            httpResponse.close();
//            return result;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
//
//    public static String Post(String url, List<NameValuePair> params) {
//        try {
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            HttpPost httpPost = new HttpPost(url);
//            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
//            httpPost.setEntity(entity);
//            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            String result = EntityUtils.toString(httpEntity, "UTF-8");
//            httpResponse.close();
//            httpClient.close();
//            return result;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
//
//    public static String Post(String url, String body) {
//        try {
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            HttpPost httpPost = new HttpPost(url);
//            httpPost.addHeader("Content-Type", "application/json");
//            httpPost.setEntity(new StringEntity(body));
//            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            String result = EntityUtils.toString(httpEntity, "UTF-8");
//            httpResponse.close();
//            httpClient.close();
//            return result;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
//
    public static String getBaseUrl(HttpServletRequest request) {
        try {
            return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
