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

package org.opensaml.saml2.metadata.support;

import java.util.List;

import org.opensaml.saml2.metadata.AttributeConsumingService;
import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.saml2.metadata.SPSSODescriptor;
import org.opensaml.samlext.saml2mdquery.AttributeQueryDescriptorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Metadata support class which selects an {@link AttributeConsumingService} based on input of a mandatory
 * {@link RoleDescriptor} and an optional index.
 * 
 * <p>
 * This implementation supports selecting an AttributeConsumingService from parent role descriptors of the following
 * types:
 * 
 * <ol>
 * <li>the standard SAML 2 metadata type {@link SPSSODescriptor}</li>
 * <li>the extension type {@link AttributeQueryDescriptorType}</li>
 * </ol>
 * </p>
 * 
 * <p>
 * Subclasses should override {@link #getCandidates()} if support for additional sources of attribute consuming services
 * is needed.
 * </p>
 * 
 * <p>
 * The selection algorithm is:
 * <ol>
 * <li>If an index is supplied, the service with that index is returned. If no such service exists in metadata: if
 * {@link #isOnBadIndexUseDefault()} is true, then the default service is returned as described below; otherwise null is
 * returned.</li>
 * <li>If an index is not supplied, then the default service is returned as follows: The service with an explicit
 * isDefault of true is returned. If no such service exists, then the first service without an explicit isDefault is
 * returned. If no service is yet selected, then the first service listed in metadata is returned.</li>
 * </ol>
 * </p>
 */
public class AttributeConsumingServiceSelector {

    /** Class logger. */
    private Logger log = LoggerFactory.getLogger(AttributeConsumingServiceSelector.class);

    /** The requested service index. */
    private Integer index;

    /** The AttributeConsumingService's parent role descriptor. */
    private RoleDescriptor roleDescriptor;

    /**
     * Flag which determines whether, in the case of an invalid index, to return the default AttributeConsumingService.
     */
    private boolean onBadIndexUseDefault;

    /**
     * Get the index of the desired service.
     * 
     * @return Returns the index.
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Set the index of the desired service.
     * 
     * @param requestedIndex The index to set.
     */
    public void setIndex(Integer requestedIndex) {
        index = requestedIndex;
    }

    /**
     * Get the AttributeConsumingServie's parent RoleDescriptor.
     * 
     * @return Returns the spSSODescriptor.
     */
    public RoleDescriptor getRoleDescriptor() {
        return roleDescriptor;
    }

    /**
     * Set the AttributeConsumingServie's parent RoleDescriptor.
     * 
     * @param descriptor The roleDescriptor to set.
     */
    public void setRoleDescriptor(RoleDescriptor descriptor) {
        roleDescriptor = descriptor;
    }

    /**
     * Set the flag which determines whether, in the case of an invalid index, to return the default
     * AttributeConsumingService. Defaults to false.
     * 
     * @param flag The onBadIndexUseDefault to set.
     */
    public void setOnBadIndexUseDefault(boolean flag) {
        onBadIndexUseDefault = flag;
    }

    /**
     * Get the flag which determines whether, in the case of an invalid index, to return the default
     * AttributeConsumingService. Defaults to false.
     * 
     * @return Returns the onBadIndexUseDefault.
     */
    public boolean isOnBadIndexUseDefault() {
        return onBadIndexUseDefault;
    }

    /**
     * Select the AttributeConsumingService.
     * 
     * @return the selected AttributeConsumingService, or null
     */
    public AttributeConsumingService selectService() {
        List<AttributeConsumingService> candidates = getCandidates();

        if (candidates == null || candidates.isEmpty()) {
            log.debug("AttributeConsumingService candidate list was empty, can not select service");
            return null;
        }

        log.debug("AttributeConsumingService index was specified: {}", index != null);

        AttributeConsumingService acs = null;
        if (index != null) {
            acs = selectByIndex(candidates);
            if (acs == null && isOnBadIndexUseDefault()) {
                acs = selectDefault(candidates);
            }
        } else {
            return selectDefault(candidates);
        }

        return acs;
    }

    /**
     * Get the list of candidate attribute consuming services.
     * 
     * <p>
     * This implementation supports selecting an AttributeConsumingService from parent role descriptors of the following
     * types:
     * 
     * <ol>
     * <li>the standard SAML 2 metadata type {@link SPSSODescriptor}</li>
     * <li>the extension type {@link AttributeQueryDescriptorType}</li>
     * </ol>
     * </p>
     * 
     * <p>
     * Subclasses should override if support for additional sources of attribute consuming services is needed.
     * </p>
     * 
     * @return the list of candidate AttributeConsumingServices, or null if none could be resolved
     */
    protected List<AttributeConsumingService> getCandidates() {
        if (roleDescriptor == null) {
            log.debug("RoleDescriptor was not supplied, unable to select AttributeConsumingService");
            return null;
        }

        if (roleDescriptor instanceof SPSSODescriptor) {
            log.debug("Resolving AttributeConsumingService candidates from SPSSODescriptor");
            return ((SPSSODescriptor) roleDescriptor).getAttributeConsumingServices();
        } else if (roleDescriptor instanceof AttributeQueryDescriptorType) {
            log.debug("Resolving AttributeConsumingService candidates from AttributeQueryDescriptorType");
            return ((AttributeQueryDescriptorType) roleDescriptor).getAttributeConsumingServices();
        } else {
            log.debug("Unable to resolve service candidates, role descriptor was of an unsupported type: {}",
                    roleDescriptor.getClass().getName());
            return null;
        }
    }

    /**
     * Select the service based on the index value.
     * 
     * @param candidates the list of candiate services
     * @return the selected candidate or null
     */
    private AttributeConsumingService selectByIndex(List<AttributeConsumingService> candidates) {
        log.debug("Selecting AttributeConsumingService by index");
        for (AttributeConsumingService attribCS : candidates) {
            // Check for null b/c don't ever want to fail with an NPE due to autoboxing.
            // Note: metadata index property is an int, not an Integer.
            if (index != null) {
                if (index == attribCS.getIndex()) {
                    log.debug("Selected AttributeConsumingService with index: {}", index);
                    return attribCS;
                }
            }
        }
        log.debug("A service index of '{}' was specified, but was not found in metadata", index);
        return null;
    }

    /**
     * Select the default service.
     * 
     * @param candidates the list of candiate services
     * @return the selected candidate or null
     */
    private AttributeConsumingService selectDefault(List<AttributeConsumingService> candidates) {
        log.debug("Selecting default AttributeConsumingService");
        AttributeConsumingService firstNoDefault = null;
        for (AttributeConsumingService attribCS : candidates) {
            if (attribCS.isDefault()) {
                log.debug("Selected AttributeConsumingService with explicit isDefault of true");
                return attribCS;
            }

            // This records the first element whose isDefault is not explicitly false
            if (firstNoDefault == null && attribCS.isDefaultXSBoolean() == null) {
                firstNoDefault = attribCS;
            }
        }

        if (firstNoDefault != null) {
            log.debug("Selected first AttributeConsumingService with no explicit isDefault");
            return firstNoDefault;
        } else {
            log.debug("Selected first AttributeConsumingService with explicit isDefault of false");
            return candidates.get(0);
        }
    }
}