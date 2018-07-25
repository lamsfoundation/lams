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

package org.opensaml.xacml.ctx;

import javax.xml.namespace.QName;

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.XACMLObject;

/** XACML context Status schema type. */
public interface StatusType extends XACMLObject {

    /** Local name of the StatusCode element. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Status";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20CTX_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, XACMLConstants.XACMLCONTEXT_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "StatusType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(XACMLConstants.XACML20CTX_NS, TYPE_LOCAL_NAME,
            XACMLConstants.XACMLCONTEXT_PREFIX);

    /**
     * Gets the status code of status.
     * 
     * @return The status code of status
     */
    public StatusCodeType getStatusCode();

    /**
     * Gets the status detail of status.
     * 
     * @return The status detail of status
     */
    public StatusDetailType getStatusDetail();

    /**
     * Gets the status message of status.
     * 
     * @return The status message of status
     */
    public StatusMessageType getStatusMessage();

    /**
     * Sets the status code for the Status.
     * 
     * @param newStatusCode The new status code
     */
    public void setStatusCode(StatusCodeType newStatusCode);

    /**
     * Sets the status detail for the Status.
     * 
     * @param statusDetail The new status message
     */
    public void setStatusDetail(StatusDetailType statusDetail);

    /**
     * Sets the status message for the Status.
     * 
     * @param newStatusMessage The new status message
     */
    public void setStatusMessage(StatusMessageType newStatusMessage);

}
