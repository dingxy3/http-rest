package util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @param
 * @Author: dingxy3
 * @Description:
 * @Date: Created in  2018/7/11
 **/
@Component
public class UrlProperties {
    @Value("${rest.appid}")
    private String appId ;

    @Value("${rest.appsecret}")
    private String appSecret ;

    @Value("${rest.url}")
    private String url ;

    @Value("${rest.appkey}")
    private String aappkey ;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAappkey() {
        return aappkey;
    }

    public void setAappkey(String aappkey) {
        this.aappkey = aappkey;
    }
}
