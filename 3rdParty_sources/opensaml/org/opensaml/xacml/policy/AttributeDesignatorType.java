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
import org.opensaml.xml.schema.XSBooleanValue;

/** XACML AttribtueDesignator schema type. */
public interface AttributeDesignatorType extends ExpressionType {

    /** Local name of the element SubjectAttributeDesignator. */
    public static final String SUBJECT_ATTRIBUTE_DESIGNATOR_ELEMENT_LOCAL_NAME = "SubjectAttributeDesignator";

    /** QName of the element SubjectAttributeDesignator. */
    public static final QName SUBJECT_ATTRIBUTE_DESIGNATOR_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS,
            SUBJECT_ATTRIBUTE_DESIGNATOR_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the element ResourceAttributeDesignator. */
    public static final String RESOURCE_ATTRIBUTE_DESIGNATOR_ELEMENT_LOCAL_NAME = "ResourceAttributeDesignator";

    /** QName of the element ResourceAttributeDesignator. */
    public static final QName RESOURCE_ATTRIBUTE_DESIGNATOR_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS,
            RESOURCE_ATTRIBUTE_DESIGNATOR_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the element ActionAttributeDesignator. */
    public static final String ACTION_ATTRIBUTE_DESIGNATOR_ELEMENT_LOCAL_NAME = "ActionAttributeDesignator";

    /** QName of the element ActionAttributeDesignator. */
    public static final QName ACTION_ATTRIBUTE_DESIGNATOR_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS,
            ACTION_ATTRIBUTE_DESIGNATOR_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the element EnvironmentAttribtueDesignator. */
    public static final String ENVIRONMENT_ATTRIBUTE_DESIGNATOR_ELEMENT_LOCAL_NAME = "EnvironmentAttributeDesignator";

    /** QName of the element EnvironmentAttribtueDesignator. */
    public static final QName ENVIRONMENT_ATTRIBUTE_DESIGNATOR_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS,
            ENVIRONMENT_ATTRIBUTE_DESIGNATOR_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "AttributeDesignatorType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** AttribtueId attribute name. */
    public static final String ATTRIBUTE_ID_ATTRIB_NAME = "AttributeId";

    /** DataType attribute name. */
    public static final String DATA_TYPE_ATTRIB_NAME = "DataType";

    /** Issuer attribute name. */
    public static final String ISSUER_ATTRIB_NAME = "Issuer";

    /** MustBePresent attribute name. */
    public static final String MUST_BE_PRESENT_ATTRIB_NAME = "MustBePresent";

    /**
     * Gets the ID of the designated attribute.
     * 
     * @return ID of the designated attribute
     */
    public String getAttributeId();

    /**
     * Sets the ID of the designated attribute.
     * 
     * @param id ID of the designated attribute
     */
    public void setAttribtueId(String id);

    /**
     * Gets the data type of the designated attribute.
     * 
     * @return data type of the designated attribute
     */
    public String getDataType();

    /**
     * Sets the data type of the designated attribute.
     * 
     * @param type data type of the designated attribute
     */
    public void setDataType(String type);

    /**
     * Gets the issuer of the designated attribute.
     * 
     * @return issuer of the designated attribute
     */
    public String getIssuer();

    /**
     * Sets the issuer of the designated attribute.
     * 
     * @param issuer issuer of the designated attribute
     */
    public void setIssuer(String issuer);

    /**
     * Gets whether the designated attribute must be present.
     * 
     * @return whether the designated attribute must be present
     */
    public XSBooleanValue getMustBePresentXSBoolean();

    /**
     * Sets whether the designated attribute must be present.
     * 
     * @param present whether the designated attribute must be present
     */
    public void setMustBePresentXSBoolean(XSBooleanValue present);

    /**
     * Sets whether the designated attribute must be present.
     * 
     * @param present whether the designated attribute must be present
     */
    public void setMustBePresent(Boolean present);

    /**
     * Gets whether the designated attribute must be present.
     * 
     * @return whether the designated attribute must be present
     */
    public Boolean getMustBePresent();
}