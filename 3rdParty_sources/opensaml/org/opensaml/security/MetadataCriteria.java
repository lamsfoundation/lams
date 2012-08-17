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

import javax.xml.namespace.QName;

import org.opensaml.xml.security.Criteria;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * An implementation of {@link Criteria} which specifies criteria pertaining 
 * to SAML 2 metadata.
 */
public final class MetadataCriteria implements Criteria {
    
    /** Metadata role indicated by the criteria. */
    private QName entityRole;

    /** Metadata protocol of the role indicated by the criteria. */
    private String entityProtocol;
    
    /**
     * Constructor.
     *
     * @param role the entity role
     * @param protocol the entity protocol
     */
    public MetadataCriteria(QName role, String protocol) {
       setRole(role);
       setProtocol(protocol);
    }

    /**
     * Get the entity protocol. 
     * 
     * @return the protocol.
     */
    public String getProtocol() {
        return entityProtocol;
    }

    /**
     * Set the entity protocol. 
     * 
     * @param protocol The protocol to set.
     */
    public void setProtocol(String protocol) {
        entityProtocol = DatatypeHelper.safeTrimOrNullString(protocol);
    }

    /**
     * Get the entity role.
     * 
     * @return Returns the role.
     */
    public QName getRole() {
        return entityRole;
    }

    /**
     * Set the entity role.
     * 
     * @param role the QName of entity role
     */
    public void setRole(QName role) {
        if (role == null) {
            throw new IllegalArgumentException("Role criteria may not be null");
        }
        entityRole = role;
    }
    
    
    
    

}
