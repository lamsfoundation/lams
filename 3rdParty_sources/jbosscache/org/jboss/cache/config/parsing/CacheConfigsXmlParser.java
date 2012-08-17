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
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.util.FileLookup;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser able to parse a series of cache configurations stored in an
 * XML document with the following structure:
 * <pre>
 * <cache-configs>
 *    <cache-config name="configA">
 *     ....
 *    </cache-config>
 *    <cache-config name="configB">
 *     ....
 *    </cache-config>
 * </cache-configs>
 * </pre>
 * <p/>
 * The '....' represents the normal content of the mbean element in a
 * JBC -service.xml config file.
 *
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 */
public class CacheConfigsXmlParser
{
   /**
    * Name of the root element in a cache configs XML document
    */
   public static final String DOCUMENT_ROOT = "cache-configs";
   /**
    * Name of the element that represents an individual cache configuration
    * in a cache configs XML document.
    */
   public static final String CONFIG_ROOT = "cache-config";
   public static final String QUALIFIED_CONFIG_ROOT = "registry:cache-config";

   /**
    * Name of the attribute in a {@link #CONFIG_ROOT cache-config} element that specifies
    * the name of the configuration.
    */
   public static final String CONFIG_NAME = "name";

   private static final Log log = LogFactory.getLog(CacheConfigsXmlParser.class);


   public Map<String, Configuration> parseConfigs(String fileName) throws CloneNotSupportedException
   {
      FileLookup fileLookup = new FileLookup();
      InputStream is = fileLookup.lookupFile(fileName);
      if (is == null)
      {
         throw new ConfigurationException("Unable to find config file " + fileName + " either in classpath or on the filesystem!");
      }

      return parseConfigs(is, fileName);
   }

   public Map<String, Configuration> parseConfigs(InputStream stream, String fileName) throws CloneNotSupportedException
   {
      // loop through all elements in XML.
      Element root = getDocumentRoot(stream);

      NodeList list = root.getElementsByTagName(CONFIG_ROOT);
      if (list == null || list.getLength() == 0)
      {
         // try looking for a QUALIFIED_CONFIG_ROOT
         list = root.getElementsByTagName(QUALIFIED_CONFIG_ROOT);
         if (list == null || list.getLength() == 0)
            throw new ConfigurationException("Can't find " + CONFIG_ROOT + " or " + QUALIFIED_CONFIG_ROOT + " tag");
      }

      Map<String, Configuration> result = new HashMap<String, Configuration>();

      for (int i = 0; i < list.getLength(); i++)
      {
         Node node = list.item(i);
         if (node.getNodeType() != Node.ELEMENT_NODE)
         {
            continue;
         }

         Element element = (Element) node;
         String name = element.getAttribute(CONFIG_NAME);
         if (name == null || name.trim().length() == 0)
            throw new ConfigurationException("Element " + element + " has no name attribute");

         XmlConfigurationParser parser = new XmlConfigurationParser();
         Configuration c;
         if (parser.isValidElementRoot(element))
         {
            // FIXME - This should be using a valid schema!!!
            c = parser.parseElementIgnoringRoot(element);
         }
         else
         {
            if (fileName == null)
               log.debug("Detected legacy configuration file format when parsing configuration XML from input stream ["+stream+"].  Migrating to the new (3.x) file format is recommended.  See FAQs for details.");
            else
               log.debug("Detected legacy configuration file format when parsing configuration file ["+fileName+"].  Migrating to the new (3.x) file format is recommended.  See FAQs for details.");
            XmlConfigurationParser2x oldParser = new XmlConfigurationParser2x();
            c = oldParser.parseConfiguration(element);
         }

         // Prove that we can successfully clone it
         c = c.clone();
         result.put(name.trim(), c);
      }

      return result;
   }

   private Element getDocumentRoot(InputStream stream)
   {
      RootElementBuilder rootElementBuilder = new RootElementBuilder(false);
      return rootElementBuilder.readRoot(stream);
   }
}