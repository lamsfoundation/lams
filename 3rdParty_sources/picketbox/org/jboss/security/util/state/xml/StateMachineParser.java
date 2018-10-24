/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.security.util.state.xml;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.util.state.State;
import org.jboss.security.util.state.StateMachine;
import org.jboss.security.util.state.Transition;
import org.jboss.security.util.xml.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** Parse an xml representation of a state machine. A sample document is:
 
<state-machine description="JACC PolicyConfiguration States">
   <state name="open">
      <transition name="inService" target="open" />
      <transition name="getContextID" target="open" />
      <transition name="getPolicyConfiguration" target="open" />
      <transition name="addToRole" target="open" />
      <transition name="removeRole" target="open" />
      <transition name="addToExcludedPolicy" target="open" />
      <transition name="removeExcludedPolicy" target="open" />
      <transition name="addToUncheckedPolicy" target="open" />
      <transition name="removeUncheckedPolicy" target="open" />
      <transition name="linkConfiguration" target="open" />
      <transition name="commit" target="inService" />
      <transition name="delete" target="deleted" />
   </state>
   <state name="inService">
      <transition name="getPolicyConfiguration" target="open" />
      <transition name="getContextID" target="inService" />
      <transition name="inService" target="inService" />
      <transition name="delete" target="deleted" />
   </state>
   <state name="deleted" isStartState="true">
      <transition name="getPolicyConfiguration" target="open" />
      <transition name="delete" target="deleted" />      
      <transition name="inService" target="deleted" />
      <transition name="getContextID" target="deleted" />
   </state>
</state-machine>

 @author Scott.Stark@jboss.org
 @author Dimitris.Andreadis@jboss.org
 @version $Revision$
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class StateMachineParser
{

   public StateMachine parse(URL source) throws Exception
   {
      // parse the XML document into a DOM structure
      InputStream in = null;
      Element root = null;
      try
      {
         in = source.openConnection().getInputStream();
         root = DOMUtils.parse(in);
      }
      finally
      {
         safeClose(in);
      }
      String description = root.getAttribute("description");
      HashMap nameToStateMap = new HashMap();
      HashMap nameToTransitionsMap = new HashMap();
      HashSet states = new HashSet();
      State startState = null;

      // parse states
      NodeList stateList = root.getChildNodes();
      for (int i = 0; i < stateList.getLength(); i++)
      {
         Node stateNode = stateList.item(i);
         if (stateNode.getNodeName().equals("state"))
         {
            Element stateElement = (Element)stateNode;
            String stateName = stateElement.getAttribute("name");
            State s = new State(stateName);
            states.add(s);
            nameToStateMap.put(stateName, s);
            HashMap transitions = new HashMap();
            
            // parse transitions
            NodeList transitionList = stateElement.getChildNodes();
            for (int j = 0; j < transitionList.getLength(); j++)
            {
               Node transitionNode = transitionList.item(j);
               if (transitionNode.getNodeName().equals("transition"))
               {
                  Element transitionElement = (Element)transitionNode;
                  String name = transitionElement.getAttribute("name");
                  String targetName = transitionElement.getAttribute("target");
                  transitions.put(name, targetName);
               }
            }
            nameToTransitionsMap.put(stateName, transitions);
            if (Boolean.valueOf(stateElement.getAttribute("isStartState")) == Boolean.TRUE)
               startState = s;
         }
      }
      
      // Resolve all transition targets
      Iterator transitions = nameToTransitionsMap.keySet().iterator();
      StringBuffer resolveFailed = new StringBuffer();
      while (transitions.hasNext())
      {
         String stateName = (String)transitions.next();
         State s = (State)nameToStateMap.get(stateName);
         HashMap stateTransitions = (HashMap)nameToTransitionsMap.get(stateName);
         Iterator it = stateTransitions.keySet().iterator();
         while (it.hasNext())
         {
            String name = (String)it.next();
            String targetName = (String)stateTransitions.get(name);
            State target = (State)nameToStateMap.get(targetName);
            if (target == null)
            {
               resolveFailed.append(PicketBoxMessages.MESSAGES.failedToResolveTargetStateMessage(targetName, name));
            }
            Transition t = new Transition(name, target);
            s.addTransition(t);
         }
      }

      if (resolveFailed.length() > 0)
         throw new Exception(resolveFailed.toString());

      StateMachine sm = new StateMachine(states, startState, description);
      return sm;
   }
   private void safeClose(InputStream fis)
   {
      try
      {
         if(fis != null)
         {
            fis.close();
         }
      }
      catch(Exception e)
      {}
   }
}