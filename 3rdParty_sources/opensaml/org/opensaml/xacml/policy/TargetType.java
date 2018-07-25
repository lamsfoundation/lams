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

package org.opensaml.xacml.policy;


import javax.xml.namespace.QName;

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.XACMLObject;

/** XACML Target schema type. */
public interface TargetType extends XACMLObject {

    /** Local name of the element Target. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Target";

    /** QName of the element Target. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "TargetType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /**
     * Gets the subjects of this target.
     * 
     * @return subjects of this target
     */
    public SubjectsType getSubjects();

    /**
     * Gets the resources of this target.
     * 
     * @return resources of this target
     */
    public ResourcesType getResources();

    /**
     * Gets the actions of this target.
     * 
     * @return actions of this target
     */
    public ActionsType getActions();

    /**
     * Gets the environments of this target.
     * 
     * @return environments of this target
     */
    public EnvironmentsType getEnvironments();
    
    /**
     * Sets the subjects in the target.
     * @param subjects the subject in the target
     */
    public void setSubjects(SubjectsType subjects);
    
    /**
     * Sets the actions in the target.
     * @param actions the subject in the target
     */
    public void setActions(ActionsType actions);
    
    /**
     * Sets the resources in the target.
     * @param resources the subject in the target
     */
    public void setResources(ResourcesType resources);
    
    /**
     * Sets the environments in the target.
     * @param environments the subject in the target
     */
    public void setEnvironments(EnvironmentsType environments);
}