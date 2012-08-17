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

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple task that periodically sweeps over a {@link StorageService} and removes expired entries.
 */
public class ExpiringObjectStorageServiceSweeper extends TimerTask {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(ExpiringObjectStorageServiceSweeper.class);

    /** Interval between sweeps. */
    private long sweepInterval;

    /** Storage service whose entries will be periodically checked. */
    private StorageService store;

    /** Storage partitions to sweep. */
    private Set<String> partitions;

    /**
     * Constructor. Registers this task with the given timer.
     * 
     * @param taskTimer timer that will sweep the given storage service
     * @param interval interval, in milliseconds, that the storage service will be swept
     * @param sweptStore storage service that will be swept
     */
    public ExpiringObjectStorageServiceSweeper(Timer taskTimer, long interval, StorageService sweptStore) {
        store = sweptStore;
        sweepInterval = interval;
        taskTimer.schedule(this, interval, interval);
        partitions = null;
    }

    /**
     * Constructor. Registers this task with the given timer.
     * 
     * @param taskTimer timer that will sweep the given storage service
     * @param interval interval, in milliseconds, that the storage service will be swept
     * @param sweptStore storage service that will be swept
     * @param sweptPartitions the partitions to sweep, if null or empty all partitions are swept
     */
    public ExpiringObjectStorageServiceSweeper(Timer taskTimer, long interval, StorageService sweptStore,
            Set<String> sweptPartitions) {
        store = sweptStore;
        if (sweptPartitions != null || sweptPartitions.isEmpty()) {
            partitions = sweptPartitions;
        } else {
            partitions = null;
        }
        sweepInterval = interval;
        taskTimer.schedule(this, interval, interval);
    }

    /** {@inheritDoc} */
    public void run() {
        try {
            Iterator<String> sweepPartitions;
            if (partitions != null && !partitions.isEmpty()) {
                sweepPartitions = partitions.iterator();
            } else {
                sweepPartitions = store.getPartitions();
            }

            String currentParition;
            Iterator<?> partitionKeys;
            Object partitionKey;
            Object partitionValue;
            while (sweepPartitions.hasNext()) {
                currentParition = sweepPartitions.next();
                log.trace("Sweeping storage service partition {}", currentParition);
                partitionKeys = store.getKeys(currentParition);
                if (partitionKeys == null) {
                    continue;
                }

                while (partitionKeys.hasNext()) {
                    partitionKey = partitionKeys.next();
                    partitionValue = store.get(currentParition, partitionKey);
                    if (partitionValue instanceof ExpiringObject) {
                        if (((ExpiringObject) partitionValue).isExpired()) {
                            log.trace("Removing expired object from storage service partition {}", currentParition);
                            partitionKeys.remove();
                        }
                    }
                }
            }
        } catch (Throwable t) {
            log.error("Caught unexpected error, sweeper will execute again in " + sweepInterval + "ms", t);
        }
    }
}