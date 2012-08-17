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

package org.opensaml.saml2.core;

/**
 * SAML 2.0 Assertion NameID schema type.
 */
public interface NameIDType {

    /** NameQualifier attribute name. */
    public static final String NAME_QUALIFIER_ATTRIB_NAME = "NameQualifier";

    /** SPNameQualifier attribute name. */
    public static final String SP_NAME_QUALIFIER_ATTRIB_NAME = "SPNameQualifier";

    /** Format attribute name. */
    public static final String FORMAT_ATTRIB_NAME = "Format";

    /** SPProviderID attribute name. */
    public static final String SPPROVIDED_ID_ATTRIB_NAME = "SPProvidedID";

    /** URI for unspecified name format. */
    public static final String UNSPECIFIED = "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified";

    /** URI for email name format. */
    public static final String EMAIL = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress";

    /** URI for X509 subject name format. */
    public static final String X509_SUBJECT = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";

    /** URI for windows domain qualified name name format. */
    public static final String WIN_DOMAIN_QUALIFIED = "urn:oasis:names:tc:SAML:1.1:nameid-format:WindowsDomainQualifiedName";

    /** URI for kerberos name format. */
    public static final String KERBEROS = "urn:oasis:names:tc:SAML:2.0:nameid-format:kerberos";

    /** URI for SAML entity name format. */
    public static final String ENTITY = "urn:oasis:names:tc:SAML:2.0:nameid-format:entity";

    /** URI for persistent name format. */
    public static final String PERSISTENT = "urn:oasis:names:tc:SAML:2.0:nameid-format:persistent";

    /** URI for transient name format. */
    public static final String TRANSIENT = "urn:oasis:names:tc:SAML:2.0:nameid-format:transient";

    /** Special URI used by NameIDPolicy to indicate a NameID should be encrypted. */
    public static final String ENCRYPTED = "urn:oasis:names:tc:SAML:2.0:nameid-format:encrypted";

    /**
     * Gets the value of this type.
     * 
     * @return the value of this type
     */
    public String getValue();

    /**
     * Sets the value of this type.
     * 
     * @param newValue the value of this type
     */
    public void setValue(String newValue);

    /**
     * Gets the NameQualifier value.
     * 
     * @return the NameQualifier value
     */
    public String getNameQualifier();

    /**
     * Sets the NameQualifier value.
     * 
     * @param newNameQualifier the NameQualifier value
     */
    public void setNameQualifier(String newNameQualifier);

    /**
     * Gets the SPNameQualifier value.
     * 
     * @return the SPNameQualifier value
     */
    public String getSPNameQualifier();

    /**
     * Sets the SPNameQualifier value.
     * 
     * @param newSPNameQualifier the SPNameQualifier value
     */
    public void setSPNameQualifier(String newSPNameQualifier);

    /**
     * Gets the format of the NameID.
     * 
     * @return the format of the NameID
     */
    public String getFormat();

    /**
     * Sets the format of the NameID.
     * 
     * @param newFormat the format of the NameID
     */
    public void setFormat(String newFormat);

    /**
     * Gets the SPProvidedID of this NameID.
     * 
     * @return the SPProvidedID of this NameID
     */
    public String getSPProvidedID();

    /**
     * Sets the SPProvddedID of this NameID.
     * 
     * @param newSPProvidedID the SPProvidedID of this NameID
     */
    public void setSPProvidedID(String newSPProvidedID);

}