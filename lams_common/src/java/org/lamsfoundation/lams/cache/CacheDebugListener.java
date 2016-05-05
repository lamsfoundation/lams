/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$  */
package org.lamsfoundation.lams.cache;

import org.apache.log4j.Logger;
import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.notifications.annotation.CacheListener;
import org.jboss.cache.notifications.annotation.CacheStarted;
import org.jboss.cache.notifications.annotation.CacheStopped;
import org.jboss.cache.notifications.annotation.NodeCreated;
import org.jboss.cache.notifications.annotation.NodeEvicted;
import org.jboss.cache.notifications.annotation.NodeLoaded;
import org.jboss.cache.notifications.annotation.NodeModified;
import org.jboss.cache.notifications.annotation.NodeRemoved;
import org.jboss.cache.notifications.annotation.NodeVisited;
import org.jboss.cache.notifications.annotation.ViewChanged;

/**
 * Log the addition/removal/eviction of items from the JBOSS cache. Turn on and off using UseCacheDebugListener entry in
 * lams.xml
 *
 * @author Fiona Malikoff
 */

@CacheListener
public class CacheDebugListener {

    protected Logger log = Logger.getLogger(CacheManager.class);
    private String cacheNameString = "Cache unknown: ";

    private void logMessage(String message) {
	log.info(cacheNameString + message);
    }

    @CacheStarted
    public void cacheStarted(Cache cache) {
	cacheNameString = "Cache " + cache.getVersion() + ": ";
	logMessage("started");
    }

    @CacheStopped
    public void cacheStopped(Cache cache) {
	logMessage("stopped");
    }

    @NodeCreated
    public void nodeCreated(Fqn fqn) {
	logMessage("node created " + fqn);
    }

    @NodeEvicted
    public void nodeEvicted(Fqn fqn) {
	logMessage("node evicted " + fqn);
    }

    @NodeLoaded
    public void nodeLoaded(Fqn fqn) {
	logMessage("node loaded " + fqn);
    }

    @NodeModified
    public void nodeModified(Fqn fqn) {
	logMessage("node modified " + fqn);
    }

    @NodeRemoved
    public void nodeRemoved(Fqn fqn) {
	logMessage("node removed " + fqn);
    }

    @NodeVisited
    public void nodeVisited(Fqn fqn) {
	logMessage("node visited " + fqn);
    }

    @ViewChanged
    public void viewChange(org.jgroups.View new_view) {
	logMessage("view changed ");
    }
}