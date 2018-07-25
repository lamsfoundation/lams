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

package org.opensaml.util.resource;

import java.io.InputStream;

/** Filter that operates over a resource. Filters may perform any logic on the incoming stream. */
public interface ResourceFilter {

    /**
     * Applies this filter to the given stream.
     * 
     * @param resource Resource to which the filter should apply.
     * 
     * @return filtered stream
     * 
     * @throws ResourceException thrown is there if a problem applying the filter
     */
    public InputStream applyFilter(InputStream resource) throws ResourceException;
}