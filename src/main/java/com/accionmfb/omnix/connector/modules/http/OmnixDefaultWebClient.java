package com.accionmfb.omnix.connector.modules.http;

import com.accionmfb.omnix.connector.commons.StringValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpMethod;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OmnixDefaultWebClient implements OmnixWebClient {

    private final WebClientLogger logger;
    private final ObjectMapper objectMapper;


    @Override
    public HttpResponse<String> getForHttpResponse(String url, Map<String, String> headers){
        initUnirestConnector();
        logger.logApiRequest(HttpMethod.GET.name(), url, headers, StringValues.EMPTY_STRING, new LinkedHashMap<>());
        HttpResponse<String> httpResponse = Unirest.get(url)
                .headers(headers)
                .asString();
        logger.logApiResponse(httpResponse);
        return httpResponse;
    }

    @Override
    public HttpResponse<String> getForHttpResponse(String url, Map<String, String> headers, Map<String, Object> params){
        initUnirestConnector();
        logger.logApiRequest(HttpMethod.GET.name(), url, headers, StringValues.EMPTY_STRING, params);
        HttpResponse<String> httpResponse = Unirest.get(url)
                .headers(headers)
                .queryString(params)
                .asString();
        logger.logApiResponse(httpResponse);
        return httpResponse;
    }

    @Override
    public String getForObject(String url, Map<String, String> headers){
       return getForHttpResponse(url, headers).getBody();
    }

    @Override
    public String getForObject(String url, Map<String, String> headers, Map<String, Object> params){
        return getForHttpResponse(url, headers, params).getBody();
    }

    @Override
    public <T> T getForObject(String url, Map<String, String> headers, Class<T> tClass){
        try {
            String responseJson = getForObject(url, headers);
            return objectMapper.readValue(responseJson, tClass);
        }catch (Exception ignored){
          return null;
        }
    }

    @Override
    public <T> T getForObject(String url, Map<String, String> headers, Map<String, Object> params, Class<T> tClass){
        try {
            String responseJson = getForObject(url, headers, params);
            return objectMapper.readValue(responseJson, tClass);
        }catch (Exception ignored){
            return null;
        }
    }


    // ------------------------------------- POST ------------------------------------- //
    @Override
    @SneakyThrows
    public HttpResponse<String> postForHttpResponse(String url, Map<String, String> headers, Object body){
        initUnirestConnector();
        logger.logApiRequest(HttpMethod.POST.name(), url, headers, body, new LinkedHashMap<>());
        HttpResponse<String> httpResponse = Unirest.post(url)
                .headers(headers)
                .body(body instanceof String ? body : objectMapper.writeValueAsString(body))
                .asString();
        logger.logApiResponse(httpResponse);
        return httpResponse;
    }

    @Override
    @SneakyThrows
    public HttpResponse<String> postForHttpResponse(String url, Map<String, String> headers, Object body, Map<String, Object> params){
        initUnirestConnector();
        logger.logApiRequest(HttpMethod.POST.name(), url, headers, body, new LinkedHashMap<>());
        HttpResponse<String> httpResponse = Unirest.post(url)
                .headers(headers)
                .body(body instanceof String ? body : objectMapper.writeValueAsString(body))
                .queryString(params)
                .asString();
        logger.logApiResponse(httpResponse);
        return httpResponse;
    }

    @Override
    public String postForObject(String url, Map<String, String> headers, Object body){
        return postForHttpResponse(url, headers, body).getBody();
    }

    @Override
    public String postForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params){
        return postForHttpResponse(url, headers, body,  params).getBody();
    }

    @Override
    public <T> T postForObject(String url, Map<String, String> headers, Object body, Class<T> tClass){
        try {
            String responseJson = postForObject(url, headers, body);
            return objectMapper.readValue(responseJson, tClass);
        }catch (Exception ignored){
            return null;
        }
    }

    @Override
    public <T> T postForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params, Class<T> tClass){
        try {
            String responseJson = postForObject(url, headers, body, params);
            return objectMapper.readValue(responseJson, tClass);
        }catch (Exception ignored){
            return null;
        }
    }


    // ------------------------------------- PUT ------------------------------------- //
    @Override
    @SneakyThrows
    public HttpResponse<String> putForHttpResponse(String url, Map<String, String> headers, Object body){
        initUnirestConnector();
        logger.logApiRequest(HttpMethod.PUT.name(), url, headers, body, new LinkedHashMap<>());
        HttpResponse<String> httpResponse = Unirest.put(url)
                .headers(headers)
                .body(body instanceof String ? body : objectMapper.writeValueAsString(body))
                .asString();
        logger.logApiResponse(httpResponse);
        return httpResponse;
    }

    @Override
    @SneakyThrows
    public HttpResponse<String> putForHttpResponse(String url, Map<String, String> headers, Object body, Map<String, Object> params){
        initUnirestConnector();
        logger.logApiRequest(HttpMethod.PUT.name(), url, headers, body, new LinkedHashMap<>());
        HttpResponse<String> httpResponse = Unirest.put(url)
                .headers(headers)
                .body(body instanceof String ? body : objectMapper.writeValueAsString(body))
                .queryString(params)
                .asString();
        logger.logApiResponse(httpResponse);
        return httpResponse;
    }

    @Override
    public String putForObject(String url, Map<String, String> headers, Object body){
        return putForHttpResponse(url, headers, body).getBody();
    }

    @Override
    public String putForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params){
        return putForHttpResponse(url, headers, body,  params).getBody();
    }

    @Override
    public <T> T putForObject(String url, Map<String, String> headers, Object body, Class<T> tClass){
        try {
            String responseJson = putForObject(url, headers, body);
            return objectMapper.readValue(responseJson, tClass);
        }catch (Exception ignored){
            return null;
        }
    }

    @Override
    public <T> T putForObject(String url, Map<String, String> headers, Object body, Map<String, Object> params, Class<T> tClass){
        try {
            String responseJson = putForObject(url, headers, body, params);
            return objectMapper.readValue(responseJson, tClass);
        }catch (Exception ignored){
            return null;
        }
    }

    private static void initUnirestConnector(){
        Unirest.config().verifySsl(false);
    }
}
