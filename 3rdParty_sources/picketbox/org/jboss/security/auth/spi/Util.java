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
package org.jboss.security.auth.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.security.auth.login.LoginException;

import org.jboss.crypto.digest.DigestCallback;
import org.jboss.security.Base64Encoder;
import org.jboss.security.Base64Utils;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SimpleGroup;

/**
 * Common login module utility methods
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class Util
{
   public static final String BASE64_ENCODING = "BASE64";
   public static final String BASE16_ENCODING = "HEX";
   public static final String RFC2617_ENCODING = "RFC2617";
   /**
    The ASCII printable characters the MD5 digest maps to for RFC2617
    */
   private static char[] MD5_HEX = "0123456789abcdef".toCharArray();

   
   
   /** Create the set of roles the user belongs to by parsing the roles.properties
    data for username=role1,role2,... and username.XXX=role1,role2,...
    patterns.
    * 
    * @param targetUser - the username to obtain roles for
    * @param roles - the Properties containing the user=roles mappings
    * @param roleGroupSeperator - the character that seperates a username
    *    from a group name, e.g., targetUser[.GroupName]=roles
    * @param aslm - the login module to use for Principal creation
    * @return Group[] containing the sets of roles
    */ 
   static Group[] getRoleSets(String targetUser, Properties roles, char roleGroupSeperator, AbstractServerLoginModule aslm)
   {
      Enumeration<?> users = roles.propertyNames();
      SimpleGroup rolesGroup = new SimpleGroup("Roles");
      ArrayList<Group> groups = new ArrayList<Group>();
      groups.add(rolesGroup);
      while (users.hasMoreElements() && targetUser != null)
      {
         String user = (String) users.nextElement();
         String value = roles.getProperty(user);

         // See if this entry is of the form targetUser[.GroupName]=roles
         //JBAS-3742 - skip potential '.' in targetUser
         int index = user.indexOf(roleGroupSeperator, targetUser.length());
         boolean isRoleGroup = false;
         boolean userMatch = false;
         if (index > 0 && targetUser.regionMatches(0, user, 0, index) == true)
            isRoleGroup = true;
         else
            userMatch = targetUser.equals(user);

         String groupName = "Roles";
          // Check for username.RoleGroup pattern
          if (isRoleGroup == true)
         {
            groupName = user.substring(index + 1);
            PicketBoxLogger.LOGGER.traceAdditionOfRoleToGroup(value, groupName);
             if (groupName.equals("Roles"))
            {
               parseGroupMembers(rolesGroup, value, aslm);
            }
            else
            {
               SimpleGroup group = new SimpleGroup(groupName);
               parseGroupMembers(group, value, aslm);
               groups.add(group);
            }
         }
         else if (userMatch == true)
         {
             PicketBoxLogger.LOGGER.traceAdditionOfRoleToGroup(value, groupName);
            // Place these roles into the Default "Roles" group
            parseGroupMembers(rolesGroup, value, aslm);
         }
      }
      Group[] roleSets = new Group[groups.size()];
      groups.toArray(roleSets);
      return roleSets;
   }

   /** Execute the rolesQuery against the dsJndiName to obtain the roles for
    the authenticated user.
     
    @return Group[] containing the sets of roles
    */
   static Group[] getRoleSets(String username, String dsJndiName, String txManagerJndiName,
      String rolesQuery, AbstractServerLoginModule aslm)
      throws LoginException
   {
      return getRoleSets(username, dsJndiName, txManagerJndiName, rolesQuery, aslm, false);
   }

   /** Execute the rolesQuery against the dsJndiName to obtain the roles for
    the authenticated user.
     
    @return Group[] containing the sets of roles
    */
   static Group[] getRoleSets(String username, String dsJndiName, String txManagerJndiName,
      String rolesQuery, AbstractServerLoginModule aslm, boolean suspendResume)
      throws LoginException
   {
      return DbUtil.getRoleSets(username, dsJndiName, txManagerJndiName, rolesQuery, aslm, suspendResume);
   }

   /** Utility method which loads the given properties file and returns a
    * Properties object containing the key,value pairs in that file.
    * The properties files should be in the class path as this method looks
    * to the thread context class loader (TCL) to locate the resource. If the
    * TCL is a URLClassLoader the findResource(String) method is first tried.
    * If this fails or the TCL is not a URLClassLoader getResource(String) is
    * tried.
    * @param defaultsName - the name of the default properties file resource
    *    that will be used as the default Properties to the ctor of the
    *    propertiesName Properties instance.
    * @param propertiesName - the name of the properties file resource
    * @return the loaded properties file if found
    * @exception java.io.IOException thrown if the properties file cannot be found
    *    or loaded 
    */
   static Properties loadProperties(String defaultsName, String propertiesName)
      throws IOException
   {
      Properties bundle = new Properties();
      ClassLoader loader = SecurityActions.getContextClassLoader();
      URL defaultUrl = null;
      URL url = null;
      // First check for local visibility via a URLClassLoader.findResource
      if( loader instanceof URLClassLoader )
      {
         URLClassLoader ucl = (URLClassLoader) loader;
         defaultUrl = SecurityActions.findResource(ucl,defaultsName);
         url = SecurityActions.findResource(ucl,propertiesName);
         PicketBoxLogger.LOGGER.traceAttemptToLoadResource(propertiesName);
      }
      // Do a general resource search
      if( defaultUrl == null ) {
         defaultUrl = loader.getResource(defaultsName);
         if (defaultUrl == null) {
            try {
               defaultUrl = new URL(defaultsName);
            } catch (MalformedURLException mue) {
               PicketBoxLogger.LOGGER.debugFailureToOpenPropertiesFromURL(mue);
               File tmp = new File(defaultsName);
               if (tmp.exists())
                  defaultUrl = tmp.toURI().toURL();
            }
         }
      }
      if( url == null ) {
         url = loader.getResource(propertiesName);
         if (url == null) {
            try {
               url = new URL(propertiesName);
            } catch (MalformedURLException mue) {
               PicketBoxLogger.LOGGER.debugFailureToOpenPropertiesFromURL(mue);
               File tmp = new File(propertiesName);
               if (tmp.exists())
                  url = tmp.toURI().toURL();
            }
         }
      }
      if( url == null && defaultUrl == null )
      {
         String propertiesFiles = propertiesName + "/" + defaultsName;
         throw PicketBoxMessages.MESSAGES.unableToFindPropertiesFile(propertiesFiles);
      }

      if (url != null) {
          InputStream is = null;
          try {
              is = SecurityActions.openStream(url);
          } catch (PrivilegedActionException e) {
              throw new IOException(e.getLocalizedMessage());
          }
          if (is != null) {
              try {
                  bundle.load(is);
              } finally {
                  safeClose(is);
              }
          } else {
              throw PicketBoxMessages.MESSAGES.unableToLoadPropertiesFile(propertiesName);
          }
          if (PicketBoxLogger.LOGGER.isTraceEnabled())
          {
             PicketBoxLogger.LOGGER.tracePropertiesFileLoaded(propertiesName, bundle.keySet());
          }
      } else {
          InputStream is = null;
          try {
              is = defaultUrl.openStream();
              bundle.load(is);
              if (PicketBoxLogger.LOGGER.isTraceEnabled())
              {
                 PicketBoxLogger.LOGGER.tracePropertiesFileLoaded(defaultsName, bundle.keySet());
              }
          } catch (Throwable e) {
              PicketBoxLogger.LOGGER.debugFailureToLoadPropertiesFile(defaultsName, e);
          } finally {
              safeClose(is);
          }
      }

      return bundle;
   }

   /** Utility method which loads the given properties file and returns a
    * Properties object containing the key,value pairs in that file.
    * The properties files should be in the class path as this method looks
    * to the thread context class loader (TCL) to locate the resource. If the
    * TCL is a URLClassLoader the findResource(String) method is first tried.
    * If this fails or the TCL is not a URLClassLoader getResource(String) is
    * tried. If not, an absolute path is tried.
    * @param propertiesName - the name of the properties file resource
    * @return the loaded properties file if found
    * @exception java.io.IOException thrown if the properties file cannot be found
    *    or loaded 
    */
   static Properties loadProperties(String propertiesName) throws IOException
   {
      Properties bundle = null;
      ClassLoader loader = SecurityActions.getContextClassLoader();
      URL url = null;
      // First check for local visibility via a URLClassLoader.findResource
      if (loader instanceof URLClassLoader)
      {
         URLClassLoader ucl = (URLClassLoader) loader;
         url = SecurityActions.findResource(ucl, propertiesName);
         PicketBoxLogger.LOGGER.traceAttemptToLoadResource(propertiesName);
      }
      // Do a general resource search
      if (url == null)
      {
         url = loader.getResource(propertiesName);
         if (url == null)
         {
            try
            {
               url = new URL(propertiesName);
            }
            catch (MalformedURLException mue)
            {
               PicketBoxLogger.LOGGER.debugFailureToOpenPropertiesFromURL(mue);
               File tmp = new File(propertiesName);
               if (tmp.exists())
                  url = tmp.toURI().toURL();
            }
         }
      }
      if (url == null)
      {
         throw PicketBoxMessages.MESSAGES.unableToFindPropertiesFile(propertiesName);
      }

      Properties defaults = new Properties();
      bundle = new Properties(defaults);
      if (url != null)
      {
         InputStream is = null;
         try
         {
            is = SecurityActions.openStream(url);
         }
         catch (PrivilegedActionException e)
         {
            throw new IOException(e.getLocalizedMessage());
         }
         if (is != null)
         {
            try
            {
               bundle.load(is);
            }
            finally
            {
               safeClose(is);
            }
         }
         else
         {
            throw PicketBoxMessages.MESSAGES.unableToLoadPropertiesFile(propertiesName);
         }
         if (PicketBoxLogger.LOGGER.isTraceEnabled())
         {
            PicketBoxLogger.LOGGER.tracePropertiesFileLoaded(propertiesName, bundle.keySet());
         }
      }

      return bundle;
   }


   /** Parse the comma delimited roles names given by value and add them to
    * group. The type of Principal created for each name is determined by
    * the createIdentity method.
    *
    * @see AbstractServerLoginModule#createIdentity(String)
    * 
    * @param group - the Group to add the roles to.
    * @param roles - the comma delimited role names.
    */ 
   static void parseGroupMembers(Group group, String roles, AbstractServerLoginModule aslm)
   {
      StringTokenizer tokenizer = new StringTokenizer(roles, ",");
      while (tokenizer.hasMoreTokens())
      {
         String token = tokenizer.nextToken();
         try
         {
            Principal p = aslm.createIdentity(token);
            group.addMember(p);
         }
         catch (Exception e)
         {
            PicketBoxLogger.LOGGER.debugFailureToCreatePrincipal(token, e);
         }
      }
   }
   
   /**
    * Calculate a password hash using a MessageDigest.
    *
    * @param hashAlgorithm - the MessageDigest algorithm name
    * @param hashEncoding - either base64 or hex to specify the type of
       encoding the MessageDigest as a string.
    * @param hashCharset - the charset used to create the byte[] passed to the
    *  MessageDigestfrom the password String. If null the platform default is
    *  used.
    * @param username - ignored in default version
    * @param password - the password string to be hashed
    * @return the hashed string if successful, null if there is a digest exception
    */
    public static String createPasswordHash(String hashAlgorithm, String hashEncoding,
       String hashCharset, String username, String password)
    {
      return createPasswordHash(hashAlgorithm, hashEncoding,
       hashCharset, username, password, null);
    }

    /**
     * Calculate a password hash using a MessageDigest.
     *
     * @param hashAlgorithm - the MessageDigest algorithm name
     * @param hashEncoding - either base64 or hex to specify the type of
        encoding the MessageDigest as a string.
     * @param hashCharset - the charset used to create the byte[] passed to the
     *  MessageDigestfrom the password String. If null the platform default is
     *  used.
     * @param username - ignored in default version
     * @param password - the password string to be hashed
     * @param callback - the callback used to allow customization of the hash
     *    to occur. The preDigest method is called before the password is added
     *    and the postDigest method is called after the password has been added.
     * @return the hashed string if successful, null if there is a digest exception
     */ 
    public static String createPasswordHash(String hashAlgorithm, String hashEncoding,
       String hashCharset, String username, String password, DigestCallback callback)
    {
       byte[] passBytes;
       String passwordHash = null;

       // convert password to byte data
       try
       {
          if(hashCharset == null)
             passBytes = password.getBytes();
          else
             passBytes = password.getBytes(hashCharset);
       }
       catch(UnsupportedEncodingException uee)
       {
          PicketBoxLogger.LOGGER.errorFindingCharset(hashCharset, uee);
          passBytes = password.getBytes();
       }

       // calculate the hash and apply the encoding.
       try
       {
          MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
          if( callback != null )
             callback.preDigest(md);
          md.update(passBytes);
          if( callback != null )
             callback.postDigest(md);
          byte[] hash = md.digest();
          if(hashEncoding.equalsIgnoreCase(BASE64_ENCODING))
          {
             passwordHash = Util.encodeBase64(hash);
          }
          else if(hashEncoding.equalsIgnoreCase(BASE16_ENCODING))
          {
             passwordHash = Util.encodeBase16(hash);
          }
          else if(hashEncoding.equalsIgnoreCase(RFC2617_ENCODING))
          {
             passwordHash = Util.encodeRFC2617(hash);
          }
          else
          {
             PicketBoxLogger.LOGGER.unsupportedHashEncodingFormat(hashEncoding);
          }
       }
       catch(Exception e)
       {
          PicketBoxLogger.LOGGER.errorCalculatingPasswordHash(e);
       }
       return passwordHash;
    }
    
    /**
    3.1.3 Representation of digest values

    An optional header allows the server to specify the algorithm used to create
    the checksum or digest. By default the MD5 algorithm is used and that is the
    only algorithm described in this document.

    For the purposes of this document, an MD5 digest of 128 bits is represented
    as 32 ASCII printable characters. The bits in the 128 bit digest are
    converted from most significant to least significant bit, four bits at a time
    to their ASCII presentation as follows. Each four bits is represented by its
    familiar hexadecimal notation from the characters 0123456789abcdef. That is,
    binary 0000 getInfos represented by the character '0', 0001, by '1', and so
    on up to the representation of 1111 as 'f'.
    
    @param data - the raw MD5 hash data
    @return the encoded MD5 representation
    */
   public static String encodeRFC2617(byte[] data)
   {
      char[] hash = new char[32];
      for (int i = 0; i < 16; i++)
      {
         int j = (data[i] >> 4) & 0xf;
         hash[i * 2] = MD5_HEX[j];
         j = data[i] & 0xf;
         hash[i * 2 + 1] = MD5_HEX[j];
      }
      return new String(hash);
   } 
   
    /**
     * Hex encoding of hashes, as used by Catalina. Each byte is converted to
     * the corresponding two hex characters.
     */
    public static String encodeBase16(byte[] bytes)
    {
       StringBuffer sb = new StringBuffer(bytes.length * 2);
       for (int i = 0; i < bytes.length; i++)
       {
          byte b = bytes[i];
          // top 4 bits
          char c = (char)((b >> 4) & 0xf);
          if(c > 9)
             c = (char)((c - 10) + 'a');
          else
             c = (char)(c + '0');
          sb.append(c);
          // bottom 4 bits
          c = (char)(b & 0xf);
          if (c > 9)
             c = (char)((c - 10) + 'a');
          else
             c = (char)(c + '0');
          sb.append(c);
       }
       return sb.toString();
    }

    /**
     * BASE64 encoder implementation.
     * Provides encoding methods, using the BASE64 encoding rules, as defined
     * in the MIME specification, <a href="http://ietf.org/rfc/rfc1521.txt">rfc1521</a>.
     */
    public static String encodeBase64(byte[] bytes)
    {
       String base64 = null;
       try
       {
          base64 = Base64Encoder.encode(bytes);
       }
       catch(Exception e)
       {
       }
       return base64;
    }
    
    // These functions assume that the byte array has MSB at 0, LSB at end.
    // Reverse the byte array (not the String) if this is not the case.
    // All base64 strings are in natural order, least significant digit last.
    public static String tob64(byte[] buffer)
    {
       return Base64Utils.tob64(buffer);  
    }

    public static byte[] fromb64(String str) throws NumberFormatException
    {
       return Base64Utils.fromb64(str); 
    }
    
    private static void safeClose(InputStream fis)
    {
       try
       {
          if(fis != null)
          {
             fis.close();
          }
       }
       catch(Exception e)
       {}
    }
}