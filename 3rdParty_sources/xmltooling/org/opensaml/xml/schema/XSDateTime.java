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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidatingXMLObject;

/**
 * XMLObject that represents an XML Schema dateTime.
 */
public interface XSDateTime extends ValidatingXMLObject {

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "dateTime"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(XMLConstants.XSD_NS, TYPE_LOCAL_NAME, XMLConstants.XSD_PREFIX);
    
    /**
     * Gets the dateTime value.
     * 
     * @return the dateTime value
     */
    public DateTime getValue();
    
    /**
     * Sets the dateTime value.
     * 
     * @param newValue the dateTime value
     */
    public void setValue(DateTime newValue);
    
    /**
     * Get the {@link DateTimeFormatter} to be used when stringifying
     * the {@link DateTime} value.
     * 
     * <p>Defaults to the formatter constructed by calling: 
     * <code>org.joda.time.format.ISODateTimeFormat.dateTime().withChronology(org.joda.time.chrono.ISOChronology.getInstanceUTC()</code>
     * </p>
     * 
     * @return the currently configured formatter
     */
    public DateTimeFormatter getDateTimeFormatter();
    
    /**
     * Set the {@link DateTimeFormatter} to be used when stringifying
     * the {@link DateTime} value.
     * 
     * <p>Defaults to the formatter constructed by calling: 
     * <code>org.joda.time.format.ISODateTimeFormat.dateTime().withChronology(org.joda.time.chrono.ISOChronology.getInstanceUTC()</code>
     * </p>
     * 
     * @param newFormatter the new formatter
     */
    public void setDateTimeFormatter(DateTimeFormatter newFormatter);
}
