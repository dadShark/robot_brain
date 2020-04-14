package com.robot_brain.nlu.flow.kit;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HTTPUntil {

    public static String get(String url){
        try {
            HttpGet request = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(3000).setConnectionRequestTimeout(1000)
                    .setSocketTimeout(3000).build();
            request.setConfig(requestConfig);
            // 执行http get请求
            HttpResponse response = HttpClients.createDefault()
                    .execute(request);

            // 根据返回码判断返回是否成功
            String result = "";
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
            } else {
                return null;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("【调用" + url + "接口出错】");
            return null;
        }
    }

    public static String post(String url,String param){
        try {
            HttpPost request = new HttpPost(url);
            request.setEntity(new StringEntity(param, "UTF-8"));
            HttpResponse response = HttpClients.createDefault()
                    .execute(request);

            // 根据返回码，判断请求是否成功
            if (200 == response.getStatusLine().getStatusCode()) {
                return EntityUtils.toString(response.getEntity());
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
