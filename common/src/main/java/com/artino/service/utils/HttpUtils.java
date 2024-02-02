package com.artino.service.utils;

import java.util.HashMap;

import com.artino.service.common.LabelValue;
import okhttp3.*;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpUtils {

    public HttpUtils(String requestId) {
        this.requestId = requestId;
    }

    public HttpUtils() {

    }

    private String requestId;

    public ResponseVo doGet(String url) throws Exception {
        return doGet(url, null, null);
    }

    /**
     * Get 请求
     *
     * @param url
     * @param query
     * @param headers
     * @return
     */
    public ResponseVo doGet(String url, List<LabelValue> query, List<LabelValue> headers) throws Exception {
        Request.Builder builder = newRequest(url, query, headers).get();
        return request(builder.build());
    }

    /**
     * json request
     *
     * @param url
     * @param method
     * @param jsonString
     * @param query
     * @param headers
     * @return
     * @throws Exception
     */
    public ResponseVo doInvokeText(String url, EHttpMethod method, String jsonString, List<LabelValue> query, List<LabelValue> headers) throws Exception {
        Request.Builder builder = newRequest(url, query, headers);
        MediaType JSON = MediaType.parse("text/plain; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonString);
        if (method == EHttpMethod.PUT) return request(builder.put(body).build());
        if (method == EHttpMethod.DELETE) return request(builder.delete(body).build());
        if (method == EHttpMethod.PATCH) return request(builder.patch(body).build());
        return request(builder.post(body).build());
    }


    /**
     * json request
     *
     * @param url
     * @param method
     * @param jsonString
     * @param query
     * @param headers
     * @return
     * @throws Exception
     */
    public ResponseVo doInvokeJSON(String url, EHttpMethod method, String jsonString, List<LabelValue> query, List<LabelValue> headers) throws Exception {
        Request.Builder builder = newRequest(url, query, headers);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonString);
        if (method == EHttpMethod.PUT) return request(builder.put(body).build());
        if (method == EHttpMethod.DELETE) return request(builder.delete(body).build());
        if (method == EHttpMethod.PATCH) return request(builder.patch(body).build());
        return request(builder.post(body).build());
    }

    /**
     * form request
     *
     * @param url
     * @param method
     * @param formData
     * @param query
     * @param headers
     * @return
     * @throws Exception
     */
    public ResponseVo doInvokeFormData(String url, EHttpMethod method, List<LabelValue> formData, List<LabelValue> query, List<LabelValue> headers) throws Exception {
        Request.Builder builder = newRequest(url, query, headers);
        MultipartBody.Builder bodyBuild = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (Objects.nonNull(formData) && !formData.isEmpty()) {
            for (LabelValue item : formData) bodyBuild.addFormDataPart(item.getLabel(), item.getValue());
        }
        if (method == EHttpMethod.PUT)
            return request(builder.put(bodyBuild.build()).build());
        if (method == EHttpMethod.DELETE)
            return request(builder.delete(bodyBuild.build()).build());
        if (method == EHttpMethod.PATCH)
            return request(builder.patch(bodyBuild.build()).build());
        return request(builder.post(bodyBuild.build()).build());
    }

    /**
     * 返回结果
     *
     * @param request
     * @return
     * @throws Exception
     */
    private ResponseVo request(Request request) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response = call.execute();
        ResponseVo vo = new ResponseVo();
        if (null != requestId && !requestId.isEmpty() && !requestId.isBlank()) {
            HashMap<String, String> headers = new HashMap<>();
            request.headers().forEach(e -> headers.put(e.getFirst(), e.getSecond()));
            if (Objects.nonNull(request.body())) headers.put("Content-Type", request.body().contentType().toString());
            vo.setHeaders(headers);
        }
        vo.setCode(response.code());
        String result = Objects.nonNull(response.body()) ? new String(response.body().bytes(), StandardCharsets.UTF_8) : null;
        if (response.isSuccessful())
            if (response.code() == 200) vo.setResponse(result);
            else vo.setErrorMessage(result);
        else vo.setErrorMessage(result);
        return vo;
    }

    /**
     * 构建Builder
     *
     * @param url
     * @param query
     * @param headers
     * @return
     */
    private Request.Builder newRequest(String url, List<LabelValue> query, List<LabelValue> headers) {
        String URL = url;
        if (Objects.nonNull(query) && !query.isEmpty()) {
            List<String> queryString = new ArrayList<>();
            query.forEach(item -> queryString.add(String.format("%s=%s", item.getLabel(), URLEncoder.encode(item.getValue(), StandardCharsets.UTF_8))));
            URL += "?" + String.join("&", queryString);
        }
        Request.Builder builder = new Request.Builder().url(URL);
        if (Objects.nonNull(headers) && !headers.isEmpty())
            for (LabelValue item : headers)
                builder = builder.addHeader(item.getLabel(), item.getValue());
        if (null != requestId && !requestId.isEmpty() && !requestId.isBlank())
            builder = builder.addHeader("requestId", requestId).addHeader("User-Agent", "Pandora LowCode/1.0.0");
        return builder;
    }

    public static class ResponseVo {
        private int code;
        private String response;
        private String errorMessage;
        private HashMap<String, String> headers;

        private String body;

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public HashMap<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(HashMap<String, String> headers) {
            this.headers = headers;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }



    static enum EHttpMethod {
        GET,
        POST,
        DELETE,
        PUT,
        PATCH
    }
}
