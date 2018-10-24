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
package org.jboss.security.util.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/** The respresentation of a state in a state machine.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public class State
{
   /** The name of the state */
   private String name;
   /** HashMap<String, Transition> */
   private HashMap allowedTransitions = new HashMap();
   /** Arbitrary state data */
   private Object data;

   public State(String name)
   {
      this(name, null);
   }
   public State(String name, Map transitions)
   {
      this.name = name;
      if( transitions != null )
      {
         allowedTransitions.putAll(transitions);
      }
   }

   /** Get the state name.
    * @return the name of the state.
    */ 
   public String getName()
   {
      return name;
   }

   public Object getData()
   {
      return data;
   }
   public void setData(Object data)
   {
      this.data = data;
   }

   /** An accept state is indicated by no transitions
    * @return true if this is an accept state, false otherwise.
    */ 
   public boolean isAcceptState()
   {
      return allowedTransitions.size() == 0;
   }

   /** Add a transition to the allowed transition map.
    * 
    * @param transition
    * @return this to allow chained addTransition calls
    */ 
   public State addTransition(Transition transition)
   {
      allowedTransitions.put(transition.getName(), transition);
      return this;
   }
   
   /** Lookup an allowed transition given its name.
    * 
    * @param name - the name of a valid transition from this state.
    * @return the valid transition if it exists, null otherwise.
    */ 
   public Transition getTransition(String name)
   {
      Transition t = (Transition) allowedTransitions.get(name);
      return t;
   }

   /** Get the Map<String, Transition> of allowed transitions for this state.
    * @return the allowed transitions map.
    */ 
   public Map getTransitions()
   {
      return allowedTransitions;
   }

   public String toString()
   {
      StringBuffer tmp = new StringBuffer("State(name=");
      tmp.append(name);
      Iterator i = allowedTransitions.entrySet().iterator();
      while( i.hasNext() )
      {
         Map.Entry e = (Map.Entry) i.next();
         tmp.append("\n\t on: ");
         tmp.append(e.getKey());
         Transition t = (Transition) e.getValue();
         tmp.append(" go to: ");
         tmp.append(t.getTarget().getName());
      }
      tmp.append(')');
      return tmp.toString();
   }
}
