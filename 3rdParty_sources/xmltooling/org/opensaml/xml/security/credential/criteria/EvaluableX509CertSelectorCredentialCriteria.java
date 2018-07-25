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

import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;

import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.x509.X509Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instance of evaluable credential criteria for evaluating whether a credential's certificate meets the criteria
 * specified by an instance of {@link X509CertSelector}.
 * 
 */
public class EvaluableX509CertSelectorCredentialCriteria implements EvaluableCredentialCriteria {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(EvaluableX509CertSelectorCredentialCriteria.class);

    /** Base criteria. */
    private X509CertSelector certSelector;

    /**
     * Constructor.
     * 
     * @param newSelector the new X509 cert selector
     */
    public EvaluableX509CertSelectorCredentialCriteria(X509CertSelector newSelector) {
        if (newSelector == null) {
            throw new IllegalArgumentException("X509 cert selector may not be null");
        }
        certSelector = newSelector;
    }

    /** {@inheritDoc} */
    public Boolean evaluate(Credential target) {
        if (target == null) {
            log.error("Credential target was null");
            return null;
        }
        if (!(target instanceof X509Credential)) {
            log.info("Credential is not an X509Credential, can not evaluate X509CertSelector criteria");
            return Boolean.FALSE;
        }
        X509Credential x509Cred = (X509Credential) target;

        X509Certificate entityCert = x509Cred.getEntityCertificate();
        if (entityCert == null) {
            log.info("X509Credential did not contain an entity certificate, can not evaluate X509CertSelector criteria");
            return Boolean.FALSE;
        }

        Boolean result = certSelector.match(entityCert);
        return result;
    }

}
