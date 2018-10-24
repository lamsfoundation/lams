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

package org.opensaml.saml1.core;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.SignableSAMLObject;

/**
 * This interface defines the base class for type derived from the SAML1 <code> ResponseAbstractType </code> .
 */
public interface ResponseAbstractType extends SignableSAMLObject {

    /** Name for the attribute which defines InResponseTo. */
    public final static String INRESPONSETO_ATTRIB_NAME = "InResponseTo";

    /** Name for the attribute which defines the Major Version (which must be "1". */
    public final static String MAJORVERSION_ATTRIB_NAME = "MajorVersion";

    /** Name for the attribute which defines the Minor Version. */
    public final static String MINORVERSION_ATTRIB_NAME = "MinorVersion";

    /** Name for the attribute which defines the Issue Instant. */
    public final static String ISSUEINSTANT_ATTRIB_NAME = "IssueInstant";

    /** Name for the attribute which defines the Recipient. */
    public final static String RECIPIENT_ATTRIB_NAME = "Recipient";

    /** Name for the attribute which defines the Issue Instant. */
    public final static String ID_ATTRIB_NAME = "ResponseID";

   /** Return the InResponseTo (attribute). */
    String getInResponseTo();

    /** Set the InResponseTo (attribute). */
    void setInResponseTo(String who);

    /** Get the ID */
    public String getID();
    
    /** Set the ID */
    public void setID(String id);
    
    /** Return the Minor Version (attribute). */
    public int getMinorVersion();
    
    /**
     * Gets the major version of this SAML message.
     * 
     * @return the major version of this SAML message
     */
    public int getMajorVersion();
    
    /**
     * Sets the SAML version for this message.
     * 
     * @param version the SAML version for this message
     */
    public void setVersion(SAMLVersion version);

    /** Return the Issue Instant (attribute). */
    public DateTime getIssueInstant();

    /** Set the Issue Instant (attribute). */
    public void setIssueInstant(DateTime date);

    /** Return the Recipient (attribute). */
    public String getRecipient();

    /** Set the Recipient (attribute). */
    public void setRecipient(String recipient);
}