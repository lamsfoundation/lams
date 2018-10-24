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
 * Interface for implementations which handle parsing and serialization of X.500 names
 * represented by {@link X500Principal}.
 */
public interface X500DNHandler {
    
    /** Specifies the string format specified in RFC 1779. */
    public static final String FORMAT_RFC1779 = X500Principal.RFC1779;
    
    /** Specifies the string format specified in RFC 2253. */
    public static final String FORMAT_RFC2253 = X500Principal.RFC2253;
    
    /**
     * Parse the string representation of a name and build a new principal instance.
     * 
     * @param name the name string to parse
     * @return a new principal instance
     * 
     * @throws IllegalArgumentException if the name value can not be parsed by the implementation
     */
    public X500Principal parse(String name);
    
    /**
     * Parse the ASN.1 DER encoding representation of a name and build a new principal instance.
     * 
     * @param name a distinguished name in ASN.1 DER encoded form
     * @return a new principal instance
     * 
     * @throws IllegalArgumentException if the name value can not be parsed by the implementation
     */
    public X500Principal parse(byte[] name);
    
    /**
     * Returns a string representation of the X.500 distinguished name using the default format
     * as defined in the underlying implementation.
     * 
     * @param principal the principal name instance to serialize
     * @return the serialized string name
     */
    public String getName(X500Principal principal);
    
    /**
     * Returns a string representation of the X.500 distinguished name using the specified format.
     * 
     * The values and meanings of the format specifier are implementation dependent. Constants for
     * two common standard formats are provided here as {@link #FORMAT_RFC1779} and {@link #FORMAT_RFC2253};
     * 
     * @param principal the principal name instance to serialize
     * @param format the format specifier of the resulting serialized string name
     * @return the serialized string name
     * 
     * @throws IllegalArgumentException if the specified format is not understood by the implementation
     */
    public String getName(X500Principal principal, String format);
    
    /**
     * Returns the distinguished name in ASN.1 DER encoded form.
     *  
     * @param principal the principal name instance to serialize
     * @return the serialized name in ASN.1 DER encoded form
     */
    public byte[] getEncoded(X500Principal principal);
    
    /**
     * Clone the handler. Implementations which maintain instance-specific configuration data, etc,
     * should implement this appropriately, possibly also implementing {@link Cloneable}.
     * 
     * @return the cloned handler
     */
    public X500DNHandler clone();

}
