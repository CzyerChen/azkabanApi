/**
 * Author:   claire
 * Date:    2020-03-13 - 09:38
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-03-13 - 09:38          V1.3.4
 */
package com.azkabanconn.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.azkabanconn.config.AzkabanConfig;
import com.azkabanconn.constant.AzkabanRequestConstant;
import com.azkabanconn.entity.dto.OkResponseDTO;
import com.azkabanconn.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-03-13 - 09:38
 */
@RestController
@RequestMapping("azkaban")
public class AzkabanController {
    @Autowired
    private AzkabanConfig azkabanConfig;

    private static ConcurrentHashMap<String, String> cookieListMap = new ConcurrentHashMap<>();

    /**
     * 功能描述: <br/>
     * 〈认证〉
     *
     * @return {@link java.lang.String}
     * @author claire
     * @date 2020-03-13 - 10:28
     */
    @RequestMapping("auth")
    public String authAzkaban() throws Exception {
        Map<String,String> paramMap = new HashMap<>(8);
        paramMap.put("action","login");
        paramMap.put("username",azkabanConfig.getUsername());
        paramMap.put("password",azkabanConfig.getPassword());
        String requestUrl = "https://"+azkabanConfig.getHost()+":"+azkabanConfig.getPort();
        OkResponseDTO okResponseDTO = HttpClientUtils.postFormParams(requestUrl, paramMap, null);
        JSONObject obj = JSON.parseObject(okResponseDTO.getBodyString());
        if(obj.containsKey("status")){
            String status = String.valueOf(obj.get("status"));
            if("success".equals(status)){
                if(obj.containsKey("session.id")){
                    String session = String.valueOf(obj.get("session.id"));
                    cookieListMap.put(requestUrl,session);
                }
            }
            return status;
        }
       return null;
    }

    /**
     * 功能描述: <br/>
     * 〈获取项目flows〉
     *
     * @param project
     * @return {@link java.lang.String}
     * @author claire
     * @date 2020-03-13 - 10:28
     */
    @RequestMapping("flows")
    public String findFlowsByProject(@RequestParam String project) throws Exception {
        Map<String,String> queryMap = new HashMap<>(16);
        queryMap.put("ajax","fetchprojectflows");
        queryMap.put("project",project);
        String requestUrl = "https://"+azkabanConfig.getHost()+":"+azkabanConfig.getPort();
        if(cookieListMap.containsKey(requestUrl)){
            queryMap.put("session.id",cookieListMap.get(requestUrl));
        }
        return HttpClientUtils.get(requestUrl+ AzkabanRequestConstant.AZKABAN_MANAGER_PATH, queryMap);
    }

    /**
     * 功能描述: <br/>
     * 〈执行一个流程〉
     *
     * @param project
     * @param flowId
     * @return {@link java.lang.String}
     * @author claire
     * @date 2020-03-13 - 10:29
     */
    @RequestMapping("executeFlow")
    public String executeFlow(@RequestParam String project,@RequestParam String flowId) throws Exception {
        Map<String,String> executeMap = new HashMap<>(16);
        executeMap.put("ajax","executeFlow");
        executeMap.put("project",project);
        executeMap.put("flow",flowId);
        String requestUrl = "https://"+azkabanConfig.getHost()+":"+azkabanConfig.getPort();
        if(cookieListMap.containsKey(requestUrl)){
            executeMap.put("session.id",cookieListMap.get(requestUrl));
        }
        return HttpClientUtils.get(requestUrl+AzkabanRequestConstant.AZKABAN_EXECUTOR_PATH, executeMap);
    }

    /**
     * 功能描述: <br/>
     * 〈获取flows执行列表〉
     *
     * @param project
     * @param flowId
     * @param start
     * @param length
     * @return {@link java.lang.String}
     * @author claire
     * @date 2020-03-13 - 10:29
     */
    @RequestMapping("list")
    public String getExecuteResultList(@RequestParam String project,@RequestParam String flowId,@RequestParam Integer start ,@RequestParam Integer length) throws Exception {
        Map<String,String> resultMap = new HashMap<>(16);
        resultMap.put("ajax","fetchFlowExecutions");
        resultMap.put("project",project);
        resultMap.put("flow",flowId);
        resultMap.put("start",String.valueOf(start));
        resultMap.put("length",String.valueOf(length));
        String requestUrl = "https://"+azkabanConfig.getHost()+":"+azkabanConfig.getPort();
        if(cookieListMap.containsKey(requestUrl)){
            resultMap.put("session.id",cookieListMap.get(requestUrl));
        }
        return  HttpClientUtils.get(requestUrl+AzkabanRequestConstant.AZKABAN_MANAGER_PATH, resultMap);
    }

    /**
     * 功能描述: <br/>
     * 〈获取执行结果〉
     *
     * @param execId
     * @return {@link java.lang.String}
     * @since 1.3.4
     * @author claire
     * @date 2020-03-13 - 10:30
     */
    @RequestMapping("check")
    public String checkExecuteResult(@RequestParam String execId) throws Exception {
        Map<String,String> resultMap = new HashMap<>(16);
        resultMap.put("ajax","fetchexecflow");
        resultMap.put("execid",execId);
        String requestUrl = "https://"+azkabanConfig.getHost()+":"+azkabanConfig.getPort();
        if(cookieListMap.containsKey(requestUrl)){
            resultMap.put("session.id",cookieListMap.get(requestUrl));
        }
        return  HttpClientUtils.get(requestUrl+AzkabanRequestConstant.AZKABAN_EXECUTOR_PATH, resultMap);
    }
}
