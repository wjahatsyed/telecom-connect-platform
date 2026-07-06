package com.wajahat.telecom.subscription.exception;

import com.wajahat.telecom.subscription.domain.SubscriptionStatus;
import java.util.UUID;

public class InvalidSubscriptionStateException extends RuntimeException {

    public InvalidSubscriptionStateException(UUID subscriptionId, SubscriptionStatus current, String action) {
        super("Cannot " + action + " subscription " + subscriptionId + " while status is " + current);
    }
}
