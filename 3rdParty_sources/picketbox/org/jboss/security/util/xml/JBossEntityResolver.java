/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.security.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.logging.Logger;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.util.StringPropertyReplacer;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Local entity resolver to handle standard J2EE DTDs and Schemas as well as JBoss
 * specific DTDs.
 * <p/>
 * Function boolean isEntityResolved() is here to avoid validation errors in
 * descriptors that do not have a DOCTYPE declaration.
 *
 * @author Scott.Stark@jboss.org
 * @author Thomas.Diesler@jboss.org
 * @author Dimitris.Andreadis@jboss.org
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public class JBossEntityResolver implements EntityResolver
{
   /** A class wide Map<String, String> of publicId/systemId to dtd/xsd file */
   private static final Map entities = new ConcurrentHashMap ();
   /** A class flag indicating whether an attempt to resolve a systemID as a
    non-file URL should produce a warning rather than a trace level log msg.
    */
   private static boolean warnOnNonFileURLs;

   private boolean entityResolved = false;
   /** Should system property refs in system ids be replaced */
   private boolean replaceSystemProperties = true;

   /** A local entities map that overrides the class level entities */
   private Map localEntities;

   static
   {
      AccessController.doPrivileged(new PrivilegedAction()
      {
         public Object run()
         {
            warnOnNonFileURLs = new Boolean(System.getProperty("org.jboss.resolver.warning", "false")).booleanValue();
            return null;
         }
      });

      // xml
      registerEntity("-//W3C//DTD/XMLSCHEMA 200102//EN", "XMLSchema.dtd");
      registerEntity("http://www.w3.org/2001/XMLSchema.dtd", "XMLSchema.dtd");
      registerEntity("datatypes", "datatypes.dtd"); // This dtd doesn't have a publicId - see XMLSchema.dtd
      registerEntity("http://www.w3.org/XML/1998/namespace", "xml.xsd");
      registerEntity("http://www.w3.org/2001/xml.xsd", "xml.xsd");
      registerEntity("http://www.w3.org/2005/05/xmlmime", "xml-media-types.xsd");

      // Java EE common
      registerEntity("http://java.sun.com/xml/ns/j2ee/j2ee_1_4.xsd", "j2ee_1_4.xsd");
      registerEntity("http://java.sun.com/xml/ns/javaee/javaee_5.xsd", "javaee_5.xsd");
      // JBoss common
      registerEntity("http://www.jboss.org/j2ee/schema/jboss-common_5_1.xsd", "jboss-common_5_1.xsd");

      // Java EE WS
      registerEntity("http://schemas.xmlsoap.org/soap/encoding/", "soap-encoding_1_1.xsd");
      registerEntity("http://www.ibm.com/webservices/xsd/j2ee_web_services_client_1_1.xsd", "j2ee_web_services_client_1_1.xsd");
      registerEntity("http://www.ibm.com/webservices/xsd/j2ee_web_services_1_1.xsd", "j2ee_web_services_1_1.xsd");
      registerEntity("http://www.ibm.com/webservices/xsd/j2ee_jaxrpc_mapping_1_1.xsd", "j2ee_jaxrpc_mapping_1_1.xsd");
      registerEntity("http://java.sun.com/xml/ns/javaee/javaee_web_services_client_1_2.xsd", "javaee_web_services_client_1_2.xsd");

      // ejb
      registerEntity("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", "ejb-jar_1_1.dtd");
      registerEntity("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN", "ejb-jar_2_0.dtd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/ejb-jar_2_1.xsd", "ejb-jar_2_1.xsd");
      // ejb3
      registerEntity("http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd", "ejb-jar_3_0.xsd");
      // jboss ejb2
      registerEntity("-//JBoss//DTD JBOSS//EN", "jboss.dtd");
      registerEntity("-//JBoss//DTD JBOSS 2.4//EN", "jboss_2_4.dtd");
      registerEntity("-//JBoss//DTD JBOSS 3.0//EN", "jboss_3_0.dtd");
      registerEntity("-//JBoss//DTD JBOSS 3.2//EN", "jboss_3_2.dtd");
      registerEntity("-//JBoss//DTD JBOSS 4.0//EN", "jboss_4_0.dtd");
      registerEntity("-//JBoss//DTD JBOSS 4.2//EN", "jboss_4_2.dtd");
      registerEntity("-//JBoss//DTD JBOSS 5.0//EN", "jboss_5_0.dtd");
      registerEntity("-//JBoss//DTD JBOSS 5.1.EAP//EN", "jboss_5_1_eap.dtd");
      registerEntity("-//JBoss//DTD JBOSS 6.0//EN", "jboss_6_0.dtd");
      registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 3.0//EN", "jbosscmp-jdbc_3_0.dtd");
      registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 3.2//EN", "jbosscmp-jdbc_3_2.dtd");
      registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 4.0//EN", "jbosscmp-jdbc_4_0.dtd");
      registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 4.2//EN", "jbosscmp-jdbc_4_2.dtd");
      // jboss ejb3
      registerEntity("http://www.jboss.org/j2ee/schema/jboss_5_0.xsd", "jboss_5_0.xsd");
      registerEntity("http://www.jboss.org/j2ee/schema/jboss_5_1.xsd", "jboss_5_1.xsd");
      
      // ear stuff
      registerEntity("-//Sun Microsystems, Inc.//DTD J2EE Application 1.2//EN", "application_1_2.dtd");
      registerEntity("-//Sun Microsystems, Inc.//DTD J2EE Application 1.3//EN", "application_1_3.dtd");
      registerEntity("-//Sun Microsystems, Inc.//DTD J2EE Application Client 1.3//EN", "application-client_1_3.dtd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/application_1_4.xsd", "application_1_4.xsd");
      registerEntity("http://java.sun.com/xml/ns/javaee/application_5.xsd", "application_5.xsd");
      // jboss-app
      registerEntity("-//JBoss//DTD J2EE Application 1.3//EN", "jboss-app_3_0.dtd");
      registerEntity("-//JBoss//DTD J2EE Application 1.3V2//EN", "jboss-app_3_2.dtd");
      registerEntity("-//JBoss//DTD J2EE Application 1.4//EN", "jboss-app_4_0.dtd");
      registerEntity("-//JBoss//DTD J2EE Application 4.2//EN", "jboss-app_4_2.dtd");
      registerEntity("-//JBoss//DTD Java EE Application 5.0//EN", "jboss-app_5_0.dtd");

      // connector descriptors
      registerEntity("-//Sun Microsystems, Inc.//DTD Connector 1.0//EN", "connector_1_0.dtd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/connector_1_5.xsd", "connector_1_5.xsd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/connector_1_6.xsd", "connector_1_6.xsd");
      // jboss-ds
      registerEntity("-//JBoss//DTD JBOSS JCA Config 1.0//EN", "jboss-ds_1_0.dtd");
      registerEntity("-//JBoss//DTD JBOSS JCA Config 1.5//EN", "jboss-ds_1_5.dtd");
      registerEntity("http://www.jboss.org/j2ee/schema/jboss-ds_5_0.xsd", "jboss-ds_5_0.xsd");
      // jboss-ra
      registerEntity("http://www.jboss.org/j2ee/schema/jboss-ra_1_0.xsd", "jboss-ra_1_0.xsd");

      // war meta-data
      registerEntity("-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN", "web-app_2_2.dtd");
      registerEntity("-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN", "web-app_2_3.dtd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd", "web-app_2_4.xsd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/web-app_2_5.xsd", "web-app_2_5.xsd");
      // jboss-web
      registerEntity("-//JBoss//DTD Web Application 2.2//EN", "jboss-web.dtd");
      registerEntity("-//JBoss//DTD Web Application 2.3//EN", "jboss-web_3_0.dtd");
      registerEntity("-//JBoss//DTD Web Application 2.3V2//EN", "jboss-web_3_2.dtd");
      registerEntity("-//JBoss//DTD Web Application 2.4//EN", "jboss-web_4_0.dtd");
      registerEntity("-//JBoss//DTD Web Application 4.2//EN", "jboss-web_4_2.dtd");      
      registerEntity("-//JBoss//DTD Web Application 5.0//EN", "jboss-web_5_0.dtd");
      registerEntity("http://www.jboss.org/j2ee/schema/jboss-web_5_1.xsd", "jboss-web_5_1.xsd");

      // application client
      registerEntity("http://java.sun.com/xml/ns/j2ee/application-client_1_4.xsd", "application-client_1_4.xsd");
      registerEntity("http://java.sun.com/xml/ns/javaee/application-client_5.xsd", "application-client_5.xsd");
      // jboss-client
      registerEntity("-//JBoss//DTD Application Client 3.2//EN", "jboss-client_3_2.dtd");
      registerEntity("-//JBoss//DTD Application Client 4.0//EN", "jboss-client_4_0.dtd");
      registerEntity("-//JBoss//DTD Application Client 4.2//EN", "jboss-client_4_2.dtd");      
      registerEntity("-//JBoss//DTD Application Client 5.0//EN", "jboss-client_5_0.dtd");
      registerEntity("http://www.jboss.org/j2ee/schema/jboss-client_5_1.xsd", "jboss-client_5_1.xsd");
      
      // jboss-specific
      registerEntity("-//JBoss//DTD Web Service Reference 4.0//EN", "service-ref_4_0.dtd");
      registerEntity("-//JBoss//DTD Web Service Reference 4.2//EN", "service-ref_4_2.dtd");   
      registerEntity("-//JBoss//DTD Web Service Reference 5.0//EN", "service-ref_5_0.dtd");       
      registerEntity("-//JBoss//DTD MBean Service 3.2//EN", "jboss-service_3_2.dtd");
      registerEntity("-//JBoss//DTD MBean Service 4.0//EN", "jboss-service_4_0.dtd");
      registerEntity("-//JBoss//DTD MBean Service 4.2//EN", "jboss-service_4_2.dtd");
      registerEntity("-//JBoss//DTD MBean Service 5.0//EN", "jboss-service_5_0.dtd");       
      registerEntity("-//JBoss//DTD JBOSS XMBEAN 1.0//EN", "jboss_xmbean_1_0.dtd");
      registerEntity("-//JBoss//DTD JBOSS XMBEAN 1.1//EN", "jboss_xmbean_1_1.dtd");
      registerEntity("-//JBoss//DTD JBOSS XMBEAN 1.2//EN", "jboss_xmbean_1_2.dtd");
      registerEntity("-//JBoss//DTD JBOSS Security Config 3.0//EN", "security_config.dtd");
      registerEntity("http://www.jboss.org/j2ee/schema/security-config_4_0.xsd", "security-config_4_0.xsd");
      registerEntity("urn:jboss:aop-deployer", "aop-deployer_1_1.xsd");
      registerEntity("urn:jboss:aop-beans:1.0", "aop-beans_1_0.xsd");
      registerEntity("urn:jboss:bean-deployer", "bean-deployer_1_0.xsd");
      registerEntity("urn:jboss:bean-deployer:2.0", "bean-deployer_2_0.xsd");
      registerEntity("urn:jboss:javabean:1.0", "javabean_1_0.xsd");
      registerEntity("urn:jboss:javabean:2.0", "javabean_2_0.xsd");
      registerEntity("urn:jboss:spring-beans:2.0", "mc-spring-beans_2_0.xsd");
      registerEntity("urn:jboss:policy:1.0", "policy_1_0.xsd");
      registerEntity("urn:jboss:osgi-beans:1.0", "osgi-beans_1_0.xsd");
      registerEntity("urn:jboss:seam-components:1.0", "seam-components_1_0.xsd");
      registerEntity("urn:jboss:security-config:4.1", "security-config_4_1.xsd");
      registerEntity("urn:jboss:security-config:5.0", "security-config_5_0.xsd");
      registerEntity("urn:jboss:jndi-binding-service:1.0", "jndi-binding-service_1_0.xsd");
      registerEntity("urn:jboss:user-roles:1.0", "user-roles_1_0.xsd");
   }

   /**
    Obtain a read-only view of the current entity map.

    @return Map<String, String> of the publicID/systemID to dtd/schema file name
    */
   public static Map getEntityMap()
   {
      return Collections.unmodifiableMap(entities);
   }

   public static boolean isWarnOnNonFileURLs()
   {
      return warnOnNonFileURLs;
   }

   public static void setWarnOnNonFileURLs(boolean warnOnNonFileURLs)
   {
      JBossEntityResolver.warnOnNonFileURLs = warnOnNonFileURLs;
   }

   /**
    * Register the mapping from the public id/system id to the dtd/xsd file
    * name. This overwrites any existing mapping.
    *
    * @param id  the DOCTYPE public id or system id such as
    * "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN"
    * @param dtdFileName the simple dtd/xsd file name, "ejb-jar.dtd"
    */
   public static void registerEntity(String id, String dtdFileName)
   {
      entities.put(id, dtdFileName);
   }

   
   public boolean isReplaceSystemProperties()
   {
      return replaceSystemProperties;
   }
   public void setReplaceSystemProperties(boolean replaceSystemProperties)
   {
      this.replaceSystemProperties = replaceSystemProperties;
   }

   /**
    * Register the mapping from the public id/system id to the dtd/xsd file
    * name. This overwrites any existing mapping.
    *
    * @param id  the DOCTYPE public id or system id such as
    * "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN"
    * @param dtdOrSchema the simple dtd/xsd file name, "ejb-jar.dtd"
    */
   public synchronized void registerLocalEntity(String id, String dtdOrSchema)
   {
      if( localEntities == null )
         localEntities = new ConcurrentHashMap();
      localEntities.put(id, dtdOrSchema);
   }

   /**
    Returns DTD/Schema inputSource. The resolution logic is:

    1. Check the publicId against the current registered values in the class
    mapping of entity name to dtd/schema file name. If found, the resulting
    file name is passed to the loadClasspathResource to locate the file as a
    classpath resource.

    2. Check the systemId against the current registered values in the class
    mapping of entity name to dtd/schema file name. If found, the resulting
    file name is passed to the loadClasspathResource to locate the file as a
    classpath resource.

    3. Strip the systemId name down to the simple file name by removing an URL
    style path elements (myschemas/x.dtd becomes x.dtd), and call
    loadClasspathResource to locate the simple file name as a classpath resource.

    4. Attempt to resolve the systemId as a URL from which the schema can be
    read. If the URL input stream can be opened this returned as the resolved
    input.

    @param publicId - Public ID of DTD, or null if it is a schema
    @param systemId - the system ID of DTD or Schema
    @return InputSource of entity
    */
   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
   {
      entityResolved = false;

      // nothing to resolve
      if( publicId == null && systemId == null )
         return null;

      boolean resolvePublicIdFirst = true;
      if(publicId != null && systemId != null)
      {
         String registeredSystemId = null;
         if(localEntities != null)
            registeredSystemId = (String) localEntities.get(publicId);
         if(registeredSystemId == null)
            registeredSystemId = (String) entities.get(publicId);
         
         if(registeredSystemId != null && !registeredSystemId.equals(systemId))
         {
            resolvePublicIdFirst = false;
            PicketBoxLogger.LOGGER.traceSystemIDMismatch(systemId, publicId, registeredSystemId);
         }
      }
      
      InputSource inputSource = null;
      
      if(resolvePublicIdFirst)
      {
         // Look for a registered publicID
         inputSource = resolvePublicID(publicId);
      }
      
      if( inputSource == null )
      {
         // Try to resolve the systemID from the registry
         inputSource = resolveSystemID(systemId);
      }

      if( inputSource == null )
      {
         // Try to resolve the systemID as a classpath reference under dtd or schema
         inputSource = resolveClasspathName(systemId);
      }

      if( inputSource == null )
      {
         // Try to resolve the systemID as a absolute URL
         inputSource = resolveSystemIDasURL(systemId);
      }

      entityResolved = (inputSource != null);
      
      if (entityResolved == false)
         PicketBoxLogger.LOGGER.debugFailureToResolveEntity(systemId, publicId);

      return inputSource;
   }

   /**
    * Returns the boolean value to inform id DTD was found in the XML file or not
    *
    * @todo this is not thread safe and should be removed?
    *
    * @return boolean - true if DTD was found in XML
    */
   public boolean isEntityResolved()
   {
      return entityResolved;
   }

   /**
    Load the schema from the class entity to schema file mapping.
    @see #registerEntity(String, String)

    @param publicId - the public entity name of the schema
    @return the InputSource for the schema file found on the classpath, null
      if the publicId is not registered or found.
    */
   protected InputSource resolvePublicID(String publicId)
   {
      if( publicId == null )
         return null;

      PicketBoxLogger.LOGGER.traceBeginResolvePublicID(publicId);

      InputSource inputSource = null;

      String filename = null;
      if( localEntities != null )
         filename = (String) localEntities.get(publicId);
      if( filename == null )
         filename = (String) entities.get(publicId);

      if( filename != null )
      {
         PicketBoxLogger.LOGGER.traceFoundEntityFromID("publicId", publicId, filename);

         InputStream ins = loadClasspathResource(filename);
         if( ins != null )
         {
            inputSource = new InputSource(ins);
            inputSource.setPublicId(publicId);
         }
         else
         {
            PicketBoxLogger.LOGGER.warnFailureToLoadIDFromResource("publicId", "classpath", filename);

            // Try the file name as a URI
            inputSource = resolveSystemIDasURL(filename);

            if (inputSource == null)
               PicketBoxLogger.LOGGER.warnFailureToLoadIDFromResource("publicId", "URL", filename);
         }
      }

      return inputSource;
   }

   /**
    Attempt to use the systemId as a URL from which the schema can be read. This
    checks to see whether the systemId is a key to an entry in the class
    entity map.

    @param systemId - the systemId
    @return the URL InputSource if the URL input stream can be opened, null
      if the systemId is not a URL or could not be opened.
    */
   protected InputSource resolveSystemID(String systemId)
   {
      if( systemId == null )
         return null;

      PicketBoxLogger.LOGGER.traceBeginResolveSystemID(systemId);

      InputSource inputSource = null;

      // Try to resolve the systemId as an entity key
      String filename = null;
      if( localEntities != null )
         filename = (String) localEntities.get(systemId);
      if( filename == null )
         filename = (String) entities.get(systemId);

      if ( filename != null )
      {
         PicketBoxLogger.LOGGER.traceFoundEntityFromID("systemId", systemId, filename);

         InputStream ins = loadClasspathResource(filename);
         if( ins != null )
         {
            inputSource = new InputSource(ins);
            inputSource.setSystemId(systemId);
         }
         else
         {
            PicketBoxLogger.LOGGER.warnFailureToLoadIDFromResource("systemId", "classpath", filename);
         }
      }

      return inputSource;
   }

   /**
   Attempt to use the systemId as a URL from which the schema can be read. This
   uses the systemID as a URL.

   @param systemId - the systemId
   @return the URL InputSource if the URL input stream can be opened, null
     if the systemId is not a URL or could not be opened.
   */
  protected InputSource resolveSystemIDasURL(String systemId)
  {
     if( systemId == null )
        return null;

     PicketBoxLogger.LOGGER.traceBeginResolveSystemIDasURL(systemId);

     InputSource inputSource = null;

     // Try to use the systemId as a URL to the schema
      try
      {
         // Replace any system property refs if isReplaceSystemProperties is true
         if(isReplaceSystemProperties())
            systemId = StringPropertyReplacer.replaceProperties(systemId);
         URL url = new URL(systemId);
         if (warnOnNonFileURLs && url.getProtocol().equalsIgnoreCase("file") == false)
         {
            PicketBoxLogger.LOGGER.warnResolvingSystemIdAsNonFileURL(systemId);
         }

         InputStream ins = url.openStream();
         if (ins != null)
         {
            inputSource = new InputSource(ins);
            inputSource.setSystemId(systemId);
         }         
         else
         {
            PicketBoxLogger.LOGGER.warnFailureToLoadIDFromResource("systemId", "URL", systemId);
         }
      }
      catch (MalformedURLException ignored)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(ignored);
      }
      catch (IOException e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      return inputSource;
  }

   /**
    Resolve the systemId as a classpath resource. If not found, the
    systemId is simply used as a classpath resource name.

    @param systemId - the system ID of DTD or Schema
    @return the InputSource for the schema file found on the classpath, null
      if the systemId is not registered or found.
    */
   protected InputSource resolveClasspathName(String systemId)
   {
      if( systemId == null )
         return null;

      PicketBoxLogger.LOGGER.traceBeginResolveClasspathName(systemId);
      String filename = systemId;
      // Parse the systemId as a uri to get the final path component
      try
      {
         URI url = new URI(systemId);
         String path = url.getPath();
         if( path == null )
            path = url.getSchemeSpecificPart();
         int slash = path.lastIndexOf('/');
         if( slash >= 0 )
            filename = path.substring(slash + 1);
         else
            filename = path;

         if(filename.length() == 0)
            return null;

         PicketBoxLogger.LOGGER.traceMappedSystemIdToFilename(filename);
      }
      catch (URISyntaxException e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }

      // Resolve the filename as a classpath resource
      InputStream is = loadClasspathResource(filename);
      InputSource inputSource = null;
      if( is != null )
      {
         inputSource = new InputSource(is);
         inputSource.setSystemId(systemId);
      }
      return inputSource;
   }

   /**
    Look for the resource name on the thread context loader resource path. This
    first simply tries the resource name as is, and if not found, the resource
    is prepended with either "dtd/" or "schema/" depending on whether the
    resource ends in ".dtd" or ".xsd".

    @param resource - the classpath resource name of the schema
    @return the resource InputStream if found, null if not found.
    */
   protected InputStream loadClasspathResource(String resource)
   {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      URL url = loader.getResource(resource);
      if( url == null )
      {
         /* Prefix the simple filename with the schema type patch as this is the
               naming convention for the jboss bundled schemas.
            */
         if( resource.endsWith(".dtd") )
            resource = "dtd/" + resource;
         else if( resource.endsWith(".xsd") )
            resource = "schema/" + resource;
         url = loader.getResource(resource);
      }

      InputStream inputStream = null;
      if( url != null )
      {
         PicketBoxLogger.LOGGER.traceMappedResourceToURL(resource, url);
         try
         {
            inputStream = url.openStream();
         }
         catch(IOException e)
         {
            PicketBoxLogger.LOGGER.debugIgnoredException(e);
         }
      }
      return inputStream;
   }

}
