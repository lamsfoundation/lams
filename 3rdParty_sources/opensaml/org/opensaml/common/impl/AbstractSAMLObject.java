/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.common.impl;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * An abstract implementation of SAMLObject.
 */
public abstract class AbstractSAMLObject extends AbstractValidatingXMLObject {

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AbstractSAMLObject(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public final boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        
        return super.equals(obj);
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * A helper function for derived classes that checks to see if the old and new value are equal and if so releases
     * the cached dom. Derived classes are expected to use this thus: <code>
     *   this.foo = prepareForAssignment(this.foo, foo);
     *   </code>
     * 
     * This method will do a (null) safe compare of the objects and will also invalidate the DOM if appropriate
     * 
     * @param oldValue - current value
     * @param newValue - proposed new value
     * 
     * @return The value to assign to the saved Object.
     */
    protected DateTime prepareForAssignment(DateTime oldValue, DateTime newValue) {
        DateTime utcValue = null;
        if (newValue != null) {
            utcValue = newValue.withZone(DateTimeZone.UTC);
        }

        return super.prepareForAssignment(oldValue, utcValue);
    }
}