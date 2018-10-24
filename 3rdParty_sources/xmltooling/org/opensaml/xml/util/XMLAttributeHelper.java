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

package org.opensaml.xml.util;

import org.opensaml.xml.AttributeExtensibleXMLObject;
import org.opensaml.xml.BaseBearing;
import org.opensaml.xml.IdBearing;
import org.opensaml.xml.LangBearing;
import org.opensaml.xml.SpaceBearing;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.SpaceBearing.XMLSpaceEnum;

/**
 * Helper methods for working with global attributes from the XML namespace. These are namely:
 * <ol>
 *   <li>xml:id</li>
 *   <li>xml:lang</li>
 *   <li>xml:base</li>
 *   <li>xml:space</li>
 * </ol>
 */
public final class XMLAttributeHelper {

    /**
     * Private constructor.
     */
    private XMLAttributeHelper() {
    }
    
    /**
     * Adds a <code>xml:id</code> attribute to the given XML object.
     * 
     * @param xmlObject the XML object to which to add the attribute
     * @param id the Id value
     */
    public static void addXMLId(XMLObject xmlObject, String id) {
        if (xmlObject instanceof IdBearing) {
            ((IdBearing)xmlObject).setXMLId(id);
        } else if (xmlObject instanceof AttributeExtensibleXMLObject) {
            ((AttributeExtensibleXMLObject)xmlObject).getUnknownAttributes()
                .put(IdBearing.XML_ID_ATTR_NAME, id);
        } else {
            throw new IllegalArgumentException("Specified object was neither IdBearing nor AttributeExtensible");
        }
    }
    
    /**
     * Gets the <code>xml:id</code> attribute from a given XML object.
     * 
     * @param xmlObject the XML object from which to extract the attribute
     * 
     * @return the value of the xml:id attribute, or null if not present
     */
    public static String getXMLId(XMLObject xmlObject) {
        String value = null;
        if (xmlObject instanceof IdBearing) {
            value = DatatypeHelper.safeTrimOrNullString(((IdBearing)xmlObject).getXMLId());
            if (value != null) {
                return value;
            }
        }
        if (xmlObject instanceof AttributeExtensibleXMLObject) {
            value = DatatypeHelper.safeTrimOrNullString(((AttributeExtensibleXMLObject)xmlObject)
                        .getUnknownAttributes().get(IdBearing.XML_ID_ATTR_NAME));
            return value;
        }
        return null;
    }
    
    /**
     * Adds a <code>xml:lang</code> attribute to the given XML object.
     * 
     * @param xmlObject the XML object to which to add the attribute
     * @param lang the lang value
     */
    public static void addXMLLang(XMLObject xmlObject, String lang) {
        if (xmlObject instanceof LangBearing) {
            ((LangBearing)xmlObject).setXMLLang(lang);
        } else if (xmlObject instanceof AttributeExtensibleXMLObject) {
            ((AttributeExtensibleXMLObject)xmlObject).getUnknownAttributes()
                .put(LangBearing.XML_LANG_ATTR_NAME, lang);
        } else {
            throw new IllegalArgumentException("Specified object was neither LangBearing nor AttributeExtensible");
        }
    }
    
    /**
     * Gets the <code>xml:lang</code> attribute from a given XML object.
     * 
     * @param xmlObject the XML object from which to extract the attribute
     * 
     * @return the value of the xml:lang attribute, or null if not present
     */
    public static String getXMLLang(XMLObject xmlObject) {
        String value = null;
        if (xmlObject instanceof LangBearing) {
            value = DatatypeHelper.safeTrimOrNullString(((LangBearing)xmlObject).getXMLLang());
            if (value != null) {
                return value;
            }
        }
        if (xmlObject instanceof AttributeExtensibleXMLObject) {
            value = DatatypeHelper.safeTrimOrNullString(((AttributeExtensibleXMLObject)xmlObject)
                        .getUnknownAttributes().get(LangBearing.XML_LANG_ATTR_NAME));
            return value;
        }
        return null;
    }
    
    /**
     * Adds a <code>xml:base</code> attribute to the given XML object.
     * 
     * @param xmlObject the XML object to which to add the attribute
     * @param base the base value
     */
    public static void addXMLBase(XMLObject xmlObject, String base) {
        if (xmlObject instanceof BaseBearing) {
            ((BaseBearing)xmlObject).setXMLBase(base);
        } else if (xmlObject instanceof AttributeExtensibleXMLObject) {
            ((AttributeExtensibleXMLObject)xmlObject).getUnknownAttributes()
                .put(BaseBearing.XML_BASE_ATTR_NAME, base);
        } else {
            throw new IllegalArgumentException("Specified object was neither BaseBearing nor AttributeExtensible");
        }
    }
    
    /**
     * Gets the <code>xml:base</code> attribute from a given XML object.
     * 
     * @param xmlObject the XML object from which to extract the attribute
     * 
     * @return the value of the xml:base attribute, or null if not present
     */
    public static String getXMLBase(XMLObject xmlObject) {
        String value = null;
        if (xmlObject instanceof BaseBearing) {
            value = DatatypeHelper.safeTrimOrNullString(((BaseBearing)xmlObject).getXMLBase());
            if (value != null) {
                return value;
            }
        }
        if (xmlObject instanceof AttributeExtensibleXMLObject) {
            value = DatatypeHelper.safeTrimOrNullString(((AttributeExtensibleXMLObject)xmlObject)
                        .getUnknownAttributes().get(BaseBearing.XML_BASE_ATTR_NAME));
            return value;
        }
        return null;
    }
    
    /**
     * Adds a <code>xml:space</code> attribute to the given XML object.
     * 
     * @param xmlObject the XML object to which to add the attribute
     * @param space the space value
     */
    public static void addXMLSpace(XMLObject xmlObject, XMLSpaceEnum space) {
        if (xmlObject instanceof SpaceBearing) {
            ((SpaceBearing)xmlObject).setXMLSpace(space);
        } else if (xmlObject instanceof AttributeExtensibleXMLObject) {
            ((AttributeExtensibleXMLObject)xmlObject).getUnknownAttributes()
                .put(SpaceBearing.XML_SPACE_ATTR_NAME, space.toString());
        } else {
            throw new IllegalArgumentException("Specified object was neither SpaceBearing nor AttributeExtensible");
        }
    }
    
    /**
     * Gets the <code>xml:space</code> attribute from a given XML object.
     * 
     * @param xmlObject the XML object from which to extract the attribute
     * 
     * @return the value of the xml:space attribute, or null if not present
     */
    public static XMLSpaceEnum getXMLSpace(XMLObject xmlObject) {
        XMLSpaceEnum valueEnum = null;
        if (xmlObject instanceof SpaceBearing) {
            valueEnum = ((SpaceBearing)xmlObject).getXMLSpace();
            if (valueEnum != null) {
                return valueEnum;
            }
        }
        String valueString = null;
        if (xmlObject instanceof AttributeExtensibleXMLObject) {
            valueString = DatatypeHelper.safeTrimOrNullString(((AttributeExtensibleXMLObject)xmlObject)
                        .getUnknownAttributes().get(SpaceBearing.XML_SPACE_ATTR_NAME));
            if (valueString != null) {
                return XMLSpaceEnum.parseValue(valueString);
            }
        }
        return null;
    }
}
