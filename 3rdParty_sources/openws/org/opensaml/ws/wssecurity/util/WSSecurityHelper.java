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

package org.opensaml.ws.wssecurity.util;

import java.util.List;

import org.opensaml.ws.wssecurity.IdBearing;
import org.opensaml.ws.wssecurity.TokenTypeBearing;
import org.opensaml.ws.wssecurity.UsageBearing;
import org.opensaml.xml.AttributeExtensibleXMLObject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.LazyList;
import org.opensaml.xml.util.XMLHelper;

/**
 * Helper methods for working with WS-Security.
 */
public final class WSSecurityHelper {

    /**
     * Private constructor.
     */
    private WSSecurityHelper() {
    }
    
    /**
     * Adds a <code>wsu:Id</code> attribute to the given SOAP object.
     * 
     * @param soapObject the SOAP object to add the attribute to
     * @param id the Id value
     */
    public static void addWSUId(XMLObject soapObject, String id) {
        if (soapObject instanceof IdBearing) {
            ((IdBearing)soapObject).setWSUId(id);
        } else if (soapObject instanceof AttributeExtensibleXMLObject) {
            ((AttributeExtensibleXMLObject)soapObject).getUnknownAttributes()
                .put(IdBearing.WSU_ID_ATTR_NAME, id);
        } else {
            throw new IllegalArgumentException("Specified object was neither IdBearing nor AttributeExtensible");
        }
    }
    
    /**
     * Gets the <code>wsu:Id</code> attribute from a given SOAP object.
     * 
     * @param soapObject the SOAP object to add the attribute to
     * 
     * @return the value of the Id attribute, or null if not present
     */
    public static String getWSUId(XMLObject soapObject) {
        String value = null;
        if (soapObject instanceof IdBearing) {
            value = DatatypeHelper.safeTrimOrNullString(((IdBearing)soapObject).getWSUId());
            if (value != null) {
                return value;
            }
        }
        if (soapObject instanceof AttributeExtensibleXMLObject) {
            value = DatatypeHelper.safeTrimOrNullString(((AttributeExtensibleXMLObject)soapObject)
                        .getUnknownAttributes().get(IdBearing.WSU_ID_ATTR_NAME));
            return value;
        }
        return null;
    }
    
    /**
     * Adds a <code>wsse11:TokenType</code> attribute to the given SOAP object.
     * 
     * @param soapObject the SOAP object to add the attribute to
     * @param tokenType the tokenType value
     */
    public static void addWSSE11TokenType(XMLObject soapObject, String tokenType) {
        if (soapObject instanceof TokenTypeBearing) {
            ((TokenTypeBearing)soapObject).setWSSE11TokenType(tokenType);
        } else if (soapObject instanceof AttributeExtensibleXMLObject) {
            ((AttributeExtensibleXMLObject)soapObject).getUnknownAttributes()
                .put(TokenTypeBearing.WSSE11_TOKEN_TYPE_ATTR_NAME, tokenType);
        } else {
            throw new IllegalArgumentException("Specified object was neither TokenTypeBearing nor AttributeExtensible");
        }
    }
    
    /**
     * Gets the <code>wsse11:TokenType</code> attribute from a given SOAP object.
     * 
     * @param soapObject the SOAP object to add the attribute to
     * 
     * @return the value of the tokenType attribute, or null if not present
     */
    public static String getWSSE11TokenType(XMLObject soapObject) {
        String value = null;
        if (soapObject instanceof TokenTypeBearing) {
            value = DatatypeHelper.safeTrimOrNullString(((TokenTypeBearing)soapObject).getWSSE11TokenType());
            if (value != null) {
                return value;
            }
        }
        if (soapObject instanceof AttributeExtensibleXMLObject) {
            value = DatatypeHelper.safeTrimOrNullString(((AttributeExtensibleXMLObject)soapObject)
                        .getUnknownAttributes().get(TokenTypeBearing.WSSE11_TOKEN_TYPE_ATTR_NAME));
            return value;
        }
        return null;
    }
    
    /**
     * Adds a single <code>wsse:Usage</code> value to the given SOAP object. If an existing <code>wsse:Usage</code>
     * attribute is present, the given usage will be added to the existing list.
     * 
     * @param soapObject the SOAP object to add the attribute to
     * @param usage the usage to add
     */
    public static void addWSSEUsage(XMLObject soapObject, String usage) {
        if (soapObject instanceof UsageBearing) {
            UsageBearing usageBearing = (UsageBearing) soapObject;
            List<String> list = usageBearing.getWSSEUsages();
            if (list == null) {
                list = new LazyList<String>();
                usageBearing.setWSSEUsages(list);
            }
            list.add(usage);
        } else if (soapObject instanceof AttributeExtensibleXMLObject) {
            AttributeMap am =  ((AttributeExtensibleXMLObject)soapObject).getUnknownAttributes();
            String list = am.get(UsageBearing.WSSE_USAGE_ATTR_NAME);
            if (list == null) {
                list = usage;
            } else {
                list = list + " " + usage;
            }
            am.put(UsageBearing.WSSE_USAGE_ATTR_NAME, list);
        } else {
            throw new IllegalArgumentException("Specified object was neither UsageBearing nor AttributeExtensible");
        }
    }
    
    /**
     * Adds a <code>wsse:Usage</code> attribute to the given SOAP object.
     * 
     * @param soapObject the SOAP object to add the attribute to
     * @param usages the list of usages to add
     */
    public static void addWSSEUsages(XMLObject soapObject, List<String> usages) {
        if (soapObject instanceof UsageBearing) {
            ((UsageBearing)soapObject).setWSSEUsages(usages);
        } else if (soapObject instanceof AttributeExtensibleXMLObject) {
            ((AttributeExtensibleXMLObject)soapObject).getUnknownAttributes()
                .put(UsageBearing.WSSE_USAGE_ATTR_NAME, 
                        DatatypeHelper.listToStringValue(usages, " "));
        } else {
            throw new IllegalArgumentException("Specified object was neither UsageBearing nor AttributeExtensible");
        }
    }
    
    /**
     * Gets the list value of the <code>wsse:Usage</code> attribute from the given SOAP object.
     * 
     * @param soapObject the SOAP object to add the attribute to
     * 
     * @return the list of usages, or null if not present
     */
    public static List<String> getWSSEUsages(XMLObject soapObject) {
        if (soapObject instanceof UsageBearing) {
            List<String> value = ((UsageBearing)soapObject).getWSSEUsages();
            if (value != null) {
                return value;
            }
        }
        if (soapObject instanceof AttributeExtensibleXMLObject) {
            String value = DatatypeHelper.safeTrimOrNullString(((AttributeExtensibleXMLObject)soapObject)
                    .getUnknownAttributes().get(UsageBearing.WSSE_USAGE_ATTR_NAME));
            if (value != null) {
                DatatypeHelper.stringToList(value, XMLHelper.LIST_DELIMITERS);
            }
        }
        return null;
    }
}
