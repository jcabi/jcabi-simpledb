/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.simpledb;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.hamcrest.MockitoHamcrest;

/**
 * Test case for {@link AwsItem}.
 *
 * @since 0.1
 */
final class AwsItemTest {

    @Test
    void loadsItemFromSimpleDb() {
        final AmazonSimpleDB aws = Mockito.mock(AmazonSimpleDB.class);
        Mockito.doReturn(
            new GetAttributesResult().withAttributes(
                new Attribute().withName("attr-1").withValue("value-1")
            )
        ).when(aws).getAttributes(ArgumentMatchers.any(GetAttributesRequest.class));
        final Credentials credentials = Mockito.mock(Credentials.class);
        Mockito.doReturn(aws).when(credentials).aws();
        final String name = "item-name";
        final String table = "table-name";
        final Item item = new AwsItem(credentials, table, name);
        item.get("hello");
        Mockito.verify(aws).getAttributes(
            GetAttributesRequest.class.cast(
                MockitoHamcrest.argThat(
                    Matchers.allOf(
                        Matchers.hasProperty(
                            "domainName",
                            Matchers.equalTo(table)
                        ),
                        Matchers.hasProperty(
                            "itemName",
                            Matchers.equalTo(name)
                        )
                    )
                )
            )
        );
    }

}
