/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2000 - 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.cache.marshall;

import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.PrepareCommand;

import java.lang.reflect.Method;

/**
 * An extension of the JGroups MethodCall class.  The reason for this subclass is a minor
 * optimisation in the way method IDs are dealt with. The JGroups class of the same name uses
 * a short as a method id, which is more efficient as far as network streaming is concerned.
 * <p/>
 * However, JBossCache uses this id for a lot of == and switch comparisons.  Java, being an
 * integer oriented virtual machine, goes through a lot of extra steps when performing such simple
 * comparisons or arithmetic on non-integer numeric types.
 * <p/>
 * See <a href="http://www.liemur.com/Articles/FineTuningJavaCode-IntOrientedMachine.html">http://www.liemur.com/Articles/FineTuningJavaCode-IntOrientedMachine.html</a>
 * <p/>
 * Thanks to Elias Ross/genman for this info.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @deprecated - in favour of {@link org.jboss.cache.commands.ReplicableCommand} instances.  Will be removed in 3.X.
 */
@Deprecated
public class MethodCall extends org.jgroups.blocks.MethodCall
{
   /**
    * It's unclear why this class would be serialized.
    */
   private static final long serialVersionUID = -5316198032742449998L;

   private int methodIdInteger = -1;

   public MethodCall()
   {
      // for serialization
   }

   /**
    * This only works for prepare() and optimisticPrepare() method calls.
    */
   public boolean isOnePhaseCommitPrepareMehod()
   {
      switch (this.getMethodId())
      {
         case PrepareCommand.METHOD_ID:
            return (Boolean) this.getArgs()[3];
         case OptimisticPrepareCommand.METHOD_ID:
            return (Boolean) this.getArgs()[4];
         default:
            return false;
      }
   }


   protected MethodCall(Method method, Object... arguments)
   {
      super(method, arguments);
   }

   protected MethodCall(Method method, int methodIdInteger, Object... arguments)
   {
      super(method, arguments);
      this.methodIdInteger = methodIdInteger;
   }

   public void setMethodId(int id)
   {
      methodIdInteger = id;
   }

   public int getMethodId()
   {
      return methodIdInteger;
   }

   @Override
   public short getId()
   {
      throw new RuntimeException("Use of incorrect method!  Are you sure you intend to do this instead of getMethodId()?!?");
   }

   @Override
   public String toString()
   {
      StringBuilder ret = new StringBuilder();
      ret.append("MethodName: ");
      ret.append(method_name);
      ret.append("; MethodIdInteger: ");
      ret.append(methodIdInteger);
      ret.append("; Args: (");
      if (args != null && args.length > 0)
      {
         if (log.isTraceEnabled())
         {
            boolean first = true;
            for (Object arg : args)
            {
               if (first) first = false;
               else ret.append(", ");

               ret.append(arg);
            }
         }
         else
         {
            ret.append(" arg[0] = ");
            ret.append(args[0]);
            if (args.length > 1) ret.append(" ...");
         }
      }

      ret.append(')');
      return ret.toString();
   }
}
