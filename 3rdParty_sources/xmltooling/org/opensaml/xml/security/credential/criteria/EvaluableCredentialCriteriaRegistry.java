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

package org.opensaml.xml.security.credential.criteria;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.opensaml.xml.Configuration;
import org.opensaml.xml.security.Criteria;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A registry which manages mappings from types of {@link Criteria} to the class type which can evaluate that criteria's
 * data against a {@link Credential} target. That latter class will be a subtype of {@link EvaluableCredentialCriteria}.
 * Each EvaluableCredentialCriteria implementation that is registered <strong>MUST</strong> implement a single-arg
 * constructor which takes an instance of the Criteria to be evaluated. The evaluable instance is instantiated
 * reflectively based on this requirement.
 */
public final class EvaluableCredentialCriteriaRegistry {

    /**
     * Properties file storing default mappings from criteria to evaluable credential criteria. Will be loaded as a
     * resource stream relative to this class.
     */
    public static final String DEFAULT_MAPPINGS_FILE = "/credential-criteria-registry.properties";

    /** Storage for the registry mappings. */
    private static Map<Class<? extends Criteria>, Class<? extends EvaluableCredentialCriteria>> registry;

    /** Flag to track whether registry is initialized. */
    private static boolean initialized;

    /** Constructor. */
    private EvaluableCredentialCriteriaRegistry() {
    }

    /**
     * Get an instance of EvaluableCredentialCriteria which can evaluate the supplied criteria's requirements against a
     * Credential target.
     * 
     * @param criteria the criteria to be evaluated against a credential
     * @return an instance of of EvaluableCredentialCriteria representing the specified criteria's requirements
     * @throws SecurityException thrown if there is an error reflectively instantiating a new instance of
     *             EvaluableCredentialCriteria based on class information stored in the registry
     */
    public static EvaluableCredentialCriteria getEvaluator(Criteria criteria) throws SecurityException {
        Logger log = getLogger();
        Class<? extends EvaluableCredentialCriteria> clazz = lookup(criteria.getClass());

        if (clazz != null) {
            log.debug("Registry located evaluable criteria class {} for criteria class {}", clazz.getName(), criteria
                    .getClass().getName());

            try {

                Constructor<? extends EvaluableCredentialCriteria> constructor = clazz
                        .getConstructor(new Class[] { criteria.getClass() });

                return constructor.newInstance(new Object[] { criteria });

            } catch (java.lang.SecurityException e) {
                log.error("Error instantiating new EvaluableCredentialCriteria instance", e);
                throw new SecurityException("Could not create new EvaluableCredentialCriteria", e);
            } catch (NoSuchMethodException e) {
                log.error("Error instantiating new EvaluableCredentialCriteria instance", e);
                throw new SecurityException("Could not create new EvaluableCredentialCriteria", e);
            } catch (IllegalArgumentException e) {
                log.error("Error instantiating new EvaluableCredentialCriteria instance", e);
                throw new SecurityException("Could not create new EvaluableCredentialCriteria", e);
            } catch (InstantiationException e) {
                log.error("Error instantiating new EvaluableCredentialCriteria instance", e);
                throw new SecurityException("Could not create new EvaluableCredentialCriteria", e);
            } catch (IllegalAccessException e) {
                log.error("Error instantiating new EvaluableCredentialCriteria instance", e);
                throw new SecurityException("Could not create new EvaluableCredentialCriteria", e);
            } catch (InvocationTargetException e) {
                log.error("Error instantiating new EvaluableCredentialCriteria instance", e);
                throw new SecurityException("Could not create new EvaluableCredentialCriteria", e);
            }

        } else {
            log.debug("Registry could not locate evaluable criteria for criteria class {}", criteria.getClass()
                    .getName());
        }
        return null;
    }

    /**
     * Lookup the class subtype of EvaluableCredentialCriteria which is registered for the specified Criteria class.
     * 
     * @param clazz the Criteria class subtype to lookup
     * @return the registered EvaluableCredentialCriteria class subtype
     */
    public static synchronized Class<? extends EvaluableCredentialCriteria> lookup(Class<? extends Criteria> clazz) {
        return registry.get(clazz);
    }

    /**
     * Register a credential evaluator class for a criteria class.
     * 
     * @param criteriaClass class subtype of {@link Criteria}
     * @param evaluableClass class subtype of {@link EvaluableCredentialCriteria}
     */
    public static synchronized void register(Class<? extends Criteria> criteriaClass,
            Class<? extends EvaluableCredentialCriteria> evaluableClass) {
        Logger log = getLogger();

        log.debug("Registering class {} as evaluator for class {}", evaluableClass.getName(), criteriaClass.getName());

        registry.put(criteriaClass, evaluableClass);

    }

    /**
     * Deregister a criteria-evaluator mapping.
     * 
     * @param criteriaClass class subtype of {@link Criteria}
     */
    public static synchronized void deregister(Class<? extends Criteria> criteriaClass) {
        Logger log = getLogger();

        log.debug("Deregistering evaluator for class {}", criteriaClass.getName());
        registry.remove(criteriaClass);
    }

    /**
     * Clear all mappings from the registry.
     */
    public static synchronized void clearRegistry() {
        Logger log = getLogger();
        log.debug("Clearing evaluable criteria registry");

        registry.clear();
    }

    /**
     * Check whether the registry has been initialized.
     * 
     * @return true if registry is already initialized, false otherwise
     */
    public static synchronized boolean isInitialized() {
        return initialized;
    }

    /**
     * Initialize the registry.
     */
    public static synchronized void init() {
        if (isInitialized()) {
            return;
        }

        registry = new HashMap<Class<? extends Criteria>, Class<? extends EvaluableCredentialCriteria>>();

        loadDefaultMappings();

        initialized = true;
    }

    /**
     * Load the default set of criteria-evaluator mappings from the default mappings properties file.
     */
    public static synchronized void loadDefaultMappings() {
        Logger log = getLogger();
        log.debug("Loading default evaluable credential criteria mappings");
        InputStream inStream = EvaluableCredentialCriteriaRegistry.class.getResourceAsStream(DEFAULT_MAPPINGS_FILE);
        if (inStream == null) {
            log.error(String.format("Could not open resource stream from default mappings file '%s'",
                    DEFAULT_MAPPINGS_FILE));
            return;
        }

        Properties defaultMappings = new Properties();
        try {
            defaultMappings.load(inStream);
        } catch (IOException e) {
            log.error("Error loading properties file from resource stream", e);
            return;
        }

        loadMappings(defaultMappings);
    }

    /**
     * Load a set of criteria-evaluator mappings from the supplied properties set.
     * 
     * @param mappings properies set where the key is the criteria class name, the value is the evaluator class name
     */
    @SuppressWarnings("unchecked")
    public static synchronized void loadMappings(Properties mappings) {
        Logger log = getLogger();
        for (Object key : mappings.keySet()) {
            if (!(key instanceof String)) {
                log.error(String.format("Properties key was not an instance of String, was '%s', skipping...", key
                        .getClass().getName()));
                continue;
            }
            String criteriaName = (String) key;
            String evaluatorName = mappings.getProperty(criteriaName);

            ClassLoader classLoader = Configuration.class.getClassLoader();
            Class criteriaClass = null;
            try {
                criteriaClass = classLoader.loadClass(criteriaName);
            } catch (ClassNotFoundException e) {
                log.error(
                        String.format("Could not find criteria class name '%s', skipping registration", criteriaName),
                        e);
                return;
            }

            Class evaluableClass = null;
            try {
                evaluableClass = classLoader.loadClass(evaluatorName);
            } catch (ClassNotFoundException e) {
                log.error(String
                        .format("Could not find evaluator class name '%s', skipping registration", criteriaName), e);
                return;
            }

            register(criteriaClass, evaluableClass);
        }

    }
    
    /**
     * Get an SLF4J Logger.
     * 
     * @return a Logger instance
     */
    private static Logger getLogger() {
        return LoggerFactory.getLogger(EvaluableCredentialCriteriaRegistry.class);
    }

    static {
        init();
    }
}
