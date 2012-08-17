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

import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.Region;
import org.jboss.cache.Region.Status;
import org.jboss.cache.buddyreplication.GravitateResult;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.optimistic.DefaultDataVersion;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionLog.LogEntry;
import org.jboss.cache.util.FastCopyHashMap;
import org.jboss.cache.util.Immutables;
import org.jgroups.Address;
import org.jgroups.stack.IpAddress;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * An enhanced marshaller for RPC calls between CacheImpl instances.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
public class CacheMarshaller200 extends AbstractMarshaller
{
   // magic numbers
   protected static final int MAGICNUMBER_METHODCALL = 1;
   protected static final int MAGICNUMBER_FQN = 2;
   protected static final int MAGICNUMBER_GTX = 3;
   protected static final int MAGICNUMBER_IPADDRESS = 4;
   protected static final int MAGICNUMBER_ARRAY_LIST = 5;
   protected static final int MAGICNUMBER_INTEGER = 6;
   protected static final int MAGICNUMBER_LONG = 7;
   protected static final int MAGICNUMBER_BOOLEAN = 8;
   protected static final int MAGICNUMBER_STRING = 9;
   protected static final int MAGICNUMBER_DEFAULT_DATA_VERSION = 10;
   protected static final int MAGICNUMBER_LINKED_LIST = 11;
   protected static final int MAGICNUMBER_HASH_MAP = 12;
   protected static final int MAGICNUMBER_TREE_MAP = 13;
   protected static final int MAGICNUMBER_HASH_SET = 14;
   protected static final int MAGICNUMBER_TREE_SET = 15;
   protected static final int MAGICNUMBER_NODEDATA_MARKER = 16;
   protected static final int MAGICNUMBER_NODEDATA_EXCEPTION_MARKER = 17;
   protected static final int MAGICNUMBER_NODEDATA = 18;
   protected static final int MAGICNUMBER_GRAVITATERESULT = 19;
   protected static final int MAGICNUMBER_SHORT = 20;
   protected static final int MAGICNUMBER_IMMUTABLE_MAPCOPY = 21;
   protected static final int MAGICNUMBER_MARSHALLEDVALUE = 22;
   protected static final int MAGICNUMBER_FASTCOPY_HASHMAP = 23;
   protected static final int MAGICNUMBER_ARRAY = 24;
   protected static final int MAGICNUMBER_BYTE = 25;
   protected static final int MAGICNUMBER_CHAR = 26;
   protected static final int MAGICNUMBER_FLOAT = 27;
   protected static final int MAGICNUMBER_DOUBLE = 28;
   protected static final int MAGICNUMBER_OBJECT = 29;
   protected static final int MAGICNUMBER_TXLOG_ENTRY = 50;
   protected static final int MAGICNUMBER_REQUEST_IGNORED_RESPONSE = 51;
   protected static final int MAGICNUMBER_EXTENDED_RESPONSE = 52;
   protected static final int MAGICNUMBER_NULL = 99;
   protected static final int MAGICNUMBER_SERIALIZABLE = 100;
   protected static final int MAGICNUMBER_REF = 101;


   protected static final InactiveRegionException IRE = new InactiveRegionException("Cannot unmarshall to an inactive region");

   public CacheMarshaller200()
   {
      initLogger();
      // enabled, since this is always enabled in JBC 2.0.0.
      useRefs = true;
   }

   protected CommandsFactory commandsFactory;

   @Inject
   public void injectCommandsFactory(CommandsFactory commandsFactory)
   {
      this.commandsFactory = commandsFactory;
   }

   // -------- AbstractMarshaller interface

   public void objectToObjectStream(Object o, ObjectOutputStream out) throws Exception
   {
      if (useRegionBasedMarshalling)
      {
         Fqn region = null;

         if (o instanceof RegionalizedReturnValue)
         {
            RegionalizedReturnValue rrv = (RegionalizedReturnValue) o;
            region = rrv.region;
            o = rrv.returnValue;
         }
         else if (o instanceof ReplicableCommand)
         {
            ReplicableCommand marshallableCommand = (ReplicableCommand) o;
            region = extractFqnRegion(marshallableCommand);
         }

         if (trace) log.trace("Region based call.  Using region " + region);
         objectToObjectStream(o, out, region);
      }
      else
      {
         // not region based!
         objectToObjectStream(o, out, null);
      }
   }

   @Override
   public RegionalizedMethodCall regionalizedMethodCallFromObjectStream(ObjectInputStream in) throws Exception
   {
      // parse the stream as per normal.
      Object[] retVal = objectFromObjectStreamRegionBased(in);
      RegionalizedMethodCall rmc = new RegionalizedMethodCall();
      rmc.command = (ReplicableCommand) retVal[0];
      rmc.region = (Fqn) retVal[1];
      return rmc;
   }


   public Object objectFromObjectStream(ObjectInputStream in) throws Exception
   {
      if (useRegionBasedMarshalling)
      {
         return objectFromObjectStreamRegionBased(in)[0];
      }
      else
      {
         UnmarshalledReferences refMap = useRefs ? new UnmarshalledReferences() : null;
         Object retValue = unmarshallObject(in, defaultClassLoader, refMap, false);
         if (trace) log.trace("Unmarshalled object " + retValue);
         return retValue;
      }
   }

   public void objectToObjectStream(Object o, ObjectOutputStream out, Fqn region) throws Exception
   {
      if (trace) log.trace("Marshalling object " + o);
      Map<Object, Integer> refMap = useRefs ? new IdentityHashMap<Object, Integer>() : null;
      ClassLoader toUse = defaultClassLoader;
      Thread current = Thread.currentThread();
      ClassLoader old = current.getContextClassLoader();
      if (old != null) toUse = old;

      try
      {
         if (useRegionBasedMarshalling) // got to check again in case this meth is called directly
         {
            if (trace) log.trace("Writing region " + region + " to stream");
            Region r = null;
            if (region != null) r = regionManager.getRegion(region, false);
            if (r != null && r.getClassLoader() != null) toUse = r.getClassLoader();
            current.setContextClassLoader(toUse);
            marshallObject(region, out, refMap);
         }
         else
         {
            current.setContextClassLoader(toUse);
         }
         marshallObject(o, out, refMap);
      }
      catch (Exception th)
      {
         if(log.isErrorEnabled()) log.error("Error while marshalling object: " + o, th);
         throw th;
      }
      finally
      {
         if (log.isTraceEnabled()) log.trace("Done serializing object: " + o);
         current.setContextClassLoader(old);
      }
   }

   /**
    * @param in
    * @return a 2-object array.  The first one is the unmarshalled object and the 2nd is an Fqn that relates to the region used.  If region-based marshalling is not used, the 2nd value is null.
    * @throws Exception
    */
   protected Object[] objectFromObjectStreamRegionBased(ObjectInputStream in) throws Exception
   {
      UnmarshalledReferences refMap = useRefs ? new UnmarshalledReferences() : null;
      Object o = unmarshallObject(in, refMap);
      Fqn regionFqn = null;
      if (o == null)
      {
         // a null region.  Could happen.  Use std marshalling.
         log.trace("Unmarshalled region as null.  Not using a context class loader to unmarshall.");
      }
      else
      {
         regionFqn = (Fqn) o;
      }

      if (trace) log.trace("Unmarshalled regionFqn " + regionFqn + " from stream");

      Region region = null;
      Object[] retValue = {null, null};

      if (regionFqn != null)
      {
         region = findRegion(regionFqn);
      }
      if (region == null)
      {
         if (log.isDebugEnabled())
            log.debug("Region does not exist for Fqn " + regionFqn + " - not using a context classloader.");
         retValue[0] = unmarshallObject(in, defaultClassLoader, refMap, false);
      }
      else
      {
         retValue[0] = unmarshallObject(in, region.getClassLoader(), refMap, true);
         retValue[1] = regionFqn;
      }
      if (trace) log.trace("Unmarshalled object " + retValue[0] + " with region " + retValue[1]);
      return retValue;
   }

   private Region findRegion(Fqn fqn) throws InactiveRegionException
   {
      Region region = regionManager.getValidMarshallingRegion(fqn);

      if (region != null)
      {
         Status status = region.getStatus();
         if (status == Status.INACTIVATING || status == Status.INACTIVE)
         {
            if (log.isDebugEnabled())
            {
               throw new InactiveRegionException("Cannot unmarshall message for region " + fqn + ". This region is inactive.");
            }
            else
            {
               throw IRE;
            }
         }
      }
      else if (defaultInactive)
      {
         if (log.isDebugEnabled())
         {
            // No region but default inactive means region is inactive
            throw new InactiveRegionException("Cannot unmarshall message for region " + fqn + ". By default region " + fqn
                  + " is inactive.");
         }
         else
         {
            throw IRE;
         }
      }

      return region;
   }

   private Fqn extractFqnRegion(ReplicableCommand cmd) throws Exception
   {
      Fqn fqn = extractFqn(cmd);

      Region r = regionManager.getValidMarshallingRegion(fqn);
      return r == null ? null : r.getFqn();
   }

   // --------- Marshalling methods

   @SuppressWarnings("deprecation")
   protected void marshallObject(Object o, ObjectOutputStream out, Map<Object, Integer> refMap) throws Exception
   {
      if (o == null)
      {
         out.writeByte(MAGICNUMBER_NULL);
      }
      else if (useRefs && refMap.containsKey(o))// see if this object has been marshalled before.
      {
         out.writeByte(MAGICNUMBER_REF);
         writeReference(out, refMap.get(o));
      }
      else if (o instanceof ReplicableCommand)
      {
         ReplicableCommand command = (ReplicableCommand) o;

         if (command.getCommandId() > -1)
         {
            out.writeByte(MAGICNUMBER_METHODCALL);
            marshallCommand(command, out, refMap);
         }
         else
         {
            throw new IllegalArgumentException("MethodCall does not have a valid method id.  Was this method call created with MethodCallFactory?");
         }
      }
      else if (o instanceof org.jgroups.blocks.MethodCall)
      {
         throw new IllegalArgumentException("Usage of a legacy MethodCall object!!");
      }
      else if (o instanceof MarshalledValue)
      {
         out.writeByte(MAGICNUMBER_MARSHALLEDVALUE);
         ((MarshalledValue) o).writeExternal(out);
      }
      else if (o instanceof Fqn)
      {
         out.writeByte(MAGICNUMBER_FQN);
         if (useRefs) writeReference(out, createReference(o, refMap));
         marshallFqn((Fqn) o, out, refMap);
      }
      else if (o instanceof GlobalTransaction)
      {
         out.writeByte(MAGICNUMBER_GTX);
         if (useRefs) writeReference(out, createReference(o, refMap));
         marshallGlobalTransaction((GlobalTransaction) o, out, refMap);
      }
      else if (o instanceof LogEntry)
      {
         out.writeByte(MAGICNUMBER_TXLOG_ENTRY);
         marshallLogEntry((LogEntry)o, out, refMap);
      }
      else if (o instanceof IpAddress)
      {
         out.writeByte(MAGICNUMBER_IPADDRESS);
         marshallIpAddress((IpAddress) o, out);
      }
      else if (o instanceof DefaultDataVersion)
      {
         out.writeByte(MAGICNUMBER_DEFAULT_DATA_VERSION);
         marshallDefaultDataVersion((DefaultDataVersion) o, out);
      }
      else if (o.getClass().equals(ArrayList.class))
      {
         out.writeByte(MAGICNUMBER_ARRAY_LIST);
         marshallCollection((Collection) o, out, refMap);
      }
      else if (o.getClass().equals(LinkedList.class))
      {
         out.writeByte(MAGICNUMBER_LINKED_LIST);
         marshallCollection((Collection) o, out, refMap);
      }
      else if (o.getClass().equals(HashMap.class))
      {
         out.writeByte(MAGICNUMBER_HASH_MAP);
         marshallMap((Map) o, out, refMap);
      }
      else if (o.getClass().equals(TreeMap.class))
      {
         out.writeByte(MAGICNUMBER_TREE_MAP);
         marshallMap((Map) o, out, refMap);
      }
      else if (o.getClass().equals(FastCopyHashMap.class))
      {
         out.writeByte(MAGICNUMBER_FASTCOPY_HASHMAP);
         marshallMap((Map) o, out, refMap);
      }
      else if (o instanceof Map && Immutables.isImmutable(o))
      {
         out.writeByte(MAGICNUMBER_IMMUTABLE_MAPCOPY);
         marshallMap((Map) o, out, refMap);
      }
      else if (o.getClass().equals(HashSet.class))
      {
         out.writeByte(MAGICNUMBER_HASH_SET);
         marshallCollection((Collection) o, out, refMap);
      }
      else if (o.getClass().equals(TreeSet.class))
      {
         out.writeByte(MAGICNUMBER_TREE_SET);
         marshallCollection((Collection) o, out, refMap);
      }
      else if (o instanceof Boolean)
      {
         out.writeByte(MAGICNUMBER_BOOLEAN);
         out.writeBoolean(((Boolean) o).booleanValue());
      }
      else if (o instanceof Integer)
      {
         out.writeByte(MAGICNUMBER_INTEGER);
         out.writeInt(((Integer) o).intValue());
      }
      else if (o instanceof Long)
      {
         out.writeByte(MAGICNUMBER_LONG);
         out.writeLong(((Long) o).longValue());
      }
      else if (o instanceof Short)
      {
         out.writeByte(MAGICNUMBER_SHORT);
         out.writeShort(((Short) o).shortValue());
      }
      else if (o instanceof String)
      {
         out.writeByte(MAGICNUMBER_STRING);
         if (useRefs) writeReference(out, createReference(o, refMap));
         marshallString((String) o, out);
      }
      else if (o instanceof NodeDataMarker)
      {
         out.writeByte(MAGICNUMBER_NODEDATA_MARKER);
         ((Externalizable) o).writeExternal(out);
      }
      else if (o instanceof NodeDataExceptionMarker)
      {
         out.writeByte(MAGICNUMBER_NODEDATA_EXCEPTION_MARKER);
         ((Externalizable) o).writeExternal(out);
      }
      else if (o instanceof NodeData)
      {
         out.writeByte(MAGICNUMBER_NODEDATA);
         ((Externalizable) o).writeExternal(out);
      }
      else if (o instanceof GravitateResult)
      {
         out.writeByte(MAGICNUMBER_GRAVITATERESULT);
         marshallGravitateResult((GravitateResult) o, out, refMap);
      }
      else if (o instanceof RequestIgnoredResponse)
      {
         out.writeByte(MAGICNUMBER_REQUEST_IGNORED_RESPONSE);
      }
      else if (o instanceof ExtendedResponse)
      {
         out.writeByte(MAGICNUMBER_EXTENDED_RESPONSE);
         marshallExtendedResponse((ExtendedResponse)o, out, refMap);
      }
      else if (o instanceof Serializable)
      {
         if (trace)
         {
            log.trace("Not optimum: using object serialization for " + o.getClass());
         }
         out.writeByte(MAGICNUMBER_SERIALIZABLE);
         if (useRefs) writeReference(out, createReference(o, refMap));
         out.writeObject(o);
      }
      else
      {
         throw new Exception("Don't know how to marshall object of type " + o.getClass());
      }
   }

   private void marshallExtendedResponse(ExtendedResponse response, ObjectOutputStream out, Map<Object, Integer> refMap) throws Exception
   {
      out.writeBoolean(response.isReplayIgnoredRequests());
      marshallObject(response.getResponse(), out, refMap);
   }

   private void marshallLogEntry(LogEntry log, ObjectOutputStream out, Map<Object, Integer> refMap) throws Exception
   {
      marshallObject(log.getTransaction(), out, refMap);
      List<WriteCommand> mods = log.getModifications();
      boolean isList = mods.size() > 1;
      out.writeBoolean(isList);
      if (isList)
         marshallObject(log.getModifications(), out, refMap);
      else
         marshallObject(mods.get(0), out, refMap);
   }

   private void marshallGravitateResult(GravitateResult gravitateResult, ObjectOutputStream out, Map<Object, Integer> refMap) throws Exception
   {
      marshallObject(gravitateResult.isDataFound(), out, refMap);
      if (gravitateResult.isDataFound())
      {
         marshallObject(gravitateResult.getNodeData(), out, refMap);
         marshallObject(gravitateResult.getBuddyBackupFqn(), out, refMap);
      }

   }

   private int createReference(Object o, Map<Object, Integer> refMap)
   {
      int reference = refMap.size();
      refMap.put(o, reference);
      return reference;
   }

   protected void marshallString(String s, ObjectOutputStream out) throws Exception
   {
      //StringUtil.saveString(out, s);
      out.writeObject(s);
   }

   private void marshallCommand(ReplicableCommand command, ObjectOutputStream out, Map<Object, Integer> refMap) throws Exception
   {
      out.writeShort(command.getCommandId());
      Object[] args = command.getParameters();
      byte numArgs = (byte) (args == null ? 0 : args.length);
      out.writeByte(numArgs);

      for (int i = 0; i < numArgs; i++)
      {
         marshallObject(args[i], out, refMap);
      }
   }

   private void marshallGlobalTransaction(GlobalTransaction globalTransaction, ObjectOutputStream out, Map<Object, Integer> refMap) throws Exception
   {
      out.writeLong(globalTransaction.getId());
      marshallObject(globalTransaction.getAddress(), out, refMap);
   }


   protected void marshallFqn(Fqn fqn, ObjectOutputStream out, Map<Object, Integer> refMap) throws Exception
   {
      boolean isRoot = fqn.isRoot();
      out.writeBoolean(isRoot);
      if (!isRoot)
      {
         out.writeShort(fqn.size());
         for (Object o : fqn.peekElements())
         {
            marshallObject(o, out, refMap);
         }
      }
   }

   private void marshallIpAddress(IpAddress ipAddress, ObjectOutputStream out) throws Exception
   {
      ipAddress.writeExternal(out);
   }

   @SuppressWarnings("unchecked")
   private void marshallCollection(Collection c, ObjectOutputStream out, Map refMap) throws Exception
   {
      writeUnsignedInt(out, c.size());
      for (Object o : c)
      {
         marshallObject(o, out, refMap);
      }
   }

   @SuppressWarnings("unchecked")
   private void marshallMap(Map map, ObjectOutputStream out, Map<Object, Integer> refMap) throws Exception
   {
      int mapSize = map.size();
      writeUnsignedInt(out, mapSize);
      if (mapSize == 0) return;

      for (Map.Entry me : (Set<Map.Entry>) map.entrySet())
      {
         marshallObject(me.getKey(), out, refMap);
         marshallObject(me.getValue(), out, refMap);
      }
   }

   // --------- Unmarshalling methods

   protected Object unmarshallObject(ObjectInputStream in, ClassLoader loader, UnmarshalledReferences refMap, boolean overrideContextClassloaderOnThread) throws Exception
   {
      if (loader == null)
      {
         return unmarshallObject(in, refMap);
      }
      else
      {
         Thread currentThread = Thread.currentThread();
         ClassLoader old = currentThread.getContextClassLoader();
         try
         {
            // only do this if we haven't already set a context class loader elsewhere.
            if (overrideContextClassloaderOnThread || old == null) currentThread.setContextClassLoader(loader);
            return unmarshallObject(in, refMap);
         }
         finally
         {
            if (overrideContextClassloaderOnThread || old == null) currentThread.setContextClassLoader(old);
         }
      }
   }

   protected Object unmarshallObject(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      byte magicNumber = in.readByte();
      int reference = 0;
      Object retVal;
      switch (magicNumber)
      {
         case MAGICNUMBER_NULL:
            return null;
         case MAGICNUMBER_REF:
            if (useRefs)
            {
               reference = readReference(in);
               return refMap.getReferencedObject(reference);
            }
            else break;
         case MAGICNUMBER_SERIALIZABLE:
            if (useRefs) reference = readReference(in);
            retVal = in.readObject();
            if (useRefs) refMap.putReferencedObject(reference, retVal);
            return retVal;
         case MAGICNUMBER_MARSHALLEDVALUE:
            MarshalledValue mv = new MarshalledValue();
            mv.readExternal(in);
            return mv;
         case MAGICNUMBER_METHODCALL:
            retVal = unmarshallCommand(in, refMap);
            return retVal;
         case MAGICNUMBER_FQN:
            if (useRefs) reference = readReference(in);
            retVal = unmarshallFqn(in, refMap);
            if (useRefs) refMap.putReferencedObject(reference, retVal);
            return retVal;
         case MAGICNUMBER_GTX:
            if (useRefs) reference = readReference(in);
            retVal = unmarshallGlobalTransaction(in, refMap);
            if (useRefs) refMap.putReferencedObject(reference, retVal);
            return retVal;
         case MAGICNUMBER_TXLOG_ENTRY:
            return unmarshallLogEntry(in, refMap);
         case MAGICNUMBER_IPADDRESS:
            retVal = unmarshallIpAddress(in);
            return retVal;
         case MAGICNUMBER_DEFAULT_DATA_VERSION:
            retVal = unmarshallDefaultDataVersion(in);
            return retVal;
         case MAGICNUMBER_ARRAY:
            return unmarshallArray(in, refMap);
         case MAGICNUMBER_ARRAY_LIST:
            return unmarshallArrayList(in, refMap);
         case MAGICNUMBER_LINKED_LIST:
            return unmarshallLinkedList(in, refMap);
         case MAGICNUMBER_HASH_MAP:
            return unmarshallHashMap(in, refMap);
         case MAGICNUMBER_TREE_MAP:
            return unmarshallTreeMap(in, refMap);
         case MAGICNUMBER_HASH_SET:
            return unmarshallHashSet(in, refMap);
         case MAGICNUMBER_TREE_SET:
            return unmarshallTreeSet(in, refMap);
         case MAGICNUMBER_IMMUTABLE_MAPCOPY:
            return unmarshallMapCopy(in, refMap);
         case MAGICNUMBER_FASTCOPY_HASHMAP:
            return unmarshallFastCopyHashMap(in, refMap);
         case MAGICNUMBER_BOOLEAN:
            return in.readBoolean() ? Boolean.TRUE : Boolean.FALSE;
         case MAGICNUMBER_INTEGER:
            return in.readInt();
         case MAGICNUMBER_LONG:
            return in.readLong();
         case MAGICNUMBER_SHORT:
            return in.readShort();
         case MAGICNUMBER_STRING:
            if (useRefs) reference = readReference(in);
            retVal = unmarshallString(in);
            if (useRefs) refMap.putReferencedObject(reference, retVal);
            return retVal;
         case MAGICNUMBER_NODEDATA_MARKER:
            retVal = new NodeDataMarker();
            ((NodeDataMarker) retVal).readExternal(in);
            return retVal;
         case MAGICNUMBER_NODEDATA_EXCEPTION_MARKER:
            retVal = new NodeDataExceptionMarker();
            ((NodeDataExceptionMarker) retVal).readExternal(in);
            return retVal;
         case MAGICNUMBER_NODEDATA:
            retVal = new NodeData();
            ((NodeData) retVal).readExternal(in);
            return retVal;
         case MAGICNUMBER_GRAVITATERESULT:
            return unmarshallGravitateResult(in, refMap);
         case MAGICNUMBER_REQUEST_IGNORED_RESPONSE:
            return new RequestIgnoredResponse();
         case MAGICNUMBER_EXTENDED_RESPONSE:
            return unmarshallExtendedResponse(in, refMap);
         default:
            if (log.isErrorEnabled())
            {
               log.error("Unknown Magic Number " + magicNumber);
            }
            throw new Exception("Unknown magic number " + magicNumber);
      }
      throw new Exception("Unknown magic number " + magicNumber);
   }

   private ExtendedResponse unmarshallExtendedResponse(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      boolean replayIgnoredRequests = in.readBoolean();
      ExtendedResponse response = new ExtendedResponse(unmarshallObject(in, refMap));
      response.setReplayIgnoredRequests(replayIgnoredRequests);

      return response;
   }

   @SuppressWarnings("unchecked")
   private LogEntry unmarshallLogEntry(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      GlobalTransaction gtx = (GlobalTransaction)unmarshallObject(in, refMap);
      boolean isList = in.readBoolean();
      List mods;
      if (isList)
         mods = (List)unmarshallObject(in, refMap);
      else
         mods = Collections.singletonList(unmarshallObject(in, refMap));
      return new LogEntry(gtx, mods);
   }

   private FastCopyHashMap unmarshallFastCopyHashMap(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      FastCopyHashMap map = new FastCopyHashMap();
      populateFromStream(in, refMap, map);
      return map;
   }

   @SuppressWarnings("unchecked")
   private GravitateResult unmarshallGravitateResult(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      Boolean found = (Boolean) unmarshallObject(in, refMap);
      if (found)
      {
         List<NodeData> stuff = (List<NodeData>) unmarshallObject(in, refMap);
         Fqn fqn = (Fqn) unmarshallObject(in, refMap);
         return GravitateResult.subtreeResult(stuff, fqn);
      }
      else
      {
         return GravitateResult.noDataFound();
      }
   }

   protected String unmarshallString(ObjectInputStream in) throws Exception
   {
      return (String) in.readObject();
   }

   private ReplicableCommand unmarshallCommand(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      short methodId = in.readShort();
      byte numArgs = in.readByte();
      Object[] args = null;

      if (numArgs > 0)
      {
         args = new Object[numArgs];
         for (int i = 0; i < numArgs; i++) args[i] = unmarshallObject(in, refMap);
      }

      return commandsFactory.fromStream(methodId, args);
   }

   private GlobalTransaction unmarshallGlobalTransaction(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      GlobalTransaction gtx = new GlobalTransaction();
      long id = in.readLong();
      Object address = unmarshallObject(in, refMap);
      gtx.setId(id);
      gtx.setAddress((Address) address);
      return gtx;
   }

   protected Fqn unmarshallFqn(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {

      boolean isRoot = in.readBoolean();
      Fqn fqn;
      if (!isRoot)
      {
         int numElements = in.readShort();
         List<Object> elements = new ArrayList<Object>(numElements);
         for (int i = 0; i < numElements; i++)
         {
            elements.add(unmarshallObject(in, refMap));
         }
         fqn = Fqn.fromList(elements, true);
      }
      else
      {
         fqn = Fqn.ROOT;
      }
      return fqn;
   }

   private IpAddress unmarshallIpAddress(ObjectInputStream in) throws Exception
   {
      IpAddress ipAddress = new IpAddress();
      ipAddress.readExternal(in);
      return ipAddress;
   }

   private List unmarshallArrayList(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      int listSize = readUnsignedInt(in);
      List list = new ArrayList(listSize);
      populateFromStream(in, refMap, list, listSize);
      return list;
   }

   private List unmarshallLinkedList(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      List list = new LinkedList();
      populateFromStream(in, refMap, list, readUnsignedInt(in));
      return list;
   }

   private Map unmarshallHashMap(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      Map map = new HashMap();
      populateFromStream(in, refMap, map);
      return map;
   }

   @SuppressWarnings("unchecked")
   private Map unmarshallMapCopy(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      // read in as a HashMap first
      Map m = unmarshallHashMap(in, refMap);
      return Immutables.immutableMapWrap(m);
   }

   private Map unmarshallTreeMap(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      Map map = new TreeMap();
      populateFromStream(in, refMap, map);
      return map;
   }

   private Set unmarshallHashSet(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      Set set = new HashSet();
      populateFromStream(in, refMap, set);
      return set;
   }

   private Set unmarshallTreeSet(ObjectInputStream in, UnmarshalledReferences refMap) throws Exception
   {
      Set set = new TreeSet();
      populateFromStream(in, refMap, set);
      return set;
   }

   @SuppressWarnings("unchecked")
   private void populateFromStream(ObjectInputStream in, UnmarshalledReferences refMap, Map mapToPopulate) throws Exception
   {
      int size = readUnsignedInt(in);
      for (int i = 0; i < size; i++) mapToPopulate.put(unmarshallObject(in, refMap), unmarshallObject(in, refMap));
   }

   @SuppressWarnings("unchecked")
   private void populateFromStream(ObjectInputStream in, UnmarshalledReferences refMap, Set setToPopulate) throws Exception
   {
      int size = readUnsignedInt(in);
      for (int i = 0; i < size; i++) setToPopulate.add(unmarshallObject(in, refMap));
   }

   @SuppressWarnings("unchecked")
   private void populateFromStream(ObjectInputStream in, UnmarshalledReferences refMap, List listToPopulate, int listSize) throws Exception
   {
      for (int i = 0; i < listSize; i++) listToPopulate.add(unmarshallObject(in, refMap));
   }

   @SuppressWarnings("deprecation")
   protected void marshallDefaultDataVersion(DefaultDataVersion ddv, ObjectOutputStream out) throws Exception
   {
      writeUnsignedLong(out, ddv.getRawVersion());
   }

   @SuppressWarnings("deprecation")
   protected DefaultDataVersion unmarshallDefaultDataVersion(ObjectInputStream in) throws Exception
   {
      return new DefaultDataVersion(readUnsignedLong(in));
   }

   /**
    * Reads a reference from a given stream.
    *
    * @param in the stream to read from
    * @return an int representing a reference in RefMap.
    * @throws IOException propagated from the OIS
    */
   protected int readReference(ObjectInputStream in) throws IOException
   {
      return in.readShort();
   }

   /**
    * Writes a reference to a given object output stream.
    *
    * @param out       the stream to write to
    * @param reference the reference to write
    * @throws java.io.IOException propagated from the OOS
    */
   protected void writeReference(ObjectOutputStream out, int reference) throws IOException
   {
      out.writeShort(reference);
   }

   protected int readUnsignedInt(ObjectInputStream in) throws IOException
   {
      return in.readInt();
   }

   protected void writeUnsignedInt(ObjectOutputStream out, int i) throws IOException
   {
      out.writeInt(i);
   }

   protected long readUnsignedLong(ObjectInputStream in) throws IOException
   {
      return in.readLong();
   }

   protected void writeUnsignedLong(ObjectOutputStream out, long i) throws IOException
   {
      out.writeLong(i);
   }

   protected Object unmarshallArray(ObjectInputStream in, UnmarshalledReferences refs) throws Exception
   {
      int sz = readUnsignedInt(in);
      byte type = in.readByte();
      switch (type)
      {
         case MAGICNUMBER_BOOLEAN:
         {
            boolean isPrim = in.readBoolean();
            if (isPrim)
            {
               boolean[] a = new boolean[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readBoolean();
               return a;
            }
            else
            {
               Boolean[] a = new Boolean[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readBoolean();
               return a;
            }
         }
         case MAGICNUMBER_INTEGER:
         {
            boolean isPrim = in.readBoolean();
            if (isPrim)
            {
               int[] a = new int[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readInt();
               return a;
            }
            else
            {
               Integer[] a = new Integer[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readInt();
               return a;
            }
         }
         case MAGICNUMBER_LONG:
         {
            boolean isPrim = in.readBoolean();
            if (isPrim)
            {
               long[] a = new long[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readLong();
               return a;
            }
            else
            {
               Long[] a = new Long[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readLong();
               return a;
            }
         }
         case MAGICNUMBER_CHAR:
         {
            boolean isPrim = in.readBoolean();
            if (isPrim)
            {
               char[] a = new char[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readChar();
               return a;
            }
            else
            {
               Character[] a = new Character[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readChar();
               return a;
            }
         }
         case MAGICNUMBER_BYTE:
         {
            boolean isPrim = in.readBoolean();
            if (isPrim)
            {
               byte[] a = new byte[sz];
               int bsize = 10240;
               int offset = 0;
               int bytesLeft = sz;
               while (bytesLeft > 0)
               {
                  int read = in.read(a, offset, Math.min(bsize, bytesLeft));
                  offset += read;
                  bytesLeft -= read;
               }
               return a;
            }
            else
            {
               Byte[] a = new Byte[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readByte();
               return a;
            }
         }
         case MAGICNUMBER_SHORT:
         {
            boolean isPrim = in.readBoolean();
            if (isPrim)
            {
               short[] a = new short[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readShort();
               return a;
            }
            else
            {
               Short[] a = new Short[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readShort();
               return a;
            }
         }
         case MAGICNUMBER_FLOAT:
         {
            boolean isPrim = in.readBoolean();
            if (isPrim)
            {
               float[] a = new float[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readFloat();
               return a;
            }
            else
            {
               Float[] a = new Float[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readFloat();
               return a;
            }
         }
         case MAGICNUMBER_DOUBLE:
         {
            boolean isPrim = in.readBoolean();
            if (isPrim)
            {
               double[] a = new double[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readDouble();
               return a;
            }
            else
            {
               Double[] a = new Double[sz];
               for (int i = 0; i < sz; i++) a[i] = in.readDouble();
               return a;
            }
         }
         case MAGICNUMBER_OBJECT:
         {
            Object[] a = new Object[sz];
            for (int i = 0; i < sz; i++) a[i] = unmarshallObject(in, refs);
            return a;
         }
         default:
            throw new CacheException("Unknown array type");
      }
   }
}

