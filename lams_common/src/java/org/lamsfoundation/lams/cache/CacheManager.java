/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;

import org.apache.log4j.Logger;
import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.TreeCacheListener;
import org.jboss.cache.TreeCacheMBean;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * Wraps the JBOSS cache. See ICacheManager for more details.
 */
public class CacheManager implements ICacheManager {

	protected Logger log = Logger.getLogger(CacheManager.class);
	private String DEFAULT_CACHE_OBJECT_NAME="jboss.cache:service=TreeCache";
	
	/* Spring configured variables */
	private String cacheObjectName = null;
	
	 /* There is one cache across the whole system, so it should be safe to
	 * use a static cache bean. Do not use this attribute directly - always
	 * get it via getCache(). */
	private TreeCacheMBean treeCache = null;
	private TreeCacheListener listener = null;
	
	/** Get the tree cache.
	 * <p> 
	 * If necessary, gets it via the MBean. If gets via MBean, then 
	 * also sets up the cache listener at the same time, if required. */
	private TreeCacheMBean getCache() {
		if ( treeCache == null ) {
			
			try {
				if ( cacheObjectName == null )
					cacheObjectName = DEFAULT_CACHE_OBJECT_NAME;
				MBeanServer server=MBeanServerLocator.locate();
				treeCache = (TreeCacheMBean)MBeanProxyExt.create(TreeCacheMBean.class, cacheObjectName, server);
				
				if ( Configuration.getAsBoolean(ConfigurationKeys.USE_CACHE_DEBUG_LISTENER) ) {
					if ( listener != null )
						treeCache.removeTreeCacheListener(listener);
					listener = new CacheDebugListener();
					treeCache.addTreeCacheListener(listener);
					log.info("Added tree cache listener.");
				}
			} catch (Exception e) {
				log.error("Unable to access the JBOSS cache mbean "+cacheObjectName+". Cache not available.",e);
			}
		}
		return treeCache;
	}
	
	/** Get the String[] version of the objects class name. */
	public String[] getPartsFromClass(Class clasz) {
		return clasz.getName().split("\\.");
	}

	/** Get the Fqn for this object, based on the class name. The Fqn is used as the part of the key to the cached object. */
	private Fqn getFqn(Class clasz) {
		return new Fqn(getPartsFromClass(clasz));
	}
	
	/** Get the Fqn for this object, based on classNameParts. The Fqn is used as the part of the key to the cached object. */
	private Fqn getFqn(String[] classNameParts) {
		return new Fqn(classNameParts);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.cache.ICacheManager#getItem(java.lang.String[], java.lang.Object)
	 */
	public Object getItem(String[] classNameParts, Object key){
		if ( key == null || classNameParts == null )
			return null;

		return getItem(getFqn(classNameParts),key);
	}
		
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.cache.ICacheManager#getItem(java.lang.Class, java.lang.Object)
	 */
	public Object getItem(Class clasz, Object key) {
		if ( key == null || clasz == null )
			return null;

		return getItem(getFqn(clasz), key);
	}
	
	/** Does the "real" get from the cache. Key and fqn must not be null or an exception may be thrown. */
	private Object getItem(Fqn fqn, Object key) {
		TreeCacheMBean cache = getCache();
		if (cache==null) {
			log.error("Unable to get item with fqn "+fqn+" key "+key+" as we can't get the JBOSS Cache mbean.");
			return null;
		}
		
		Object obj = null;
		try {
			obj = (Object) cache.get(fqn, key);
			if ( obj != null ) {
				log.debug("Retrieved object from cache fqn "+fqn+" key "+key);
			}
		} catch (CacheException e) {
			log.error("JBOSS Cache exception occured getting object from cache. fqn "+fqn+" key "+key, e);
		}

		return obj;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.cache.ICacheManager#addItem(java.lang.String[], java.lang.Object, java.lang.Object)
	 */
	public void addItem(String[] classNameParts, Object key, Object item){
		if ( item != null && key != null && classNameParts != null )
			addItem(getFqn(classNameParts), key, item);
	}
		
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.cache.ICacheManager#addItem(java.lang.Class, java.lang.Object, java.lang.Object)
	 */
	public void addItem(Class clasz, Object key, Object item){
		if ( item != null && key != null && clasz != null )
			addItem(getFqn(clasz),key,item);
	}

	/** Does the "real" put in the cache. Key, fqn and item must not be null or an exception may be thrown. */
	private void addItem(Fqn fqn, Object key, Object item){
		TreeCacheMBean cache = getCache();
		if (cache==null) {
			log.error("Unable to get cache item with fqn "+fqn+" key "+key+" as we can't get the JBOSS Cache mbean.");
			return;
		}

		try {
			cache.put(fqn, key, item);
		} catch (CacheException e) {
			log.error("JBOSS Cache exception occured putting object in cache. fqn "+fqn+" key "+key,e);
		}
	}

	public Map getCachedItems() {
		TreeCacheMBean cache = getCache();
		Map allChildNames = new TreeMap();
		if (cache==null) {
			log.error("Unable to get cache items as we can't get the JBOSS Cache mbean.");
		} else {
			addChildren("/", cache, allChildNames);
		}
		return allChildNames;
	}

	/* Recursively add all the child nodes to the map. This is where the format of FQNs is important - 
	 * this code will hardcode in "/" between each step. */
	private void addChildren(String node, TreeCacheMBean cache, Map allChildNames ) {
		try {
			Set childNames = cache.getChildrenNames(node);
			if ( childNames != null ) {
				allChildNames.put(node, childNames);
				Iterator iter = childNames.iterator();
				while ( iter.hasNext() ) {
					String childNode = (String) iter.next();
					if ( node.endsWith("/") ) {
						addChildren(node+childNode,cache,allChildNames);
					} else {
						addChildren(node+"/"+childNode,cache,allChildNames);
					}
				}
			}
		} catch (CacheException e) {
			log.error("JBOSS Cache exception occured getting child names from cache",e);
		}
	}

	/** Clear all the nodes in the cache. Works on nodes starting with /org, /com and /net */
	public void clearCache(String node) {
		TreeCacheMBean cache = getCache();
		if (cache==null) {
			log.error("Unable to clear cache items as we can't get the JBOSS Cache mbean.");
		} else {
			try {
				cache.remove(node);
			} catch (CacheException e) {
				log.error("JBOSS Cache exception occured getting child names from cache",e);
			}
		}
	}
	/* **** Spring initialisation methods */
	
	public String getCacheObjectName() {
		return cacheObjectName;
	}

	public void setCacheObjectName(String cacheObjectName) {
		this.cacheObjectName = cacheObjectName;
	}
	
}
