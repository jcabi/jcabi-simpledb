/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.simpledb;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Amazon SimpleDB region.
 *
 * <p>It is recommended to use {@link Region.Simple} in most cases.
 *
 * <p>You can use {@link #aws()} method to get access to Amazon SimpleDB
 * client directly.
 *
 * @since 0.1
 */
@Immutable
public interface Region {

    /**
     * Get SimpleDB client.
     * @return The client
     */
    @NotNull(message = "AWS SimpleDB client is never NULL")
    AmazonSimpleDB aws();

    /**
     * Get one domain.
     * @param name Domain name
     * @return Domain
     */
    @NotNull(message = "domain is never NULL")
    Domain domain(@NotNull String name);

    /**
     * Simple region, basic implementation.
     *
     * @since 0.1
     */
    @Immutable
    @Loggable(Loggable.DEBUG)
    @ToString
    @EqualsAndHashCode(of = "credentials")
    final class Simple implements Region {
        /**
         * Credentials.
         */
        private final transient Credentials credentials;

        /**
         * Public ctor.
         * @param creds Credentials
         */
        public Simple(@NotNull(message = "credentials can't be NULL")
            final Credentials creds) {
            this.credentials = creds;
        }

        @Override
        @NotNull(message = "AWS client is never NULL")
        public AmazonSimpleDB aws() {
            return this.credentials.aws();
        }

        @Override
        @NotNull(message = "domain is never NULL")
        public Domain domain(@NotNull final String name) {
            return new AwsDomain(this.credentials, name);
        }
    }

}
