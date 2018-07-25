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
import org.opensaml.xacml.policy.ObligationsType;

/** XACML context Result schema type. */
public interface ResultType extends XACMLObject {

    /** Local name of the element. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Result";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20CTX_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, XACMLConstants.XACMLCONTEXT_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "ResultType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(XACMLConstants.XACML20CTX_NS, TYPE_LOCAL_NAME,
            XACMLConstants.XACMLCONTEXT_PREFIX);

    /** ResourceId attribute name. */
    public static final String RESOURCE_ID_ATTTRIB_NAME = "ResourceId";

    /**
     * Returns the decision in the result.
     * 
     * @return XACMLDecision the decision in the result
     */
    public DecisionType getDecision();

    /**
     * Returns the list of Obligations in the result.
     * 
     * @return the list of Obligations in the result
     */
    public ObligationsType getObligations();

    /**
     * Sets the obligations for this result.
     * 
     * @param obligations obligations for this result
     */
    public void setObligations(ObligationsType obligations);

    /**
     * Gets the ResourceId of the result.
     * 
     * @return The ResourceId of the subject
     */
    public String getResourceId();

    /**
     * Returns the status in the result.
     * 
     * @return the status in the result
     */
    public StatusType getStatus();

    /**
     * Sets the result status.
     * 
     * @param status result status
     */
    public void setStatus(StatusType status);

    /**
     * Sets the decision in the result.
     * 
     * @param newDecision The decision in the result
     */
    public void setDecision(DecisionType newDecision);

    /**
     * Sets the ResourceId.
     * 
     * @param resourceId is the ResourceId
     */
    public void setResourceId(String resourceId);
}
