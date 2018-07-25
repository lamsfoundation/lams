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

package org.opensaml.security;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.Reference;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transform;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.IdResolver;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.impl.SignatureImpl;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A validator for instances of {@link Signature}, which validates that the signature meets security-related
 * requirements indicated by the SAML profile of XML Signature.
 */
public class SAMLSignatureProfileValidator implements Validator<Signature> {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(SAMLSignatureProfileValidator.class);

    /** {@inheritDoc} */
    public void validate(Signature signature) throws ValidationException {

        if (!(signature instanceof SignatureImpl)) {
            log.info("Signature was not an instance of SignatureImpl, was {} validation not supported", signature
                    .getClass().getName());
            return;
        }

        validateSignatureImpl((SignatureImpl) signature);
    }

    /**
     * Validate an instance of {@link SignatureImpl}, which is in turn based on underlying Apache XML Security
     * <code>XMLSignature</code> instance.
     * 
     * @param sigImpl the signature implementation object to validate
     * @throws ValidationException thrown if the signature is not valid with respect to the profile
     */
    protected void validateSignatureImpl(SignatureImpl sigImpl) throws ValidationException {

        if (sigImpl.getXMLSignature() == null) {
            log.error("SignatureImpl did not contain the an Apache XMLSignature child");
            throw new ValidationException("Apache XMLSignature does not exist on SignatureImpl");
        }
        XMLSignature apacheSig = sigImpl.getXMLSignature();

        if (!(sigImpl.getParent() instanceof SignableSAMLObject)) {
            log.error("Signature is not an immedidate child of a SignableSAMLObject");
            throw new ValidationException("Signature is not an immediate child of a SignableSAMLObject.");
        }
        SignableSAMLObject signableObject = (SignableSAMLObject) sigImpl.getParent();

        Reference ref = validateReference(apacheSig);

        String uri = ref.getURI();
        
        validateReferenceURI(uri, signableObject);

        validateTransforms(ref);
        
        validateObjectChildren(apacheSig);
    }

    /**
     * Validate the Signature's SignedInfo Reference.
     * 
     * The SignedInfo must contain exactly 1 Reference.
     * 
     * @param apacheSig the Apache XML Signature instance
     * @return the valid Reference contained within the SignedInfo
     * @throws ValidationException thrown if the Signature does not contain exactly 1 Reference, or if there is an error
     *             obtaining the Reference instance
     */
    protected Reference validateReference(XMLSignature apacheSig) throws ValidationException {
        int numReferences = apacheSig.getSignedInfo().getLength();
        if (numReferences != 1) {
            log.error("Signature SignedInfo had invalid number of References: " + numReferences);
            throw new ValidationException("Signature SignedInfo must have exactly 1 Reference element");
        }

        Reference ref = null;
        try {
            ref = apacheSig.getSignedInfo().item(0);
        } catch (XMLSecurityException e) {
            log.error("Apache XML Security exception obtaining Reference", e);
            throw new ValidationException("Could not obtain Reference from Signature/SignedInfo", e);
        }
        if (ref == null) {
            log.error("Signature Reference was null");
            throw new ValidationException("Signature Reference was null");
        }
        return ref;
    }
    
    /**
     * Validate the Signature's Reference URI.
     * 
     * First validate the Reference URI against the parent's ID itself.  Then validate that the 
     * URI (if non-empty) resolves to the same Element node as is cached by the SignableSAMLObject.
     * 
     * 
     * @param uri the Signature Reference URI attribute value
     * @param signableObject the SignableSAMLObject whose signature is being validated
     * @throws ValidationException  if the URI is invalid or doesn't resolve to the expected DOM node
     */
    protected void validateReferenceURI(String uri, SignableSAMLObject signableObject) throws ValidationException {
        String id = signableObject.getSignatureReferenceID();
        validateReferenceURI(uri, id);
        
        if (DatatypeHelper.isEmpty(uri)) {
            return;
        }
        
        String uriID = uri.substring(1);
        
        Element expected = signableObject.getDOM();
        if (expected == null) {
            log.error("SignableSAMLObject does not have a cached DOM Element.");
            throw new ValidationException("SignableSAMLObject does not have a cached DOM Element.");
        }
        Document doc = expected.getOwnerDocument();
        
        Element resolved = IdResolver.getElementById(doc, uriID);
        if (resolved == null) {
            log.error("Apache xmlsec IdResolver could not resolve the Element for id reference: {}", uriID);
            throw new ValidationException("Apache xmlsec IdResolver could not resolve the Element for id reference: "
                    +  uriID);
        }
        
        if (!expected.isSameNode(resolved)) {
            log.error("Signature Reference URI '{}' did not resolve to the expected parent Element", uri);
            throw new ValidationException("Signature Reference URI did not resolve to the expected parent Element");
        }
    }

    /**
     * Validate the Reference URI and parent ID attribute values.
     * 
     * The URI must either be null or empty (indicating that the entire enclosing document was signed), or else it must
     * be a local document fragment reference and point to the SAMLObject parent via the latter's ID attribute value.
     * 
     * @param uri the Signature Reference URI attribute value
     * @param id the Signature parents ID attribute value
     * @throws ValidationException thrown if the URI or ID attribute values are invalid
     */
    protected void validateReferenceURI(String uri, String id) throws ValidationException {
        if (!DatatypeHelper.isEmpty(uri)) {
            if (!uri.startsWith("#")) {
                log.error("Signature Reference URI was not a document fragment reference: " + uri);
                throw new ValidationException("Signature Reference URI was not a document fragment reference");
            } else if (DatatypeHelper.isEmpty(id)) {
                log.error("SignableSAMLObject did not contain an ID attribute");
                throw new ValidationException("SignableSAMLObject did not contain an ID attribute");
            } else if (uri.length() < 2 || !id.equals(uri.substring(1))) {
                log.error("Reference URI '{}' did not point to SignableSAMLObject with ID '{}'", uri, id);
                throw new ValidationException("Reference URI did not point to parent ID");
            }
        }
    }

    /**
     * Validate the transforms included in the Signature Reference.
     * 
     * The Reference may contain at most 2 transforms. One of them must be the Enveloped signature transform. An
     * Exclusive Canonicalization transform (with or without comments) may also be present. No other transforms are
     * allowed.
     * 
     * @param reference the Signature reference containing the transforms to evaluate
     * @throws ValidationException thrown if the set of transforms is invalid
     */
    protected void validateTransforms(Reference reference) throws ValidationException {
        Transforms transforms = null;
        try {
            transforms = reference.getTransforms();
        } catch (XMLSecurityException e) {
            log.error("Apache XML Security error obtaining Transforms instance", e);
            throw new ValidationException("Apache XML Security error obtaining Transforms instance", e);
        }

        if (transforms == null) {
            log.error("Error obtaining Transforms instance, null was returned");
            throw new ValidationException("Transforms instance was null");
        }

        int numTransforms = transforms.getLength();
        if (numTransforms > 2) {
            log.error("Invalid number of Transforms was present: " + numTransforms);
            throw new ValidationException("Invalid number of transforms");
        }

        boolean sawEnveloped = false;
        for (int i = 0; i < numTransforms; i++) {
            Transform transform = null;
            try {
                transform = transforms.item(i);
            } catch (TransformationException e) {
                log.error("Error obtaining transform instance", e);
                throw new ValidationException("Error obtaining transform instance", e);
            }
            String uri = transform.getURI();
            if (Transforms.TRANSFORM_ENVELOPED_SIGNATURE.equals(uri)) {
                log.debug("Saw Enveloped signature transform");
                sawEnveloped = true;
            } else if (Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS.equals(uri)
                    || Transforms.TRANSFORM_C14N_EXCL_WITH_COMMENTS.equals(uri)) {
                log.debug("Saw Exclusive C14N signature transform");
            } else {
                log.error("Saw invalid signature transform: " + uri);
                throw new ValidationException("Signature contained an invalid transform");
            }
        }

        if (!sawEnveloped) {
            log.error("Signature was missing the required Enveloped signature transform");
            throw new ValidationException("Transforms did not contain the required enveloped transform");
        }
    }

    /**
     * Validate that the Signature instance does not contain any ds:Object children.
     * 
     * @param apacheSig the Apache XML Signature instance
     * @throws ValidationException if the signature contains ds:Object children
     */
    protected void validateObjectChildren(XMLSignature apacheSig) throws ValidationException {
        if (apacheSig.getObjectLength() > 0) {
            log.error("Signature contained {} ds:Object child element(s)", apacheSig.getObjectLength());
            throw new ValidationException("Signature contained illegal ds:Object children");
        }
    }

}
