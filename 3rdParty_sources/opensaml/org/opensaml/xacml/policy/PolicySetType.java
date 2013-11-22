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

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.XACMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/** XACML PolicySet schema types. */
public interface PolicySetType extends XACMLObject {

    /** Local name of the element PolicySet. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "PolicySet";

    /** QName of the element PolicySet. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "PolicySetType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** PolicySetId attribute name. */
    public static final String POLICY_SET_ID_ATTRIB_NAME = "PolicySetId";

    /** Version attribute name. */
    public static final String VERSION_ATTRIB_NAME = "Version";

    /** PolicyCombiningAlgId attribute name. */
    public static final String POLICY_COMBINING_ALG_ID_ATTRIB_NAME = "PolicyCombiningAlgId";

    /**
     * Gets the description for this policy set.
     * 
     * @return description for this policy set
     */
    public DescriptionType getDescription();

    /**
     * Sets the description for this policy set.
     * 
     * @param description description for this policy set
     */
    public void setDescription(DescriptionType description);

    /**
     * Gets the backing object for the choice group containing the {@link PolicySetType}, {@link PolicyType},
     * {@link IdReferenceType}, {@link CombinerParametersType}, {@link PolicyCombinerParametersType},
     * {@link PolicySetCombinerParametersType}. The individual getter/setter methods should be preferred over this
     * method, however this method may be used to fine tune the ordering of all of these objects if that should be
     * necessary.
     * 
     * @return backing object for the choice group containing the {@link PolicySetType}, {@link PolicyType},
     *         {@link IdReferenceType}, {@link CombinerParametersType}, {@link PolicyCombinerParametersType},
     *         {@link PolicySetCombinerParametersType}
     */
    public IndexedXMLObjectChildrenList<XACMLObject> getPolicyChoiceGroup();

    /**
     * Gets the defaults for this policy set.
     * 
     * @return defaults for this policy set
     */
    public DefaultsType getPolicySetDefaults();

    /**
     * Sets the defaults for this policy set.
     * 
     * @param defaults defaults for this policy set
     */
    public void setPolicySetDefaults(DefaultsType defaults);

    /**
     * Gets the target of this policy set.
     * 
     * @return target of this policy set
     */
    public TargetType getTarget();

    /**
     * Sets the target of this policy set.
     * 
     * @param target target of this policy set
     */
    public void setTarget(TargetType target);

    /**
     * Gets the child policy sets.
     * 
     * @return child policy sets
     */
    public List<PolicySetType> getPolicySets();

    /**
     * Gets the child policies.
     * 
     * @return child policies
     */
    public List<PolicyType> getPolicies();

    /**
     * Gets the policy set Id references.
     * 
     * @return policy set Id references
     */
    public List<IdReferenceType> getPolicySetIdReferences();

    /**
     * Gets the policy Id references.
     * 
     * @return policy Id references
     */
    public List<IdReferenceType> getPolicyIdReferences();

    /**
     * Gets the combiner parameters for this policy set.
     * 
     * @return combiner parameters for this policy set
     */
    public List<CombinerParametersType> getCombinerParameters();

    /**
     * Gets the policy combiner parameters for this policy set.
     * 
     * @return policy combiner parameters for this policy set
     */
    public List<PolicyCombinerParametersType> getPolicyCombinerParameters();

    /**
     * Gets the policy set combiner parameters for this policy set.
     * 
     * @return policy set combiner parameters for this policy set
     */
    public List<PolicySetCombinerParametersType> getPolicySetCombinerParameters();

    /**
     * Gets the obligations of this policy set.
     * 
     * @return obligations of this policy set
     */
    public ObligationsType getObligations();

    /**
     * Sets the obligations of this policy set.
     * 
     * @param obligations obligations of this policy set
     */
    public void setObligations(ObligationsType obligations);

    /**
     * Gets the ID of this policy set.
     * 
     * @return ID of this policy set
     */
    public String getPolicySetId();

    /**
     * Sets the ID of this policy set.
     * 
     * @param id ID of this policy set
     */
    public void setPolicySetId(String id);

    /**
     * Gets the XACML version of this policy set.
     * 
     * @return XACML version of this policy set
     */
    public String getVersion();

    /**
     * Sets the XACML version of this policy set.
     * 
     * @param version XACML version of this policy set
     */
    public void setVersion(String version);

    /**
     * Gets the policy combining algorithm used with this policy set.
     * 
     * @return policy combining algorithm used with this policy set
     */
    public String getPolicyCombiningAlgoId();

    /**
     * Sets the policy combining algorithm used with this policy set.
     * 
     * @param id policy combining algorithm used with this policy set
     */
    public void setPolicyCombiningAlgoId(String id);
}