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

import java.util.Map;

import org.opensaml.xml.security.CriteriaSet;

//TODO amend docs (and impl) for symmetric key storage and retrieval


/**
 * <strong>NOTE:</strong> this class is not yet implemented
 * A {@link CredentialResolver} that pulls credential information from the file system.
 * 
 * This credential resolver attempts to retrieve credential information from the file system. Specifically it will
 * attempt to find key, cert, and crl information from files within the given directory. The filename must start with
 * the entity ID and be followed by one of the follow extensions:
 * 
 * <ul>
 * <li>.name - for key names. File must contain a carriage return seperated list of key names</li>
 * <li>.priv - for private key. File must contain one PEM or DER encoded private key</li>
 * <li>.pub - for public keys. File must contain one or more PEM or DER encoded private key</li>
 * <li>.crt - for public certificates. File must contain one or more PEM or DER encoded X.509 certificates</li>
 * <li>.crl - for certificate revocation lists. File must contain one or more CRLs</li>
 * </ul>
 */
public class FilesystemCredentialResolver extends AbstractCriteriaFilteringCredentialResolver {

    /**
     * Constructor.
     * 
     * @param credentialDirectory directory credential information can be found in
     * @param passwords passwords for encrypted private keys, key is the entity ID, value is the password
     */
    public FilesystemCredentialResolver(String credentialDirectory, Map<String, String> passwords) {
        super();
    }

    /** {@inheritDoc} */
    protected Iterable<Credential> resolveFromSource(CriteriaSet criteriaSet) {
        throw new UnsupportedOperationException("Functionality not yet implemented");
    }
}