package com.accionmfb.omnix.connector.modules.http;

import com.accionmfb.omnix.connector.commons.StringValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebClientLogger {

    private final ObjectMapper objectMapper;

    public void logApiRequest(String method, String url, Map<String, String> headers, Object body, Map<String, Object> params){
        try{
            System.out.println();
            log.info("================================================= EXTERNAL SERVICE API REQUEST START ==============================================");
            log.info("{} : {}", method, url);
            log.info("Request Body: {}", Objects.nonNull(body) ? (body instanceof String ? body : objectMapper.writeValueAsString(body)) : StringValues.EMPTY_STRING);
            log.info("Request Headers: {}", headers);
            log.info("Request Parameters: {}", params);
            log.info("------------------------------------------------------------------------------------------------------------------------------------");
        }catch (Exception ignored){}
    }

    public void logApiResponse(HttpResponse<String> httpResponse){
        try {
            log.info("------------------------------------------------ EXTERNAL SERVICE API RESPONSE END --------------------------------------------------");
            log.info("Http Status: {} {}", httpResponse.getStatus(), httpResponse.getStatusText());
            try{ log.info("Response Body: {}", httpResponse.getBody().length() > 10_000 ? httpResponse.getBody().substring(0, 10_000) : httpResponse.getBody()); } catch (Exception ignored){}
            try { log.info("Response Headers: {}", objectMapper.writeValueAsString(httpResponse.getHeaders().all()));} catch (Exception ignored){}
            log.info("Response Cookies: {}", httpResponse.getCookies());
            log.info("=====================================================================================================================================");
            System.out.println();
        }catch (Exception ignored){}
    }

    public void logApiResponse(ResponseEntity<?> responseEntity){
        try {
            log.info("------------------------------------------------ EXTERNAL SERVICE API RESPONSE END --------------------------------------------------");
            log.info("Http Status: {}", responseEntity.getStatusCode());
            try{
                String body = responseEntity.getBody() instanceof String ? (String)responseEntity.getBody() : objectMapper.writeValueAsString(responseEntity.getBody());
                log.info("Response Body: {}", body.length() > 10_000 ? body.substring(0, 10_000) : body);
            } catch (Exception ignored){}
            try { log.info("Response Headers: {}", objectMapper.writeValueAsString(responseEntity.getHeaders().toSingleValueMap()));} catch (Exception ignored){}
            log.info("Response Cookies: {}", new LinkedList<>());
            log.info("=====================================================================================================================================");
            System.out.println();
        }catch (Exception ignored){}
    }

    public void logHttpStatusCodeException(HttpStatusCodeException exception){
        try {
            log.info("------------------------------------------------ EXTERNAL SERVICE API EXCEPTION END --------------------------------------------------");
            log.info("Http Status: {}", exception.getStatusCode());
            try{ log.info("Response Body: {}", exception.getResponseBodyAsString()); } catch (Exception ignored){}
            log.info("Response Cookies: {}", new LinkedList<>());
            log.info("Exception message: {}", exception.getMessage());
            log.info("=====================================================================================================================================");
            System.out.println();
        }catch (Exception ignored){}
    }
}
