/* $Id: BeanPropertySetter.java 992750 2010-09-05 09:54:37Z simonetripodi $
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.digester.annotations.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.digester.BeanPropertySetterRule;
import org.apache.commons.digester.annotations.DigesterRule;
import org.apache.commons.digester.annotations.DigesterRuleList;
import org.apache.commons.digester.annotations.providers.BeanPropertySetterRuleProvider;

/**
 * Fields annotated with {@code BeanPropertySetter} will be bound with
 * {@code BeanPropertySetterRule} digester rule.
 *
 * @see org.apache.commons.digester.Digester#addBeanPropertySetter(String,String)
 * @since 2.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@DigesterRule(
        reflectsRule = BeanPropertySetterRule.class,
        providedBy = BeanPropertySetterRuleProvider.class
)
public @interface BeanPropertySetter {

    /**
     * The element matching pattern.
     *
     * @return the element matching pattern.
     */
    String pattern();

    /**
     * Defines several {@code @BeanPropertySetter} annotations on the same element.
     *
     * @see BeanPropertySetter
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @DigesterRuleList
    @interface List {
        BeanPropertySetter[] value();
    }

}
