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

package org.opensaml.samlext.saml2mdui;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.xml.LangBearing;

/**
 * Localized logo type.
 * 
 * 
 * @author RDW 27/Aug/2010
 * 
 * See IdP Discovery and Login UI Metadata Extension Profile.
 *  
 */
public interface Logo extends LangBearing, SAMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Logo";
    
    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(UIInfo.MDUI_NS, 
            DEFAULT_ELEMENT_LOCAL_NAME, UIInfo.MDUI_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "LogoType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME =
        new QName(UIInfo.MDUI_NS, TYPE_LOCAL_NAME, UIInfo.MDUI_PREFIX);
    
    /** Attribute label. */
    public static final String HEIGHT_ATTR_NAME = "height";

    /** Attribute label. */
    public static final String WIDTH_ATTR_NAME = "width";


    /**
     * Gets the URL.
     * 
     * @return the URL
     */
    public String getURL();
    
    /**
     * Sets the URL.
     * 
     * @param newURL the URL
     */
    public void setURL(String newURL);

    /**
     * Get the height of the logo.
     * @return the height of the logo
     */
    public Integer getHeight();
    
    /**
     * Sets the height of the logo.
     * @param newHeight the height of the logo
     */
    public void setHeight(Integer newHeight);

    /**
     * Get the width of the logo.
     * @return the width of the logo
     */
    public Integer getWidth();
    
    /**
     * Sets the width of the logo.
     * @param newWidth the height of the logo
     */
    public void setWidth(Integer newWidth);
}
