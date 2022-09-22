/**
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
import com.jcabi.aspects.Immutable;
import javax.validation.constraints.NotNull;

/**
 * Amazon SimpleDB domain abstraction.
 *
 * <p>Get an item from the domain and manipulate it, for example:
 *
 * <pre> Region region = new Region.Simple(...);
 * Domain domain = region.domain("employees");
 * Item employee = domain.item("324");
 * employee.put("Name", "Jeffrey Lebowski");
 * for (Map.Entry&lt;String, String&gt; attr : employee.entrySet()) {
 *   System.out.println(attr.getKey() + ": " + attr.getValue());
 * }</pre>
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public interface Domain {

    /**
     * Create it.
     */
    void create();

    /**
     * Drop it.
     */
    void drop();

    /**
     * Get its name.
     * @return Name of domain
     */
    @NotNull(message = "name is never NULL")
    String name();

    /**
     * Get item.
     * @param name Name of the item
     * @return Item just created
     */
    @NotNull(message = "item is never NULL")
    Item item(@NotNull String name);

    /**
     * Select multiple items.
     * @param request Select request
     * @return Items found
     */
    @NotNull(message = "collection of items is never NULL")
    Iterable<Item> select(@NotNull SelectRequest request);

}
