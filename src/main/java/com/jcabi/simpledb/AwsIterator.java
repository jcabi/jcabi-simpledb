/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.simpledb;

import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.jcabi.aspects.Loggable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import lombok.EqualsAndHashCode;

/**
 * Iterator of items in SimpleDB.
 *
 * @since 0.1
 */
@Loggable(Loggable.DEBUG)
@EqualsAndHashCode(of = { "credentials", "table" })
final class AwsIterator implements Iterator<Item> {

    /**
     * AWS credentials.
     */
    private final transient Credentials credentials;

    /**
     * Domain name.
     */
    private final transient String table;

    /**
     * Select request.
     */
    private final transient SelectRequest request;

    /**
     * Most recent result.
     */
    private transient SelectResult result;

    /**
     * Public ctor.
     * @param creds Credentials
     * @param name Domain name
     * @param req Request
     */
    AwsIterator(final Credentials creds, final String name,
        final SelectRequest req) {
        this.credentials = creds;
        this.table = name;
        this.request = req;
    }

    @Override
    public boolean hasNext() {
        if (this.result == null) {
            this.result = this.credentials.aws().select(this.request);
        } else if (this.result.getItems().isEmpty()
            && this.result.getNextToken() != null) {
            this.result = this.credentials.aws().select(
                this.request.withNextToken(this.result.getNextToken())
            );
        }
        return !this.result.getItems().isEmpty();
    }

    @Override
    public Item next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        return new AwsItem(
            this.credentials,
            this.table,
            this.result.getItems().remove(0)
        );
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
