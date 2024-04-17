package com.accionmfb.omnix.connector.util;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class ConnectorUtils {

    public static ZoneId getDefaultZone(){
        return ZoneId.of("Africa/Lagos");
    }
    public static LocalDateTime getCurrentDateTime(){
        return LocalDateTime.now(getDefaultZone());
    }

    public static <T> T returnOrDefault(T value, T defaultValue){
        return Objects.isNull(value) ? defaultValue : value;
    }
}
