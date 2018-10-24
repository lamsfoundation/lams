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

/** Replay cache storage service entry. */
public class ReplayCacheEntry extends AbstractExpiringObject implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 1066201734851002196L;

    /** ID of the message that may not be replayed. */
    private String messageId;

    /**
     * Constructor.
     * 
     * @param id ID of the message that may not be replayed
     * @param expiration time when this entry expires
     */
    public ReplayCacheEntry(String id, DateTime expiration) {
        super(expiration);
        messageId = id;
    }

    /**
     * Gets the ID of the message that may not be replayed.
     * 
     * @return ID of the message that may not be replayed
     */
    public String getMessageId() {
        return messageId;
    }
}