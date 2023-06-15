package com.example.karama.helper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class APIHelper {
    public static final Map<String, String> API_HEADER(String token) {
        try {
            Map<String, String> API_HEADER = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("Content-Type", "application/json;charset=UTF-8");
                put("Accept", "application/json");
                put("Cache-Control", " max-age=640000");
            }};
            return API_HEADER;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static RequestBody parseReq(String strReq) {
        RequestBody requestBody = RequestBody.create(JSON, strReq);
        return requestBody;
    }
}
