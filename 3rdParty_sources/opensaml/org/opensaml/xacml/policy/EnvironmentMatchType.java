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

import javax.xml.namespace.QName;

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.XACMLObject;

/** XACML EnvironmentMatch schema type. */
public interface EnvironmentMatchType extends XACMLObject {

    /** Local name of the element EnvironmentMatch. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "EnvironmentMatch";

    /** QName of the element EnvironmentMatch. */
    public static final QName DEFAULT_ELEMENT_QNAME = new QName(XACMLConstants.XACML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "EnvironmentMatchType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);
    
    /** MatchID attribute name. */
    public static final String MATCH_ID_ATTRIB_NAME = "MatchId";

    /**
     * Gets the attribute value for this match.
     * 
     * @return attribute value for this match
     */
    public AttributeValueType getAttributeValue();

    /**
     * Sets the attribute value for this match.
     * 
     * @param value attribute value for this match
     */
    public void setAttributeValue(AttributeValueType value);

    /**
     * Gets the environment attribute designator for this match.
     * 
     * @return environment attribute designator for this match
     */
    public AttributeDesignatorType getEnvironmentAttributeDesignator();

    /**
     * Sets the environment attribute designator for this match.
     * 
     * @param attribute environment attribute designator for this match
     */
    public void setEnvironmentAttributeDesignator(AttributeDesignatorType attribute);

    /**
     * Gets the attribute selector for this match.
     * 
     * @return attribute selector for this match
     */
    public AttributeSelectorType getAttributeSelector();

    /**
     * Sets the attribute selector for this match.
     * 
     * @param selector attribute selector for this match
     */
    public void setAttributeSelector(AttributeSelectorType selector);
    
    /**
     * Gets the ID of this match.
     * 
     * @return ID of this match
     */
    public String getMatchId();
    
    /**
     * Sets the ID of this match.
     * 
     * @param id ID of this match
     */
    public void setMatchId(String id);
}
