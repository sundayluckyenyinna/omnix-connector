package com.accionmfb.omnix.connector.modules.http;

import kong.unirest.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface OmnixWebClient {

    // ------------------------------------ GET ------------------------------------ //
    ResponseEntity<String> getForHttpResponse(String url, Map<String, String> headers);
    ResponseEntity<String> getForHttpResponse(String url, Map<String, String> headers, Map<String, Object> params);
    String getForObject(String url, Map<String, String> headers);

    String getForObject(String url, Map<String, String> headers, Map<String, Object> params);

    <T> T getForObject(String url, Map<String, String> headers, Class<T> tClass);
    <T> T getForObject(String url, Map<String, String> headers, Map<String, Object> params, Class<T> tClass);



    // ------------------------------------- POST ------------------------------------- //
    ResponseEntity<String> postForHttpResponse(String url, Map<String, String> headers, Object body);

    ResponseEntity<String> postForHttpResponse(String url, Map<String, String> headers, Object body, Map<String, Object> params);

    String postForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params);

    String postForObject(String url, Map<String, String> headers, Object body);

    <T> T postForObject(String url, Map<String, String> headers, Object body, Class<T> tClass);

    <T> T postForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params, Class<T> tClass);

    // ------------------------------------- PUT ------------------------------------- //
    ResponseEntity<String> putForHttpResponse(String url, Map<String, String> headers, Object body);

    ResponseEntity<String> putForHttpResponse(String url, Map<String, String> headers, Object body, Map<String, Object> params);

    String putForObject(String url, Map<String, String> headers, Object body);

    String putForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params);

    <T> T putForObject(String url, Map<String, String> headers, Object body, Class<T> tClass);

    <T> T putForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params, Class<T> tClass);

    // ---------------------------- FORM DATA -------------------- //
    <T> T postForm(String url, MultiValueMap<String, Object> formData, Map<String, String> headers, Class<T> tClass);

    <T> T postForm(String url, MultiValueMap<String, Object> formData, Map<String, String> headers, Map<String, Object> params, Class<T> tClass);
}
