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

package org.opensaml.xml.security.x509.tls;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLException;

import org.apache.commons.ssl.Certificates;
import org.apache.commons.ssl.HostnameVerifier.AbstractVerifier;
import org.opensaml.xml.security.x509.X509Util;

/**
 * An implementation of {@link javax.net.ssl.HostnameVerifier} which implements strict hostname validation.
 * 
 * <p>
 * This verifier implementation is based on the not-yet-commons-ssl {@link AbstractVerifier}.
 * It differs from the not-yet-commons-ssl STRICT implementation by performing certificate common name (CN) 
 * extraction by parsing the ASN.1 representation of the certificate's {@link javax.security.auth.x500.X500Principal}.
 * For further implementation details, see the Javadocs for {@link org.apache.commons.ssl.HostnameVerifier.STRICT}.
 * </p>
 */
public class StrictHostnameVerifier extends AbstractVerifier {
    
    /** {@inheritDoc} */
    public final void check(final String[] host, final String[] cns, final String[] subjectAlts) throws SSLException {
        check(host, cns, subjectAlts, false, true);
    }

    /** {@inheritDoc} */
    public void check(String[] host, X509Certificate cert) throws SSLException {
        String[] cns = X509Util.getCommonNames(cert.getSubjectX500Principal()).toArray(new String[0]);
        String[] subjectAlts = Certificates.getDNSSubjectAlts(cert);
        //Note: could use X509Util for subject alt names also, per below.
        //List<String> subjectAltsList = X509Util.getAltNames(cert, new Integer[]{X509Util.DNS_ALT_NAME});
        //String[] subjectAlts = subjectAltsList.toArray(new String[0]);
        check(host, cns, subjectAlts);
    }

    /** {@inheritDoc} */
    public final String toString() { return "XMLTOOLING_STRICT"; }

}
