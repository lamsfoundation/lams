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
package org.jboss.cache.optimistic;

import java.io.Serializable;

/**
 * When versioning data nodes in optimistic locking, a DataVersion is assigned
 * to each node.  Versions need to implement the {@link #newerThan} method so
 * they can be compared during the validation phase upon commit.
 * <p/>
 * It is recommended that implementations implement {@link java.io.Externalizable} and make use
 * of a good marshalling/unmarshalling mechanism for the sake of efficiency, as these objects are
 * frequently serialized to be replicated across the wire.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @deprecated this is to support a deprecated locking scheme (Optimistic Locking).  Will be removed when Optimistic Locking support is removed.
 */
@Deprecated
public interface DataVersion extends Serializable
{
   /**
    * Returns true if this is a newer version than <code>other</code>.  There is no guarantee that the DataVersion passed
    * in is of the same implementation as the current instance.  The implementation will have to check for this (if necessary)
    * and (if necessary) throw a {@link org.jboss.cache.optimistic.DataVersioningException}.
    */
   boolean newerThan(DataVersion other);
}
