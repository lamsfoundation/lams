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

/** XACML SubjectAttributeDesignator schema type. */
public interface SubjectAttributeDesignatorType extends AttributeDesignatorType {

    /** Local name of the element Obligation. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "SubjectAttributeDesignator";

    /** QName of the element Obligation. */
    public static final QName DEFAULT_ELEMENT_QNAME = new QName(XACMLConstants.XACML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "SubjectAttributeDesignatorType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** SubjectCategory attribute name. */
    public static final String SUBJECT_CATEGORY_ATTRIB_NAME = "SubjectCategory";

    /**
     * Gets the category of the Subject.
     * 
     * @return category of the Subject
     */
    public String getSubjectCategory();

    /**
     * Sets the category of the Subject.
     * 
     * @param category category of the Subject
     */
    public void setSubjectCategory(String category);
}