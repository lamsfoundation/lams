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
package org.jboss.cache.notifications.event;

import java.util.Map;

/**
 * This event is passed in to any method annotated with {@link org.jboss.cache.notifications.annotation.NodeModified}
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 * @since 2.0.0
 */
public interface NodeModifiedEvent extends NodeEvent
{
   /**
    * Different cache modification types.
    */
   static enum ModificationType
   {
      PUT_DATA, REMOVE_DATA, PUT_MAP
   }

   /**
    * @return an instance of the {@link org.jboss.cache.notifications.event.NodeModifiedEvent.ModificationType} enumeration.
    */
   ModificationType getModificationType();

   /**
    * When called with <tt>isPre() == true</tt>, this is the initial state of the {@link org.jboss.cache.Node}
    * before modification.
    * <p/>
    * When called with <tt>isPre() == false</tt>, this depends on the value of <tt>getModificationType()</tt>:
    * <ul>
    * <li><b>{@link ModificationType#PUT_DATA}</b>: Map contains the single key/value pair that was added or modified.</li>
    * <li><b>{@link ModificationType#REMOVE_DATA}</b>: Map contains the key/value pairs that were removed.</li>
    * <li><b>{@link ModificationType#PUT_MAP}</b>: Map contains the new state of the {@link org.jboss.cache.Node} following modification.  This map includes modified key/value
    * pairs as well as any that were not affected.</li>
    * </ul>
    * <p/>
    * Implementations interested in seeing the difference in the node data in the {@link ModificationType#PUT_MAP} case
    * can cache the value of <tt>getData()</tt> map passed when <tt>isPre() == true</tt>, and then when the
    * <tt>isPre() == false</tt> callback is received, pass the cached map and the new result of <tt>getData()</tt> to
    * {@link org.jboss.cache.util.Util#diffNodeData(java.util.Map,java.util.Map)}
    *
    * @return Unmodifiable {@link java.util.Map}; will not be <code>null</code>. See description above.
    */
   Map getData();
}
