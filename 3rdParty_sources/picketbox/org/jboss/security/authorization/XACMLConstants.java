/*
  * JBoss, Home of Professional Open Source
  * Copyright 2007, JBoss Inc., and individual contributors as indicated
  * by the @authors tag. See the copyright.txt in the distribution for a
  * full listing of individual contributors.
  *
  * This is free software; you can redistribute it and/or modify it
  * under the terms of the GNU Lesser General Public License as
  * published by the Free Software Foundation; either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  * Lesser General Public License for more details.
  *
  * You should have received a copy of the GNU Lesser General Public
  * License along with this software; if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
  */
package org.jboss.security.authorization;

//$Id$

/**
 *  Predefined constants for the XACML layer
 *  @author Anil.Saldhana@redhat.com
 *  @since  May 8, 2007 
 *  @version $Revision$
 */
public interface XACMLConstants
{
  String ACTION_IDENTIFIER = "urn:oasis:names:tc:xacml:1.0:action:action-id";
  String CURRENT_TIME_IDENTIFIER = "urn:oasis:names:tc:xacml:1.0:environment:current-time";
  String RESOURCE_IDENTIFIER = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
  String SUBJECT_IDENTIFIER = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
  String SUBJECT_ROLE_IDENTIFIER = "urn:oasis:names:tc:xacml:2.0:subject:role";
  String SUBJECT_GROUP_IDENTIFIER = "urn:oasis:names:tc:xacml:2.0:subject:group";
  
  //JBoss Specific
  String JBOSS_DYNAMIC_POLICY_SET_IDENTIFIER = "urn:org:jboss:xacml:support:finder:dynamic-policy-set";
  String JBOSS_RESOURCE_PARAM_IDENTIFIER = "urn:oasis:names:tc:xacml:2.0:request-param:attribute:";
}
