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

package org.opensaml.xml.security.x509;

import javax.security.auth.x500.X500Principal;

/**
 * Basic implementation of {@link X500DNHandler} which uses the internal built-in mechanisms
 * provided by {@link X500Principal} directly.
 */
public class InternalX500DNHandler implements X500DNHandler {

    /** {@inheritDoc} */
    public byte[] getEncoded(X500Principal principal) {
        if (principal == null) {
            throw new NullPointerException("X500Principal may not be null");
        }
        return principal.getEncoded();
    }

    /** {@inheritDoc} */
    public String getName(X500Principal principal) {
        if (principal == null) {
            throw new NullPointerException("X500Principal may not be null");
        }
        return principal.getName();
    }

    /** {@inheritDoc} */
    public String getName(X500Principal principal, String format) {
        if (principal == null) {
            throw new NullPointerException("X500Principal may not be null");
        }
        return principal.getName(format);
    }

    /** {@inheritDoc} */
    public X500Principal parse(String name) {
        if (name == null) {
            throw new NullPointerException("X.500 name string may not be null");
        }
        return new X500Principal(name);
    }

    /** {@inheritDoc} */
    public X500Principal parse(byte[] name) {
        if (name == null) {
            throw new NullPointerException("X.500 DER-encoded name may not be null");
        }
        return new X500Principal(name);
    }

    /** {@inheritDoc} */
    public X500DNHandler clone() {
        // We don't have any state, just return a new instance to maintain the clone() contract.
        return new InternalX500DNHandler();
    }

}
