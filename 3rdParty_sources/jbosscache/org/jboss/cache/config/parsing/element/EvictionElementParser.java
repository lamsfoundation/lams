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

import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.EvictionAlgorithmConfig;
import org.jboss.cache.config.EvictionConfig;
import org.jboss.cache.config.EvictionRegionConfig;
import org.jboss.cache.config.MissingPolicyException;
import org.jboss.cache.config.parsing.XmlConfigHelper;
import org.jboss.cache.config.parsing.XmlParserBase;
import org.jboss.cache.config.parsing.RootElementBuilder;
import org.jboss.cache.eviction.EvictionAlgorithm;
import org.jboss.cache.util.Util;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Knows how to parse the <b>eviction</b> xml element.
 * <pre>
 * Note: class does not rely on element position in the configuration file.
 *       It does not rely on element's name either.
 * </pre>
 *
 * @author Mircea.Markus@jboss.com
 * @since 3.0
 */
public class EvictionElementParser extends XmlParserBase
{
   public EvictionElementParser()
   {
      this(RootElementBuilder.JBOSSCACHE_CORE_NS_31);
   }

   public EvictionElementParser(String coreNamespace)
   {
      this.coreNamespace = coreNamespace;
   }

   public EvictionConfig parseEvictionElement(Element evictionElement)
   {
      assertNotLegacyElement(evictionElement);
      
      EvictionConfig evictionConfig = new EvictionConfig();

      String wakeUpInterval = getAttributeValue(evictionElement, "wakeUpInterval");
      if (existsAttribute(wakeUpInterval))
      {
         evictionConfig.setWakeupInterval(getInt(wakeUpInterval));
      }
      else
      {
         throw new ConfigurationException("Missing mandatory attribute wakeUpInterval");
      }

      List<EvictionRegionConfig> evictionRegionConfigs = new LinkedList<EvictionRegionConfig>();
      Element defaultRegion = getSingleElementInCoreNS("default", evictionElement);

      if (defaultRegion != null)
      {
         EvictionRegionConfig defaultRegionConfig = getEvictionRegionConfig(defaultRegion, null, true);
         if (defaultRegionConfig.getEvictionAlgorithmConfig() == null)
            throw new MissingPolicyException("Default eviction region should have an evictionAlgorithmClass defined.");
         evictionConfig.setDefaultEvictionRegionConfig(defaultRegionConfig);
      }

      NodeList regions = evictionElement.getElementsByTagName("region");
      for (int i = 0; i < regions.getLength(); i++)
      {
         Element regionConfig = (Element) regions.item(i);
         EvictionRegionConfig erc = getEvictionRegionConfig(regionConfig, evictionConfig.getDefaultEvictionRegionConfig(), false);
         evictionConfig.applyDefaults(erc);
         evictionRegionConfigs.add(erc);
      }
      evictionConfig.setEvictionRegionConfigs(evictionRegionConfigs);
      return evictionConfig;
   }

   @SuppressWarnings("unchecked")
   private EvictionRegionConfig getEvictionRegionConfig(Element element, EvictionRegionConfig defaultRegion, boolean isDefault)
   {
      EvictionRegionConfig erc = new EvictionRegionConfig();
      erc.setRegionName(getAttributeValue(element, "name"));
      String queueSize = getAttributeValue(element, "eventQueueSize");
      if (existsAttribute(queueSize))
      {
         erc.setEventQueueSize(getInt(queueSize));
      }
      else if (defaultRegion == null)
      {
         erc.setEventQueueSize(EvictionConfig.EVENT_QUEUE_SIZE_DEFAULT);
      }

      String algorithmClassName = getAttributeValue(element, "algorithmClass");
      EvictionAlgorithmConfig algorithmConfig = null; // every eviction region config needs an algorithm config.

      if (existsAttribute(algorithmClassName))
      {
         EvictionAlgorithm algorithm;
         Class<? extends EvictionAlgorithm> algorithmClass;
         // try using a 'getInstance()' factory.

         try
         {
            algorithmClass = Util.loadClass(algorithmClassName);
         }
         catch (Exception e)
         {
            throw new RuntimeException("Unable to load eviction algorithm class [" + algorithmClassName + "]", e);
         }


         try
         {
            algorithm = Util.getInstance(algorithmClass);
         }
         catch (Exception e)
         {
            throw new ConfigurationException("Unable to construct eviction algorithm class [" + algorithmClassName + "]", e);
         }

         try
         {
            algorithmConfig = Util.getInstance(algorithm.getConfigurationClass());
         }
         catch (Exception e)
         {
            throw new RuntimeException("Failed to instantiate eviction algorithm configuration class [" +
                  algorithm.getConfigurationClass() + "]", e);
         }
      }
      else
      {
         if (!isDefault)
         {
            if (defaultRegion == null || defaultRegion.getEvictionAlgorithmConfig() == null)
            {
               throw new MissingPolicyException("There is no Eviction Algorithm Class specified on the region or for the entire cache!");
            }
            else
            {
               try
               {
                  algorithmConfig = defaultRegion.getEvictionAlgorithmConfig().clone();
               }
               catch (CloneNotSupportedException e)
               {
                  throw new ConfigurationException("Unable to clone eviction algorithm configuration from default", e);
               }
            }
         }
      }

      if (algorithmConfig != null)
      {
         parseEvictionPolicyConfig(element, algorithmConfig);

         erc.setEvictionAlgorithmConfig(algorithmConfig);
      }

      String actionPolicyClass = getAttributeValue(element, "actionPolicyClass");
      if (existsAttribute(actionPolicyClass))
      {
         erc.setEvictionActionPolicyClassName(actionPolicyClass);
      }
      else if (defaultRegion == null)
      {
         // this is the default region. Make sure we set the default EvictionActionPolicyClass.
         erc.setEvictionActionPolicyClassName(EvictionConfig.EVICTION_ACTION_POLICY_CLASS_DEFAULT);
      }


      return erc;
   }

   public static void parseEvictionPolicyConfig(Element element, EvictionAlgorithmConfig target)
   {
//      target.reset();
      Properties p = XmlConfigHelper.extractProperties(element);
      XmlConfigHelper.setValues(target, p, false, true);
   }
}
