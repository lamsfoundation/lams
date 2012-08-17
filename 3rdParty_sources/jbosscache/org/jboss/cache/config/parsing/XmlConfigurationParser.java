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

import org.jboss.cache.config.BuddyReplicationConfig;
import org.jboss.cache.config.CacheLoaderConfig;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.Configuration.CacheMode;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.CustomInterceptorConfig;
import org.jboss.cache.config.parsing.element.BuddyElementParser;
import org.jboss.cache.config.parsing.element.CustomInterceptorsElementParser;
import org.jboss.cache.config.parsing.element.EvictionElementParser;
import org.jboss.cache.config.parsing.element.LoadersElementParser;
import org.jboss.cache.config.parsing.JGroupsStackParser;
import org.jboss.cache.lock.IsolationLevel;
import org.jboss.cache.util.FileLookup;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Reads in XMLconfiguration files and spits out a {@link org.jboss.cache.config.Configuration} object.
 * By default this class uses a validating parser (configurable).
 * <p/>
 * Following system properties can be used for customizing parser behavior:
 * <ul>
 * <li> <b>-Djbosscache.config.validate=false</b> will make the parser non-validating </li>
 * <li> <b>-Djbosscache.config.schemaLocation=url</b> allows one to specify a validation schema that would override the one specified in the the xml document </li>
 * </ul>
 * This class is stateful and one instance should be used for parsing a single configuration file.
 *
 * @author Mircea.Markus@jboss.com
 * @see org.jboss.cache.config.parsing.RootElementBuilder
 * @since 3.0
 */
public class XmlConfigurationParser extends XmlParserBase
{
   private RootElementBuilder rootElementBuilder;

   /**
    * the resulting configuration.
    */
   private Configuration config = new Configuration();
   private Element root;

   /**
    * If validation is on (default) one can specify an error handler for handling validation errors.
    * The default error handler just logs parsing errors received.
    */
   public XmlConfigurationParser(ErrorHandler errorHandler)
   {
      rootElementBuilder = new RootElementBuilder(errorHandler);
   }

   /**
    * Same as {@link #XmlConfigurationParser(org.xml.sax.ErrorHandler)}.
    *
    * @param validating should the underlaying parser disable the validation?
    */
   public XmlConfigurationParser(boolean validating, ErrorHandler errorHandler)
   {
      rootElementBuilder = new RootElementBuilder(errorHandler, validating);
   }

   /**
    * Constructs a parser having validation enabled with a ErrorHandler that only logs the parser errors.
    */
   public XmlConfigurationParser()
   {
      rootElementBuilder = new RootElementBuilder();
   }

   /**
    * Parses an XML file and returns a new configuration.
    * For looking up the file, {@link org.jboss.cache.util.FileLookup} is used.
    *
    * @see org.jboss.cache.util.FileLookup
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
    * Similar to {@link #parseFile(String)}, just that it does not create the input stream.
    */
   public Configuration parseStream(InputStream configStream)
   {
      readRoot(configStream);
      return processElements(false);
   }

   /**
    * Root should be the <b>jbosscache</b> element in the configuration file.
    */
   public Configuration parseElement(Element root)
   {
      this.root = root;
      this.root.normalize();
      return processElements(false);
   }

   public Configuration parseElementIgnoringRoot(Element root)
   {
      this.root = root;
      this.root.normalize();
      return processElements(true);
   }

   public boolean isValidating()
   {
      return rootElementBuilder.isValidating();
   }

   private Configuration processElements(boolean ignoreRoot)
   {
      coreNamespace = root.getNamespaceURI();
      if (coreNamespace == null) coreNamespace = RootElementBuilder.JBOSSCACHE_CORE_NS_31; // use the default
      
      if (!ignoreRoot &&
            (!"jbosscache".equals(root.getLocalName()) ||
             (!RootElementBuilder.JBOSSCACHE_CORE_NS_31.equals(coreNamespace) &&
             !RootElementBuilder.JBOSSCACHE_CORE_NS_30.equals(coreNamespace))
         ))
      {
            throw new ConfigurationException("Expected root element <jbosscache />" + (isValidating() ? " in either {" +
                  RootElementBuilder.JBOSSCACHE_CORE_NS_30 + "} or {" + RootElementBuilder.JBOSSCACHE_CORE_NS_31 + "} namespaces" : ""));
      }

      try
      {
         configureLocking(getSingleElement("locking"));
         configureTransaction(getSingleElement("transaction"));
         configureClustering(getSingleElement("clustering"));
         configureSerialization(getSingleElement("serialization"));
         configureInvalidation(getSingleElement("invalidation"));
         configureStartup(getSingleElement("startup"));
         configureShutdown(getSingleElement("shutdown"));
         configureJmxStatistics(getSingleElement("jmxStatistics"));
         configureEviction(getSingleElement("eviction"));
         configureCacheLoaders(getSingleElement("loaders"));
         configureCustomInterceptors(getSingleElement("customInterceptors"));
         configureListeners(getSingleElement("listeners"));
         configureInvocationBatching(getSingleElement("invocationBatching"));
      }
      catch (Exception e)
      {
         throw new ConfigurationException("Unexpected exception while parsing the configuration file", e);
      }
      return config;
   }

   private void configureClustering(Element e)
   {
      if (e == null) return; //we might not have this configured
      // there are 2 attribs - mode and clusterName
      boolean repl = true;
      String mode = getAttributeValue(e, "mode").toUpperCase();
      if (mode.startsWith("R"))
         repl = true;
      else if (mode.startsWith("I"))
         repl = false;

      Element asyncEl = getSingleElementInCoreNS("async", e);
      Element syncEl = getSingleElementInCoreNS("sync", e);
      if (syncEl != null && asyncEl != null) throw new ConfigurationException("Cannot have sync and async elements within the same cluster element!");
      boolean sync = asyncEl == null; // even if both are null, we default to sync
      if (sync)
      {
         config.setCacheMode(repl ? CacheMode.REPL_SYNC : CacheMode.INVALIDATION_SYNC);
         configureSyncMode(syncEl);
      }
      else
      {
         config.setCacheMode(repl ? CacheMode.REPL_ASYNC : CacheMode.INVALIDATION_ASYNC);
         configureAsyncMode(asyncEl);
      }
      String cn = getAttributeValue(e, "clusterName");
      if (existsAttribute(cn)) config.setClusterName(cn);
      configureBuddyReplication(getSingleElementInCoreNS("buddy", e));
      configureStateRetrieval(getSingleElementInCoreNS("stateRetrieval", e));
      configureTransport(getSingleElementInCoreNS("jgroupsConfig", e));
   }

   private void configureStateRetrieval(Element element)
   {
      if (element == null) return; //we might not have this configured
      String tmp = getAttributeValue(element, "fetchInMemoryState");
      if (existsAttribute(tmp)) config.setFetchInMemoryState(getBoolean(tmp));
      tmp = getAttributeValue(element, "timeout");
      if (existsAttribute(tmp)) config.setStateRetrievalTimeout(getLong(tmp));
      tmp = getAttributeValue(element, "nonBlocking");
      if (existsAttribute(tmp)) config.setNonBlockingStateTransfer(getBoolean(tmp));      
   }

   private void configureTransaction(Element element)
   {
      if (element == null) return;
      String attrName = "transactionManagerLookupClass";
      String txMngLookupClass = getAttributeValue(element, attrName);
      if (existsAttribute(txMngLookupClass)) config.setTransactionManagerLookupClass(txMngLookupClass);
      String syncRollbackPhase = getAttributeValue(element, "syncRollbackPhase");
      if (existsAttribute(syncRollbackPhase)) config.setSyncRollbackPhase(getBoolean(syncRollbackPhase));
      String syncCommitPhase = getAttributeValue(element, "syncCommitPhase");
      if (existsAttribute(syncCommitPhase)) config.setSyncCommitPhase(getBoolean(syncCommitPhase));
   }

   private void configureSerialization(Element element)
   {
      if (element == null) return;
      String objectInputStreamPoolSize = getAttributeValue(element, "objectInputStreamPoolSize");
      if (existsAttribute(objectInputStreamPoolSize))
         config.setObjectInputStreamPoolSize(getInt(objectInputStreamPoolSize));
      String objectOutputStreamPoolSize = getAttributeValue(element, "objectOutputStreamPoolSize");
      if (existsAttribute(objectOutputStreamPoolSize))
         config.setObjectOutputStreamPoolSize(getInt(objectOutputStreamPoolSize));
      String version = getAttributeValue(element, "version");
      if (existsAttribute(version)) config.setReplVersionString(version);
      String marshallerClass = getAttributeValue(element, "marshallerClass");
      if (existsAttribute(marshallerClass)) config.setMarshallerClass(marshallerClass);
      String useLazyDeserialization = getAttributeValue(element, "useLazyDeserialization");
      if (existsAttribute(useLazyDeserialization)) config.setUseLazyDeserialization(getBoolean(useLazyDeserialization));
      String useRegionBasedMarshalling = getAttributeValue(element, "useRegionBasedMarshalling");
      if (existsAttribute(useRegionBasedMarshalling))
         config.setUseRegionBasedMarshalling(getBoolean(useRegionBasedMarshalling));
   }

   private void configureCustomInterceptors(Element element)
   {
      if (element == null) return; //this element might be missing
      CustomInterceptorsElementParser parser = new CustomInterceptorsElementParser(coreNamespace);
      List<CustomInterceptorConfig> interceptorConfigList = parser.parseCustomInterceptors(element);
      config.setCustomInterceptors(interceptorConfigList);
   }

   private void configureListeners(Element element)
   {
      if (element == null) return; //this element is optional
      String asyncPoolSizeStr = getAttributeValue(element, "asyncPoolSize");
      if (existsAttribute(asyncPoolSizeStr)) config.setListenerAsyncPoolSize(getInt(asyncPoolSizeStr));

      String asyncQueueSizeStr = getAttributeValue(element, "asyncQueueSize");
      if (existsAttribute(asyncQueueSizeStr)) config.setListenerAsyncQueueSize(getInt(asyncQueueSizeStr));      
   }

   private void configureInvocationBatching(Element element)
   {
      if (element == null) return; //this element is optional
      boolean enabled = getBoolean(getAttributeValue(element, "enabled"));
      config.setInvocationBatchingEnabled(enabled);
   }

   private void configureBuddyReplication(Element element)
   {
      if (element == null) return;//buddy config might not exist, expect that
      BuddyElementParser buddyElementParser = new BuddyElementParser(coreNamespace);
      BuddyReplicationConfig brConfig = buddyElementParser.parseBuddyElement(element);
      config.setBuddyReplicationConfig(brConfig);
   }

   private void configureCacheLoaders(Element element)
   {
      if (element == null) return; //null cache loaders are allowed
      LoadersElementParser clElementParser = new LoadersElementParser(coreNamespace);
      CacheLoaderConfig cacheLoaderConfig = clElementParser.parseLoadersElement(element);
      config.setCacheLoaderConfig(cacheLoaderConfig);
   }

   private void configureEviction(Element element)
   {
      if (element == null) return; //no eviction might be configured
      EvictionElementParser evictionElementParser = new EvictionElementParser(coreNamespace);
      config.setEvictionConfig(evictionElementParser.parseEvictionElement(element));
   }

   private void configureJmxStatistics(Element element)
   {
      if (element == null) return; //might not be specified
      String enabled = getAttributeValue(element, "enabled");
      config.setExposeManagementStatistics(getBoolean(enabled));
   }

   private void configureShutdown(Element element)
   {
      if (element == null) return;
      String hookBehavior = getAttributeValue(element, "hookBehavior");
      if (existsAttribute(hookBehavior)) config.setShutdownHookBehavior(hookBehavior);
   }

   private void configureTransport(Element element)
   {
      if (element == null) return; //transport might be missing

      // first see if a configFile is provided
      String cfgFile = getAttributeValue(element, "configFile");
      if (existsAttribute(cfgFile))
      {
         // try and load this file
         URL u = new FileLookup().lookupFileLocation(cfgFile);
         config.setJgroupsConfigFile(u);
      }
      else
      {
         String multiplexerStack = getAttributeValue(element, "multiplexerStack");
         if (existsAttribute(multiplexerStack))
         {
            config.setMultiplexerStack(multiplexerStack);
         }
         else
         {
            JGroupsStackParser stackParser = new JGroupsStackParser();
            String clusterConfigStr = stackParser.parseClusterConfigXml(element);
            if (clusterConfigStr != null && clusterConfigStr.trim().length() > 0)
               config.setClusterConfig(clusterConfigStr);
         }
      }
   }

   private void configureStartup(Element element)
   {
      if (element == null) return; //we might not have this configured
      String inactiveOnStartup = getAttributeValue(element, "regionsInactiveOnStartup");
      if (existsAttribute(inactiveOnStartup)) config.setInactiveOnStartup(getBoolean(inactiveOnStartup));
   }

   private void configureInvalidation(Element element)
   {
      if (element == null) return; //might be replication
      Element async = getSingleElement("async");
      if (async != null)
      {
         config.setCacheMode(Configuration.CacheMode.INVALIDATION_ASYNC);
         configureAsyncMode(getSingleElementInCoreNS("async", element));
      }
      Element sync = getSingleElement("sync");
      if (sync != null)
      {
         config.setCacheMode(Configuration.CacheMode.INVALIDATION_SYNC);
         configureSyncMode(getSingleElementInCoreNS("sync", element));
      }
   }

   private void configureSyncMode(Element element)
   {
      String replTimeout = getAttributeValue(element, "replTimeout");
      if (existsAttribute(replTimeout)) config.setSyncReplTimeout(getLong(replTimeout));
   }

   private void configureAsyncMode(Element element)
   {
      String useReplQueue = getAttributeValue(element, "useReplQueue");
      if (existsAttribute(useReplQueue)) config.setUseReplQueue(getBoolean(useReplQueue));
      String replQueueInterval = getAttributeValue(element, "replQueueInterval");
      if (existsAttribute(replQueueInterval)) config.setReplQueueInterval(getLong(replQueueInterval));
      String replQueueMaxElements = getAttributeValue(element, "replQueueMaxElements");

      if (existsAttribute(replQueueMaxElements)) config.setReplQueueMaxElements(getInt(replQueueMaxElements));
      String serializationExecutorPoolSize = getAttributeValue(element, "serializationExecutorPoolSize");
      if (existsAttribute(serializationExecutorPoolSize))
         config.setSerializationExecutorPoolSize(getInt(serializationExecutorPoolSize));

      String serializationExecutorQueueSize = getAttributeValue(element, "serializationExecutorQueueSize");
      if (existsAttribute(serializationExecutorQueueSize))
         config.setSerializationExecutorQueueSize(getInt(serializationExecutorQueueSize));
   }

   private void configureLocking(Element element)
   {
      String tmp = getAttributeValue(element, "isolationLevel");
      if (existsAttribute(tmp)) config.setIsolationLevel(IsolationLevel.valueOf(tmp));
      tmp = getAttributeValue(element, "lockParentForChildInsertRemove");
      if (existsAttribute(tmp)) config.setLockParentForChildInsertRemove(getBoolean(tmp));
      tmp = getAttributeValue(element, "lockAcquisitionTimeout");
      if (existsAttribute(tmp)) config.setLockAcquisitionTimeout(getLong(tmp));
      tmp = getAttributeValue(element, "nodeLockingScheme");
      if (existsAttribute(tmp)) config.setNodeLockingScheme(tmp);
      tmp = getAttributeValue(element, "writeSkewCheck");
      if (existsAttribute(tmp)) config.setWriteSkewCheck(getBoolean(tmp));
      tmp = getAttributeValue(element, "useLockStriping");
      if (existsAttribute(tmp)) config.setUseLockStriping(getBoolean(tmp));
      tmp = getAttributeValue(element, "concurrencyLevel");
      if (existsAttribute(tmp)) config.setConcurrencyLevel(getInt(tmp));
   }

   private Element getSingleElement(String elementName)
   {
      return getSingleElementInCoreNS(elementName, root);
   }

   private void readRoot(InputStream config)
   {
      root = rootElementBuilder.readRoot(config);
   }

   /**
    * Tests whether the element passed in is a modern (3.0) config element rather than a legacy one.
    *
    * @param element element to test
    * @return true of the element is a modern one and can be parsed using the current parser.
    */
   public boolean isValidElementRoot(Element element)
   {
      // simply test for the "jbosscache" element.
      NodeList elements = element.getElementsByTagName("jbosscache");
      return elements != null && elements.getLength() > 0;
   }
}
