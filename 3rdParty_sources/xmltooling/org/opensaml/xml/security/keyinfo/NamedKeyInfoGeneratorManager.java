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

package org.opensaml.xml.security.keyinfo;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.util.LazyMap;

/**
 * A manager for named sets of {@link KeyInfoGeneratorFactory} instances. Each name key serves as an index to an
 * instance of {@link KeyInfoGeneratorManager}.
 */
public class NamedKeyInfoGeneratorManager {
    
    /** The set of named factory managers. */
    private Map<String, KeyInfoGeneratorManager> managers;
    
    /** The default manager for unnamed factories. */
    private KeyInfoGeneratorManager defaultManager;
    
    /** Flag indicating whether the default (unnamed) factory manager will be used to 
     * lookup factories for credentials. */
    private boolean useDefaultManager;
    
    /** Constructor. */
    public NamedKeyInfoGeneratorManager() {
        managers = new LazyMap<String, KeyInfoGeneratorManager>();
        defaultManager = new KeyInfoGeneratorManager();
        useDefaultManager = true;
    }
    
    /**
     * Set the option as to whether the default (unnamed) manager will be used to lookup factories
     * for credentials if there is no appropriate named factory for the credential type.
     * 
     * @param newValue the new option value
     */
    public void setUseDefaultManager(boolean newValue) {
        useDefaultManager = newValue;
    }
    
    /**
     * Get the (unmodifiable) set of names of factory managers currently available.
     * 
     * @return the set of all manager names currently configured
     */
    public Set<String> getManagerNames() {
        return Collections.unmodifiableSet(managers.keySet());
    }
 
    /**
     * Get the named factory manager.  If it doesn't exist yet, one will be created.
     * 
     * @param name the name of the manager to obtain
     * @return the named manager
     */
    public KeyInfoGeneratorManager getManager(String name) {
        KeyInfoGeneratorManager manager = managers.get(name);
        if (manager == null) {
            manager = new KeyInfoGeneratorManager();
            managers.put(name, manager);
        }
        return manager;
    }
    
    /**
     * Remove the named factory manager, and all its managed factories.
     * 
     * @param name the name of the manager to remove
     */
    public void removeManager(String name) {
        managers.remove(name);
    }
    
    /**
     * Register a factory within the specified named manager. If that 
     * named manager does not currently exist, it will be created.
     * 
     * @param name the name of the factory manager
     * @param factory the factory to register
     */
    public void registerFactory(String name, KeyInfoGeneratorFactory factory) {
        KeyInfoGeneratorManager manager = getManager(name);
        manager.registerFactory(factory);
    }
    
    /**
     * De-register a factory within the specified named manager.
     * 
     * @param name the name of the factory manager
     * @param factory the factory to de-register
     */
    public void deregisterFactory(String name, KeyInfoGeneratorFactory factory) {
        KeyInfoGeneratorManager manager = managers.get(name);
        if (manager == null) {
            throw new IllegalArgumentException("Manager with name '" + name + "' does not exist");
        }
        
        manager.deregisterFactory(factory);
    }

    /**
     * Register a factory with the default (unnamed) manager.
     * 
     * @param factory the factory to register
     */
    public void registerDefaultFactory(KeyInfoGeneratorFactory factory) {
        defaultManager.registerFactory(factory);
    }
    
    /**
     * De-register a factory with the default (unnamed) manager.
     * 
     * @param factory the factory to de-register
     */
    public void deregisterDefaultFactory(KeyInfoGeneratorFactory factory) {
        defaultManager.deregisterFactory(factory);
    }
    
    /**
     * Get the default (unnamed) factory manager.
     * 
     * @return the default factory manager
     */
    public KeyInfoGeneratorManager getDefaultManager() {
        return defaultManager;
    }
    
    /**
     * Lookup and return the named generator factory for the type of the credential specified.
     * 
     * @param name the name of the factory manger
     * @param credential the credential to evaluate
     * @return a factory for generators appropriate for the specified credential
     */
    public KeyInfoGeneratorFactory getFactory(String name, Credential credential) {
        KeyInfoGeneratorManager manager = managers.get(name);
        if (manager == null) {
            throw new IllegalArgumentException("Manager with name '" + name + "' does not exist");
        }
            
        KeyInfoGeneratorFactory factory = manager.getFactory(credential);
        if (factory == null) {
            if (useDefaultManager) {
                factory = defaultManager.getFactory(credential);
            }
        }
        return factory;
    }

}
