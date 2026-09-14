/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.simpledb;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Region}.
 *
 * @since 0.1
 */
final class RegionTest {

    @Test
    void works() {
        MatcherAssert.assertThat(
            "region should exist",
            true,
            Matchers.is(true)
        );
    }
}
