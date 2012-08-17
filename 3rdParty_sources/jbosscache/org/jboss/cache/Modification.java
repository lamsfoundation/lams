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
package org.jboss.cache;


import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;


/**
 * Represents a modification in the cache. Contains the nature of the modification
 * (e.g. PUT, REMOVE), the fqn of the node, the new value and the previous value.
 * A list of modifications will be sent to all nodes in a cluster when a transaction
 * has been committed (PREPARE phase). A Modification is also used to roll back changes,
 * e.g. since we know the previous value, we can reconstruct the previous state by
 * applying the changes in a modification listin reverse order.
 *
 * @author <a href="mailto:bela@jboss.org">Bela Ban</a> Apr 12, 2003
 * @version $Revision$
 */
public class Modification implements Externalizable
{

   private static final long serialVersionUID = 7463314130283897197L;

   public static enum ModificationType
   {
      PUT_KEY_VALUE,
      PUT_DATA,
      PUT_DATA_ERASE,
      REMOVE_NODE,
      REMOVE_KEY_VALUE,
      REMOVE_DATA,
      MOVE,
      UNKNOWN
   }

   private ModificationType type = ModificationType.UNKNOWN;
   private Fqn fqn = null;
   private Fqn fqn2 = null;
   private Object key = null;
   private Object value = null;
   private Object old_value = null;
   private Map data = null;

   /**
    * Constructs a new modification.
    */
   public Modification()
   {
   }

   /**
    * Constructs a new modification with details.
    */
   public Modification(ModificationType type, Fqn fqn, Object key, Object value)
   {
      this.type = type;
      this.fqn = fqn;
      this.key = key;
      this.value = value;
   }

   /**
    * Constructs a new modification with key.
    */
   public Modification(ModificationType type, Fqn fqn, Object key)
   {
      this.type = type;
      this.fqn = fqn;
      this.key = key;
   }

   /**
    * Constructs a new modification with data map.
    */
   public Modification(ModificationType type, Fqn fqn, Map data)
   {
      this.type = type;
      this.fqn = fqn;
      this.data = data;
   }

   /**
    * Constructs a new modification with fqn only.
    */
   public Modification(ModificationType type, Fqn fqn)
   {
      this.type = type;
      this.fqn = fqn;
   }

   /**
    * Constructs a new modification with fqn only.
    */
   public Modification(ModificationType type, Fqn fqn1, Fqn fqn2)
   {
      this.type = type;
      this.fqn = fqn1;
      this.fqn2 = fqn2;
   }


   /**
    * Returns the type of modification.
    */
   public ModificationType getType()
   {
      return type;
   }

   /**
    * Sets the type of modification.
    */
   public void setType(ModificationType type)
   {
      this.type = type;
   }

   /**
    * Returns the modification fqn.
    */
   public Fqn getFqn()
   {
      return fqn;
   }

   /**
    * Sets the modification fqn.
    */
   public void setFqn(Fqn fqn)
   {
      this.fqn = fqn;
   }

   public void setFqn2(Fqn fqn2)
   {
      this.fqn2 = fqn2;
   }

   public Fqn getFqn2()
   {
      return fqn2;
   }

   /**
    * Returns the modification key.
    */
   public Object getKey()
   {
      return key;
   }

   /**
    * Sets the modification key.
    */
   public void setKey(Object key)
   {
      this.key = key;
   }

   /**
    * Returns the modification value.
    */
   public Object getValue()
   {
      return value;
   }

   /**
    * Sets the modification value.
    */
   public void setValue(Object value)
   {
      this.value = value;
   }

   /**
    * Returns the <i>post</i> modification old value.
    */
   public Object getOldValue()
   {
      return old_value;
   }

   /**
    * Sets the <i>post</i> modification old value.
    */
   public void setOldValue(Object old_value)
   {
      this.old_value = old_value;
   }

   /**
    * Returns the modification Map set.
    */
   public Map getData()
   {
      return data;
   }

   /**
    * Sets the modification Map set.
    */
   public void setData(Map data)
   {
      this.data = data;
   }

   /**
    * Writes data to an external stream.
    */
   public void writeExternal(ObjectOutput out) throws IOException
   {
      out.writeObject(type);

      out.writeBoolean(fqn != null);
      if (fqn != null)
      {
         fqn.writeExternal(out);
      }

      out.writeObject(key);
      out.writeObject(value);
      out.writeObject(old_value);

      out.writeBoolean(data != null);
      if (data != null)
      {
         out.writeObject(data);
      }
   }

   /**
    * Reads data from an external stream.
    */
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   {
      type = (ModificationType) in.readObject();

      if (in.readBoolean())
      {
         fqn = Fqn.fromExternalStream(in);
      }

      key = in.readObject();
      value = in.readObject();
      old_value = in.readObject();

      if (in.readBoolean())
      {
         data = (Map) in.readObject();
      }
   }

   /**
    * Returns debug information about this modification.
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append(type.toString()).append(": ").append(fqn);
      if (key != null)
      {
         sb.append("\nkey=").append(key);
      }
      if (value != null)
      {
         sb.append("\nvalue=").append(value);
      }
      if (old_value != null)
      {
         sb.append("\nold_value=").append(old_value);
      }
      if (data != null)
      {
         sb.append("\ndata=").append(data);
      }
      return sb.toString();
   }

}
