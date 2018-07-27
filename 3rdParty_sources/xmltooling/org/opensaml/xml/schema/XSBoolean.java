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

package org.opensaml.xml.schema;

import javax.xml.namespace.QName;

import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidatingXMLObject;

/** XSBoolean is the <code>xs:boolean</code> schema type. */
public abstract interface XSBoolean extends ValidatingXMLObject {

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "boolean"; 
            
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(XMLConstants.XSD_NS, TYPE_LOCAL_NAME, XMLConstants.XSD_PREFIX);
    
    /**
     * Returns the XSBooleanValue value.
     * 
     * @return the {@link XSBooleanValue} value
     */
    public XSBooleanValue getValue();

    /**
     * Sets the XSBooleanValue value.
     * 
     * @param value The {@link XSBooleanValue} value
     */
    public void setValue(XSBooleanValue value);

}
