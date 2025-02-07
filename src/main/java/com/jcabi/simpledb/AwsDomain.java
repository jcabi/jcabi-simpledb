/*
 * Copyright (c) 2012-2025 Yegor Bugayenko
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
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
