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

package org.opensaml.xml.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import net.jcip.annotations.NotThreadSafe;

import org.opensaml.xml.Configuration;
import org.opensaml.xml.NamespaceManager;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A map of attribute names and attribute values that invalidates the DOM of the attribute owning XMLObject when the
 * attributes change.
 * 
 * <strong>Note:</strong> 
 */
@NotThreadSafe
public class AttributeMap implements Map<QName, String> {
    
    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(AttributeMap.class);

    /** XMLObject owning the attributes. */
    private XMLObject attributeOwner;

    /** Map of attributes. */
    private Map<QName, String> attributes;
    
    /** Set of attribute QNames which have been locally registered as having an ID type within this 
     * AttributeMap instance. */
    private Set<QName> idAttribNames;
    
    /** Set of attribute QNames which have been locally registered as having an QName value type within this 
     * AttributeMap instance. */
    private Set<QName> qnameAttribNames;
    
    /** Flag indicating whether an attempt should be made to infer QName values, 
     * if attribute is not registered as a QName type. */
    private boolean inferQNameValues;

    /**
     * Constructor.
     *
     * @param newOwner the XMLObject that owns these attributes
     * 
     * @throws NullPointerException thrown if the given XMLObject is null
     */
    public AttributeMap(XMLObject newOwner) throws NullPointerException {
        if (newOwner == null) {
            throw new NullPointerException("Attribute owner XMLObject may not be null");
        }

        attributeOwner = newOwner;
        attributes = new LazyMap<QName, String>();
        idAttribNames = new LazySet<QName>();
        qnameAttribNames = new LazySet<QName>();
    }

    /** {@inheritDoc} */
    public String put(QName attributeName, String value) {
        String oldValue = get(attributeName);
        if (!DatatypeHelper.safeEquals(value, oldValue)) {
            releaseDOM();
            attributes.put(attributeName, value);
            if (isIDAttribute(attributeName) || Configuration.isIDAttribute(attributeName)) {
                attributeOwner.getIDIndex().deregisterIDMapping(oldValue);
                attributeOwner.getIDIndex().registerIDMapping(value, attributeOwner);
            }
            if (!DatatypeHelper.isEmpty(attributeName.getNamespaceURI())) {
                if (value == null) {
                    attributeOwner.getNamespaceManager().deregisterAttributeName(attributeName);
                } else {
                    attributeOwner.getNamespaceManager().registerAttributeName(attributeName);
                }
            }
            checkAndDeregisterQNameValue(attributeName, oldValue);
            checkAndRegisterQNameValue(attributeName, value);
        }
        
        return oldValue;
    }
    
    /**
     * Set an attribute value as a QName.  This method takes care of properly registering and 
     * deregistering the namespace information associated with the new QName being added, and
     * with the old QName being possibly removed.
     * 
     * @param attributeName the attribute name
     * @param value the QName attribute value
     * @return the old attribute value, possibly null
     */
    public QName put(QName attributeName, QName value) {
        String oldValueString = get(attributeName);
        
        QName oldValue = null;
        if (!DatatypeHelper.isEmpty(oldValueString)) {
            oldValue = resolveQName(oldValueString, true);
        }
        
        if (!DatatypeHelper.safeEquals(oldValue, value)) {
            releaseDOM();
            if (value != null) {
                // new value is not null, old value was either null or non-equal
                String newStringValue = constructAttributeValue(value);
                attributes.put(attributeName, newStringValue);
                registerQNameValue(attributeName, value);
                attributeOwner.getNamespaceManager().registerAttributeName(attributeName);
            } else {
                // new value is null, old value was not null
                deregisterQNameValue(attributeName);
                attributeOwner.getNamespaceManager().deregisterAttributeName(attributeName);
            }
        }
        
        return oldValue;
    }

    /** {@inheritDoc} */
    public void clear() {
        LazySet<QName> keys = new LazySet<QName>();
        keys.addAll(attributes.keySet());
        for (QName attributeName : keys) {
            remove(attributeName);
        }
    }

    /**
     * Returns the set of keys.
     * 
     * @return unmodifiable set of keys
     */
    public Set<QName> keySet() {
        return Collections.unmodifiableSet(attributes.keySet());
    }

    /** {@inheritDoc} */
    public int size() {
        return attributes.size();
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return attributes.containsKey(key);
    }

    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        return attributes.containsValue(value);
    }

    /** {@inheritDoc} */
    public String get(Object key) {
        return attributes.get(key);
    }

    /** {@inheritDoc} */
    public String remove(Object key) {
        String removedValue = attributes.remove(key);
        if (removedValue != null) {
            releaseDOM();
            QName attributeName = (QName) key;
            if (isIDAttribute(attributeName) || Configuration.isIDAttribute(attributeName)) {
                attributeOwner.getIDIndex().deregisterIDMapping(removedValue);
            }
            attributeOwner.getNamespaceManager().deregisterAttributeName(attributeName);
            checkAndDeregisterQNameValue(attributeName, removedValue);
        }

        return removedValue;
    }

    /** {@inheritDoc} */
    public void putAll(Map<? extends QName, ? extends String> t) {
        if (t != null && t.size() > 0) {
            for (Entry<? extends QName, ? extends String> entry : t.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Returns the values in this map.
     * 
     * @return an unmodifiable collection of values
     */
    public Collection<String> values() {
        return Collections.unmodifiableCollection(attributes.values());
    }

    /**
     * Returns the set of entries.
     * 
     * @return unmodifiable set of entries
     */
    public Set<Entry<QName, String>> entrySet() {
        return Collections.unmodifiableSet(attributes.entrySet());
    }
    
    /**
     * Register an attribute as having a type of ID.
     * 
     * @param attributeName the QName of the ID attribute to be registered
     */
    public void registerID(QName attributeName) {
        if (! idAttribNames.contains(attributeName)) {
            idAttribNames.add(attributeName);
        }
        
        // In case attribute already has a value,
        // register the current value mapping with the XMLObject owner.
        if (containsKey(attributeName)) {
            attributeOwner.getIDIndex().registerIDMapping(get(attributeName), attributeOwner);
        }
    }
    
    /**
     * Deregister an attribute as having a type of ID.
     * 
     * @param attributeName the QName of the ID attribute to be de-registered
     */
    public void deregisterID(QName attributeName) {
        if (idAttribNames.contains(attributeName)) {
            idAttribNames.remove(attributeName);
        }
        
        // In case attribute already has a value,
        // deregister the current value mapping with the XMLObject owner.
        if (containsKey(attributeName)) {
            attributeOwner.getIDIndex().deregisterIDMapping(get(attributeName));
        }
    }
    
    /**
     * Check whether a given attribute is locally registered as having an ID type within
     * this AttributeMap instance.
     * 
     * @param attributeName the QName of the attribute to be checked for ID type.
     * @return true if attribute is registered as having an ID type.
     */
    public boolean isIDAttribute(QName attributeName) {
        return idAttribNames.contains(attributeName);
    }
    
    /**
     * Register an attribute as having a type of QName.
     * 
     * @param attributeName the name of the QName-valued attribute to be registered
     */
    public void registerQNameAttribute(QName attributeName) {
        qnameAttribNames.add(attributeName);
    }
    
    /**
     * Deregister an attribute as having a type of QName.
     * 
     * @param attributeName the name of the QName-valued attribute to be registered
     */
    public void deregisterQNameAttribute(QName attributeName) {
        qnameAttribNames.remove(attributeName);
    }
    
    /**
     * Check whether a given attribute is known to have a QName type.
     * 
     * @param attributeName the QName of the attribute to be checked for QName type.
     * @return true if attribute is registered as having an QName type.
     */
    public boolean isQNameAttribute(QName attributeName) {
        return qnameAttribNames.contains(attributeName);
    }
    
    /**
     * Get the flag indicating whether an attempt should be made to infer QName values, 
     * if attribute is not registered via a configuration as a QName type. Default is false.
     * 
     * @return true if QName types should be inferred, false if not
     * 
     */
    public boolean isInferQNameValues() {
        return inferQNameValues;
    }
    
    /**
     * Set the flag indicating whether an attempt should be made to infer QName values, 
     * if attribute is not registered via a configuration as a QName type. Default is false.
     * 
     * @param flag true if QName types should be inferred, false if not
     * 
     */
    public void setInferQNameValues(boolean flag) {
        inferQNameValues = flag;
    }
    
    /**
     * Releases the DOM caching associated XMLObject and its ancestors.
     */
    private void releaseDOM() {
        attributeOwner.releaseDOM();
        attributeOwner.releaseParentDOM(true);
    }
    
    /**
     * Check whether the attribute value is a QName type, and if it is,
     * register it with the owner's namespace manger.
     * 
     * @param attributeName the attribute name
     * @param attributeValue the attribute value
     */
    private void checkAndRegisterQNameValue(QName attributeName, String attributeValue) {
        if (attributeValue == null) {
            return;
        }
        
        QName qnameValue = checkQName(attributeName, attributeValue);
        if (qnameValue != null) {
            log.trace("Attribute '{}' with value '{}' was evaluated to be QName type", 
                    attributeName, attributeValue);
            registerQNameValue(attributeName, qnameValue);
        } else {
            log.trace("Attribute '{}' with value '{}' was not evaluated to be QName type", 
                    attributeName, attributeValue);
        }
        
    }
    
    /**
     * Register a QName attribute value with the owner's namespace manger.
     * 
     * @param attributeName the attribute name
     * @param attributeValue the attribute value
     */
    private void registerQNameValue(QName attributeName, QName attributeValue) {
        if (attributeValue == null) {
            return;
        }
        
        String attributeID = NamespaceManager.generateAttributeID(attributeName);
        log.trace("Registering QName attribute value '{}' under attibute ID '{}'",
                attributeValue, attributeID);
        attributeOwner.getNamespaceManager().registerAttributeValue(attributeID, attributeValue);
    }
    
    /**
     * Check whether the attribute value is a QName type, and if it is,
     * deregister it with the owner's namespace manger.
     * 
     * @param attributeName the attribute name
     * @param attributeValue the attribute value
     */
    private void checkAndDeregisterQNameValue(QName attributeName, String attributeValue) {
        if (attributeValue == null) {
            return;
        }
        
        QName qnameValue = checkQName(attributeName, attributeValue);
        if (qnameValue != null) {
            log.trace("Attribute '{}' with value '{}' was evaluated to be QName type", 
                    attributeName, attributeValue);
            deregisterQNameValue(attributeName);
        } else {
            log.trace("Attribute '{}' with value '{}' was not evaluated to be QName type", 
                    attributeName, attributeValue);
        }
    }
    
    /**
     * Deregister a QName attribute value with the owner's namespace manger.
     * 
     * @param attributeName the attribute name whose QName attribute value should be deregistered
     */
    private void deregisterQNameValue(QName attributeName) {
        String attributeID = NamespaceManager.generateAttributeID(attributeName);
        log.trace("Deregistering QName attribute with attibute ID '{}'", attributeID);
        attributeOwner.getNamespaceManager().deregisterAttributeValue(attributeID);
    }
    
    /**
     * Check where the attribute value is a QName type, and if so, return the QName.
     * 
     * @param attributeName the attribute name
     * @param attributeValue the attribute value
     * @return the QName if the attribute value is a QName type, otherwise null
     */
    private QName checkQName(QName attributeName, String attributeValue) {
        log.trace("Checking whether attribute '{}' with value {} is a QName type", attributeName, attributeValue);
        
        if (attributeValue == null) {
            log.trace("Attribute value was null, returning null");
            return null;
        }
        
        if (isQNameAttribute(attributeName)) {
            log.trace("Configuration indicates attribute with name '{}' is a QName type, resolving value QName", 
                    attributeName);
            // Do support the default namespace in this scenario, since we know it should be a QName
            QName valueName = resolveQName(attributeValue, true);
            if (valueName != null) {
                log.trace("Successfully resolved attribute value to QName: {}", valueName);
            } else {
                log.trace("Could not resolve attribute value to QName, returning null");
            }
            return valueName;
        } else if (isInferQNameValues()) {
            log.trace("Attempting to infer whether attribute value is a QName");
            // Do not support the default namespace in this scenario, since we're trying to infer.
            // Better to fail to resolve than to infer a bogus QName value.
            QName valueName = resolveQName(attributeValue, false);
            if (valueName != null) {
                log.trace("Resolved attribute as a QName: '{}'", valueName);
            } else {
                log.trace("Attribute value was not resolveable to a QName, returning null");
            }
            return valueName;
        } else {
            log.trace("Attribute was not registered in configuration as a QName type and QName inference is disabled");
            return null;
        }

    }
    
    /**
     * Attempt to resolve the specified attribute value into a QName.
     * 
     * @param attributeValue the value to evaluate
     * @param isDefaultNSOK flag indicating whether resolution should be attempted if the prefix is null, 
     *           that is, the value is considered to be be potentially in the default XML namespace
     * 
     * @return the QName, or null if unable to resolve into a QName
     */
    private QName resolveQName(String attributeValue, boolean isDefaultNSOK) {
        if (attributeValue == null) {
            return null;
        }
        log.trace("Attemtping to resolve QName from attribute value '{}'", attributeValue);
        
        // Attempt to resolve value as a QName by splitting on colon and then attempting to resolve
        // this candidate prefix into a namespace URI. 
        String candidatePrefix = null;
        String localPart = null;
        int ci = attributeValue.indexOf(':');
        if (ci > -1) {
            candidatePrefix = attributeValue.substring(0, ci);
            log.trace("Evaluating candiate namespace prefix '{}'", candidatePrefix);
            localPart = attributeValue.substring(ci+1);
        } else {
            // No prefix - possibly evaluate as if in the default namespace
            if (isDefaultNSOK) {
                candidatePrefix = null;
                log.trace("Value did not contain a colon, evaluating as default namespace");
                localPart = attributeValue;
            } else {
                log.trace("Value did not contain a colon, default namespace is disallowed, returning null");
                return null;
            }
        }
        
        log.trace("Evaluated QName local part as '{}'", localPart);
        
        String nsURI = XMLObjectHelper.lookupNamespaceURI(attributeOwner, candidatePrefix);
        log.trace("Resolved namespace URI '{}'", nsURI);
        if (nsURI != null) {
            QName name = XMLHelper.constructQName(nsURI, localPart, candidatePrefix);
            log.trace("Resolved QName '{}'", name);
            return name;
        } else {
            log.trace("Namespace URI for candidate prefix '{}' could not be resolved", candidatePrefix);
        }
        
        log.trace("Value was either not a QName, or namespace URI could not be resolved");
        
        return null;
    }
    
    /**
     * Construct the string representation of a QName attribute value.
     * 
     * @param attributeValue the QName to process
     * @return the attribute value string representation of the QName
     */
    private String constructAttributeValue(QName attributeValue) {
        String trimmedLocalName = DatatypeHelper.safeTrimOrNullString(attributeValue.getLocalPart());

        if (trimmedLocalName == null) {
            throw new IllegalArgumentException("Local name may not be null or empty");
        }

        String qualifiedName;
        String trimmedPrefix = DatatypeHelper.safeTrimOrNullString(attributeValue.getPrefix());
        if (trimmedPrefix != null) {
            qualifiedName = trimmedPrefix + ":" + DatatypeHelper.safeTrimOrNullString(trimmedLocalName);
        } else {
            qualifiedName = DatatypeHelper.safeTrimOrNullString(trimmedLocalName);
        }
        return qualifiedName;
    }

}