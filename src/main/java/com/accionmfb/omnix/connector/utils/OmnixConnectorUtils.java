package com.accionmfb.omnix.connector.utils;

import java.util.Objects;

public class OmnixConnectorUtils {

    public static boolean isNullOrEmpty(Object object){
        return Objects.isNull(object) || String.valueOf(object).isEmpty();
    }

}
