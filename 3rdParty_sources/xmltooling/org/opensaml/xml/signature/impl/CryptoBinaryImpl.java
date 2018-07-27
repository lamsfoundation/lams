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

package org.opensaml.xml.signature.impl;

import java.math.BigInteger;

import org.opensaml.xml.schema.impl.XSBase64BinaryImpl;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.signature.CryptoBinary;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * Concrete implementation of {@link org.opensaml.xml.signature.CryptoBinary}.
 */
public class CryptoBinaryImpl extends XSBase64BinaryImpl implements CryptoBinary {
    
    /** The cached BigInteger representation of the element's base64-encoded value. */
    private BigInteger bigIntValue;

    /**
     * Constructor.
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected CryptoBinaryImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public BigInteger getValueBigInt() {
        if (bigIntValue == null && !DatatypeHelper.isEmpty(getValue())) {
            bigIntValue = KeyInfoHelper.decodeBigIntegerFromCryptoBinary(getValue());
        }
        return bigIntValue;
    }

    /** {@inheritDoc} */
    public void setValueBigInt(BigInteger bigInt) {
        if (bigInt == null) {
            setValue(null);
        } else {
            setValue(KeyInfoHelper.encodeCryptoBinaryFromBigInteger(bigInt));
        }
        bigIntValue = bigInt;
    }
    
    /** {@inheritDoc} */
    public void setValue(String newValue) {
        if (bigIntValue != null 
                && (!DatatypeHelper.safeEquals(getValue(), newValue) || newValue == null)) {
            // Just clear the cached value, my not be needed in big int form again,
            // let it be lazily recreated if necessary
            bigIntValue = null;
        }
        super.setValue(newValue);
    }

}
