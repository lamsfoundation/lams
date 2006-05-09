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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$  */
package org.lamsfoundation.lams.cache;

import org.apache.log4j.Logger;
import org.jboss.cache.Fqn;
import org.jboss.cache.TreeCache;
import org.jboss.cache.TreeCacheListener;

/** Log the addition/removal/eviction of items from the JBOSS cache.
 * Turn on and off using UseCacheDebugListener entry in lams.xml
 * 
 * @author Fiona Malikoff
 */
public class CacheDebugListener implements TreeCacheListener {

	protected Logger log = Logger.getLogger(CacheManager.class);
	private String cacheNameString = "Cache unknown: ";

	private void logMessage(String message) {
		log.info(cacheNameString+message);
	}
				
	public void cacheStarted(TreeCache cache) {
		 cacheNameString = "Cache "+cache.getName()+": ";
		 logMessage("started");
	 }
	
	 public void 	cacheStopped(TreeCache cache) {
		 logMessage("stopped");
	 }
	 
	 public void 	nodeCreated(Fqn fqn) {
		 logMessage("node created "+fqn);
	 }
	 public void 	nodeEvicted(Fqn fqn) {
		 logMessage("node evicted "+fqn);
	 }
	 public void 	nodeLoaded(Fqn fqn) {
		 logMessage("node loaded "+fqn);
	 }
	 public void 	nodeModified(Fqn fqn) {
		 logMessage("node modified "+fqn);
	 }
	 public void 	nodeRemoved(Fqn fqn) {
		 logMessage("node removed "+fqn);
	 }
	 public void 	nodeVisited(Fqn fqn) {
		 logMessage("node visited "+fqn);
	 }
	 public void 	viewChange(org.jgroups.View new_view){
		 logMessage("view changed ");
	 } 
}
