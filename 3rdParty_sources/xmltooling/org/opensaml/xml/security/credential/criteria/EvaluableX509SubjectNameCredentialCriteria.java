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

import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.x509.X509Credential;
import org.opensaml.xml.security.x509.X509SubjectNameCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instance of evaluable credential criteria for evaluating whether a credential's certificate contains a particular
 * subject name.
 */
public class EvaluableX509SubjectNameCredentialCriteria implements EvaluableCredentialCriteria {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(EvaluableX509SubjectNameCredentialCriteria.class);

    /** Base criteria. */
    private X500Principal subjectName;

    /**
     * Constructor.
     * 
     * @param criteria the criteria which is the basis for evaluation
     */
    public EvaluableX509SubjectNameCredentialCriteria(X509SubjectNameCriteria criteria) {
        if (criteria == null) {
            throw new NullPointerException("Criteria instance may not be null");
        }
        subjectName = criteria.getSubjectName();
    }

    /**
     * Constructor.
     * 
     * @param newSubjectName the subject name criteria value which is the basis for evaluation
     */
    public EvaluableX509SubjectNameCredentialCriteria(X500Principal newSubjectName) {
        if (newSubjectName == null) {
            throw new IllegalArgumentException("Subject name may not be null");
        }
        subjectName = newSubjectName;
    }

    /** {@inheritDoc} */
    public Boolean evaluate(Credential target) {
        if (target == null) {
            log.error("Credential target was null");
            return null;
        }
        if (!(target instanceof X509Credential)) {
            log.info("Credential is not an X509Credential, does not satisfy subject name criteria");
            return Boolean.FALSE;
        }
        X509Credential x509Cred = (X509Credential) target;

        X509Certificate entityCert = x509Cred.getEntityCertificate();
        if (entityCert == null) {
            log.info("X509Credential did not contain an entity certificate, does not satisfy criteria");
            return Boolean.FALSE;
        }

        Boolean result = entityCert.getSubjectX500Principal().equals(subjectName);
        return result;
    }

}
