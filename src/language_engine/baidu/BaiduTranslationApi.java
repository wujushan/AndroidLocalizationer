package language_engine.baidu;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.ide.util.PropertiesComponent;
import data.Key;
import data.Log;
import data.StorageDataKey;
import language_engine.HttpUtils;
import module.SupportedLanguages;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import java.net.URLEncoder;
import java.util.*;

public class BaiduTranslationApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private String appid;
    private String securityKey;

    public BaiduTranslationApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    /**
     * @param querys
     * @param targetLanguageCode
     * @param sourceLanguageCode
     * @return
     */
    public static List<String> getTranslationJSON(@NotNull List<String> querys,
                                                  @NotNull SupportedLanguages targetLanguageCode,
                                                  @NotNull SupportedLanguages sourceLanguageCode) {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        if (querys.isEmpty())
            return null;
        StringBuilder queryBuilder = new StringBuilder();
        for (int i = 0; i < querys.size(); i++) {
            if (i != querys.size() - 1) {
                queryBuilder.append(querys.get(i)).append("\n");
//                queryBuilder.append(URLEncoder.encode(querys.get(i))).append("\n");
            } else if (i == querys.size() - 1) {
                queryBuilder.append(querys.get(i));
//                queryBuilder.append(URLEncoder.encode(querys.get(i)));
            }
        }

        String query = queryBuilder.toString();
        Map<String, String> params = new HashMap<>();
        params.put("q", query);
        params.put("from", sourceLanguageCode.getLanguageCode());
        params.put("to", targetLanguageCode.getLanguageCode());
        String appid = new BasicNameValuePair("client_id",
                propertiesComponent.getValue(StorageDataKey.BaiduClientIdStored, Key.BAIDU_CLIENT_ID)).getValue();
        params.put("appid", appid);
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        String securityKey = new BasicNameValuePair("client_secret",
                propertiesComponent.getValue(StorageDataKey.BaiduClientSecretStored, Key.BAIDU_CLIENT_SECRET)).getValue();
        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        params.put("sign", MD5.md5(src));
        String getResult = HttpGet.get(TRANS_API_HOST, params);
        if (getResult != null) {
            JsonObject resultObj = new JsonParser().parse(getResult).getAsJsonObject();
            JsonElement errorElement = resultObj.get("error_code");
            if (errorElement != null) {
                String errorCode = errorElement.getAsString();
                String errorMsg = resultObj.get("error_msg").getAsString();
                Logger.error(errorCode + " :" + errorMsg);
                return null;
            } else {
                JsonArray translations = resultObj.getAsJsonArray("trans_result");
                if (translations != null) {
                    List<String> result = new ArrayList<>();
                    for (int i = 0; i < translations.size(); i++) {
                        result.add(translations.get(i).getAsJsonObject().get("dst").getAsString());
                    }
                    return result;
                }
            }
        } else {
            return null;
        }
        return null;
    }
}
