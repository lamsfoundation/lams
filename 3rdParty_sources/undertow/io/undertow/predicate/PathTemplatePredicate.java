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

import io.undertow.attribute.ExchangeAttribute;
import io.undertow.attribute.ExchangeAttributes;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.PathTemplate;

/**
 * @author Stuart Douglas
 */
public class PathTemplatePredicate implements Predicate {

    private final ExchangeAttribute attribute;
    private final PathTemplate value;

    public PathTemplatePredicate(final String template, final ExchangeAttribute attribute) {
        this.attribute = attribute;
        this.value = PathTemplate.create(template);
    }

    @Override
    public boolean resolve(final HttpServerExchange exchange) {
        final Map<String, String> params = new HashMap<>();
        String path = attribute.readAttribute(exchange);
        if(path == null) {
            return false;
        }
        boolean result = this.value.matches(path, params);
        if (result) {
            Map<String, Object> context = exchange.getAttachment(PREDICATE_CONTEXT);
            if (context != null) {
                context.putAll(params);
            }
        }
        return result;
    }

    public static class Builder implements PredicateBuilder {

        @Override
        public String name() {
            return "path-template";
        }

        @Override
        public Map<String, Class<?>> parameters() {
            final Map<String, Class<?>> params = new HashMap<>();
            params.put("value", String.class);
            params.put("match", ExchangeAttribute.class);
            return params;
        }

        @Override
        public Set<String> requiredParameters() {
            final Set<String> params = new HashSet<>();
            params.add("value");
            return params;
        }

        @Override
        public String defaultParameter() {
            return "value";
        }

        @Override
        public Predicate build(final Map<String, Object> config) {
            ExchangeAttribute match = (ExchangeAttribute) config.get("match");
            if (match == null) {
                match = ExchangeAttributes.relativePath();
            }
            String value = (String) config.get("value");
            return new PathTemplatePredicate(value, match);
        }
    }

}
