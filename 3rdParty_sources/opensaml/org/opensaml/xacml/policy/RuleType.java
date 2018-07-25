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

/** XACML Rule schema type. */
public interface RuleType extends XACMLObject {

    /** Local name of the element Rule. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Rule";

    /** QName of the element Rule. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** Local name of the XSI type. */
    public static final String SCHEMA_TYPE_LOCAL_NAME = "RuleType";

    /** QName of the XSI type. */
    public static final QName SCHEMA_TYPE_NAME = new QName(XACMLConstants.XACML20_NS, SCHEMA_TYPE_LOCAL_NAME,
            XACMLConstants.XACML_PREFIX);

    /** RuleId attribute name. */
    public static final String RULE_ID_ATTRIB_NAME = "RuleId";

    /** Effect attribute name. */
    public static final String EFFECT_ATTRIB_NAME = "Effect";

    /**
     * Gets the description of this rule.
     * 
     * @return description of this rule
     */
    public DescriptionType getDescription();

    /**
     * Sets the description of this rule.
     * 
     * @param description the description of this rule
     */
    public void setDescription(DescriptionType description);

    /**
     * Gets the target of this rule.
     * 
     * @return the target of this rule
     */
    public TargetType getTarget();

    /**
     * Sets the target of this rule.
     * 
     * @param target the target of this rule
     */
    public void setTarget(TargetType target);

    /**
     * Gets the condition for this rule.
     * 
     * @return the condition for this rule
     */
    public ConditionType getCondition();

    /**
     * Sets the the condition for this rule.
     * 
     * @param condition the condition for this rule
     */
    public void setCondition(ConditionType condition);

    /**
     * Gets the ID for this rule.
     * 
     * @return the ID for this rule
     */
    public String getRuleId();

    /**
     * Sets the ID for this rule.
     * 
     * @param id the ID for this rule
     */
    public void setRuleId(String id);

    /**
     * Gets the effect of the rule.
     * 
     * @return the effect of the rule
     */
    public EffectType getEffect();

    /**
     * Sets the effect of the rule.
     * 
     * @param type the effect of the rule
     */
    public void setEffect(EffectType type);
}