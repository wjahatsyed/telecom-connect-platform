package com.wajahat.telecom.esimprovisioning.exception;

public class EsimProfileNotFoundException extends RuntimeException {

    public EsimProfileNotFoundException(String iccid) {
        super("eSIM profile not found: " + iccid);
    }
}
