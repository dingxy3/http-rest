package client;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import util.RestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * RestClient
 * rest请求客户端
 * @Author: dingxy3
 * @Description:
 * @Date: Created in  2018/5/28
 **/
@Service("restClient")
public class RestClient {

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    /**
     * 连接超时时间
     */
    public static final int CONNECT_TIME_OUT = 40000;

    /**
     * 读取超时时间
     */
    public static final int READ_TIME_OUT = 40000;


    private RestTemplate template;

    /**
     * 初始化restTemplate
     * @param builder
     */
    public RestClient(RestTemplateBuilder builder)
    {
        template = builder.setConnectTimeout(CONNECT_TIME_OUT)
                .setReadTimeout(READ_TIME_OUT).build();

    }


    /**
     * request在请求头的 rest请求
     * @param urlAddress
     * @param responseType
     * @param requestMap
     * @param <T>
     * @return
     */
    public <T> T postForObjet(String urlAddress, Class<T> responseType, Object requestMap)
    {

        logger.warn("请求路径：{}", urlAddress);
        logger.warn("请求参数：{}", requestMap.toString());

        ResponseEntity<T> responseEntity =
                template.exchange(urlAddress, HttpMethod.POST, RestUtils.wrapHttpEntity(requestMap), responseType);

        logger.warn("返回结果：{}", responseEntity.toString());
        checkResponse(responseEntity);
        return responseEntity.getBody();
    }

    /**
     * 参数放url的rest请求
     * @param url
     * @param responseType
     * @param requestMap
     * @param key
     * @param <T>
     * @return
     */
    public <T> T postForObjectByUrl(String url, Class<T> responseType, Map<String, Object> requestMap, String key)
    {

        logger.info("请求路径：{}", url);
        logger.warn("请求参数：{}", JSON.toJSONString(requestMap));

        //url参数
        Map<String, Object> urlVauriable = new HashMap<String, Object>();

        urlVauriable.put(key, requestMap);

        ResponseEntity<T> responseEntity =
                template.exchange(url, HttpMethod.POST, null, responseType, urlVauriable);

        logger.warn("返回结果：{}", responseEntity.toString());
        checkResponse(responseEntity);
        return responseEntity.getBody();
    }


    /**
     * 检查respon
     *
     * @param responseEntity
     */
    private void checkResponse(ResponseEntity<?> responseEntity)
    {
        if (hasError(responseEntity.getStatusCode()))
        {
            logger.debug("rest调用出错: " + responseEntity.toString());
        }
    }

    /**
     * 调用是否出错
     *
     * @param statusCode
     * @return
     */
    private static boolean hasError(HttpStatus statusCode)
    {
        return ((statusCode.series() == HttpStatus.Series.CLIENT_ERROR) ||
                (statusCode.series() == HttpStatus.Series.SERVER_ERROR));
    }


}
