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

package org.opensaml.ws.wsaddressing;

import javax.xml.namespace.QName;

import org.opensaml.xml.schema.XSBooleanValue;

/**
 * Interface for element having a <code>@wsa:IsReferenceParameter</code> attribute.
 * 
 */
public interface IsReferenceParameterBearing {

    /** the <code>IsReferenceParameter</code> attribute local name. */
    public static final String WSA_IS_REFERENCE_PARAMETER_ATTR_LOCAL_NAME = "IsReferenceParameter";

    /** the <code>wsa:IsReferenceParameter</code> qualified attribute name. */
    public static final QName WSA_IS_REFERENCE_PARAMETER_ATTR_NAME =
        new QName(WSAddressingConstants.WSA_NS, WSA_IS_REFERENCE_PARAMETER_ATTR_LOCAL_NAME,
                WSAddressingConstants.WSA_PREFIX);

    /**
     * Returns the <code>@wsa:IsReferenceParameter</code> attribute value.
     * 
     * @return The <code>@wsa:IsReferenceParameter</code> attribute value or <code>null</code>.
     */
    public Boolean isWSAIsReferenceParameter();

    /**
     * Returns the <code>@wsa:IsReferenceParameter</code> attribute value.
     * 
     * @return The <code>@wsa:IsReferenceParameter</code> attribute value or <code>null</code>.
     */
    public XSBooleanValue isWSAIsReferenceParameterXSBoolean();

    /**
     * Sets the <code>@wsa:IsReferenceParameter</code> attribute value.
     * 
     * @param newIsReferenceParameter The <code>@wsa:IsReferenceParameter</code> attribute value
     */
    public void setWSAIsReferenceParameter(Boolean newIsReferenceParameter);
    
    /**
     * Sets the <code>@wsa:IsReferenceParameter</code> attribute value.
     * 
     * @param newIsReferenceParameter The <code>@wsa:IsReferenceParameter</code> attribute value
     */
    public void setWSAIsReferenceParameter(XSBooleanValue newIsReferenceParameter);

}
