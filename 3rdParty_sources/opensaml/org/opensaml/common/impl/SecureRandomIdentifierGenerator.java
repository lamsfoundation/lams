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

package org.opensaml.common.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.bouncycastle.util.encoders.Hex;
import org.opensaml.common.IdentifierGenerator;

/**
 * Generates identifiers using random data obtained from a {@link java.security.SecureRandom} instance.
 */
public class SecureRandomIdentifierGenerator implements IdentifierGenerator {

    /** Random number generator. */
    private static SecureRandom random;

    /**
     * Constructor.
     * 
     * @throws NoSuchAlgorithmException thrown if the SHA1PRNG algorithm is not supported by the JVM
     */
    public SecureRandomIdentifierGenerator() throws NoSuchAlgorithmException {
        random = SecureRandom.getInstance("SHA1PRNG");
    }

    /**
     * Constructor.
     * 
     * @param algorithm the random number generation algorithm to use
     * 
     * @throws NoSuchAlgorithmException thrown if the algorithm is not supported by the JVM
     */
    public SecureRandomIdentifierGenerator(String algorithm) throws NoSuchAlgorithmException {
        random = SecureRandom.getInstance(algorithm);
    }

    /** {@inheritDoc} */
    public String generateIdentifier() {
        return generateIdentifier(16);
    }

    /** {@inheritDoc} */
    public String generateIdentifier(int size) {
        byte[] buf = new byte[size];
        random.nextBytes(buf);
        return "_".concat(new String(Hex.encode(buf)));
    }
}