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

/**
 * A <code>ResourceListSource</code> that exposes the <code>WEB-INF/classes</code>
 * directory and web application libraries to the discovery API.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class WebAppResourceListSource extends AggregateResourceListSource
{
    /**
     * Creates a new <code>WebAppResourceListSource</code>
     * 
     * @param webinfPath the real, on-disk path to WEB-INF.
     */
    public WebAppResourceListSource(final String webinfPath)
    {
        super();
        ArchiveResourceListSource jarsrc = new WebArchiveResourceListSource(webinfPath);
        DirectoryResourceListSource dirsrc = new WebDirectoryResourceListSource(webinfPath);
        addResourceListSource(jarsrc);
        addResourceListSource(dirsrc);
    }
}
