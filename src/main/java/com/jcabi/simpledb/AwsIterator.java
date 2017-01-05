/**
 * Copyright (c) 2012-2017, jcabi.com
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

import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.jcabi.aspects.Loggable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import lombok.EqualsAndHashCode;

/**
 * Iterator of items in SimpleDB.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
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
    protected AwsIterator(final Credentials creds, final String name,
        final SelectRequest req) {
        this.credentials = creds;
        this.table = name;
        this.request = req;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
