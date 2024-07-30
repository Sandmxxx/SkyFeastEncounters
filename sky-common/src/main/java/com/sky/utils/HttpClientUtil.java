package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http工具类
 */
public class HttpClientUtil {

    static final  int TIMEOUT_MSEC = 5 * 1000;

    /**
     * 使用GET方法发送HTTP请求。
     *
     * @param url 请求的URL地址。
     * @param paramMap 请求参数的映射表，键值对形式。
     * @return 返回HTTP响应的字符串内容。
     */
    public static String doGet(String url,Map<String,String> paramMap){
        // 创建默认的HttpClient实例
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 初始化响应结果字符串
        String result = "";
        // 声明CloseableHttpResponse实例，用于处理响应
        CloseableHttpResponse response = null;

        try{
            // 构建请求的URI，包括URL和参数
            URIBuilder builder = new URIBuilder(url);
            // 如果有参数，则添加到URI构建器
            if(paramMap != null){
                for (String key : paramMap.keySet()) {
                    builder.addParameter(key,paramMap.get(key));
                }
            }
            // 根据构建器生成最终的URI
            URI uri = builder.build();

            // 创建HttpGet请求对象，指定URI
            // 创建GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行HTTP请求，获取响应
            // 发送请求
            response = httpClient.execute(httpGet);

            // 检查响应状态码，仅当为200时，处理响应体
            // 判断响应状态
            if(response.getStatusLine().getStatusCode() == 200){
                // 将响应实体转换为字符串，指定字符编码
                result = EntityUtils.toString(response.getEntity(),"UTF-8");
            }
        }catch (Exception e){
            // 打印异常堆栈跟踪
            e.printStackTrace();
        }finally {
            // 关闭响应和HttpClient，释放资源
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                // 打印IO异常堆栈跟踪
                e.printStackTrace();
            }
        }

        // 返回处理后的响应结果
        return result;
    }


    /**
     * 发送一个POST请求，并获取响应。
     *
     * @param url 请求的URL地址。
     * @param paramMap 请求参数的映射表，包含参数名和参数值。
     * @return 返回服务器的响应内容。
     * @throws IOException 如果发生I/O错误。
     */
    public static String doPost(String url, Map<String, String> paramMap) throws IOException {
        // 创建一个默认的HttpClient实例
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";

        try {
            // 创建HttpPost请求对象
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            // 如果有参数，创建参数列表，并设置到请求对象中
            // 创建参数列表
            if (paramMap != null) {
                List<NameValuePair> paramList = new ArrayList();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                // 设置表单实体，用于传递参数
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }

            // 配置请求参数
            httpPost.setConfig(builderRequestConfig());

            // 执行请求，并获取响应
            // 执行http请求
            response = httpClient.execute(httpPost);

            // 将响应实体转换为字符串，使用UTF-8编码
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw e;
        } finally {
            // 关闭响应和HttpClient，释放资源
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 返回响应内容
        return resultString;
    }


    /**
     * 发送POST方式请求，application/json参数
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String doPost4Json(String url, Map<String, String> paramMap) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";

        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            if (paramMap != null) {
                //构造json格式数据
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    jsonObject.put(param.getKey(),param.getValue());
                }
                StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");
                //设置请求编码
                entity.setContentEncoding("utf-8");
                //设置数据类型
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }

            httpPost.setConfig(builderRequestConfig());

            // 执行http请求
            response = httpClient.execute(httpPost);

            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }
    private static RequestConfig builderRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_MSEC)
                .setConnectionRequestTimeout(TIMEOUT_MSEC)
                .setSocketTimeout(TIMEOUT_MSEC).build();
    }

}
