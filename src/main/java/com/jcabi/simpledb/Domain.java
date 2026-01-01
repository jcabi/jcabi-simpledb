/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
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
