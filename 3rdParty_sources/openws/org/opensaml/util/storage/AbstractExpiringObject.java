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

package org.opensaml.util.storage;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;

/** Base implementation for {@link ExpiringObject}. */
public abstract class AbstractExpiringObject implements ExpiringObject, Serializable {

    /** Moment of expiration in UTC. */
    private long expiration;

    /**
     * Constructor.
     * 
     * @param expirationTime time this object should expire
     */
    public AbstractExpiringObject(DateTime expirationTime) {
        expiration = expirationTime.toDateTime(ISOChronology.getInstanceUTC()).getMillis();
    }

    /** {@inheritDoc} */
    public DateTime getExpirationTime() {
        return new DateTime(expiration, ISOChronology.getInstanceUTC());
    }

    /** {@inheritDoc} */
    public boolean isExpired() {
        return getExpirationTime().isBeforeNow();
    }

    /** {@inheritDoc} */
    public void onExpire() {
        // no-op, implementations should override if they need to do something
    }
}