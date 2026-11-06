/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.simpledb;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import java.util.Arrays;
import java.util.stream.StreamSupport;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link AwsIterator}.
 *
 * @since 1.0
 */
final class AwsIteratorTest {

    @Test
    void traversesEmptyPages() {
        final AmazonSimpleDB client = Mockito.mock(AmazonSimpleDB.class);
        Mockito.when(client.select(Mockito.any(SelectRequest.class))).thenReturn(
            new SelectResult().withNextToken("first"),
            new SelectResult().withNextToken("second"),
            new SelectResult().withItems(
                new Item().withName("found")
            )
        );
        MatcherAssert.assertThat(
            "must follow tokens through consecutive empty pages",
            new AwsIterator(() -> client, "table", new SelectRequest()).next().name(),
            Matchers.equalTo("found")
        );
    }

    @Test
    void preservesCallerToken() {
        final AmazonSimpleDB client = Mockito.mock(AmazonSimpleDB.class);
        Mockito.when(client.select(Mockito.any(SelectRequest.class))).thenReturn(
            new SelectResult().withItems(
                new Item().withName("initial")
            ).withNextToken("later"),
            new SelectResult()
        );
        final SelectRequest request = new SelectRequest().withNextToken("original");
        final AwsIterator iterator = new AwsIterator(() -> client, "table", request);
        iterator.next();
        while (iterator.hasNext()) {
            iterator.next();
        }
        MatcherAssert.assertThat(
            "pagination must preserve the caller's initial token",
            request.getNextToken(), Matchers.equalTo("original")
        );
    }

    @Test
    void startsEachIterationFromTheOriginalRequest() {
        final AmazonSimpleDB client = Mockito.mock(AmazonSimpleDB.class);
        Mockito.when(client.select(Mockito.any(SelectRequest.class))).thenAnswer(
            call -> {
                final SelectRequest request = call.getArgument(0);
                final SelectResult result = new SelectResult();
                if (request.getNextToken() == null) {
                    result.withItems(
                        new Item().withName("start")
                    ).withNextToken("end");
                }
                return result;
            }
        );
        final Iterable<?> items = new AwsDomain(() -> client, "table")
            .select(new SelectRequest());
        MatcherAssert.assertThat(
            "each new iterator must include the initial page",
            Arrays.asList(
                StreamSupport.stream(items.spliterator(), false).count(),
                StreamSupport.stream(items.spliterator(), false).count()
            ),
            Matchers.contains(1L, 1L)
        );
    }
}
