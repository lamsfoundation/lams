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

import org.joda.time.DateTime;

/**
 * A simple interface for objects that may expire at a given time.
 */
public interface ExpiringObject {

    /**
     * Gets the time the object expires.
     * 
     * @return time the object expires
     */
    public DateTime getExpirationTime();

    /**
     * Gets whether this object has expired.
     * 
     * @return true if the expiration time has passed, false if not
     */
    public boolean isExpired();

    /**
     * A callback method invoked when this object is expiring. Note, this method may not be invoked at the exact instant
     * of expiration but may, instead, be invoked the next time the object is read and noticed to have expired.
     */
    public void onExpire();
}