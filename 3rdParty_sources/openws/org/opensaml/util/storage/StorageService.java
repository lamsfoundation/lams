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

/**
 * Generic data storage facility for use by services that require some degree of persistence.
 * 
 * The storage service is partitioned. This is to allow different objects to use the service, each with its own
 * partition, without the worry of conflicting keys.
 * 
 * @param <KeyType> object type of the keys
 * @param <ValueType> object type of the values
 */
public interface StorageService<KeyType, ValueType> {

    /**
     * Checks if a given key exists.
     * 
     * @param partition partition on which to operate
     * @param key the key to check
     * 
     * @return true of the given key exists, false if not
     */
    public boolean contains(String partition, KeyType key);

    /**
     * Gets the partitions within the service. Removal of a partition identifier from the iterator removes the partition
     * from the storage service.
     * 
     * @return partitions within the service
     */
    public Iterator<String> getPartitions();

    /**
     * Gets the keys for entries in the storage service. Removal of a key from the iterator removes the the key and
     * associated value from the store.
     * 
     * <strong>Note:</strong> this operation may be very expensive
     * 
     * @param partition partition on which to operate
     * 
     * @return list of keys currently within the store
     */
    public Iterator<KeyType> getKeys(String partition);

    /**
     * Gets the value stored under a particular key.
     * 
     * @param partition partition on which to operate
     * @param key the key
     * 
     * @return the value for that key, or null if there is no value for the given key
     */
    public ValueType get(String partition, KeyType key);

    /**
     * Adds a value, indexed by a key, in to storage. Note that implementations of this service may determine, on its
     * own, when to evict items from storage, the expiration time given here is meant only as a system provided hint.
     * 
     * @param partition partition on which to operate
     * @param key the key
     * @param value the value
     * 
     * @return the value that was registered under that key previously, if there was a previous value
     */
    public ValueType put(String partition, KeyType key, ValueType value);

    /**
     * Removes an item from storage.
     * 
     * @param partition partition on which to operate
     * @param key the key to the value to remove
     * 
     * @return the value that was removed
     */
    public ValueType remove(String partition, KeyType key);
}