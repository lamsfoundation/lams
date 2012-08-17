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

package org.opensaml.saml2.metadata.provider;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.saml2.metadata.EntitiesDescriptor;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A metadata filter that requires the presence of a <code>validUntil</code> attribute on the root element of the
 * metadata document. It can optionally also enforce that the validity period (now minus <code>validUntil</code> date)
 * is not longer than a specified amount.
 * 
 * A maximum validity interval of less than 1 means the no restriction is placed on the metadata's
 * <code>validUntil</code> attribute.
 */
public class RequiredValidUntilFilter implements MetadataFilter {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(RequiredValidUntilFilter.class);

    /** The maximum interval, in milliseconds, between now and the <code>validUntil</code> date. */
    private long maxValidityInterval;

    /** Constructor. */
    public RequiredValidUntilFilter() {
        maxValidityInterval = 0;
    }

    /**
     * Constructor.
     * 
     * @param maxValidity maximum internal, in seconds, between now and the <code>validUntil</code> date
     */
    public RequiredValidUntilFilter(long maxValidity) {
        this.maxValidityInterval = maxValidity * 1000;
    }

    /**
     * Gets the maximum internal, in milliseconds, between now and the <code>validUntil</code> date. A value of less than 1
     * indicates that there is no restriction.
     * 
     * @return maximum internal, in milliseconds, between now and the <code>validUntil</code> date
     */
    public long getMaxValidityInterval() {
        return maxValidityInterval;
    }

    /** {@inheritDoc} */
    public void doFilter(XMLObject metadata) throws FilterException {
        DateTime validUntil = getValidUntil(metadata);

        if (validUntil == null) {
            throw new FilterException("Metadata did not include a validUntil attribute");
        }

        DateTime now = new DateTime(ISOChronology.getInstanceUTC());
        if (maxValidityInterval > 0 && validUntil.isAfter(now)) {
            long validityInterval = validUntil.getMillis() - now.getMillis();
            if (validityInterval > maxValidityInterval) {
                throw new FilterException("Metadata's validity interval, " + validityInterval
                        + "ms, is larger than is allowed, " + maxValidityInterval + "ms.");
            }
        }
    }

    /**
     * Gets the validUntil time of the metadata, if present.
     * 
     * @param metadata metadata from which to get the validUntil instant
     * 
     * @return the valid until instant or null if it is not present
     * 
     * @throws FilterException thrown if the given XML object is not an {@link EntitiesDescriptor} or
     *             {@link EntityDescriptor}
     */
    protected DateTime getValidUntil(XMLObject metadata) throws FilterException {
        if (metadata instanceof EntitiesDescriptor) {
            return ((EntitiesDescriptor) metadata).getValidUntil();
        } else if (metadata instanceof EntityDescriptor) {
            return ((EntityDescriptor) metadata).getValidUntil();
        } else {
            log.error("Metadata root element was not an EntitiesDescriptor or EntityDescriptor it was a {}", metadata
                    .getElementQName());
            throw new FilterException("Metadata root element was not an EntitiesDescriptor or EntityDescriptor");
        }
    }
}