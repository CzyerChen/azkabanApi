/**
 * Author:   claire
 * Date:    2020-03-12 - 18:26
 * Description: azkaban认证和调用
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-03-12 - 18:26          V1.3.4           azkaban认证和调用
 */
package com.azkabanconn.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.azkabanconn.AzkabanConnTestApplication;
import com.azkabanconn.entity.dto.OkResponseDTO;
import com.azkabanconn.utils.HttpClientUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能简述 <br/> 
 * 〈azkaban认证和调用〉
 *
 * @author claire
 * @date 2020-03-12 - 18:26
 * @since 1.3.4
 */
@SpringBootTest(classes = AzkabanConnTestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AzkabanTaskTest {

    private static ConcurrentHashMap<String, String> cookieListMap = new ConcurrentHashMap<>();


    @Test
    public void testAzkabanAuth() throws Exception {
        Map<String,String> paramMap = new HashMap<>(8);
        paramMap.put("action","login");
        paramMap.put("username","azkaban");
        paramMap.put("password","azkaban");
        OkResponseDTO okResponseDTO = HttpClientUtils.postFormParams("https://127.0.0.1:8443", paramMap, null);
        System.out.println(okResponseDTO.toString());
        JSONObject obj = JSON.parseObject(okResponseDTO.getBodyString());
        if(obj.containsKey("session.id")){
            String session = String.valueOf(obj.get("session.id"));
            cookieListMap.put("https://127.0.0.1:8443",session);
        }


        Map<String,String> queryMap = new HashMap<>(16);
        queryMap.put("ajax","fetchprojectflows");
        queryMap.put("project","HelloTest");
        if(cookieListMap.containsKey("https://127.0.0.1:8443")){
            queryMap.put("session.id",cookieListMap.get("https://127.0.0.1:8443"));
        }
        String result = HttpClientUtils.get("https://127.0.0.1:8443/manager", queryMap);
        System.out.println(result);


        Map<String,String> executeMap = new HashMap<>(16);
        executeMap.put("ajax","executeFlow");
        executeMap.put("project","HelloTest");
        executeMap.put("flow","hello");
        if(cookieListMap.containsKey("https://127.0.0.1:8443")){
            executeMap.put("session.id",cookieListMap.get("https://127.0.0.1:8443"));
        }
        String executeResult = HttpClientUtils.get("https://127.0.0.1:8443/executor", executeMap);
        System.out.println(executeResult);


        Map<String,String> resultMap = new HashMap<>(16);
        resultMap.put("ajax","fetchFlowExecutions");
        resultMap.put("project","HelloTest");
        resultMap.put("flow","hello");
        resultMap.put("start","2");
        resultMap.put("length","1");
        if(cookieListMap.containsKey("https://127.0.0.1:8443")){
            resultMap.put("session.id",cookieListMap.get("https://127.0.0.1:8443"));
        }
        String executeResultList = HttpClientUtils.get("https://127.0.0.1:8443/manager", resultMap);
        System.out.println(executeResultList);

    }
}
