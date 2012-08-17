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
package org.jboss.cache.mvcc;

import org.jboss.cache.DataContainer;

/**
 * A marker node to represent a null node for repeatable read, so that a read that returns a null can continue to return
 * null.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public class NullMarkerNode extends NullMarkerNodeForRemoval
{
   private static final NullMarkerNode INSTANCE = new NullMarkerNode();

   private NullMarkerNode()
   {
      super(null, null);
   }

   public static NullMarkerNode getInstance()
   {
      return INSTANCE;
   }

   /**
    * A no-op.
    */
   @Override
   public void markForUpdate(DataContainer d, boolean b)
   {
      // no op
   }
}
