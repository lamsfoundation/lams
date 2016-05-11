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

import org.jboss.cache.Fqn;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

/**
 * Serializable representation of the data of a node (FQN and attributes)
 *
 * @author Bela Ban
 *
 */
// TODO: 3.0.0: remove Externalizable and rely on the CacheMarshaller.
public class NodeData<K, V> implements Externalizable
{
   private Fqn fqn = null;
   private Map<K, V> attrs = null;

   static final long serialVersionUID = -7571995794010294485L;

   public NodeData()
   {
   }

   public NodeData(Fqn fqn)
   {
      this.fqn = fqn;
   }

   public NodeData(Fqn fqn, Map<K, V> attrs, boolean mapSafe)
   {
      this.fqn = fqn;
      if (mapSafe || attrs == null)
         this.attrs = attrs;
      else
         this.attrs = new HashMap<K, V>(attrs);
   }

   public NodeData(String fqn, Map<K, V> attrs, boolean mapSafe)
   {
      this(Fqn.fromString(fqn), attrs, mapSafe);
   }

   public Map<K, V> getAttributes()
   {
      return attrs;
   }

   public Fqn getFqn()
   {
      return fqn;
   }

   public boolean isMarker()
   {
      return false;
   }

   public boolean isExceptionMarker()
   {
      return false;
   }

   // TODO: 3.0.0: Remove and replace with marshallNodeData/unmarshallNodeData methods in the CacheMarshaller so that we can use the same marshalling framework for Fqns.
   public void writeExternal(ObjectOutput out) throws IOException
   {
      out.writeObject(fqn);
      if (attrs != null)
      {
         out.writeBoolean(true);
         out.writeObject(attrs);
      }
      else
      {
         out.writeBoolean(false);
      }
   }

   // TODO: 3.0.0: Remove in and replace with marshallNodeData/unmarshallNodeData methods in the CacheMarshaller so that we can use the same marshalling framework for Fqns.
   @SuppressWarnings("unchecked")
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   {
      fqn = (Fqn) in.readObject();
      if (in.readBoolean())
      {
         attrs = (Map<K, V>) in.readObject();
      }
   }

   @Override
   public String toString()
   {
      return "NodeData {fqn: " + fqn + ", attrs=" + attrs + "}";
   }


   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      NodeData nodeData = (NodeData) o;

      if (attrs != null ? !attrs.equals(nodeData.attrs) : nodeData.attrs != null) return false;
      if (fqn != null ? !fqn.equals(nodeData.fqn) : nodeData.fqn != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result;
      result = (fqn != null ? fqn.hashCode() : 0);
      result = 31 * result + (attrs != null ? attrs.hashCode() : 0);
      return result;
   }
}
