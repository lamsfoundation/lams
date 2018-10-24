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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;

/** The representation of a finite state machine.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public class StateMachine implements Cloneable
{
   /** A description of the state machine */
   private String description;
   /** The set of states making up the state machine */
   private HashSet states;
   /** The starting state */
   private State startState;
   /** The current state of the state machine */
   private State currentState;

   /** Create a state machine given its states and start state.
    * 
    * @param states - Set<State> for the state machine
    * @param startState - the starting state
    */ 
   public StateMachine(Set states, State startState)
   {
      this(states, startState, null);
   }
   /** Create a state machine given its states and start state.
    * 
    * @param states - Set<State> for the state machine
    * @param startState - the starting state
    * @param description - an optional description of the state machine
    */ 
   public StateMachine(Set states, State startState, String description)
   {
      this.states = new HashSet(states);
      this.startState = startState;
      this.currentState = startState;
      this.description = description;
   }

   /** Make a copy of the StateMachine maintaining the current state.
    * 
    * @return a copy of the StateMachine.
    */ 
   public Object clone()
   {
      StateMachine clone = new StateMachine(states, startState, description);
      clone.currentState = currentState;
      return clone;
   }

   /** Get the state machine description.
    * @return an possibly null description.
    */ 
   public String getDescription()
   {
      return description;
   }

   /** Get the current state of the state machine.
    * @return the current state.
    */ 
   public State getCurrentState()
   {
      return currentState;
   }

   /** Get the start state of the state machine.
    * @return the start state.
    */ 
   public State getStartState()
   {
      return startState;
   }

   /** Get the states of the state machine.
    * @return the machine states.
    */ 
   public Set getStates()
   {
      return states;
   }

   /** Transition to the next state given the name of a valid transition.
    * @param actionName - the name of transition that is valid for the
    * current state. 
    * @return the next state
    * @throws IllegalTransitionException
    */ 
   public State nextState(String actionName)
      throws IllegalTransitionException
   {
      Transition t = currentState.getTransition(actionName);
      if(t == null)
      {
         throw new IllegalTransitionException(PicketBoxMessages.MESSAGES.invalidTransitionForActionMessage(actionName,
                 currentState != null ? currentState.getName() : null));
      }
      State nextState = t.getTarget();
      PicketBoxLogger.LOGGER.traceStateMachineNextState(actionName, nextState != null ? nextState.getName() : null);
      currentState = nextState;
      return currentState;
   }

   /** Reset the state machine back to the start state
    * 
    * @return the start state
    */ 
   public State reset()
   {
      this.currentState = startState;
      return currentState;
   }

   public String toString()
   {
      StringBuffer tmp = new StringBuffer("StateMachine[:\n");
      tmp.append("\tCurrentState: "+currentState.getName());
      Iterator i = states.iterator();
      while( i.hasNext() )
      {
         tmp.append('\n').append(i.next());
      }
      tmp.append(']');
      return tmp.toString();
   }
}
