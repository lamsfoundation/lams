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

package org.opensaml.xacml.policy;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.XACMLObject;

/** XACML Obligation schema type. */
public interface ObligationType extends XACMLObject {

    /** Local name of the element Obligation. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Obligation";

    /** QName of the element Obligation. */
    public static final QName DEFAULT_ELEMENT_QNAME = new QName(XACMLConstants.XACML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "ObligationType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** ObligationId attribute name. */
    public static final String OBLIGATION_ID_ATTRIB_NAME = "ObligationId";

    /** FulfillOn attribute name. */
    public static final String FULFILL_ON_ATTRIB_NAME = "FulfillOn";

    /**
     * Gets the attribute assignments for this obligation.
     * 
     * @return attribute assignments for this obligation
     */
    public List<AttributeAssignmentType> getAttributeAssignments();

    /**
     * Gets the ID of this obligation.
     * 
     * @return ID of this obligation
     */
    public String getObligationId();

    /**
     * Sets the ID of this obligation.
     * 
     * @param id ID of this obligation
     */
    public void setObligationId(String id);

    /**
     * Gets the fulfill on effect.
     * 
     * @return fulfill on effect
     */
    public EffectType getFulfillOn();

    /**
     * Sets fulfill on effect.
     * 
     * @param type fulfill on effect
     */
    public void setFulfillOn(EffectType type);
}