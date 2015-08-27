/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.undertow.predicate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.undertow.attribute.ExchangeAttribute;
import io.undertow.attribute.ExchangeAttributes;
import io.undertow.server.HttpServerExchange;

/**
 * A predicate that does a regex match against an exchange.
 * <p/>
 * <p/>
 * By default this match is done against the relative URI, however it is possible to set it to match against other
 * exchange attributes.
 *
 * @author Stuart Douglas
 */
public class RegularExpressionPredicate implements Predicate {

    private final Pattern pattern;
    private final ExchangeAttribute matchAttribute;
    private final boolean requireFullMatch;

    public RegularExpressionPredicate(final String regex, final ExchangeAttribute matchAttribute, final boolean requireFullMatch) {
        this.requireFullMatch = requireFullMatch;
        pattern = Pattern.compile(regex);
        this.matchAttribute = matchAttribute;
    }

    public RegularExpressionPredicate(final String regex, final ExchangeAttribute matchAttribute) {
        this(regex, matchAttribute, false);
    }

    @Override
    public boolean resolve(final HttpServerExchange value) {
        String input = matchAttribute.readAttribute(value);
        if(input == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(input);
        final boolean matches;
        if (requireFullMatch) {
            matches = matcher.matches();
        } else {
            matches = matcher.find();
        }

        if (matches) {
            Map<String, Object> context = value.getAttachment(PREDICATE_CONTEXT);
            if (context != null) {
                int count = matcher.groupCount();
                for (int i = 0; i <= count; ++i) {
                    context.put(Integer.toString(i), matcher.group(i));
                }
            }
        }
        return matches;
    }

    public static class Builder implements PredicateBuilder {

        @Override
        public String name() {
            return "regex";
        }

        @Override
        public Map<String, Class<?>> parameters() {
            final Map<String, Class<?>> params = new HashMap<>();
            params.put("pattern", String.class);
            params.put("value", ExchangeAttribute.class);
            params.put("full-match", Boolean.class);
            return params;
        }

        @Override
        public Set<String> requiredParameters() {
            final Set<String> params = new HashSet<>();
            params.add("pattern");
            return params;
        }

        @Override
        public String defaultParameter() {
            return "pattern";
        }

        @Override
        public Predicate build(final Map<String, Object> config) {
            ExchangeAttribute value = (ExchangeAttribute) config.get("value");
            if(value == null) {
                value = ExchangeAttributes.relativePath();
            }
            Boolean fullMatch = (Boolean) config.get("full-match");
            String pattern = (String) config.get("pattern");
            return new RegularExpressionPredicate(pattern, value, fullMatch == null ? false : fullMatch);
        }
    }
}
