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
package org.jboss.cache.config.parsing.element;

import org.jboss.cache.config.CacheLoaderConfig;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.parsing.XmlConfigHelper;
import org.jboss.cache.config.parsing.XmlParserBase;
import org.jboss.cache.config.parsing.RootElementBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Properties;

/**
 * Utility class for parsing the 'loaders' element in the .xml configuration file.
 * <pre>
 * Note: class does not rely on element position in the configuration file.
 *       It does not rely on element's name either.
 * </pre>
 *
 * @author Mircea.Markus@jboss.com
 * @since 3.0
 */
public class LoadersElementParser extends XmlParserBase
{
   public LoadersElementParser()
   {
      this(RootElementBuilder.JBOSSCACHE_CORE_NS_31);
   }

   public LoadersElementParser(String coreNamespace)
   {
      this.coreNamespace = coreNamespace;
   }

   public CacheLoaderConfig parseLoadersElement(Element element)
   {
      assertNotLegacyElement(element);
      
      CacheLoaderConfig cacheLoaderConfig = new CacheLoaderConfig();
      String passivation = getAttributeValue(element, "passivation");
      if (existsAttribute(passivation)) cacheLoaderConfig.setPassivation(getBoolean(passivation));
      String shared = getAttributeValue(element, "shared");
      if (existsAttribute(shared)) cacheLoaderConfig.setShared(getBoolean(shared));
      String preload = getPreloadString(getSingleElementInCoreNS("preload", element));
      if (preload != null) cacheLoaderConfig.setPreload(preload);

      NodeList cacheLoaderNodes = element.getElementsByTagName("loader");
      for (int i = 0; i < cacheLoaderNodes.getLength(); i++)
      {
         Element indivElement = (Element) cacheLoaderNodes.item(i);
         CacheLoaderConfig.IndividualCacheLoaderConfig iclc = parseIndividualCacheLoaderConfig(indivElement);
         cacheLoaderConfig.addIndividualCacheLoaderConfig(iclc);
      }
      return cacheLoaderConfig;
   }

   private CacheLoaderConfig.IndividualCacheLoaderConfig parseIndividualCacheLoaderConfig(Element indivElement)
   {
      CacheLoaderConfig.IndividualCacheLoaderConfig iclc = new CacheLoaderConfig.IndividualCacheLoaderConfig();

      String async = getAttributeValue(indivElement, "async");
      if (existsAttribute(async)) iclc.setAsync(getBoolean(async));
      String fetchPersistentState = getAttributeValue(indivElement, "fetchPersistentState");
      if (existsAttribute(fetchPersistentState)) iclc.setFetchPersistentState(getBoolean(fetchPersistentState));
      String ignoreModifications = getAttributeValue(indivElement, "ignoreModifications");
      if (existsAttribute(ignoreModifications)) iclc.setIgnoreModifications(getBoolean(ignoreModifications));
      String purgeOnStartup = getAttributeValue(indivElement, "purgeOnStartup");
      if (existsAttribute(purgeOnStartup)) iclc.setPurgeOnStartup(getBoolean(purgeOnStartup));
      String clClass = getAttributeValue(indivElement, "class");
      if (!existsAttribute(clClass))
         throw new ConfigurationException("Missing 'class'  attribute for cache loader configuration");
      iclc.setClassName(clClass);
      iclc.setProperties(XmlConfigHelper.readPropertiesContents(indivElement, "properties"));
      CacheLoaderConfig.IndividualCacheLoaderConfig.SingletonStoreConfig ssc = parseSingletonStoreConfig(getSingleElementInCoreNS("singletonStore", indivElement));
      if (ssc != null)
      {
         iclc.setSingletonStoreConfig(ssc);
      }
      return iclc;
   }

   private String getPreloadString(Element preloadElement)
   {
      if (preloadElement == null) return null; //might be no preload
      NodeList nodesToPreload = preloadElement.getElementsByTagName("node");
      StringBuilder result = new StringBuilder();
      for (int i = 0; i < nodesToPreload.getLength(); i++)
      {
         Element node = (Element) nodesToPreload.item(i);
         String fqn2preload = getAttributeValue(node, "fqn");
         if (!existsAttribute(fqn2preload))
            throw new ConfigurationException("Missing 'fqn' attribute in 'preload' element");
         if (i > 0) result.append(",");
         result.append(fqn2preload);
      }
      //no elements defined for preload so by default load the root
      if (nodesToPreload.getLength() == 0)
      {
         result.append("/");
      }
      return result.toString();
   }

   public CacheLoaderConfig.IndividualCacheLoaderConfig.SingletonStoreConfig parseSingletonStoreConfig(Element element)
   {
      if (element == null) return null; //might happen, this config option is not mandatory
      boolean singletonStoreEnabled = getBoolean(getAttributeValue(element, "enabled"));
      String singletonStoreClass = getAttributeValue(element, "class");
      CacheLoaderConfig.IndividualCacheLoaderConfig.SingletonStoreConfig ssc = new CacheLoaderConfig.IndividualCacheLoaderConfig.SingletonStoreConfig();
      if (existsAttribute(singletonStoreClass)) ssc.setSingletonStoreClass(singletonStoreClass);
      Properties singletonStoreproperties = XmlConfigHelper.readPropertiesContents(element, "properties");
      ssc.setSingletonStoreEnabled(singletonStoreEnabled);
      ssc.setSingletonStoreClass(singletonStoreClass);
      ssc.setSingletonStoreproperties(singletonStoreproperties);
      return ssc;
   }
}
