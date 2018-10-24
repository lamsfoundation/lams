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

package org.opensaml.saml2.metadata;

/**
 * A type safe enumeration of contact types used by {@link org.opensaml.saml2.metadata.ContactPerson}.
 */
public final class ContactPersonTypeEnumeration {

    /** "technical" contact type */
    public static final ContactPersonTypeEnumeration TECHNICAL = new ContactPersonTypeEnumeration("technical");

    /** "support" contact type */
    public static final ContactPersonTypeEnumeration SUPPORT = new ContactPersonTypeEnumeration("support");

    /** "administrative" contact type */
    public static final ContactPersonTypeEnumeration ADMINISTRATIVE = new ContactPersonTypeEnumeration("administrative");

    /** "billing" contact type */
    public static final ContactPersonTypeEnumeration BILLING = new ContactPersonTypeEnumeration("billing");

    /** "other" contact type */
    public static final ContactPersonTypeEnumeration OTHER = new ContactPersonTypeEnumeration("other");

    /** the contact type */
    private String type;

    /**
     * Constructor
     * 
     * @param type the contact type
     */
    protected ContactPersonTypeEnumeration(String type) {
        this.type = type;
    }

    /**
     * Gets the contact type as a string.
     * 
     * @return the contact type
     */
    public String toString() {
        return type;
    }
}