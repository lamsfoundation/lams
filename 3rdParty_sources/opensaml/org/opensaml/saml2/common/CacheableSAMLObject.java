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

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * A functional interface for SAMLElements that provide cache duration information.
 *
 */
public interface CacheableSAMLObject extends SAMLObject{

	/** "cacheDuration" attribute name */
	public final static String CACHE_DURATION_ATTRIB_NAME = "cacheDuration";
	
	/** "cacheDuration" attribute's QName */
	public final static QName CACHE_DURATION_ATTRIB_QNAME = new QName(SAMLConstants.SAML20MD_NS, CACHE_DURATION_ATTRIB_NAME, SAMLConstants.SAML20MD_PREFIX);
	
	/**
	 * Gets the maximum time, in milliseconds, that this descriptor should be cached.
	 *  
	 * @return the maximum time that this descriptor should be cached
	 */
	public Long getCacheDuration();

	/**
	 * Sets the maximum time, in milliseconds, that this descriptor should be cached.
	 * 
	 * @param duration the maximum time that this descriptor should be cached
	 */
	public void setCacheDuration(Long duration);

}