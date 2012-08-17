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

package org.opensaml.ws.soap.soap12;

import javax.xml.namespace.QName;

import org.opensaml.ws.soap.util.SOAPConstants;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * Interface for element having a <code>@soap12:mustUnderstand</code> attribute.
 */
public interface MustUnderstandBearing {
    
    /** The soap12:@mustUnderstand attribute local name. */
    public static final String SOAP12_MUST_UNDERSTAND_ATTR_LOCAL_NAME = "mustUnderstand";

    /** The soap12:@mustUnderstand qualified attribute name. */
    public static final QName SOAP12_MUST_UNDERSTAND_ATTR_NAME =
        new QName(SOAPConstants.SOAP12_NS, SOAP12_MUST_UNDERSTAND_ATTR_LOCAL_NAME, SOAPConstants.SOAP12_PREFIX);
    
    /**
     * Get the attribute value.
     * 
     * @return return the attribute value
     */
    public Boolean isSOAP12MustUnderstand();
    
    /**
     * Get the attribute value.
     * 
     * @return return the attribute value
     */
    public XSBooleanValue isSOAP12MustUnderstandXSBoolean();
    
    /**
     * Set the attribute value.
     * 
     * @param newMustUnderstand the new attribute value
     */
    public void setSOAP12MustUnderstand(Boolean newMustUnderstand);
    
    /**
     * Set the attribute value.
     * 
     * @param newMustUnderstand the new attribute value
     */
    public void setSOAP12MustUnderstand(XSBooleanValue newMustUnderstand);

}
