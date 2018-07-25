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

package org.opensaml.saml2.metadata.provider;

import java.util.HashSet;
import java.util.Iterator;

import org.opensaml.saml2.metadata.AffiliationDescriptor;
import org.opensaml.saml2.metadata.EntitiesDescriptor;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.criteria.UsageCriteria;
import org.opensaml.xml.signature.SignableXMLObject;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureTrustEngine;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A metadata filter that validates XML signatures.
 */
public class SignatureValidationFilter implements MetadataFilter {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(SignatureValidationFilter.class);

    /** Trust engine used to validate a signature. */
    private SignatureTrustEngine signatureTrustEngine;

    /** Indicates whether signed metadata is required. */
    private boolean requireSignature;
    
    /** Set of externally specified default criteria for input to the trust engine. */
    private CriteriaSet defaultCriteria;
    
    /** Pre-validator for XML Signature instances. */
    private Validator<Signature> sigValidator;

    /**
     * Constructor.
     * 
     * @param engine the trust engine used to validate signatures on incoming metadata.
     */
    public SignatureValidationFilter(SignatureTrustEngine engine) {
        if (engine == null) {
            throw new IllegalArgumentException("Signature trust engine may not be null");
        }

        signatureTrustEngine = engine;
        sigValidator = new SAMLSignatureProfileValidator();
    }
    
    /**
     * Constructor.
     * 
     * @param engine the trust engine used to validate signatures on incoming metadata.
     * @param signatureValidator optional pre-validator used to validate Signature elements prior to the actual
     *            cryptographic validation operation
     */
    public SignatureValidationFilter(SignatureTrustEngine engine, Validator<Signature> signatureValidator) {
        if (engine == null) {
            throw new IllegalArgumentException("Signature trust engine may not be null");
        }

        signatureTrustEngine = engine;
        sigValidator = signatureValidator;
    }

    /**
     * Gets the trust engine used to validate signatures on incoming metadata.
     * 
     * @return trust engine used to validate signatures on incoming metadata
     */
    public SignatureTrustEngine getSignatureTrustEngine() {
        return signatureTrustEngine;
    }
    
    /**
     * Get the validator used to perform pre-validation on Signature tokens.
     * 
     * @return the configured Signature validator, or null
     */
    public Validator<Signature> getSignaturePrevalidator() {
        return sigValidator;
    }

    /**
     * Gets whether incoming metadata's root element is required to be signed.
     * 
     * @return whether incoming metadata is required to be signed
     */
    public boolean getRequireSignature() {
        return requireSignature;
    }

    /**
     * Sets whether incoming metadata's root element is required to be signed.
     * 
     * @param require whether incoming metadata is required to be signed
     */
    public void setRequireSignature(boolean require) {
        requireSignature = require;
    }
 
    /**
     * Get the set of default criteria used as input to the trust engine.
     * 
     * @return the criteria set
     */
    public CriteriaSet getDefaultCriteria() {
        return defaultCriteria;
    }
    
    /**
     * Set the set of default criteria used as input to the trust engine.
     * 
     * @param newCriteria the new criteria set to use
     */
    public void setDefaultCriteria(CriteriaSet newCriteria) {
        defaultCriteria = newCriteria;
    }

    /** {@inheritDoc} */
    public void doFilter(XMLObject metadata) throws FilterException {
        SignableXMLObject signableMetadata = (SignableXMLObject) metadata;

        if (!signableMetadata.isSigned()){
            if (getRequireSignature()) {
                throw new FilterException("Metadata root element was unsigned and signatures are required.");
            }
        }
        
        if (signableMetadata instanceof EntityDescriptor) {
            processEntityDescriptor((EntityDescriptor) signableMetadata);
        } else if (signableMetadata instanceof EntitiesDescriptor) {
            processEntityGroup((EntitiesDescriptor) signableMetadata);
        } else {
            log.error("Internal error, metadata object was of an unsupported type: {}", metadata.getClass().getName());
        }
    }
    
    /**
     * Process the signatures on the specified EntityDescriptor and any signed children.
     * 
     * If signature verification fails on a child, it will be removed from the entity descriptor.
     * 
     * @param entityDescriptor the EntityDescriptor to be processed
     * @throws FilterException thrown if an error occurs during the signature verification process
     *                          on the root EntityDescriptor specified
     */
    protected void processEntityDescriptor(EntityDescriptor entityDescriptor) throws FilterException {
        String entityID = entityDescriptor.getEntityID();
        log.trace("Processing EntityDescriptor: {}", entityID);
        
        if (entityDescriptor.isSigned()) {
            verifySignature(entityDescriptor, entityID, false);
        }
        
        Iterator<RoleDescriptor> roleIter = entityDescriptor.getRoleDescriptors().iterator();
        while (roleIter.hasNext()) {
           RoleDescriptor roleChild = roleIter.next(); 
            if (!roleChild.isSigned()) {
                log.trace("RoleDescriptor member '{}' was not signed, skipping signature processing...",
                        roleChild.getElementQName());
                continue;
            } else {
                log.trace("Processing signed RoleDescriptor member: {}", roleChild.getElementQName());
            }
            
            try {
                String roleID = getRoleIDToken(entityID, roleChild);
                verifySignature(roleChild, roleID, false);
            } catch (FilterException e) {
                log.error("RoleDescriptor '{}' subordinate to entity '{}' failed signature verification, " 
                       + "removing from metadata provider", 
                       roleChild.getElementQName(), entityID); 
                // Note that this is ok since we're iterating over an IndexedXMLObjectChildrenList directly,
                // rather than a sublist like in processEntityGroup, and iterator remove() is supported there.
               roleIter.remove();
            }
        }
        
        if (entityDescriptor.getAffiliationDescriptor() != null) {
            AffiliationDescriptor affiliationDescriptor = entityDescriptor.getAffiliationDescriptor();
            if (!affiliationDescriptor.isSigned()) {
                log.trace("AffiliationDescriptor member was not signed, skipping signature processing...");
            } else {
                log.trace("Processing signed AffiliationDescriptor member with owner ID: {}", 
                        affiliationDescriptor.getOwnerID());
                
                try {
                    verifySignature(affiliationDescriptor, affiliationDescriptor.getOwnerID(), false);
                } catch (FilterException e) {
                    log.error("AffiliationDescriptor with owner ID '{}' subordinate to entity '{}' " + 
                            "failed signature verification, removing from metadata provider", 
                            affiliationDescriptor.getOwnerID(), entityID); 
                    entityDescriptor.setAffiliationDescriptor(null);
                }
                
            }
        }
    }
 
    
    /**
     * Process the signatures on the specified EntitiesDescriptor and any signed children.
     * 
     * If signature verification fails on a child, it will be removed from the entities descriptor group.
     * 
     * @param entitiesDescriptor the EntitiesDescriptor to be processed
     * @throws FilterException thrown if an error occurs during the signature verification process
     *                          on the root EntitiesDescriptor specified
     */
    protected void processEntityGroup(EntitiesDescriptor entitiesDescriptor) throws FilterException {
        log.trace("Processing EntitiesDescriptor group: {}", entitiesDescriptor.getName());
        
        if (entitiesDescriptor.isSigned()) {
            verifySignature(entitiesDescriptor, entitiesDescriptor.getName(), true);
        }
        
        // Can't use IndexedXMLObjectChildrenList sublist iterator remove() to remove members,
        // so just note them in a set and then remove after iteration has completed.
        HashSet<XMLObject> toRemove = new HashSet<XMLObject>();
        
        Iterator<EntityDescriptor> entityIter = entitiesDescriptor.getEntityDescriptors().iterator();
        while (entityIter.hasNext()) {
            EntityDescriptor entityChild = entityIter.next();
            if (!entityChild.isSigned()) {
                log.trace("EntityDescriptor member '{}' was not signed, skipping signature processing...",
                        entityChild.getEntityID());
                continue;
            } else {
                log.trace("Processing signed EntityDescriptor member: {}", entityChild.getEntityID());
            }
            
            try {
                processEntityDescriptor(entityChild);
            } catch (FilterException e) {
               log.error("EntityDescriptor '{}' failed signature verification, removing from metadata provider", 
                       entityChild.getEntityID()); 
               toRemove.add(entityChild);
            }
        }
        
        if (!toRemove.isEmpty()) {
            entitiesDescriptor.getEntityDescriptors().removeAll(toRemove);
            toRemove.clear();
        }
        
        Iterator<EntitiesDescriptor> entitiesIter = entitiesDescriptor.getEntitiesDescriptors().iterator();
        while(entitiesIter.hasNext()) {
            EntitiesDescriptor entitiesChild = entitiesIter.next();
            log.trace("Processing EntitiesDescriptor member: {}", entitiesChild.getName());
            try {
                processEntityGroup(entitiesChild);
            } catch (FilterException e) {
               log.error("EntitiesDescriptor '{}' failed signature verification, removing from metadata provider", 
                       entitiesChild.getName()); 
               toRemove.add(entitiesChild);
            }
        }
        
        if (!toRemove.isEmpty()) {
            entitiesDescriptor.getEntitiesDescriptors().removeAll(toRemove);
        }
    }

    /**
     * Evaluate the signature on the signed metadata instance.
     * 
     * @param signedMetadata the metadata object whose signature is to be verified
     * @param metadataEntryName the EntityDescriptor entityID, EntitiesDescriptor Name,
     *                          AffiliationDescriptor affiliationOwnerID, 
     *                          or RoleDescriptor {@link #getRoleIDToken(String, RoleDescriptor)}
     *                          corresponding to the element whose signature is being evaluated.
     *                          This is used exclusively for logging/debugging purposes and
     *                          should not be used operationally (e.g. for building a criteria set).
     * @param isEntityGroup flag indicating whether the signed object is a metadata group (EntitiesDescriptor),
     *                      primarily useful for constructing a criteria set for the trust engine
     * @throws FilterException thrown if the metadata entry's signature can not be established as trusted,
     *                         or if an error occurs during the signature verification process
     */
    protected void verifySignature(SignableXMLObject signedMetadata, String metadataEntryName, 
            boolean isEntityGroup) throws FilterException {
        
        log.debug("Verifying signature on metadata entry: {}", metadataEntryName);
        
        Signature signature = signedMetadata.getSignature();
        if (signature == null) {
            // We shouldn't ever be calling this on things that aren't actually signed, but just to be safe...
            log.warn("Signature was null, skipping processing on metadata entry: {}", metadataEntryName);
            return;
        }
        
        performPreValidation(signature, metadataEntryName);
        
        CriteriaSet criteriaSet = buildCriteriaSet(signedMetadata, metadataEntryName, isEntityGroup);
        
        try {
            if ( getSignatureTrustEngine().validate(signature, criteriaSet) ) {
                log.trace("Signature trust establishment succeeded for metadata entry {}", metadataEntryName);
                return;
            } else {
                log.error("Signature trust establishment failed for metadata entry {}", metadataEntryName);
                throw new FilterException("Signature trust establishment failed for metadata entry");
            }
        } catch (SecurityException e) {
            // Treat evaluation errors as fatal
            log.error("Error processing signature verification for metadata entry '{}': {} ",
                    metadataEntryName, e.getMessage());
            throw new FilterException("Error processing signature verification for metadata entry", e);
        }
    }

    /**
     * Perform pre-validation on the Signature token.
     * 
     * @param signature the signature to evaluate
     * @param metadataEntryName the EntityDescriptor entityID, EntitiesDescriptor Name,
     *                          AffiliationDescriptor affiliationOwnerID, 
     *                          or RoleDescriptor {@link #getRoleIDToken(String, RoleDescriptor)}
     *                          corresponding to the element whose signature is being evaluated.
     *                          This is used exclusively for logging/debugging purposes and
     *                          should not be used operationally (e.g. for building a criteria set).
     * @throws FilterException thrown if the signature element fails pre-validation
     */
    protected void performPreValidation(Signature signature, String metadataEntryName) throws FilterException {
        if (getSignaturePrevalidator() != null) {
            try {
                getSignaturePrevalidator().validate(signature);
            } catch (ValidationException e) {
                log.error("Signature on metadata entry '{}' failed signature pre-validation", metadataEntryName);
                throw new FilterException("Metadata instance signature failed signature pre-validation", e);
            }
        }
    }
    
    /**
     * Build the criteria set which will be used as input to the configured trust engine.
     * 
     * @param signedMetadata the metadata element whose signature is being verified
     * @param metadataEntryName the EntityDescriptor entityID, EntitiesDescriptor Name,
     *                          AffiliationDescriptor affiliationOwnerID, 
     *                          or RoleDescriptor {@link #getRoleIDToken(String, RoleDescriptor)}
     *                          corresponding to the element whose signature is being evaluated.
     *                          This is used exclusively for logging/debugging purposes and
     *                          should not be used operationally (e.g. for building the criteria set).
     * @param isEntityGroup flag indicating whether the signed object is a metadata group (EntitiesDescriptor)
     * @return the newly constructed criteria set
     */
    protected CriteriaSet buildCriteriaSet(SignableXMLObject signedMetadata,
            String metadataEntryName, boolean isEntityGroup) {
        
        CriteriaSet newCriteriaSet = new CriteriaSet();
        
        if (getDefaultCriteria() != null) {
            newCriteriaSet.addAll( getDefaultCriteria() );
        }
        
        if (!newCriteriaSet.contains(UsageCriteria.class)) {
            newCriteriaSet.add( new UsageCriteria(UsageType.SIGNING) );
        }
        
        // TODO how to handle adding dynamic entity ID and/or other criteria for trust engine consumption?
        //
        // Have 4 signed metadata types:
        // 1) EntitiesDescriptor
        // 2) EntityDescriptor
        // 3) RoleDescriptor
        // 4) AffiliationDescriptor
        //
        // Logic will likely vary for how to specify criteria to trust engine for different types + specific use cases,
        // e.g. for federation metadata publishers of EntitiesDescriptors vs. "self-signed" EntityDescriptors.
        // May need to delegate to more specialized subclasses.
        
        return newCriteriaSet;
    }
    
    /**
     * Get a string token for logging/debugging purposes that contains role information and containing entityID.
     * 
     * @param entityID the containing entityID
     * @param role the role descriptor
     * 
     * @return the constructed role ID token.
     */
    protected String getRoleIDToken(String entityID, RoleDescriptor role) {
        String roleName = role.getElementQName().getLocalPart();
        return "[Role: " + entityID + "::" + roleName + "]";
    }
}