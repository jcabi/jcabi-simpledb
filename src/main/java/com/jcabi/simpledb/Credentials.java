/**
 * Copyright (c) 2012-2015, jcabi.com
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

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.Validate;

/**
 * Amazon SimpleDB credentials.
 *
 * <p>It is recommended to use {@link Credentials.Simple} in most cases.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public interface Credentials {

    /**
     * Test credentials, for unit testing mostly.
     */
    Credentials TEST = new Credentials.Simple(
        "AAAAAAAAAAAAAAAAAAAA",
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    );

    /**
     * Build AWS client.
     *
     * <p>Don't forget to shut it down after use,
     * using {@link AmazonSimpleDB#shutdown()}.
     *
     * @return Amazon Dynamo DB client
     */
    @NotNull
    AmazonSimpleDB aws();

    /**
     * Simple implementation.
     */
    @Immutable
    @Loggable(Loggable.DEBUG)
    @EqualsAndHashCode(of = { "key", "secret", "region" })
    final class Simple implements Credentials {
        /**
         * AWS key.
         */
        private final transient String key;
        /**
         * AWS secret.
         */
        private final transient String secret;
        /**
         * Region name.
         */
        private final transient String region;
        /**
         * Public ctor, with "us-east-1" region.
         * @param akey AWS key
         * @param scrt Secret
         */
        public Simple(@NotNull final String akey, @NotNull final String scrt) {
            this(akey, scrt, Regions.US_EAST_1.getName());
        }
        /**
         * Public ctor.
         * @param akey AWS key
         * @param scrt Secret
         * @param reg Region
         */
        public Simple(
            @NotNull(message = "AWS key can't be NULL") final String akey,
            @NotNull(message = "AWS secret can't be NULL") final String scrt,
            @NotNull(message = "AWS region can't be NULL") final String reg) {
            Validate.matchesPattern(
                akey, "[A-Z0-9]{20}",
                "Invalid AWS key '%s'", akey
            );
            this.key = akey;
            Validate.matchesPattern(
                scrt, "[a-zA-Z0-9+/=]{40}",
                "Invalid AWS secret key '%s'", scrt
            );
            this.secret = scrt;
            Validate.matchesPattern(
                reg, "[-a-z0-9]+",
                "Invalid AWS region name '%s'", reg
            );
            this.region = reg;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return String.format("%s/%s", this.region, this.key);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public AmazonSimpleDB aws() {
            final AmazonSimpleDB aws = new AmazonSimpleDBClient(
                new BasicAWSCredentials(this.key, this.secret)
            );
            final com.amazonaws.regions.Region reg =
                RegionUtils.getRegion(this.region);
            Validate.notNull(reg, "Failed to find region '%s'", this.region);
            aws.setRegion(reg);
            return aws;
        }
    }

    /**
     * Assumed AWS IAM role.
     *
     * @see <a href="http://docs.aws.amazon.com/IAM/latest/UserGuide/role-usecase-ec2app.html">Granting Applications that Run on Amazon EC2 Instances Access to AWS Resources</a>
     */
    @Immutable
    @Loggable(Loggable.DEBUG)
    @EqualsAndHashCode(of = "region")
    final class Assumed implements Credentials {
        /**
         * Region name.
         */
        private final transient String region;
        /**
         * Public ctor.
         */
        public Assumed() {
            this(Regions.US_EAST_1.getName());
        }
        /**
         * Public ctor.
         * @param reg Region
         */
        public Assumed(@NotNull(message = "SimpleDB region can't be NULL")
            final String reg) {
            Validate.matchesPattern(
                reg, "[-0-9a-z]+",
                "Invalid AWS region name: '%s'", reg
            );
            this.region = reg;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return this.region;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public AmazonSimpleDB aws() {
            final AmazonSimpleDB aws = new AmazonSimpleDBClient();
            final com.amazonaws.regions.Region reg =
                RegionUtils.getRegion(this.region);
            Validate.notNull(reg, "Failed to detect region '%s'", this.region);
            aws.setRegion(reg);
            return aws;
        }
    }

    /**
     * With explicitly specified endpoint.
     */
    @Immutable
    @Loggable(Loggable.DEBUG)
    @EqualsAndHashCode(of = { "origin", "endpoint" })
    final class Direct implements Credentials {
        /**
         * Original credentials.
         */
        private final transient Credentials origin;
        /**
         * Endpoint.
         */
        private final transient String endpoint;
        /**
         * Public ctor.
         * @param creds Original credentials
         * @param pnt Endpoint
         */
        public Direct(
            @NotNull(message = "credentials is NULL") final Credentials creds,
            @NotNull(message = "end point can't be NULL") final String pnt) {
            this.origin = creds;
            this.endpoint = pnt;
        }
        /**
         * Public ctor.
         * @param creds Original credentials
         * @param port Port number for localhost
         */
        public Direct(@NotNull final Credentials creds, final int port) {
            this(creds, String.format("http://localhost:%d", port));
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return String.format("%s at %s", this.origin, this.endpoint);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public AmazonSimpleDB aws() {
            final AmazonSimpleDB aws = this.origin.aws();
            aws.setEndpoint(this.endpoint);
            return aws;
        }
    }

}
