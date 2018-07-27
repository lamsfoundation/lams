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
import org.opensaml.xml.AttributeExtensibleXMLObject;
import org.opensaml.xml.ElementExtensibleXMLObject;

/** XACML AttributeValue schema type. */
public interface AttributeValueType extends ExpressionType, AttributeExtensibleXMLObject, ElementExtensibleXMLObject {

    /** Local name of the element AttributeValue. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "AttributeValue";

    /** QName of the element AttributeValue. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "AttributeValueType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** DataType attribute name. */
    public static final String DATA_TYPE_ATTRIB_NAME = "DataType";

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
     * Gets the text content of the element.
     * 
     * @return text content of the element
     */
    public String getValue();

    /**
     * Sets the text content of the element.
     * 
     * <strong>NOTE</strong> because the library does not support mixed content setting textual content will prohibit
     * element content.
     * 
     * @param value text content of the element
     */
    public void setValue(String value);
}