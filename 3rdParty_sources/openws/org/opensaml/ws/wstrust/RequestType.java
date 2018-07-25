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

package org.opensaml.ws.wstrust;

import javax.xml.namespace.QName;

import org.opensaml.xml.schema.XSURI;

/**
 * The wst:RequestType element.
 * 
 */
public interface RequestType extends XSURI, WSTrustObject {

    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "RequestType";

    /** Default element name. */
    public static final QName ELEMENT_NAME =
        new QName(WSTrustConstants.WST_NS, ELEMENT_LOCAL_NAME, WSTrustConstants.WST_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "RequestTypeOpenEnum"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSTrustConstants.WST_NS, TYPE_LOCAL_NAME, WSTrustConstants.WST_PREFIX);

    // Single action request types
    
    /** RequestType Issue URI. */
    public static final String ISSUE = WSTrustConstants.WST_NS + "/Issue";

    /** RequestType Renew URI. */
    public static final String RENEW = WSTrustConstants.WST_NS + "/Renew";

    /** RequestType Cancel URI. */
    public static final String CANCEL = WSTrustConstants.WST_NS + "/Cancel";

    /** RequestType STSCancel URI. */
    public static final String STSCANCEL = WSTrustConstants.WST_NS + "/STSCancel";

    /** RequestType Validate URI. */
    public static final String VALIDATE = WSTrustConstants.WST_NS + "/Validate";
    
    /** RequestType Key Exchange Token (KET) URI. */
    public static final String KET = WSTrustConstants.WST_NS + "/KET";
    
    // Batch action request types
    
    /** RequestType BatchIssue URI. */
    public static final String BATCH_ISSUE = WSTrustConstants.WST_NS + "/BatchIssue";

    /** RequestType BatchRenew URI. */
    public static final String BATCH_RENEW = WSTrustConstants.WST_NS + "/BatchRenew";

    /** RequestType BatchCancel URI. */
    public static final String BATCH_CANCEL = WSTrustConstants.WST_NS + "/BatchCancel";

    /** RequestType BatchValidate URI. */
    public static final String BATCH_VALIDATE = WSTrustConstants.WST_NS + "/BatchValidate";

}
