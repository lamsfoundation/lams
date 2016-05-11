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
package org.jboss.cache.loader.tcp;

import org.jboss.cache.Cache;
import org.jboss.cache.Lifecycle;
import org.jboss.cache.jmx.CacheJmxWrapperMBean;

import java.net.UnknownHostException;

/**
 * StandardMBean interface for {@link TcpCacheServer}.
 *
 * @author Bela Ban
 * @author Brian Stansberry
 *
 */
public interface TcpCacheServerMBean extends Lifecycle
{
   String getBindAddress();

   void setBindAddress(String bind_addr) throws UnknownHostException;

   int getPort();

   void setPort(int port);

   String getConfig();

   void setConfig(String config);

   Cache getCache();

   void setCacheJmxWrapper(CacheJmxWrapperMBean wrapper);

   String getConnections();
}
