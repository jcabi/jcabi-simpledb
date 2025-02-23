/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.simpledb;

import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import lombok.EqualsAndHashCode;

/**
 * Single table in SimpleDB, through AWS SDK.
 *
 * @since 0.1
 */
@Immutable
@Loggable(Loggable.DEBUG)
@EqualsAndHashCode(of = { "credentials", "table" })
final class AwsDomain implements Domain {

    /**
     * AWS credentials.
     */
    private final transient Credentials credentials;

    /**
     * Domain name.
     */
    private final transient String table;

    /**
     * Public ctor.
     * @param creds Credentials
     * @param name Domain name
     */
    AwsDomain(final Credentials creds, final String name) {
        this.credentials = creds;
        this.table = name;
    }

    @Override
    public String toString() {
        return this.table;
    }

    @Override
    public String name() {
        return this.table;
    }

    @Override
    public void create() {
        this.credentials.aws().createDomain(
            new CreateDomainRequest().withDomainName(this.table)
        );
    }

    @Override
    public void drop() {
        this.credentials.aws().deleteDomain(
            new DeleteDomainRequest().withDomainName(this.table)
        );
    }

    @Override
    public Item item(final String name) {
        return new AwsItem(this.credentials, this.table, name);
    }

    @Override
    public Iterable<Item> select(final SelectRequest request) {
        return () -> new AwsIterator(
            this.credentials,
            this.table,
            request
        );
    }

}
