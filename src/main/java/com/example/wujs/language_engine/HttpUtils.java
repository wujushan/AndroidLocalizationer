/*
 * Copyright 2014-2015 Wesley Lin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.wujs.language_engine;

import com.intellij.openapi.util.io.StreamUtil;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Wesley Lin on 12/2/14.
 */
public class HttpUtils {

    public static String doHttpGet(String url) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse resp = httpClient.execute(httpGet);

            return StreamUtil.readText(resp.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doHttpGet(String url, Header[] headers) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeaders(headers);
            HttpResponse resp = httpClient.execute(httpGet);

            return StreamUtil.readText(resp.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doHttpPost(String url, List<NameValuePair> params) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse resp = httpClient.execute(httpPost);
            return StreamUtil.readText(resp.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doHttpPost(String url, String xmlBody, Header[] headers) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeaders(headers);
            httpPost.setEntity(new StringEntity(xmlBody, "UTF-8"));
            HttpResponse resp = httpClient.execute(httpPost);
            return StreamUtil.readText(resp.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
