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

package org.opensaml.xml.io;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This thread-safe factory creates {@link org.opensaml.xml.io.Marshaller}s that can be used to convert
 * {@link org.opensaml.xml.XMLObject}s into W3C DOM elements. Marshallers are stored and retrieved by a
 * {@link javax.xml.namespace.QName} key. This key is either the XML Schema Type or element QName of the XML element the
 * XMLObject is marshalled into.
 */
public class MarshallerFactory {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(MarshallerFactory.class);

    /** Map of marshallers to the elements they are for. */
    private Map<QName, Marshaller> marshallers;

    /**
     * Constructor.
     */
    public MarshallerFactory() {
        marshallers = new ConcurrentHashMap<QName, Marshaller>();
    }

    /**
     * Gets the Marshaller for a particular element or null if no marshaller is registered for an element.
     * 
     * @param key the key the marshaller was registered under
     * 
     * @return the Marshaller or null
     */
    public Marshaller getMarshaller(QName key) {
        if (key == null) {
            return null;
        }

        return marshallers.get(key);
    }

    /**
     * Retrieves the marshaller for the given XMLObject. The schema type, if present, is tried first as the key with the
     * element QName used if no schema type is present or does not have a marshaller registered under it.
     * 
     * @param xmlObject the XMLObject to retrieve the marshaller for
     * 
     * @return the marshaller that can be used for the given XMLObject
     */
    public Marshaller getMarshaller(XMLObject xmlObject) {
        Marshaller marshaller;

        marshaller = getMarshaller(xmlObject.getSchemaType());

        if (marshaller == null) {
            marshaller = getMarshaller(xmlObject.getElementQName());
        }

        return marshaller;
    }

    /**
     * Gets an immutable listing of all the Marshallers currently registered.
     * 
     * @return a listing of all the Marshallers currently registered
     */
    public Map<QName, Marshaller> getMarshallers() {
        return Collections.unmodifiableMap(marshallers);
    }

    /**
     * Registers a Marshaller with this factory. If a Marshaller exist for the element name given it is replaced with
     * the given marshaller.
     * 
     * @param key the key the marshaller was registered under
     * @param marshaller the Marshaller
     */
    public void registerMarshaller(QName key, Marshaller marshaller) {
        log.debug("Registering marshaller, {}, for object type {}", marshaller.getClass().getName(), key);
        if(key == null){
            throw new IllegalArgumentException("Marshaller key may not be null");
        }
        marshallers.put(key, marshaller);
    }

    /**
     * Deregisters the marshaller for the given element.
     * 
     * @param key the key the marshaller was registered under
     * 
     * @return the Marshaller previously registered or null
     */
    public Marshaller deregisterMarshaller(QName key) {
        log.debug("Deregistering marshaller for object type {}", key);
        if(key != null){
            return marshallers.remove(key);
        }
        
        return null;
    }
}