package com.accionmfb.omnix.connector.modules.t24.builder;

import com.accionmfb.omnix.connector.commons.StringValues;
import com.accionmfb.omnix.connector.modules.t24.annotation.T24Field;
import com.accionmfb.omnix.connector.utils.OmnixConnectorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OfsGeneratorUtils
{
    private final List<OfsKeyValueMessageDataBuilder> keyValueMessageDataBuilders;

    public String generateOfsMessageData(Object payload){
         Map<String, Object> valueMap = mapValuesToT24Fields(payload);
        StringJoiner stringJoiner = new StringJoiner(StringValues.COMMA);
         valueMap.forEach((key, value) -> {
             String keyValueCompile = getSupportingKeyValueBuilder(value).build(key, value);
             stringJoiner.add(keyValueCompile);
         });
         return stringJoiner.toString();
    }

    public Map<String, Object> mapValuesToT24Fields(Object payload){
        Map<String, Object> result = new LinkedHashMap<>();
        Arrays.stream(payload.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .forEach(field -> {
                    T24Field t24FieldAnnotation = field.getDeclaredAnnotation(T24Field.class);
                    if(!OmnixConnectorUtils.isNullOrEmpty(t24FieldAnnotation)){
                        String t24Field = t24FieldAnnotation.value();
                        Object objectFieldValue = getObjectFieldValue(field, payload);
                        if(Objects.nonNull(objectFieldValue)) {
                            result.put(t24Field, objectFieldValue);
                        }else{
                            if(!t24FieldAnnotation.ignoreOnNullValue()){
                                result.put(t24Field, t24FieldAnnotation.defaultForNull());
                            }
                        }
                    }
                });
        return result;
    }

    private OfsKeyValueMessageDataBuilder getSupportingKeyValueBuilder(Object value){
        return keyValueMessageDataBuilders.stream()
                .filter(ofsKeyValueMessageDataBuilder -> ofsKeyValueMessageDataBuilder.supports(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ofs value type not supported!"));
    }

    private static Object getObjectFieldValue(Field field, Object parentObject){
        try {
            return field.get(parentObject);
        } catch (IllegalAccessException e) {
            log.error("Error occurred while reading field value of field: {} in class: {}. Exception message is: {}", field.getName(), parentObject.getClass().getSimpleName(), e.getMessage());
            return StringValues.EMPTY_STRING;
        }
    }
}
