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

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.XACMLObject;

/** XACML context MissingAttributeDetail schema type. */
public interface MissingAttributeDetailType extends XACMLObject {

    /** Local name of the element MissingAttributeDetail. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "MissingAttributeDetail";

    /** QName of the element MissingAttributeDetail. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20CTX_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XACMLConstants.XACMLCONTEXT_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "MissingAttributeDetailType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20CTX_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACMLCONTEXT_PREFIX);

    /** AttributeId attribute name. */
    public static final String ATTRIBUTE_ID_ATTRIB_NAME = "AttributeId";

    /** DataType attribute name. */
    public static final String DATA_TYPE_ATTRIB_NAME = "DataType";

    /** Issuer attribute name. */
    public static final String ISSUER_ATTRIB_NAME = "Issuer";

    /**
     * Gets the missing attribute values.
     * 
     * @return missing attribute values
     */
    public List<AttributeValueType> getAttributeValues();

    /**
     * Gets the ID of the attribute.
     * 
     * @return ID of the attribute
     */
    public String getAttributeId();

    /**
     * Sets the ID of the attribute.
     * 
     * @param id ID of the attribute
     */
    public void setAttributeId(String id);

    /**
     * Gets the data type of the attribute.
     * 
     * @return data type of the attribute
     */
    public String getDataType();

    /**
     * Sets the data type of the attribute.
     * 
     * @param type data type of the attribute
     */
    public void setDataType(String type);

    /**
     * Gets the issuer of the attribute.
     * 
     * @return issuer of the attribute
     */
    public String getIssuer();

    /**
     * Sets the issuer of the attribute.
     * 
     * @param issuer issuer of the attribute
     */
    public void setIssuer(String issuer);
}