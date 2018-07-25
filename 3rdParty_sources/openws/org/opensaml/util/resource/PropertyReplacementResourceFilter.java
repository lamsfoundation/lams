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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;

import org.opensaml.xml.util.DatatypeHelper;

/**
 * A resource filter that buffers a resource into a string and replaces instance of macros with properties read from a
 * file. Macros are of the syntax '${MACRO_NAME}', the same syntax used within the Java Expression Language.
 * 
 * The property file is read at invocation of this filter.
 * 
 * The {@link InputStream} should be a character stream as {@link InputStreamReader} will be used to convert the stream
 * into a string.
 */
public class PropertyReplacementResourceFilter implements ResourceFilter {

    /** Location of the property file. */
    private File propertyFilePath;

    /**
     * Constructor.
     * 
     * @param propertyFile property file whose properties will be expanded within the resource
     */
    public PropertyReplacementResourceFilter(File propertyFile) {
        if (propertyFile == null) {
            throw new IllegalArgumentException("Property file may not be null");
        }
        propertyFilePath = propertyFile;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public InputStream applyFilter(InputStream resource) throws ResourceException {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(propertyFilePath));
        } catch (IOException e) {
            throw new ResourceException("Unable to read property file", e);
        }
        
        if(props.isEmpty()){
            return resource;
        }

        try {
            String resourceString = DatatypeHelper.inputstreamToString(resource, null);

            Iterator<String> keyItr = (Iterator<String>) props.propertyNames();
            String key;
            while (keyItr.hasNext()) {
                key = keyItr.next();
                resourceString = resourceString.replace("${" + key + "}", props.getProperty(key));
            }

            resourceString.trim();
            return new ByteArrayInputStream(resourceString.getBytes());
        } catch (IOException e) {
            throw new ResourceException("Unable to read contents of resource", e);
        }
    }
}