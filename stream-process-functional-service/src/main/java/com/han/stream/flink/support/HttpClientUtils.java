package com.han.stream.flink.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.naming.ldap.PagedResultsControl;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author: Hanl
 * @date :2019/8/29
 * @desc:
 */
@Slf4j
public class HttpClientUtils {

    public static String httpPost(String url, String content) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            HttpEntity postEntity = new StringEntity(content, Charset.forName("UTF-8"));
            httpPost.setEntity(postEntity);
            response = httpClient.execute(httpPost);
            if (null != response) {
                HttpEntity httpEntity = response.getEntity();
                if (null != httpEntity) {
                    InputStream inputStream = httpEntity.getContent();
                    result = IOUtils.toString(inputStream, "UTF-8");
                }
            }
        } catch (Exception e) {
            log.warn("Failed to http post url={},content={}", new Object[]{url, content}, new Throwable(e));
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
