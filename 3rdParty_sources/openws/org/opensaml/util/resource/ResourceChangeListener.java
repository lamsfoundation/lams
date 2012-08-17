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

/**
 * A resource change listener.
 */
public interface ResourceChangeListener{
    
    /** Types of change events; creation, update, and deletion. */
    public enum ResourceChange{ CREATION, UPDATE, DELETE};
    
    /**
     * Called when a resource is created.
     * 
     * @param resource the resource that was created
     */
    public void onResourceCreate(Resource resource);
    
    /**
     * Called when a resource is update.
     * 
     * @param resource the resource that was updated
     */
    public void onResourceUpdate(Resource resource);
    
    /**
     * Called when a resource is deleted.
     * 
     * @param resource the resource that was deleted
     */
    public void onResourceDelete(Resource resource);
}