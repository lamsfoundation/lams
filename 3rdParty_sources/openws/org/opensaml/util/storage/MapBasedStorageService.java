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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple {@link Map} based {@link StorageService} implementation.
 * 
 * @param <KeyType> object type of the keys
 * @param <ValueType> object type of the values
 */
public class MapBasedStorageService<KeyType, ValueType> implements StorageService<KeyType, ValueType> {

    /** Backing map. */
    private Map<String, Map<KeyType, ValueType>> store;

    /** Constructor. */
    public MapBasedStorageService() {
        store = new ConcurrentHashMap<String, Map<KeyType, ValueType>>();
    }

    /**
     * Constructor.
     * 
     * @param serviceStore the map to use as storage
     */
    protected MapBasedStorageService(Map<String, Map<KeyType, ValueType>> serviceStore) {
        store = serviceStore;
    }

    /** {@inheritDoc} */
    public Iterator<String> getPartitions() {
        Set<String> keys = store.keySet();
        if (keys != null) {
            return keys.iterator();
        }

        return null;
    }

    /** {@inheritDoc} */
    public Iterator<KeyType> getKeys(String partition) {
        if (store.containsKey(partition)) {
            Set<KeyType> keys = store.get(partition).keySet();
            if (keys != null) {
                return keys.iterator();
            }
        }

        return null;
    }

    /** {@inheritDoc} */
    public boolean contains(String partition, KeyType key) {
        if (key == null) {
            return false;
        }

        if (store.containsKey(partition)) {
            return store.get(partition).containsKey(key);
        }

        return false;
    }

    /** {@inheritDoc} */
    public ValueType get(String partition, KeyType key) {
        if (key == null) {
            return null;
        }

        if (store.containsKey(partition)) {
            return store.get(partition).get(key);
        }

        return null;
    }

    /** {@inheritDoc} */
    public ValueType put(String partition, KeyType key, ValueType value) {
        if (key == null) {
            return null;
        }

        Map<KeyType, ValueType> partitionMap;
        synchronized (store) {
            partitionMap = store.get(partition);
            if (partitionMap == null) {
                partitionMap = new ConcurrentHashMap<KeyType, ValueType>();
            }
            store.put(partition, partitionMap);
        }

        return partitionMap.put(key, value);
    }

    /** {@inheritDoc} */
    public ValueType remove(String partition, KeyType key) {
        if (key == null) {
            return null;
        }

        if (store.containsKey(partition)) {
            return store.get(partition).remove(key);
        }

        return null;
    }
}