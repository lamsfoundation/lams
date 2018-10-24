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

package org.opensaml.xml;

import javax.xml.namespace.QName;

import org.opensaml.xml.util.XMLConstants;

/**
 * Interface for element having a <code>@xml:space</code> attribute.
 * 
 */
public interface SpaceBearing {
    
    /** Enum representing the allowed values of the xml:space attribute. */
    public enum XMLSpaceEnum {
        /** xml:space value "default". */
        DEFAULT,
        /** xml:space value "preserve". */
        PRESERVE;
        
        // Unfortunately "default" is a reserved word in Java, so the enum value above has to be upper case
        // and we have the mess below.
        
        /** {@inheritDoc} */
        public String toString() {
            return super.toString().toLowerCase();
        }
        
        /**
         * Parse a string value into an XMLSpaceEnum.
         * 
         * <p>
         * The legal values are "default" and "preserve".
         * </p>
         * 
         * @param value the value to parse
         * @return the corresponding XMLSpaceEnum
         */
        public static XMLSpaceEnum parseValue(String value) {
            return XMLSpaceEnum.valueOf(value.toUpperCase());
        }
        
    }

    /** The <code>space</code> attribute local name. */
    public static final String XML_SPACE_ATTR_LOCAL_NAME = "space";

    /** The <code>xml:space</code> qualified attribute name. */
    public static final QName XML_SPACE_ATTR_NAME =
        new QName(XMLConstants.XML_NS, XML_SPACE_ATTR_LOCAL_NAME, XMLConstants.XML_PREFIX);

    /**
     * Returns the <code>@xml:space</code> attribute value.
     * 
     * @return The <code>@xml:space</code> attribute value or <code>null</code>.
     */
    public XMLSpaceEnum getXMLSpace();

    /**
     * Sets the <code>@xml:space</code> attribute value.
     * 
     * @param newSpace The <code>@xml:space</code> attribute value
     */
    public void setXMLSpace(XMLSpaceEnum newSpace);

}
