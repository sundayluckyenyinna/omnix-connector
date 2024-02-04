package com.accionmfb.omnix.connector.modules.t24.processor;

import com.accionmfb.omnix.connector.modules.t24.constants.OfsRequestType;

public class EnquiryOfsGenerator extends AbstractOfsGenerator{

    @Override
    public String generate() {
        return null;
    }

    @Override
    public boolean supports(OfsRequestType ofsRequestType) {
        return ofsRequestType == OfsRequestType.ENQUIRY;
    }
}
