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

/**
 * 
 */

package org.opensaml.samlext.samlec;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.ws.soap.soap11.ActorBearing;
import org.opensaml.ws.soap.soap11.MustUnderstandBearing;
import org.opensaml.xml.schema.XSBase64Binary;

/**
 * SAML-EC GSS-API GeneratedKey element.
 */
public interface GeneratedKey extends XSBase64Binary, SAMLObject, MustUnderstandBearing, ActorBearing {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "GeneratedKey";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAMLEC_GSS_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAMLEC_GSS_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "GeneratedKeyType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAMLEC_GSS_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAMLEC_GSS_PREFIX);
}