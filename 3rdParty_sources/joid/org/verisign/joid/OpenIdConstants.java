/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.verisign.joid;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Various query string key constants used by OpenID.
 *
 * @author <a href="mailto:akarasulu@apache.org">Alex Karasulu</a>
 */
public interface OpenIdConstants
{
    //-------------------
    // keys
    //-------------------
    
    String OPENID_SESSION_TYPE = "openid.session_type";
    String OPENID_DH_CONSUMER_PUBLIC = "openid.dh_consumer_public";
    String OPENID_DH_GENERATOR = "openid.dh_gen";
    String OPENID_DH_MODULUS = "openid.dh_modulus";
    String OPENID_ASSOCIATION_TYPE = "openid.assoc_type";
    String OPENID_MODE = "openid.mode";
    String OPENID_NS = "openid.ns";
    
    //-------------------
    // other constants 
    //-------------------
    
    String ENCODED_NS_VERSION2 = "http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0";
    String OPENID_20_NAMESPACE = "http://specs.openid.net/auth/2.0";
    
    Set<String> OPENID_RESERVED_WORDS = Collections.unmodifiableSet ( 
            new HashSet<String>( Arrays.asList( new String[]
            {   "assoc_handle", "assoc_type", "claimed_id", "contact", "delegate",
                "dh_consumer_public", "dh_gen", "dh_modulus", "error", "identity",
                "invalidate_handle", "mode", "ns", "op_endpoint", "openid", "realm",
                "reference", "response_nonce", "return_to", "server", "session_type",
                "sig", "signed", "trust_root" 
            } ) ) );
}
