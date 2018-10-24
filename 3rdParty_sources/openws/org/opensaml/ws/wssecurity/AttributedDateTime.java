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

package org.opensaml.ws.wssecurity;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.opensaml.xml.AttributeExtensibleXMLObject;
import org.opensaml.xml.schema.XSString;

/**
 * Interface for elements of complex type AttributedDateTime.
 * 
 */
public interface AttributedDateTime extends XSString, IdBearing, AttributeExtensibleXMLObject, WSSecurityObject  {

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "AttributedDateTime"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSSecurityConstants.WSU_NS, TYPE_LOCAL_NAME, WSSecurityConstants.WSU_PREFIX);
    
    /** Default DateTime format. 
     * 
     * @deprecated replaced by use of a {@link DateTimeFormatter} 
     *              configured via {@link #setDateTimeFormatter(DateTimeFormatter)}.
     * 
     * */
    public static final String DEFAULT_DATETIME_FORMAT= "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * Returns the DateTime content or attribute value.
     * 
     * @return the {@link DateTime} object.
     */
    public DateTime getDateTime();

    /**
     * Sets the DateTime content or attribute value.
     * 
     * @param dateTime
     *            the {@link DateTime} object to set.
     */
    public void setDateTime(DateTime dateTime);
    
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
