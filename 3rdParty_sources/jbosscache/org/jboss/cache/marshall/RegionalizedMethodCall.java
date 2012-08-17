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
import org.jboss.cache.commands.ReplicableCommand;

/**
 * A regionalized MethodCall object, created when {@link Marshaller#regionalizedMethodCallFromByteBuffer(byte[])} or
 * {@link org.jboss.cache.marshall.Marshaller#regionalizedMethodCallFromObjectStream(java.io.ObjectInputStream)} is called.
 * <p/>
 * Specifically used by the {@link org.jboss.cache.marshall.InactiveRegionAwareRpcDispatcher} so that the region used to unmarshall
 * the method call is known, and can be used to marshall a result to return to the remote caller.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.1.1
 */
public class RegionalizedMethodCall
{
   public ReplicableCommand command;
   public Fqn region;
}
