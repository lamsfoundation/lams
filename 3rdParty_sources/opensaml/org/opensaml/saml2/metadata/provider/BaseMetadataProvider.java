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

import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.UnmarshallerFactory;

/**
 * Base class for metadata providers.
 */
public abstract class BaseMetadataProvider implements MetadataProvider {

    /** Unmarshaller factory used to get an unmarshaller for the metadata DOM. */
    protected UnmarshallerFactory unmarshallerFactory;

    /** Whether metadata is required to be valid. */
    private boolean requireValidMetadata;

    /** Filter applied to all metadata. */
    private MetadataFilter mdFilter;

    /** Constructor. */
    public BaseMetadataProvider() {
        requireValidMetadata = false;
        unmarshallerFactory = Configuration.getUnmarshallerFactory();
    }

    /** {@inheritDoc} */
    public boolean requireValidMetadata() {
        return requireValidMetadata;
    }

    /** {@inheritDoc} */
    public void setRequireValidMetadata(boolean require) {
        requireValidMetadata = require;
    }

    /** {@inheritDoc} */
    public MetadataFilter getMetadataFilter() {
        return mdFilter;
    }

    /** {@inheritDoc} */
    public void setMetadataFilter(MetadataFilter newFilter) throws MetadataProviderException {
        mdFilter = newFilter;
    }
    
    /** Destroys the metadata provider and frees any resources current held by it.  Default method is a no-op. */
    public synchronized void destroy(){
        //no-op
    }
}