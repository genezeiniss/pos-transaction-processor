package com.genezeiniss.pos_transaction_processor.exception;

import org.jooq.Query;

public class NonUniqueResultException extends RuntimeException {

    public NonUniqueResultException(Query query) {
        this(String.format("more than one result returned from query %s", query));
    }

    public NonUniqueResultException(final String message) {
        super(message);
    }
}
