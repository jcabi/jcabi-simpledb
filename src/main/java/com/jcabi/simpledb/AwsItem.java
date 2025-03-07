/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.simpledb;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.EqualsAndHashCode;

/**
 * Single item/row in a SimpleDB table.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCoupling (500 lines)
 */
@Immutable
@Loggable(Loggable.DEBUG)
@EqualsAndHashCode(of = { "credentials", "table", "label" })
@SuppressWarnings("PMD.TooManyMethods")
final class AwsItem implements Item {

    /**
     * AWS credentials.
     */
    private final transient Credentials credentials;

    /**
     * Table name.
     */
    private final transient String table;

    /**
     * Table name.
     */
    private final transient String label;

    /**
     * Public ctor.
     * @param creds Credentials
     * @param tbl Table name
     * @param item Item name
     */
    AwsItem(final Credentials creds, final String tbl,
        final String item) {
        this.credentials = creds;
        this.table = tbl;
        this.label = item;
    }

    /**
     * Public ctor.
     * @param creds Credentials
     * @param tbl Table name
     * @param item Item name
     */
    AwsItem(final Credentials creds, final String tbl,
        final com.amazonaws.services.simpledb.model.Item item) {
        this(creds, tbl, item.getName());
    }

    @Override
    public String toString() {
        return String.format("%s in %s", this.label, this.table);
    }

    @Override
    public String name() {
        return this.label;
    }

    @Override
    public int size() {
        return this.entrySet().size();
    }

    @Override
    public boolean isEmpty() {
        return this.entrySet().isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.containsValue(value);
    }

    @Override
    public String get(final Object key) {
        final Set<Map.Entry<String, String>> entries = this.entrySet();
        String value = null;
        for (final Map.Entry<String, String> entry : entries) {
            if (entry.getKey().equals(key)) {
                value = entry.getValue();
            }
        }
        return value;
    }

    @Override
    public String put(final String key, final String value) {
        final String before = this.get(key);
        final ConcurrentMap<String, String> map =
            new ConcurrentHashMap<>(0);
        map.put(key, value);
        this.putAll(map);
        return before;
    }

    @Override
    public String remove(final Object key) {
        final String before = this.get(key);
        this.credentials.aws().deleteAttributes(
            new DeleteAttributesRequest()
                .withDomainName(this.table)
                .withItemName(this.label)
                .withAttributes(new Attribute().withName(key.toString()))
        );
        return before;
    }

    @Override
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public void putAll(final Map<? extends String, ? extends String> map) {
        final Collection<ReplaceableAttribute> attrs =
            new ArrayList<>(map.size());
        for (final Map.Entry<?, ?> entry : map.entrySet()) {
            attrs.add(
                new ReplaceableAttribute()
                    .withName(entry.getKey().toString())
                    .withValue(entry.getValue().toString())
                    .withReplace(true)
            );
        }
        this.credentials.aws().putAttributes(
            new PutAttributesRequest()
                .withDomainName(this.table)
                .withItemName(this.label)
                .withAttributes(attrs)
        );
    }

    @Override
    public void clear() {
        this.credentials.aws().deleteAttributes(
            new DeleteAttributesRequest()
                .withDomainName(this.table)
                .withItemName(this.label)
        );
    }

    @Override
    public Set<String> keySet() {
        final Set<Map.Entry<String, String>> entries = this.entrySet();
        final Set<String> keys = new HashSet<>(entries.size());
        for (final Map.Entry<String, String> entry : entries) {
            keys.add(entry.getValue());
        }
        return keys;
    }

    @Override
    public Collection<String> values() {
        final Set<Map.Entry<String, String>> entries = this.entrySet();
        final Collection<String> values = new ArrayList<>(entries.size());
        for (final Map.Entry<String, String> entry : entries) {
            values.add(entry.getValue());
        }
        return values;
    }

    @Override
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public Set<Map.Entry<String, String>> entrySet() {
        final GetAttributesResult result = this.credentials.aws().getAttributes(
            new GetAttributesRequest()
                .withConsistentRead(true)
                .withDomainName(this.table)
                .withItemName(this.label)
        );
        final Set<Map.Entry<String, String>> entries =
            new HashSet<>(0);
        for (final Attribute attr : result.getAttributes()) {
            entries.add(
                new AbstractMap.SimpleImmutableEntry<>(
                    attr.getName(), attr.getValue()
                )
            );
        }
        return entries;
    }

}
