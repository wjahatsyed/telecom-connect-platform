package com.wajahat.telecom.esimprovisioning.exception;

import com.wajahat.telecom.esimprovisioning.domain.EsimStatus;

public class InvalidEsimStateException extends RuntimeException {

    public InvalidEsimStateException(String iccid, EsimStatus current, String action) {
        super("Cannot " + action + " eSIM profile " + iccid + " while status is " + current);
    }
}
