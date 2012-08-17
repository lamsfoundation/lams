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

package org.opensaml.ws.wspolicy;

import javax.xml.namespace.QName;

import org.opensaml.xml.AttributeExtensibleXMLObject;

/**
 * The wsp:PolicyReference element.
 * 
 * @see "WS-Policy (http://schemas.xmlsoap.org/ws/2004/09/policy)"
 * 
 */
public interface PolicyReference extends AttributeExtensibleXMLObject, WSPolicyObject {

    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "PolicyReference";

    /** Default element name. */
    public static final QName ELEMENT_NAME =
        new QName(WSPolicyConstants.WSP_NS, ELEMENT_LOCAL_NAME, WSPolicyConstants.WSP_PREFIX);

    /** The wsp:PolicyReference/@URI attribute local name. */
    public static final String URI_ATTRIB_NAME = "URI";

    /** The wsp:PolicyReference/@Digest attribute local name. */
    public static final String DIGEST_ATTRIB_NAME = "Digest";

    /** The wsp:PolicyReference/@Digest attribute local name. */
    public static final String DIGEST_ALGORITHM_ATTRIB_NAME = "DigestAlgorithm";

    /** The default wsp:PolicyReference/@DigestAlgorithm attribute value. */
    public static final String DIGEST_ALGORITHM_SHA1EXC = WSPolicyConstants.WSP_NS + "/Sha1Exc";

    /**
     * Returns the wsp:PolicyReference/@URI attribute value.
     * 
     * @return the URI attribute value.
     */
    public String getURI();

    /**
     * Sets the wsp:PolicyReference/@URI attribute value.
     * 
     * @param uri the URI attribute value to set.
     */
    public void setURI(String uri);

    /**
     * Returns the wsp:PolicyReference/@Digest attribute URI value.
     * 
     * @return the Digest attribute URI value.
     */
    public String getDigest();

    /**
     * Sets the wsp:PolicyReference/@Digest attribute URI value.
     * 
     * @param digest the Digest attribute URI value to set.
     */
    public void setDigest(String digest);

    /**
     * Returns the wsp:PolicyReference/@DigestAlgoritm attribute Base64 binary value.
     * 
     * @return the DigestAlgoritm attribute Base64 binary value.
     */
    public String getDigestAlgorithm();

    /**
     * Sets the wsp:PolicyReference/@DigestAlgoritm attribute Base64 binary value.
     * 
     * @param digestAlgorithm the DigestAlgoritm attribute Base64 binary value to set.
     */
    public void setDigestAlgorithm(String digestAlgorithm);

}
