package com.accionmfb.omnix.connector.modules.http;

import kong.unirest.HttpResponse;

import java.util.Map;

public interface OmnixWebClient {

    // ------------------------------------ GET ------------------------------------ //
    HttpResponse<String> getForHttpResponse(String url, Map<String, String> headers);
    HttpResponse<String> getForHttpResponse(String url, Map<String, String> headers, Map<String, Object> params);
    String getForObject(String url, Map<String, String> headers);

    String getForObject(String url, Map<String, String> headers, Map<String, Object> params);

    <T> T getForObject(String url, Map<String, String> headers, Class<T> tClass);
    <T> T getForObject(String url, Map<String, String> headers, Map<String, Object> params, Class<T> tClass);



    // ------------------------------------- POST ------------------------------------- //
    HttpResponse<String> postForHttpResponse(String url, Map<String, String> headers, Object body);

    HttpResponse<String> postForHttpResponse(String url, Map<String, String> headers, Object body, Map<String, Object> params);

    String postForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params);

    String postForObject(String url, Map<String, String> headers, Object body);

    <T> T postForObject(String url, Map<String, String> headers, Object body, Class<T> tClass);

    <T> T postForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params, Class<T> tClass);

    // ------------------------------------- PUT ------------------------------------- //
    HttpResponse<String> putForHttpResponse(String url, Map<String, String> headers, Object body);

    HttpResponse<String> putForHttpResponse(String url, Map<String, String> headers, Object body, Map<String, Object> params);

    String putForObject(String url, Map<String, String> headers, Object body);

    String putForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params);

    <T> T putForObject(String url, Map<String, String> headers, Object body, Class<T> tClass);

    <T> T putForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params, Class<T> tClass);
}
