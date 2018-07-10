package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @param
 * @Author: dingxy3
 * @Description:
 * @Date: Created in  2018/7/10
 **/
public class RestUtils {


    private static Logger logger = LoggerFactory.getLogger(RestUtils.class);

    /**
     * 封装请求头
     * @param request
     * @return
     */
    public static HttpEntity<?> wrapHttpEntity(Object request)
    {

        //请求头封装
        HttpEntity<?> httpEntity = null;
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        if (null != request && MultiValueMap.class.isAssignableFrom(request.getClass()))
        {
            headers.add("Content-Type", "application/x-www-form-urlencoded");

        }

        headers.add("Accept-Encoding", "");
        httpEntity = new HttpEntity<Object>(request, headers);
        return httpEntity;
    }
    /**
     * 设置请求头map
     * @param body
     * @param atta
     * @return
     */
    public static Map<String, Object> setRequestMap(Object body, Object atta, String appId, String appSecret)
    {

        Map<String, Object> map = new HashMap<String, Object>();

        //请求头
        try {
            map.put("UNI_BSS_HEAD", setRequstHead(appId, appSecret));
            //请求body
            map.put("UNI_BSS_BODY", body);
            if(atta !=null){
                //报文附加信息
                map.put("UNI_BSS_ATTACHED", atta);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }
    /**
     * 设置请求头
     *
     * @return
     */
    public static Map<String, Object> setRequstHead(String appId, String appSecret) throws Exception {

        String transId = DateUtils.date2StrByInt(new Date(), "yyyyMMddHHmmssFFF") +
                (int) ((Math.random() * 9 + 1) * 100000);

        String timeStamp = DateUtils.date2StrByInt(new Date(), "yyyy-MM-dd HH:mm:ss FFF");
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("APP_ID", appId);
        content.put("TOKEN", getToken(appId, transId, appSecret, timeStamp));
        content.put("TIMESTAMP", timeStamp);
        content.put("TRANS_ID", transId);

        //RESERVED 数组 作为保留字段
        List<Map<String,Object>> ressrved = new ArrayList<Map<String,Object>>();
        content.put("RESERVED",ressrved);
        return content;
    }
    /**
     * 组装token
     *
     * @param trandId
     * @return
     */
    private static String getToken(String appId, String trandId, String appSecret, String timeStamp) throws Exception {

        SortedMap<String, String> map = new TreeMap<String, String>();

        map.put("APP_ID", appId);
        map.put("TIMESTAMP", timeStamp);
        map.put("TRANS_ID", trandId);

        //跟字母从小到大排序 组装成字符串
        String s = FormatBizQueryParaMap(map, false);
        s = s + appSecret;

        return encode(s);
    }

    /**
     * 与上述方法相同，根据不同的编码方式编码
     *
     * @param paraMap
     * @param urlencode
     * @return
     * @throws
     */
    public static String FormatBizQueryParaMap(Map<String, String> paraMap, boolean urlencode)
            throws Exception
    {
        String buff = "";
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(paraMap.entrySet());

        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()
        {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)
            {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
        for (int i = 0; i < infoIds.size(); i++)
        {
            Map.Entry<String, String> item = infoIds.get(i);
            if (item.getKey() != "")
            {
                String key = item.getKey();
                String val = item.getValue();
                if (urlencode)
                {
                    val = URLEncoder.encode(val, "utf-8");
                }
                buff += key +  val ;
            }
        }
        return buff;
    }
    /**
     * 加密方法.
     *
     * @param input
     *            需要加密的字符串
     * @return 加密后得到的字符串
     */
    public static String encode(String input) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(input.getBytes());
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            // throw new SystemException(ErrorCode.UNKNOW_ERROR, e);
        }
        return byte2hex(digesta);
    }
    /**
     * Byte2hex.
     *
     * @param b
     *           需要加密的byte数组
     * @return 加密后得到的字符串
     */
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toLowerCase();
    }

}
