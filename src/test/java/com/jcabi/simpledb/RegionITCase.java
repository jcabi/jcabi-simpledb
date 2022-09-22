/*
 * Copyright (c) 2012-2022, jcabi.com
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
import com.jcabi.aspects.Tv;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

/**
 * Integration case for {@link Region}.
 *
 * @since 0.1
 */
final class RegionITCase {

    /**
     * SimpleDB key.
     */
    private static final String KEY =
        System.getProperty("failsafe.sdb.key");

    /**
     * SimpleDB secret key.
     */
    private static final String SECRET =
        System.getProperty("failsafe.sdb.secret");

    @Test
    void putsAndRemovesIndividualItems() {
        final Domain domain = this.domain();
        try {
            final String name = RandomStringUtils.randomAlphanumeric(Tv.TEN);
            final String attr = RandomStringUtils.randomAlphabetic(Tv.EIGHT);
            final String value = RandomStringUtils.randomAlphanumeric(Tv.TEN);
            for (int idx = 0; idx < 2; ++idx) {
                domain.item(name).put(attr, value);
                MatcherAssert.assertThat(
                    domain.item(name), Matchers.hasKey(attr)
                );
                domain.item(name).remove(attr);
                MatcherAssert.assertThat(
                    domain.item(name), Matchers.not(Matchers.hasKey(attr))
                );
            }
        } finally {
            domain.drop();
        }
    }

    @Test
    void selectsMultipleItems() {
        final Domain domain = this.domain();
        try {
            final String attr = "alpha";
            domain.item("first").put(attr, "val-99");
            domain.item("second").put("beta", "");
            MatcherAssert.assertThat(
                domain.select(
                    new SelectRequest().withSelectExpression(
                        String.format(
                            "SELECT * FROM `%s` WHERE `%s` = 'val-99'",
                            domain.name(), attr
                        )
                    ).withConsistentRead(true)
                ),
                Matchers.hasItem(Matchers.hasKey(attr))
            );
        } finally {
            domain.drop();
        }
    }

    /**
     * Region.Simple can select many items.
     */
    @Test
    void selectsManyItems() {
        final Domain domain = this.domain();
        try {
            for (int idx = 0; idx < Tv.TEN; ++idx) {
                domain.item(String.format("i-%d", idx)).put("hey", "");
            }
            MatcherAssert.assertThat(
                domain.select(
                    new SelectRequest().withSelectExpression(
                        String.format("SELECT * FROM `%s`", domain.name())
                    ).withConsistentRead(true)
                ),
                Matchers.iterableWithSize(Tv.TEN)
            );
        } finally {
            domain.drop();
        }
    }

    /**
     * Make domain.
     * @return Domain
     */
    private Domain domain() {
        Assumptions.assumeFalse(RegionITCase.KEY.isEmpty());
        final Region region = new Region.Simple(
            new Credentials.Simple(RegionITCase.KEY, RegionITCase.SECRET)
        );
        final Domain domain = region.domain(
            String.format(
                "jcabi-test-%s",
                RandomStringUtils.randomAlphabetic(5)
            )
        );
        domain.create();
        return domain;
    }

}
