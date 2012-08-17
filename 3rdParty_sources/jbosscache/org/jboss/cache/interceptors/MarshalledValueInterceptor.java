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
package org.jboss.cache.interceptors;

import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.interceptors.base.CommandInterceptor;
import org.jboss.cache.marshall.MarshalledValue;
import org.jboss.cache.marshall.MarshalledValueHelper;
import org.jboss.cache.marshall.MarshalledValueMap;

import java.io.IOException;
import java.io.NotSerializableException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Interceptor that handles the wrapping and unwrapping of cached data using {@link org.jboss.cache.marshall.MarshalledValue}s.
 * Known "excluded" types are not wrapped/unwrapped, which at this time include {@link String}, Java primitives
 * and their Object wrappers, as well as arrays of excluded types.
 * <p/>
 * The {@link org.jboss.cache.marshall.MarshalledValue} wrapper handles lazy deserialization from byte array representations.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see org.jboss.cache.marshall.MarshalledValue
 * @since 2.1.0
 */
public class MarshalledValueInterceptor extends CommandInterceptor
{

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      if (command.getDataVersion() != null)
      {
         return invokeNextInterceptor(ctx, command);
      }
      Set<MarshalledValue> marshalledValues = new HashSet<MarshalledValue>();
      command.setData(wrapMap(command.getData(), marshalledValues, ctx));
      Object retVal = invokeNextInterceptor(ctx, command);
      return ctx.isBypassUnmarshalling() ? retVal : compactAndProcessRetVal(marshalledValues, retVal);
   }

   @Override
   public Object visitGetDataMapCommand(InvocationContext ctx, GetDataMapCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      if (retVal instanceof Map)
      {
         if (trace) log.trace("Return value is a Map and we're retrieving data.  Wrapping as a MarshalledValueMap.");
         Map retValMap = (Map) retVal;
         if (!ctx.isBypassUnmarshalling() && !retValMap.isEmpty()) retVal = new MarshalledValueMap(retValMap);
      }
      return retVal;
   }

   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return visitPutKeyValueCommand(ctx, command);
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      Set<MarshalledValue> marshalledValues = new HashSet<MarshalledValue>();
      if (!MarshalledValueHelper.isTypeExcluded(command.getKey().getClass()))
      {
         Object newKey = createAndAddMarshalledValue(command.getKey(), marshalledValues, ctx);
         command.setKey(newKey);
      }
      if (!MarshalledValueHelper.isTypeExcluded(command.getValue().getClass()))
      {
         Object value = createAndAddMarshalledValue(command.getValue(), marshalledValues, ctx);
         command.setValue(value);
      }
      Object retVal = invokeNextInterceptor(ctx, command);
      return ctx.isBypassUnmarshalling() ? retVal : compactAndProcessRetVal(marshalledValues, retVal);
   }

   @Override
   public Object visitGetNodeCommand(InvocationContext ctx, GetNodeCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      return ctx.isBypassUnmarshalling() ? retVal :  processRetVal(retVal);
   }

   @Override
   public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      return ctx.isBypassUnmarshalling() ? retVal :  processRetVal(retVal);
   }

   @Override
   public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
   {
      Set<MarshalledValue> marshalledValues = new HashSet<MarshalledValue>();
      if (!MarshalledValueHelper.isTypeExcluded(command.getKey().getClass()))
      {
         Object value = createAndAddMarshalledValue(command.getKey(), marshalledValues, ctx);
         command.setKey(value);
      }
      Object retVal = invokeNextInterceptor(ctx, command);
      return ctx.isBypassUnmarshalling() ? retVal :  compactAndProcessRetVal(marshalledValues, retVal);
   }

   @Override
   public Object visitGetChildrenNamesCommand(InvocationContext ctx, GetChildrenNamesCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      return ctx.isBypassUnmarshalling() ? retVal :  processRetVal(retVal);
   }

   @Override
   public Object visitGetKeysCommand(InvocationContext ctx, GetKeysCommand command) throws Throwable
   {
      Object retVal = invokeNextInterceptor(ctx, command);
      return ctx.isBypassUnmarshalling() ? retVal :  processRetVal(retVal);
   }

   @Override
   public Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      Set<MarshalledValue> marshalledValues = new HashSet<MarshalledValue>();
      if (!MarshalledValueHelper.isTypeExcluded(command.getKey().getClass()))
      {
         Object value = createAndAddMarshalledValue(command.getKey(), marshalledValues, ctx);
         command.setKey(value);
      }
      Object retVal = invokeNextInterceptor(ctx, command);
      return ctx.isBypassUnmarshalling() ? retVal :  compactAndProcessRetVal(marshalledValues, retVal);
   }

   private Object compactAndProcessRetVal(Set<MarshalledValue> marshalledValues, Object retVal)
         throws IOException, ClassNotFoundException
   {
      if (trace) log.trace("Compacting MarshalledValues created");
      for (MarshalledValue mv : marshalledValues) mv.compact(false, false);

      return processRetVal(retVal);
   }

   private Object processRetVal(Object retVal)
         throws IOException, ClassNotFoundException
   {
      if (retVal instanceof MarshalledValue)
      {
         if (trace) log.trace("Return value is a MarshalledValue.  Unwrapping.");
         retVal = ((MarshalledValue) retVal).get();
      }
      return retVal;
   }

   @SuppressWarnings("unchecked")
   protected Map wrapMap(Map<Object, Object> m, Set<MarshalledValue> marshalledValues, InvocationContext ctx) throws NotSerializableException
   {
      if (m == null)
      {
         if (trace) log.trace("Map is nul; returning an empty map.");
         return Collections.emptyMap();
      }
      if (trace) log.trace("Wrapping map contents of argument " + m);
      Map copy = new HashMap();
      for (Map.Entry me : m.entrySet())
      {
         Object key = me.getKey();
         Object value = me.getValue();
         copy.put((key == null || MarshalledValueHelper.isTypeExcluded(key.getClass())) ? key : createAndAddMarshalledValue(key, marshalledValues, ctx),
               (value == null || MarshalledValueHelper.isTypeExcluded(value.getClass())) ? value : createAndAddMarshalledValue(value, marshalledValues, ctx));
      }
      return copy;
   }

   protected MarshalledValue createAndAddMarshalledValue(Object toWrap, Set<MarshalledValue> marshalledValues, InvocationContext ctx) throws NotSerializableException
   {
      MarshalledValue mv = new MarshalledValue(toWrap);
      marshalledValues.add(mv);
      if (!ctx.isOriginLocal()) mv.setEqualityPreferenceForInstance(false);
      return mv;
   }
}
