package http;

import client.RestClient;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import util.RestUtils;
import util.UrlProperties;

import java.util.Map;

/**
 * @param
 * @Author: dingxy3
 * @Description:
 * @Date: Created in  2018/7/11
 **/
public class HttpRestUtil {


    @Autowired
    private RestClient client;

    @Autowired
    private UrlProperties disConfig;

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    /**
     * 接入方式 -  map 对应的key
     *               OUT_BODY : 请求报文
     *               SERVICE_PATH 请求能力的名称
     * @param outMap
     * @return
     */
    public Map<String, Object> adapter(Map<String, Object> outMap)
    {

        logger.debug("入参：{}"+ JSON.toJSONString(outMap));
        if (outMap.get("OUT_BODY") == null || outMap.get("SERVICE_PATH") == null)
        {
            throw new RuntimeException("参数不完整");
        }

        Object bodyInfo = outMap.get("OUT_BODY");
        String servicePath = outMap.get("SERVICE_PATH").toString();

        logger.warn("访问外围 servicePath : {}-->"+servicePath+"  bodyInfo:{}--->" +bodyInfo.toString());

        String appId = disConfig.getAppId();

        String appSecret = disConfig.getAppSecret();

        String url = disConfig.getUrl();

        //拼装请求参数
        Map<String, Object> requestMap = RestUtils.setRequestMap(bodyInfo, null, appId, appSecret);

        return client.postForObjet(url + servicePath, Map.class, requestMap);

    }

}
