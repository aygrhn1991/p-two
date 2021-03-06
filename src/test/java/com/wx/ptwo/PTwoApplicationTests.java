package com.wx.ptwo;

import com.wx.ptwo.wxsec.WXBizMsgCrypt;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PTwoApplicationTests {

    @Test
    void contextLoads() {
//        String timestamp = "1587095098";
//        String nonce = "1878241826";
//        String encrypt_type = "aes";
//        String msg_signature = "d72c0798decae9a2ab47437d7b34cc11c54f6880";
//        String text = null;
//        try {
//            text = "<xml><AppId><![CDATA[wxf5d8d7427ebabd90]]></AppId><Encrypt><![CDATA[TIrOwA8aG4Pz8R7+4KR04yzEY4y9UrkrS0bA/yEG2NPKhdP2nulifBjZqJg6KdFy5GcPJAYjEEq50pbO8Xq9hLLCiR1pvUAZqXul9OVpTAvbJNqQq5GtiPN4P/SBD4qi3kmo0LbcU5Jg9Wx45lq9xPybnMYCffOzFgAKocnthClWrYLmN9kDIME37EfdZ/lFOHMWR0PIO+W4vpyRuWtTlftAaGyJ3+9Fh5U7BIT9pGg6Gc7YErJDz/W4+W29oHi8yWwbo2AFXXGw2SkqMDT0x0wgZTqaVCWWZ7AHWnmVeEDVkqKquIIS+3y0KQ0MkaLLibtLcUMSxPP/mM6XaDpP0LElcfpKdSI4zmVXS+yNjMmfgjUcuYPI3lF5YMDhL57Xqa2xl+Bb2W4/kqqUoWvl57O6OGdTXcDPciKc2gIUaFPYMjVdmBP57iHvuQwmIUUNwQZcFuQsnXfIlUk9yW2NoA==]]></Encrypt></xml>";
//            String appId = "wxf5d8d7427ebabd90";
//            String encodingAesKey = "ef8dgq54regx9saol27z1bszubanp6knao09pg31h7l";
//            String token = "hello";
//            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
//            String result2 = pc.decryptMsg(msg_signature, timestamp, nonce, text);
//            System.out.println("解密后明文: " + result2);
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

        String appid = "wxf5d8d7427ebabd90";
        String ticket = "ticket@@@xI4zg2BRy_O3dvAVOC-AIQc4nTdx2wQqtHAZMDeuYC4AcUfF-hLbrI4fMmqy4_BwVVurEWUaR44K8lhqUSV_hw";
//        //token
//        Map map=new HashMap<>();
//        map.put("component_appid",appid);
//        map.put("component_appsecret","4770f604fa35e23007e253fa3dd8eaba");
//        map.put("component_verify_ticket",ticket);
//        RestTemplate restTemplate = new RestTemplate();
//        String result= restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/component/api_component_token",map,String.class);
        String token = "32_MjQqV8-o9eWJUgjUtp9Dawxj2MYt09WKrJqpnEb-w2H8MvVaeF9lBEzA1Tfp1qyBsajY7dQ7cpYHhN7YS5rINmvQm0BS_pSXavpoF4TmMrjFP6fBsNNdZ8bbYFF2a2zcv6_r4SAu2b-oQtRWEOYcAHAUEO";
        Map map = new HashMap<>();
        map.put("component_appid", appid);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=" + token, map, String.class);
        System.out.println(result);
    }

    String encodingAesKey = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG";
    String token = "pamtest";
    String timestamp = "1409304348";
    String nonce = "xxxxxx";
    String appId = "wxb11529c136998cb6";
    String replyMsg = "我是中文abcd123";
    String xmlFormat = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
    String afterAesEncrypt = "jn1L23DB+6ELqJ+6bruv21Y6MD7KeIfP82D6gU39rmkgczbWwt5+3bnyg5K55bgVtVzd832WzZGMhkP72vVOfg==";
    String randomStr = "aaaabbbbccccdddd";

    String replyMsg2 = "<xml><ToUserName><![CDATA[oia2Tj我是中文jewbmiOUlr6X-1crbLOvLw]]></ToUserName><FromUserName><![CDATA[gh_7f083739789a]]></FromUserName><CreateTime>1407743423</CreateTime><MsgType><![CDATA[video]]></MsgType><Video><MediaId><![CDATA[eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0]]></MediaId><Title><![CDATA[testCallBackReplyVideo]]></Title><Description><![CDATA[testCallBackReplyVideo]]></Description></Video></xml>";
    String afterAesEncrypt2 = "jn1L23DB+6ELqJ+6bruv23M2GmYfkv0xBh2h+XTBOKVKcgDFHle6gqcZ1cZrk3e1qjPQ1F4RsLWzQRG9udbKWesxlkupqcEcW7ZQweImX9+wLMa0GaUzpkycA8+IamDBxn5loLgZpnS7fVAbExOkK5DYHBmv5tptA9tklE/fTIILHR8HLXa5nQvFb3tYPKAlHF3rtTeayNf0QuM+UW/wM9enGIDIJHF7CLHiDNAYxr+r+OrJCmPQyTy8cVWlu9iSvOHPT/77bZqJucQHQ04sq7KZI27OcqpQNSto2OdHCoTccjggX5Z9Mma0nMJBU+jLKJ38YB1fBIz+vBzsYjrTmFQ44YfeEuZ+xRTQwr92vhA9OxchWVINGC50qE/6lmkwWTwGX9wtQpsJKhP+oS7rvTY8+VdzETdfakjkwQ5/Xka042OlUb1/slTwo4RscuQ+RdxSGvDahxAJ6+EAjLt9d8igHngxIbf6YyqqROxuxqIeIch3CssH/LqRs+iAcILvApYZckqmA7FNERspKA5f8GoJ9sv8xmGvZ9Yrf57cExWtnX8aCMMaBropU/1k+hKP5LVdzbWCG0hGwx/dQudYR/eXp3P0XxjlFiy+9DMlaFExWUZQDajPkdPrEeOwofJb";


    //@Test
    public void testNormal() {
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
            String afterEncrpt = pc.encryptMsg(replyMsg, timestamp, nonce);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(afterEncrpt);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("Encrypt");
            NodeList nodelist2 = root.getElementsByTagName("MsgSignature");

            String encrypt = nodelist1.item(0).getTextContent();
            String msgSignature = nodelist2.item(0).getTextContent();
            String fromXML = String.format(xmlFormat, encrypt);

            // 第三方收到公众号平台发送的消息
            String afterDecrpt = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
            assertEquals(replyMsg, afterDecrpt);
        } catch (Exception e) {
            System.out.println("正常流程，怎么就抛出异常了？？？？？？");
        }
    }

//    @Test
//    public void testAesEncrypt() {
//        try {
//            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
//            assertEquals(afterAesEncrypt, pc.encrypt(randomStr, replyMsg));
//        } catch (AesException e) {
//            e.printStackTrace();
//            fail("no异常");
//        }
//    }
//
//    @Test
//    public void testAesEncrypt2() {
//        try {
//            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
//            assertEquals(afterAesEncrypt2, pc.encrypt(randomStr, replyMsg2));
//
//        } catch (AesException e) {
//            e.printStackTrace();
//            fail("no异常");
//        }
//    }
//
//    @Test
//    public void testIllegalAesKey() {
//        try {
//            new WXBizMsgCrypt(token, "abcde", appId);
//        } catch (AesException e) {
//            assertEquals(AesException.IllegalAesKey, e.getCode());
//            return;
//        }
//        fail("错误流程不抛出异常？？？");
//    }
//
//    @Test
//    public void testValidateSignatureError() throws ParserConfigurationException, SAXException,
//            IOException {
//        try {
//            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
//            String afterEncrpt = pc.encryptMsg(replyMsg, timestamp, nonce);
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            StringReader sr = new StringReader(afterEncrpt);
//            InputSource is = new InputSource(sr);
//            Document document = db.parse(is);
//
//            Element root = document.getDocumentElement();
//            NodeList nodelist1 = root.getElementsByTagName("Encrypt");
//
//            String encrypt = nodelist1.item(0).getTextContent();
//            String fromXML = String.format(xmlFormat, encrypt);
//            pc.decryptMsg("12345", timestamp, nonce, fromXML); // 这里签名错误
//        } catch (AesException e) {
//            assertEquals(AesException.ValidateSignatureError, e.getCode());
//            return;
//        }
//        fail("错误流程不抛出异常？？？");
//    }
//
//    @Test
//    public void testVerifyUrl() throws AesException {
//        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt("QDG6eK",
//                "jWmYm7qr5nMoAUwZRjGtBxmz3KA1tkAj3ykkR6q2B2C", "wx5823bf96d3bd56c7");
//        String verifyMsgSig = "5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3";
//        String timeStamp = "1409659589";
//        String nonce = "263014780";
//        String echoStr = "P9nAzCzyDtyTWESHep1vC5X9xho/qYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp+4RPcs8TgAE7OaBO+FZXvnaqQ==";
//        wxcpt.verifyUrl(verifyMsgSig, timeStamp, nonce, echoStr);
//        // 只要不抛出异常就好
//    }

}
