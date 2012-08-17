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

/** XACML Policy schema type. */
public interface PolicyType extends XACMLObject {

    /** Local name of the element Policy. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Policy";

    /** QName of the element Policy. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "PolicyType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** PolicyId attribute name. */
    public static final String POLICY_ID_ATTRIB_NAME = "PolicyId";

    /** Version attribute name. */
    public static final String VERSION_ATTRIB_NAME = "Version";

    /** RuleCombiningAlgId attribute name. */
    public static final String RULE_COMBINING_ALG_ID_ATTRIB_NAME = "RuleCombiningAlgId";

    /**
     * Gets the description for this policy.
     * 
     * @return description for this policy
     */
    public DescriptionType getDescription();

    /**
     * Sets the description for this policy.
     * 
     * @param description description for this policy
     */
    public void setDescription(DescriptionType description);

    /**
     * Gets the defaults for this policy.
     * 
     * @return defaults for this policy
     */
    public DefaultsType getPolicyDefaults();

    /**
     * Sets the defaults for this policy.
     * 
     * @param defaults defaults for this policy
     */
    public void setPolicyDefaults(DefaultsType defaults);

    /**
     * Gets the target of this policy.
     * 
     * @return target of this policy
     */
    public TargetType getTarget();

    /**
     * Sets the target of this policy.
     * 
     * @param target target of this policy
     */
    public void setTarget(TargetType target);

    /**
     * Gets the combiner parameters for this policy.
     * 
     * @return combiner parameters for this policy
     */
    public List<CombinerParametersType> getCombinerParameters();

    /**
     * Gets the rule combiner parameters for this policy.
     * 
     * @return rule combiner parameters for this policy
     */
    public List<RuleCombinerParametersType> getRuleCombinerParameters();

    /**
     * Gets the variable definition for this policy.
     * 
     * @return variable definition for this policy
     */
    public List<VariableDefinitionType> getVariableDefinitions();

    /**
     * Gets the rules for this policy.
     * 
     * @return rules for this policy
     */
    public List<RuleType> getRules();

    /**
     * Gets the obligations of this policy.
     * 
     * @return obligations of this policy
     */
    public ObligationsType getObligations();

    /**
     * Sets the obligations of this policy.
     * 
     * @param obligations obligations of this policy
     */
    public void setObligations(ObligationsType obligations);

    /**
     * Gets the ID of this policy.
     * 
     * @return ID of this policy
     */
    public String getPolicyId();

    /**
     * Sets the ID of this policy.
     * 
     * @param id ID of this policy
     */
    public void setPolicyId(String id);

    /**
     * Gets the XACML version of this policy.
     * 
     * @return XACML version of this policy
     */
    public String getVersion();

    /**
     * Sets the XACML version of this policy.
     * 
     * @param version XACML version of this policy
     */
    public void setVersion(String version);

    /**
     * Gets the rule combining algorithm used with this policy.
     * 
     * @return rule combining algorithm used with this policy
     */
    public String getRuleCombiningAlgoId();

    /**
     * Sets the rule combining algorithm used with this policy.
     * 
     * @param id rule combining algorithm used with this policy
     */
    public void setRuleCombiningAlgoId(String id);
}