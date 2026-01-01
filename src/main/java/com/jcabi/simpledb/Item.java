/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.simpledb;

import com.jcabi.aspects.Immutable;
import java.util.Map;
import javax.validation.constraints.NotNull;

/**
 * Immutable Amazon SimpleDB item.
 *
 * <p>The class is immutable, which means that every call to
 * {@link #put(String,String)} changes
 * data in Amazon, but doesn't change the object. The object will contain
 * dirty data right after PUT operation, and should not be used anymore.
 *
 * @since 0.1
 */
@Immutable
public interface Item extends Map<String, String> {

    /**
     * Get its name.
     * @return Name of the item
     */
    @NotNull(message = "name is never NULL")
    String name();

}
