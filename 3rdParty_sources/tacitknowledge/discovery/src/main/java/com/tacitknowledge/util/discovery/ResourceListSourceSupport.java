/* Copyright 2004 Tacit Knowledge
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tacitknowledge.util.discovery;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides base functionality of <code>ResourceListSource</code> implementations.
 * Subclasses do not need to deal with <code>ResourceCriteria</code>; they only
 * need to provide lists of resources to examine.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public abstract class ResourceListSourceSupport implements ResourceListSource
{
    /**
     * @see ResourceListSource#getResources(String, ResourceCriteria)
     */
    public String[] getResources(String basePath, ResourceCriteria criteria)
    {
        String root = (basePath == null) ? "" : basePath;
        List resources = new ArrayList();
        String[] resourceNames = getResources(root);
        
        for (int i = 0; i < resourceNames.length; i++)
        {
            String resourceName = resourceNames[i];
            boolean addToResourceList = true;
            if (criteria != null)
            {
                addToResourceList = criteria.matches(resourceName);
            }
            
            if (addToResourceList)
            {
                resources.add(resourceName);
            }
        }
        return (String[]) resources.toArray(new String[resources.size()]);
    }
    
    /**
     * Returns an iterator of the names of resources in the given path.
     * 
     * @param  root the directory containing to resources to enumerate
     * @return an iterator of the names of resources in the given path
     */
    protected abstract String[] getResources(String root);
}
