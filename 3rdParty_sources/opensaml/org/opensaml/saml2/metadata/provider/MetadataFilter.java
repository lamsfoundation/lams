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

package org.opensaml.saml2.metadata.provider;

import org.opensaml.xml.XMLObject;

/**
 * A metadata filter is used to process a metadata document after it has been unmarshalled into object.
 * 
 * Some example filters might remove everything but identity providers roles, decreasing the data a service provider
 * needs to work with, or a filter could be used to perform integrity checking on the retrieved metadata by verifying a
 * digital signature.
 */
public interface MetadataFilter {

    /**
     * Filters the given metadata, perhaps to remove elements that are not wanted.
     * 
     * @param metadata the metadata to be filtered.
     * 
     * @throws FilterException thrown if an error occurs during the filtering process
     */
    public void doFilter(XMLObject metadata) throws FilterException;
}