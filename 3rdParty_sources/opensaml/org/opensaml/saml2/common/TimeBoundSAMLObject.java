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

package org.opensaml.saml2.common;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * A functional interface for SAMLElements that are bounded with a 
 * "validUntil" attribute. 
 */
public interface TimeBoundSAMLObject extends SAMLObject{

	/** "validUntil" attribute's local name */
	public final static String VALID_UNTIL_ATTRIB_NAME = "validUntil";
	
	/** "validUntil" attribute's QName */
	public final static QName VALID_UNTIL_ATTRIB_QNAME = new QName(SAMLConstants.SAML20MD_NS, VALID_UNTIL_ATTRIB_NAME, SAMLConstants.SAML20MD_PREFIX);
	
	/**
	 * Checks to see if the current time is past the validUntil time.
	 * 
	 * @return true of this descriptor is still valid otherwise false
	 */
	public boolean isValid();

	/**
	 * Gets the date until which this descriptor is valid.
	 * 
	 * @return the date until which this descriptor is valid
	 */
	public DateTime getValidUntil();

	/**
	 * Sets the date until which this descriptor is valid.
	 * 
	 * @param validUntil the date until which this descriptor is valid
	 */
	public void setValidUntil(DateTime validUntil);

}