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
package org.jboss.cache.config.parsing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.RegionManagerImpl;
import org.jboss.cache.buddyreplication.NextMemberBuddyLocator;
import org.jboss.cache.config.BuddyReplicationConfig;
import org.jboss.cache.config.BuddyReplicationConfig.BuddyLocatorConfig;
import org.jboss.cache.config.CacheLoaderConfig;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig.SingletonStoreConfig;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.EvictionAlgorithmConfig;
import org.jboss.cache.config.EvictionConfig;
import org.jboss.cache.config.EvictionRegionConfig;
import org.jboss.cache.config.MissingPolicyException;
import org.jboss.cache.eviction.EvictionAlgorithm;
import org.jboss.cache.eviction.EvictionPolicy;
import org.jboss.cache.eviction.ModernizablePolicy;
import org.jboss.cache.util.FileLookup;
import org.jboss.cache.util.Util;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Reads in XMLconfiguration files and spits out a {@link org.jboss.cache.config.Configuration} object.  When deployed as a
 * JBoss MBean, this role is performed by the JBoss Microcontainer.  This class is only used internally in unit tests
 * or within {@link org.jboss.cache.CacheFactory} implementations for standalone JBoss Cache usage.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @since 2.00.
 * @deprecated
 */
public class XmlConfigurationParser2x
{
   private static final Log log = LogFactory.getLog(XmlConfigurationParser2x.class);

   public static final String ATTR = "attribute";
   public static final String NAME = "name";

   /**
    * Parses an XML file and returns a new configuration.  This method attempts to look for the file name passed in on
    * the classpath.  If not found, it will search for the file on the file system instead, treating the name as an
    * absolute path.
    *
    * @param filename the name of the XML file to parse.
    * @return a configured Configuration object representing the configuration in the file
    */
   public Configuration parseFile(String filename)
   {
      InputStream is = new FileLookup().lookupFile(filename);
      if (is == null)
      {
         throw new ConfigurationException("Unable to find config file " + filename + " either in classpath or on the filesystem!");
      }

      return parseStream(is);
   }

   /**
    * Parses an input stream containing XML text and returns a new configuration.
    *
    * @param stream input stream to parse.  SHould not be null.
    * @return a configured Configuration object representing the configuration in the stream
    * @since 2.1.0
    */
   public Configuration parseStream(InputStream stream)
   {
      // loop through all elements in XML.
      Element root = XmlConfigHelper.getDocumentRoot(stream);
      Element mbeanElement = getMBeanElement(root);

      return parseConfiguration(mbeanElement);
   }

   public Configuration parseConfiguration(Element configurationRoot)
   {
      ParsedAttributes attributes = XmlConfigHelper.extractAttributes(configurationRoot);

      // Deal with legacy attributes we no longer support
      handleRemovedAttributes(attributes);

      // Deal with legacy attributes that we renamed or otherwise altered
      handleRenamedAttributes(attributes);

      Configuration c = new Configuration();
      XmlConfigHelper.setValues(c, attributes.stringAttribs, false, false);
      // Special handling for XML elements -- we hard code the parsing
      setXmlValues(c, attributes.xmlAttribs);
      if (c.getEvictionConfig() != null) correctEvictionUnlimitedValues(c.getEvictionConfig());
      return c;
   }

   private void correctEvictionUnlimitedValues(EvictionConfig ec)
   {
      EvictionRegionConfig def = ec.getDefaultEvictionRegionConfig();
      EvictionAlgorithmConfig eac = def.getEvictionAlgorithmConfig();

   }

   /**
    * Check for and remove any attributes that were supported in the
    * 1.x releases and no longer are.  Log a WARN or throw a
    * {@link ConfigurationException} if any are found. Which is done depends
    * on the attribute:
    * <p/>
    * <ul>
    * <li><i>MultiplexerService</i> -- throws an Exception</li>
    * <li><i>ServiceName</i> -- logs a WARN</li>
    * </ul>
    *
    * @param attributes
    */
   protected void handleRemovedAttributes(ParsedAttributes attributes)
   {
      String evictionPolicy = attributes.stringAttribs.remove("EvictionPolicyClass");
      if (evictionPolicy != null)
      {
         throw new ConfigurationException("XmlConfigurationParser does not " +
               "support the JBC 1.x attribute EvictionPolicyClass. Set the default " +
               "eviction policy via the policyClass element in the EvictionConfig section");
      }
      String multiplexerService = attributes.stringAttribs.remove("MultiplexerService");
      if (multiplexerService != null)
      {
         throw new ConfigurationException("XmlConfigurationParser does not " +
               "support the JBC 1.x attribute MultiplexerService. Inject the " +
               "multiplexer directly using Configuration.getRuntimeConfig().setMuxChannelFactory()");
      }
      String serviceName = attributes.stringAttribs.remove("ServiceName");
      if (serviceName != null)
      {
         log.warn("XmlConfigurationParser does not support the deprecated " +
               "attribute ServiceName. If JMX registration is needed, " +
               "register a CacheJmxWrapper or PojoCacheJmxWrapper in " +
               "JMX with the desired name");
      }
   }

   /**
    * Check for any attributes that were supported in the
    * 1.x releases but whose name has changed.  Log a WARN if any are found, but
    * convert the attribute to the new name.
    * <p/>
    * <ul>
    * <li><i>UseMbean</i> becomes <i>ExposeManagementStatistics</i></li>
    * </ul>
    *
    * @param attributes
    */
   private void handleRenamedAttributes(ParsedAttributes attributes)
   {
      String keepStats = attributes.stringAttribs.remove("UseInterceptorMbeans");
      if (keepStats != null && attributes.stringAttribs.get("ExposeManagementStatistics") == null)
      {
         log.warn("Found non-existent JBC 1.x attribute 'UseInterceptorMbeans' and replaced " +
               "with 'ExposeManagementStatistics'. Please update your config " +
               "to use the new attribute name");
         attributes.stringAttribs.put("ExposeManagementStatistics", keepStats);
      }
      Element clc = attributes.xmlAttribs.remove("CacheLoaderConfiguration");
      if (clc != null && attributes.xmlAttribs.get("CacheLoaderConfig") == null)
      {
         log.warn("Found non-existent JBC 1.x attribute 'CacheLoaderConfiguration' and replaced " +
               "with 'CacheLoaderConfig'. Please update your config " +
               "to use the new attribute name");
         attributes.xmlAttribs.put("CacheLoaderConfig", clc);
      }
   }

   protected Element getMBeanElement(Element root)
   {
      // This is following JBoss convention.
      NodeList list = root.getElementsByTagName(XmlConfigHelper.ROOT);
      if (list == null) throw new ConfigurationException("Can't find " + XmlConfigHelper.ROOT + " tag");

      if (list.getLength() > 1) throw new ConfigurationException("Has multiple " + XmlConfigHelper.ROOT + " tag");

      Node node = list.item(0);
      Element element;
      if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
      {
         element = (Element) node;
      }
      else
      {
         throw new ConfigurationException("Can't find " + XmlConfigHelper.ROOT + " element");
      }
      return element;
   }

   protected void setXmlValues(Configuration conf, Map<String, Element> attribs)
   {
      for (Entry<String, Element> entry : attribs.entrySet())
      {
         String propname = entry.getKey();
         if ("BuddyReplicationConfiguration".equals(propname)
               || "BuddyReplicationConfig".equals(propname))
         {
            BuddyReplicationConfig brc = parseBuddyReplicationConfig(entry.getValue());
            conf.setBuddyReplicationConfig(brc);
         }
         else if ("CacheLoaderConfiguration".equals(propname)
               || "CacheLoaderConfig".equals(propname))
         {
            CacheLoaderConfig clc = parseCacheLoaderConfig(entry.getValue());
            conf.setCacheLoaderConfig(clc);
         }
         else if ("EvictionPolicyConfiguration".equals(propname)
               || "EvictionPolicyConfig".equals(propname))
         {
            EvictionConfig ec = parseEvictionConfig(entry.getValue());
            conf.setEvictionConfig(ec);
         }
         else if ("ClusterConfig".equals(propname))
         {
            String jgc = parseClusterConfigXml(entry.getValue());
            conf.setClusterConfig(jgc);
         }
         else
         {
            throw new ConfigurationException("Unknown configuration element " + propname);
         }
      }
   }

   public static BuddyReplicationConfig parseBuddyReplicationConfig(Element element)
   {
      BuddyReplicationConfig brc = new BuddyReplicationConfig();
      brc.setEnabled(XmlConfigHelper.readBooleanContents(element, "buddyReplicationEnabled"));
      brc.setDataGravitationRemoveOnFind(XmlConfigHelper.readBooleanContents(element, "dataGravitationRemoveOnFind", true));
      brc.setDataGravitationSearchBackupTrees(XmlConfigHelper.readBooleanContents(element, "dataGravitationSearchBackupTrees", true));
      brc.setAutoDataGravitation(brc.isEnabled() && XmlConfigHelper.readBooleanContents(element, "autoDataGravitation", false));

      String strBuddyCommunicationTimeout = XmlConfigHelper.readStringContents(element, "buddyCommunicationTimeout");
      try
      {
         brc.setBuddyCommunicationTimeout(Integer.parseInt(strBuddyCommunicationTimeout));
      }
      catch (Exception e)
      {
         if (log.isTraceEnabled()) log.trace(e.getMessage());
      }
      finally
      {
         if (log.isDebugEnabled())
         {
            log.debug("Using buddy communication timeout of " + brc.getBuddyCommunicationTimeout() + " millis");
         }
      }
      String buddyPoolName = XmlConfigHelper.readStringContents(element, "buddyPoolName");
      if ("".equals(buddyPoolName))
      {
         buddyPoolName = null;
      }

      brc.setBuddyPoolName(buddyPoolName);

      // now read the buddy locator details

      String buddyLocatorClass = XmlConfigHelper.readStringContents(element, "buddyLocatorClass");
      if (buddyLocatorClass == null || buddyLocatorClass.length() == 0)
      {
         buddyLocatorClass = NextMemberBuddyLocator.class.getName();
      }
      Properties props = null;
      props = XmlConfigHelper.readPropertiesContents(element, "buddyLocatorProperties");
      BuddyLocatorConfig blc = new BuddyLocatorConfig();
      blc.setBuddyLocatorClass(buddyLocatorClass);
      blc.setBuddyLocatorProperties(props);
      brc.setBuddyLocatorConfig(blc);

      return brc;
   }

   public static CacheLoaderConfig parseCacheLoaderConfig(Element element)
   {
      CacheLoaderConfig clc = new CacheLoaderConfig();
      clc.setPassivation(XmlConfigHelper.readBooleanContents(element, "passivation"));
      String s = XmlConfigHelper.readStringContents(element, "preload");
      if (s != null && s.length() > 0) clc.setPreload(s);
      clc.setShared(XmlConfigHelper.readBooleanContents(element, "shared"));

      NodeList cacheLoaderNodes = element.getElementsByTagName("cacheloader");
      for (int i = 0; i < cacheLoaderNodes.getLength(); i++)
      {
         Node node = cacheLoaderNodes.item(i);
         if (node.getNodeType() == Node.ELEMENT_NODE)
         {
            Element indivElement = (Element) node;
            CacheLoaderConfig.IndividualCacheLoaderConfig iclc = new CacheLoaderConfig.IndividualCacheLoaderConfig();
            iclc.setAsync(XmlConfigHelper.readBooleanContents(indivElement, "async", false));
            iclc.setIgnoreModifications(XmlConfigHelper.readBooleanContents(indivElement, "ignoreModifications", false));
            iclc.setFetchPersistentState(XmlConfigHelper.readBooleanContents(indivElement, "fetchPersistentState", false));
            iclc.setPurgeOnStartup(XmlConfigHelper.readBooleanContents(indivElement, "purgeOnStartup", false));
            iclc.setClassName(XmlConfigHelper.readStringContents(indivElement, "class"));
            iclc.setProperties(XmlConfigHelper.readPropertiesContents(indivElement, "properties"));

            SingletonStoreConfig ssc = parseSingletonStoreConfig(indivElement);
            if (ssc != null)
            {
               iclc.setSingletonStoreConfig(ssc);
            }

            clc.addIndividualCacheLoaderConfig(iclc);
         }
      }
      return clc;
   }

   private static CacheLoaderConfig.IndividualCacheLoaderConfig.SingletonStoreConfig parseSingletonStoreConfig(Element cacheLoaderelement)
   {
      /* singletonStore element can only appear once in a cacheloader, so we just take the first one ignoring any
      subsequent definitions in cacheloader element*/
      Node singletonStoreNode = cacheLoaderelement.getElementsByTagName("singletonStore").item(0);
      if (singletonStoreNode != null && singletonStoreNode.getNodeType() == Node.ELEMENT_NODE)
      {
         Element singletonStoreElement = (Element) singletonStoreNode;
         boolean singletonStoreEnabled = XmlConfigHelper.readBooleanContents(singletonStoreElement, "enabled");
         String singletonStoreClass = XmlConfigHelper.readStringContents(singletonStoreElement, "class");
         Properties singletonStoreproperties;
         singletonStoreproperties = XmlConfigHelper.readPropertiesContents(singletonStoreElement, "properties");
         CacheLoaderConfig.IndividualCacheLoaderConfig.SingletonStoreConfig ssc = new CacheLoaderConfig.IndividualCacheLoaderConfig.SingletonStoreConfig();
         ssc.setSingletonStoreEnabled(singletonStoreEnabled);
         ssc.setSingletonStoreClass(singletonStoreClass);
         ssc.setSingletonStoreproperties(singletonStoreproperties);

         return ssc;
      }

      return null;
   }

   @SuppressWarnings("unchecked")
   public static EvictionConfig parseEvictionConfig(Element element)
   {
      EvictionConfig evictionConfig = new EvictionConfig();

      if (element != null)
      {
         // If they set the default eviction policy in the element, use that
         // in preference to the external attribute
         String temp = XmlConfigHelper.getTagContents(element, "policyClass", ATTR, NAME);
         String defaultEvPolicyClassName = null;
         if (temp != null && temp.length() > 0)
         {
            defaultEvPolicyClassName = temp;
            EvictionAlgorithmConfig eac = getEvictionAlgorithmConfig(temp);
            evictionConfig.getDefaultEvictionRegionConfig().setEvictionAlgorithmConfig(eac);
         }

         temp = XmlConfigHelper.getTagContents(element, "wakeUpIntervalSeconds", ATTR, NAME);

         int wakeupIntervalSeconds = 0;
         if (temp != null)
         {
            wakeupIntervalSeconds = Integer.parseInt(temp);
         }

         if (wakeupIntervalSeconds <= 0)
         {
            wakeupIntervalSeconds = EvictionConfig.WAKEUP_DEFAULT;
         }

         evictionConfig.setWakeupInterval(wakeupIntervalSeconds * 1000);

         int eventQueueSize = 0;
         temp = XmlConfigHelper.getTagContents(element, "eventQueueSize", ATTR, NAME);

         if (temp != null)
         {
            eventQueueSize = Integer.parseInt(temp);
         }

         if (eventQueueSize <= 0)
         {
            eventQueueSize = EvictionConfig.EVENT_QUEUE_SIZE_DEFAULT;
         }

         evictionConfig.getDefaultEvictionRegionConfig().setEventQueueSize(eventQueueSize);

         NodeList list = element.getElementsByTagName(EvictionRegionConfig.REGION);
         if (list != null && list.getLength() > 0)
         {
            List regionConfigs = new ArrayList(list.getLength());
            for (int i = 0; i < list.getLength(); i++)
            {
               org.w3c.dom.Node node = list.item(i);
               if (node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE)
               {
                  continue;
               }
               try
               {
                  EvictionRegionConfig evictionRegionConfig = parseEvictionRegionConfig((Element) node, defaultEvPolicyClassName, eventQueueSize);
                  if (!evictionRegionConfig.getRegionFqn().equals(RegionManagerImpl.DEFAULT_REGION))
                  {
                     regionConfigs.add(evictionRegionConfig);
                  }
                  else
                  {
                     evictionConfig.getDefaultEvictionRegionConfig().setEventQueueSize(evictionRegionConfig.getEventQueueSize());
                     evictionConfig.getDefaultEvictionRegionConfig().setEvictionAlgorithmConfig(evictionRegionConfig.getEvictionAlgorithmConfig());
                  }
               }
               catch (MissingPolicyException missingPolicy)
               {
                  LogFactory.getLog(EvictionConfig.class).warn(missingPolicy.getLocalizedMessage());
                  throw missingPolicy;
               }
            }

            evictionConfig.setEvictionRegionConfigs(regionConfigs);
         }
      }

      return evictionConfig;

   }

   private static EvictionRegionConfig parseEvictionRegionConfig(Element element, String defaultEvPolicyClassName, int defaultQueueCapacity)
   {
      EvictionRegionConfig erc = new EvictionRegionConfig();

      erc.setRegionName(element.getAttribute(EvictionRegionConfig.NAME));

      String temp = element.getAttribute("eventQueueSize");
      if (temp != null && temp.length() > 0)
      {
         erc.setEventQueueSize(Integer.parseInt(temp));
      }
      else
      {
         erc.setEventQueueSize(defaultQueueCapacity);
      }
      String evictionClass = element.getAttribute("policyClass");

      if (evictionClass == null || evictionClass.length() == 0)
      {
         evictionClass = defaultEvPolicyClassName;
         // if it's still null... what do we setCache?
         if (evictionClass == null || evictionClass.length() == 0)
         {
            throw new MissingPolicyException(
                  "There is no Eviction Policy Class specified on the region or for the entire cache!");
         }
      }

      EvictionAlgorithmConfig algorithmConfig = getEvictionAlgorithmConfig(evictionClass);

      parseEvictionPolicyConfig(element, algorithmConfig);

      erc.setEvictionAlgorithmConfig(algorithmConfig);
      return erc;
   }

   private static EvictionAlgorithmConfig getEvictionAlgorithmConfig(String evictionClass)
   {
      EvictionConfig.assertIsTransformable(evictionClass);

      EvictionAlgorithm algorithm;

      try
      {
         EvictionPolicy ep = (EvictionPolicy) Util.getInstance(evictionClass);
         Class<? extends EvictionAlgorithm> algoClass = ((ModernizablePolicy) ep).modernizePolicy();
         if (log.isTraceEnabled()) log.trace("Using algo class " + algoClass);
         algorithm = Util.getInstance(algoClass);
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         throw new RuntimeException("Eviction class is not properly loaded in classloader", e);
      }

      EvictionAlgorithmConfig algorithmConfig;
      try
      {
         algorithmConfig = algorithm.getConfigurationClass().newInstance();
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         throw new RuntimeException("Failed to instantiate eviction configuration of class " +
               algorithm.getConfigurationClass(), e);
      }
      return algorithmConfig;
   }

   @SuppressWarnings("unchecked")
   private static void parseEvictionPolicyConfig(Element element, EvictionAlgorithmConfig target)
   {
      target.reset();
      ParsedAttributes attributes = XmlConfigHelper.extractAttributes(element);
      Map updatedElements = new HashMap();
      for (Map.Entry entry : attributes.stringAttribs.entrySet())
      {
         String key = (String) entry.getKey();
         String value = (String) entry.getValue();
         if (key.indexOf("Seconds") > 0)
         {
            key = key.substring(0, key.length() - "Seconds".length());


            value = value.trim() + "000";
         }
         int intval = 1;
         try
         {
            intval = Integer.parseInt(value);

         }
         catch (NumberFormatException e)
         {

         }
         value = intval < 1 ? "-1" : value;

         updatedElements.put(key, value);
      }
      attributes.stringAttribs.clear();
      attributes.stringAttribs.putAll(updatedElements);
      XmlConfigHelper.setValues(target, attributes.stringAttribs, false, true);
      XmlConfigHelper.setValues(target, attributes.xmlAttribs, true, true);
   }


   /**
    * Parses the cluster config which is used to start a JGroups channel
    *
    * @param config an old-style JGroups protocol config String
    */
   public static String parseClusterConfigXml(Element config)
   {
      StringBuilder buffer = new StringBuilder();
      NodeList stack = config.getChildNodes();
      int length = stack.getLength();

      for (int s = 0; s < length; s++)
      {
         org.w3c.dom.Node node = stack.item(s);
         if (node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE)
         {
            continue;
         }

         Element tag = (Element) node;
         String protocol = tag.getTagName();
         buffer.append(protocol);
         NamedNodeMap attrs = tag.getAttributes();
         int attrLength = attrs.getLength();
         if (attrLength > 0)
         {
            buffer.append('(');
         }
         for (int a = 0; a < attrLength; a++)
         {
            Attr attr = (Attr) attrs.item(a);
            String name = attr.getName();
            String value = attr.getValue();
            buffer.append(name);
            buffer.append('=');
            buffer.append(value);
            if (a < attrLength - 1)
            {
               buffer.append(';');
            }
         }
         if (attrLength > 0)
         {
            buffer.append(')');
         }
         buffer.append(':');
      }
      // Remove the trailing ':'
      buffer.setLength(buffer.length() - 1);
      return buffer.toString();
   }
}
