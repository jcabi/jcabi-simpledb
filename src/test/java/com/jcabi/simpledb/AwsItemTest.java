/*
 * Copyright (c) 2012-2025, jcabi.com
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
