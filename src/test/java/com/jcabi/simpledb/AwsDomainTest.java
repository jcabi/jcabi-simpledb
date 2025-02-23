/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.simpledb;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link AwsDomain}.
 *
 * @since 0.1
 */
final class AwsDomainTest {

    @Test
    void makesAnItem() {
        final Credentials credentials = Mockito.mock(Credentials.class);
        final Domain table = new AwsDomain(credentials, "test");
        final Item item = table.item("x");
        MatcherAssert.assertThat(item, Matchers.notNullValue());
    }

}
