package com.accionmfb.omnix.connector.modules.http;

import com.accionmfb.omnix.connector.commons.StringValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpMethod;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class OmnixDefaultWebClient implements OmnixWebClient {

    private final WebClientLogger logger;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    private static void initUnirestConnector(){
        Unirest.config().verifySsl(false);
    }

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


    // ---------------------------- FORM DATA -------------------- //
    @Override
    public <T> T postForm(String url, MultiValueMap<String, Object> formData, Map<String, String> headers, Class<T> tClass){
        return this.postForm(url, formData, headers, null, tClass);
    }

    @Override
    public <T> T postForm(String url, MultiValueMap<String, Object> formData, Map<String, String> headers, Map<String, Object> params, Class<T> tClass){
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            headers.forEach(httpHeaders::add);
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

            logger.logApiRequest(org.springframework.http.HttpMethod.POST.name(), url, headers, formData, new LinkedHashMap<>());
            String completeUrl = formatBaseUrlWithQueryParams(url, Objects.isNull(params) ? new LinkedHashMap<>() : params);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(formData, httpHeaders);
            ResponseEntity<T> responseEntity = restTemplate.exchange(completeUrl, org.springframework.http.HttpMethod.POST, httpEntity, tClass);
            logger.logApiResponse(responseEntity);
            return responseEntity.getBody();
        }catch (Exception exception){
            if(exception instanceof HttpStatusCodeException){
                HttpStatusCodeException statusCodeException = (HttpStatusCodeException) exception;
                logger.logHttpStatusCodeException(statusCodeException);
            }else{
                log.error("Exception occurred while reaching: {}", url);
                log.error("Exception message is: {}", exception.getMessage());
            }
            throw new RuntimeException("External party API failure");
        }
    }

    private static String formatBaseUrlWithQueryParams(String url, Map<String, Object> params){
        if(params.isEmpty()){
            return url;
        }
        StringJoiner queryJoiner = new StringJoiner("&");
        params.forEach((key, value) -> {
            if(Stream.of(key, value).allMatch(Objects::nonNull)){
                String queryKeyValue = key.concat("=").concat(String.valueOf(value));
                queryJoiner.add(queryKeyValue);
            }
        });
        String totalQueryParams = queryJoiner.toString();
        return url.concat("?").concat(totalQueryParams);
    }
}
