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

package org.opensaml.xml.security.credential;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorFactory;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.impl.KeyInfoBuilder;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A factory implementation which produces instances of {@link KeyInfoGenerator} capable of 
 * handling the information contained within a {@link Credential}.
 * 
 * All boolean options default to false.
 */
public class BasicKeyInfoGeneratorFactory implements KeyInfoGeneratorFactory {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(BasicKeyInfoGeneratorFactory.class);
    
    /** The set of options configured for the factory. */
    private BasicOptions options;
    
    /**
     * Constructor.
     * 
     * All boolean options are initialzed as false;
     */
    public BasicKeyInfoGeneratorFactory() {
        options = newOptions();
    }
    
    /** {@inheritDoc} */
    public Class<? extends Credential> getCredentialType() {
        return Credential.class;
    }

    /** {@inheritDoc} */
    public boolean handles(Credential credential) {
        // This top-level class can handle any Credential type, with output limited to basic Credential information
        return true;
    }

    /** {@inheritDoc} */
    public KeyInfoGenerator newInstance() {
        //TODO lock options during cloning ?
        BasicOptions newOptions = options.clone();
        return new BasicKeyInfoGenerator(newOptions);
    }
    
    /**
     * Get the option to emit the entity ID value in a Credential as a KeyName element.
     * 
     * @return return the option value
     */
    public boolean emitEntityIDAsKeyName() {
        return options.emitEntityIDAsKeyName;
    }

    /**
     * Set the option to emit the entity ID value in a Credential as a KeyName element.
     * 
     * @param newValue the new option value to set
     */
    public void setEmitEntityIDAsKeyName(boolean newValue) {
        options.emitEntityIDAsKeyName = newValue;
    }

    /**
     * Get the option to emit key names found in a Credential as KeyName elements.
     * 
     * @return the option value
     */
    public boolean emitKeyNames() {
        return options.emitKeyNames;
    }

    /**
     * Set the option to emit key names found in a Credential as KeyName elements.
     * 
     * @param newValue the new option value to set
     */
    public void setEmitKeyNames(boolean newValue) {
        options.emitKeyNames = newValue;
    }

    /**
     * Get the option to emit the value of {@link Credential#getPublicKey()} as a KeyValue element.
     * 
     * @return the option value
     */
    public boolean emitPublicKeyValue() {
        return options.emitPublicKeyValue;
    }

    /**
     * Set the option to emit the value of {@link Credential#getPublicKey()} as a KeyValue element.
     * 
     * @param newValue the new option value to set
     */
    public void setEmitPublicKeyValue(boolean newValue) {
        options.emitPublicKeyValue = newValue;        
    }

    /**
     * Get the option to emit the value of {@link Credential#getPublicKey()} as a DEREncodedKeyValue element.
     * 
     * @return the option value
     */
    public boolean emitPublicDEREncodedKeyValue() {
        return options.emitPublicDEREncodedKeyValue;
    }

    /**
     * Set the option to emit the value of {@link Credential#getPublicKey()} as a DEREncodedKeyValue element.
     * 
     * @param newValue the new option value to set
     */
    public void setEmitPublicDEREncodedKeyValue(boolean newValue) {
        options.emitPublicDEREncodedKeyValue = newValue;
    }
    
    /**
     * Get a new instance to hold options.  Used by the top-level superclass constructor.
     * Subclasses <strong>MUST</strong> override to produce an instance of the appropriate 
     * subclass of {@link BasicOptions}.
     * 
     * @return a new instance of factory/generator options
     */
    protected BasicOptions newOptions() {
        return new BasicOptions();
    }
    
    /**
     * Get the options of this instance. Used by subclass constructors to get the options built by 
     * the top-level class constructor with {@link #newOptions()}.
     * 
     * @return the options instance
     */
    protected BasicOptions getOptions() {
        return options;
    }
    
    /**
     * An implementation of {@link KeyInfoGenerator} capable of  handling the information 
     * contained within a {@link Credential}.
    */
    public class BasicKeyInfoGenerator implements KeyInfoGenerator {
        
        /** The set of options to be used by the generator.*/
        private BasicOptions options;
       
        /** Builder for KeyInfo objects. */
        private KeyInfoBuilder keyInfoBuilder;
       
        /**
         * Constructor.
         * 
         * @param newOptions the options to be used by the generator
         */
        protected BasicKeyInfoGenerator(BasicOptions newOptions) {
            options = newOptions;
            keyInfoBuilder = 
                (KeyInfoBuilder) Configuration.getBuilderFactory().getBuilder(KeyInfo.DEFAULT_ELEMENT_NAME);
        }

        /** {@inheritDoc} */
        public KeyInfo generate(Credential credential) throws SecurityException {
            KeyInfo keyInfo = keyInfoBuilder.buildObject();
            
            processKeyNames(keyInfo, credential);
            processEntityID(keyInfo, credential);
            processPublicKey(keyInfo, credential);
            
            List<XMLObject> children = keyInfo.getOrderedChildren();
            if (children != null && children.size() > 0) {
                return keyInfo;
            } else {
                return null;
            }
        }
        
        /** Process the values of {@link Credential#getKeyNames()}.
         * 
         * @param keyInfo the KeyInfo that is being built
         * @param credential the Credential that is geing processed
         */
        protected void processKeyNames(KeyInfo keyInfo, Credential credential) {
            if (options.emitKeyNames) {
                for (String keyNameValue : credential.getKeyNames()) {
                    if ( ! DatatypeHelper.isEmpty(keyNameValue)) {
                        KeyInfoHelper.addKeyName(keyInfo, keyNameValue);
                    }
                }
            }
        }
        
        /** Process the value of {@link Credential#getEntityId()}.
         * 
         * @param keyInfo the KeyInfo that is being built
         * @param credential the Credential that is geing processed
         */
        protected void processEntityID(KeyInfo keyInfo, Credential credential) {
            if (options.emitEntityIDAsKeyName) {
                String keyNameValue = credential.getEntityId();
                if ( ! DatatypeHelper.isEmpty(keyNameValue)) {
                    KeyInfoHelper.addKeyName(keyInfo, keyNameValue);
                }
            }
        }
        
        /** Process the value of {@link Credential#getPublicKey()}.
         * 
         * @param keyInfo the KeyInfo that is being built
         * @param credential the Credential that is geing processed
         */
        protected void processPublicKey(KeyInfo keyInfo, Credential credential) {
            if (credential.getPublicKey() != null) {
                if (options.emitPublicKeyValue) {
                    KeyInfoHelper.addPublicKey(keyInfo, credential.getPublicKey());
                }
                if (options.emitPublicDEREncodedKeyValue) {
                    try {
                        KeyInfoHelper.addDEREncodedPublicKey(keyInfo, credential.getPublicKey());
                    } catch (NoSuchAlgorithmException e) {
                        // TODO: should wrap in SecurityException once API can be changed
                        log.error("Can't DER-encode key, unsupported key algorithm", e);
                    } catch (InvalidKeySpecException e) {
                        // TODO: should wrap in SecurityException once API can be changed
                        log.error("Can't DER-encode key, invalid key specification", e);
                    }
                }
            }
        }
    }
    
    /**
     * Options to be used in the production of a {@link KeyInfo} from a {@link Credential}.
     */
    protected class BasicOptions implements Cloneable {
        
        /** Emit key names found in a Credential as KeyName elements. */
        private boolean emitKeyNames;
        
        /** Emit the entity ID value in a Credential as a KeyName element. */
        private boolean emitEntityIDAsKeyName;
        
        /** Emit the value of {@link Credential#getPublicKey()} as a KeyValue element. */
        private boolean emitPublicKeyValue;
        
        /** Emit the value of {@link Credential#getPublicKey()} as a DEREncodedKeyValue element. */
        private boolean emitPublicDEREncodedKeyValue;
        
        /** {@inheritDoc} */
        protected BasicOptions clone() {
            try {
                return (BasicOptions) super.clone();
            } catch (CloneNotSupportedException e) {
                // we know we're cloneable, so this will never happen
                return null;
            }
        }
        
    }

}