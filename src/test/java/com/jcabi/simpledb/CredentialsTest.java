/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.simpledb;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Credentials}.
 *
 * @since 0.1
 */
final class CredentialsTest {

    @Test
    @SuppressWarnings("PMD.UnnecessaryLocalRule")
    void instantiatesAwsClient() {
        final Credentials creds = new Credentials.Simple(
            "ABABABABABABABABABEF",
            "ABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCEF"
        );
        MatcherAssert.assertThat("should be not null", creds.aws(), Matchers.notNullValue());
    }

    @Test
    @SuppressWarnings("PMD.UnnecessaryLocalRule")
    void instantiatesAwsClientWithCustomRegion() {
        final Credentials creds = new Credentials.Simple(
            "ABABABABABABABABABAB",
            "ABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDE",
            "eu-west-1"
        );
        MatcherAssert.assertThat("should be not null", creds.aws(), Matchers.notNullValue());
    }

    @Test
    void doesNotLeakSecretInValidationError() {
        final String secret = "this-secret-must-never-end-up-in-logs!!!";
        MatcherAssert.assertThat(
            "secret value must not be echoed in the exception message",
            Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Credentials.Simple("ABABABABABABABABABAB", secret),
                "invalid AWS secret must be rejected"
            ).getMessage(),
            Matchers.not(Matchers.containsString(secret))
        );
    }
}
