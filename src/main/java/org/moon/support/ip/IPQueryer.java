package org.moon.support.ip;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.moon.exception.ApplicationRunTimeException;
import org.moon.utils.Objects;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ip查询工具类
 * @author Gavin
 * @date 15-1-11 上午12:03
 */
@Component
public class IPQueryer implements DisposableBean {

    private String taobaoIPURL = "http://ip.taobao.com/service/getIpInfo.php?ip=%s";

    private String lock = "lock";

    CloseableHttpClient client = null;

    /**
     * 获取ip地址的归属地,返回格式为：
     * <p><code>{country:国家,area:西南地区,region:省,city:市,isp:服务商,如电信}</code></p>
     * @param ip
     * @return
     */
    public Map<String,String> getAddress(String ip){
        InputStream responseStream = null;
        try{
            String ipUrl = String.format(taobaoIPURL,ip);
            client = HttpClients.createDefault();
            HttpGet get = new HttpGet(ipUrl);
            HttpResponse response = getClient().execute(get);
            ObjectMapper mapper = new ObjectMapper();
            responseStream = response.getEntity().getContent();
            Map<String,Object> result = mapper.readValue(responseStream, HashMap.class);
            return (Map<String, String>) result.get("data");
        } catch (IOException e) {
            throw new ApplicationRunTimeException(e);
        }finally {
            try{
                if(Objects.nonNull(responseStream)){
                    responseStream.close();
                }
            }catch (Exception e){
                throw new ApplicationRunTimeException(e);
            }
        }
    }

    /**
     * 获取httpclient
     * @return
     */
    private HttpClient getClient(){
        if(Objects.isNull(client)){
            synchronized (lock){//单例模式
                if(Objects.isNull(client)){
                    client = HttpClients.custom().build();
                }
            }
        }

        return client;
    }


    @Override
    public void destroy() throws Exception {
        if(Objects.nonNull(client)){
            client.close();
        }
    }
}
