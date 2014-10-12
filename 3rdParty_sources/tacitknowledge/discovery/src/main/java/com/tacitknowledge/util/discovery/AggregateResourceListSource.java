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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Combines two or more <code>ResourceListSource</code> implementations.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class AggregateResourceListSource implements ResourceListSource
{
    /**
     * The list of sources to aggregate
     */
    private List sources = new ArrayList();
    
    /**
     * Creates a new <code>AggregateResourceListSource</code> containing
     * <strong>no</strong> <code>ResourceListSource</code>s.
     */
    public AggregateResourceListSource()
    {
        // Nothing to do
    }
    
    /**
     * Creates a new <code>AggregateResourceListSource</code> containing
     * the given list of <code>ResourceListSource</code>s.
     * 
     * @param l the list of <code>ResourceListSource</code>s to aggregate
     */
    public AggregateResourceListSource(List l)
    {
        if (l == null)
        {
            throw new IllegalArgumentException("ResourceListSources list cannot be null");
        }
        sources.addAll(l);
    }
    
    /**
     * Creates a new <code>AggregateResourceListSource</code> containing
     * the given array of <code>ResourceListSource</code>s.
     * 
     * @param sources the array of <code>ResourceListSource</code>s to aggregate
     */
    public AggregateResourceListSource(ResourceListSource[] sources)
    {
        if (sources == null)
        {
            throw new IllegalArgumentException("ResourceListSources array cannot be null");
        }
        this.sources.addAll(Arrays.asList(sources));
    }

    /**
     * @see ResourceListSource#getResources(String, ResourceCriteria)
     */
    public String[] getResources(String basePath, ResourceCriteria criteria)
    {
        Set resourceNames = new HashSet();
        for (Iterator i = sources.iterator(); i.hasNext();)
        {
            ResourceListSource source = (ResourceListSource) i.next();
            resourceNames.addAll(Arrays.asList(source.getResources(basePath, criteria)));
        }
        return (String[]) resourceNames.toArray(new String[resourceNames.size()]);
    }
    
    /**
     * Adds the given <code>ResourceListSource</code> to the list of sources
     * used by <code>getResources</code>.
     * 
     * @param source the source to add
     */
    public void addResourceListSource(ResourceListSource source)
    {
        sources.add(source);
    }
}
