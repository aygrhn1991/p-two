package com.wx.ptwo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class PTwoApplicationTests {

    @Test
    void contextLoads() {
        RestTemplate restTemplate = new RestTemplate();
        String result= restTemplate.getForObject("http://www.fenglingtime.com/api/list.json",String.class);
        System.out.println(result);
    }

}
