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
package org.jboss.cache.loader;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;
import org.jboss.cache.lock.StripedLock;
import org.jboss.util.stream.MarshalledValueInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple file-based CacheLoader implementation. Nodes are directories, attributes of a node is a file in the directory
 * <p/>
 * The FileCacheLoader has some severe limitations which restrict its use in a production
 * environment, or if used in such an environment, it should be used with due care and sufficient
 * understanding of these limitations.
 * <ul>
 * <li>Due to the way the FileCacheLoader represents a tree structure on disk (directories and files) traversal is inefficient for deep trees.</li>
 * <li>Usage on shared filesystems like NFS, Windows shares, etc. should be avoided as these do not implement proper file locking and can cause data corruption.</li>
 * <li>Usage with an isolation level of NONE can cause corrupt writes as multiple threads attempt to write to the same file.</li>
 * <li>File systems are inherently not transactional, so when attempting to use your cache in a transactional context, failures when writing to the file (which happens during the commit phase) cannot be recovered.</li>
 * </ul>
 * <p/>
 * As a rule of thumb, it is recommended that the FileCacheLoader not be used in a highly concurrent,
 * transactional or stressful environment, and its use is restricted to testing.
 * <p/>
 * In terms of concurrency, file systems are notoriously inconsistent in their implementations of concurrent locks.  To get around
 * this and to meet the <b>thread safety</b> contracts set out in {@link CacheLoader}, this implementation uses a {@link org.jboss.cache.lock.StripedLock}
 *
 * @author Bela Ban
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 *
 */
@ThreadSafe
public class FileCacheLoader extends AbstractCacheLoader
{
   File root = null;
   String rootPath = null;
   private static final Log log = LogFactory.getLog(FileCacheLoader.class);

   protected final StripedLock lock = new StripedLock();

   private FileCacheLoaderConfig config;

   /**
    * CacheImpl data file.
    */
   public static final String DATA = "data.dat";

   /**
    * CacheImpl directory suffix.
    */
   public static final String DIR_SUFFIX = "fdb";

   /**
    * For full path, check '*' '<' '>' '|' '"' '?' Regex: [\*<>|"?]
    */
   public static final Pattern PATH_PATTERN = Pattern.compile("[\\*<>|\"?]");

   /**
    * For fqn, check '*' '<' '>' '|' '"' '?' and also '\' '/' and ':'
    */
   public static final Pattern FQN_PATTERN = Pattern.compile("[\\\\\\/:*<>|\"?]");
   private static boolean isOldWindows;

   static
   {
      float osVersion = -1;
      try
      {
         osVersion = Float.parseFloat(System.getProperty("os.version").trim());
      }
      catch (Exception e)
      {
         if (log.isTraceEnabled()) log.trace("Unable to determine operating system version!");
      }
      // 4.x is windows NT/2000 and 5.x is XP.
      isOldWindows = System.getProperty("os.name").toLowerCase().startsWith("windows") && osVersion < 4;
   }

   public void setConfig(IndividualCacheLoaderConfig base)
   {
      if (base instanceof FileCacheLoaderConfig)
      {
         this.config = (FileCacheLoaderConfig) base;
      }
      else if (base != null)
      {
         this.config = new FileCacheLoaderConfig(base);
      }
   }

   public IndividualCacheLoaderConfig getConfig()
   {
      return config;
   }

   @Override
   public void create() throws Exception
   {
      lock.acquireLock(Fqn.ROOT, true);
      String location = this.config != null ? this.config.getLocation() : null;
      if (location != null && location.length() > 0)
      {
         root = new File(location);
         rootPath = root.getAbsolutePath() + File.separator;
      }
      try
      {
         if (root == null)
         {
            String tmpLocation = System.getProperty("java.io.tmpdir", "C:\\tmp");
            root = new File(tmpLocation);
            rootPath = root.getAbsolutePath() + File.separator;
         }
         if (!root.exists())
         {
            if (log.isTraceEnabled())
            {
               log.trace("Creating cache loader location " + root);
            }

            if (config.isCheckCharacterPortability())
            {
               /* Before creating the root, check whether the path is character portable. Anything that comes after is part
                  of the fqn which is inspected later. */
               isCharacterPortableLocation(root.getAbsolutePath());
            }

            boolean created = root.mkdirs();
            if (!created)
            {
               throw new IOException("Unable to create cache loader location " + root);
            }
         }

         if (!root.isDirectory())
         {
            throw new IOException("Cache loader location [" + root + "] is not a directory!");
         }
      }
      finally
      {
         lock.releaseLock(Fqn.ROOT);
      }
   }

   public Set<String> getChildrenNames(Fqn fqn) throws Exception
   {
      lock.acquireLock(fqn, true);
      try
      {
         File parent = getDirectory(fqn, false);
         if (parent == null) return null;
         File[] children = parent.listFiles();
         Set<String> s = new HashSet<String>();
         for (File child : children)
         {
            if (child.isDirectory() && child.getName().endsWith(DIR_SUFFIX))
            {
               String child_name = child.getName();
               child_name = child_name.substring(0, child_name.lastIndexOf(DIR_SUFFIX) - 1);
               s.add(child_name);
            }
         }
         return s.size() == 0 ? null : s;
      }
      finally
      {
         lock.releaseLock(fqn);
      }
   }

   public Map get(Fqn fqn) throws Exception
   {
      lock.acquireLock(fqn, true);
      try
      {
         return loadAttributes(fqn);
      }
      finally
      {
         lock.releaseLock(fqn);
      }
   }

   public boolean exists(Fqn fqn) throws Exception
   {
      lock.acquireLock(fqn, true);
      try
      {
         File f = getDirectory(fqn, false);
         return f != null;
      }
      finally
      {
         lock.releaseLock(fqn);
      }
   }

   public Object put(Fqn fqn, Object key, Object value) throws Exception
   {
      lock.acquireLock(fqn, true);
      try
      {
         Object retval;
         Map m = loadAttributes(fqn);
         if (m == null) m = new HashMap();
         retval = m.put(key, value);
         storeAttributes(fqn, m);
         return retval;
      }
      finally
      {
         lock.releaseLock(fqn);
      }
   }

   public void put(Fqn fqn, Map attributes) throws Exception
   {
      put(fqn, attributes, true);
   }


   @Override
   public void put(Fqn fqn, Map attributes, boolean erase) throws Exception
   {
      lock.acquireLock(fqn, true);
      try
      {
         Map m = erase ? new HashMap() : loadAttributes(fqn);
         if (m == null) m = new HashMap();
         if (attributes != null)
         {
            m.putAll(attributes);
         }
         storeAttributes(fqn, m);
      }
      finally
      {
         lock.releaseLock(fqn);
      }
   }

   void put(Fqn fqn) throws Exception
   {
      getDirectory(fqn, true);
   }

   public Object remove(Fqn fqn, Object key) throws Exception
   {
      lock.acquireLock(fqn, true);
      try
      {
         Object retval;
         Map m = loadAttributes(fqn);
         if (m == null) return null;
         retval = m.remove(key);
         storeAttributes(fqn, m);
         return retval;
      }
      finally
      {
         lock.releaseLock(fqn);
      }
   }

   public void remove(Fqn fqn) throws Exception
   {
      lock.acquireLock(fqn, true);
      try
      {
         File dir = getDirectory(fqn, false);
         if (dir != null)
         {
            boolean flag = removeDirectory(dir, true);
            if (!flag)
            {
               log.warn("failed removing " + fqn);
            }
         }
      }
      finally
      {
         lock.releaseLock(fqn);
      }
   }

   public void removeData(Fqn fqn) throws Exception
   {
      lock.acquireLock(fqn, true);
      try
      {
         File f = getDirectory(fqn, false);
         if (f != null)
         {
            File data = new File(f, DATA);
            if (data.exists())
            {
               boolean flag = data.delete();
               if (!flag)
               {
                  log.warn("failed removing file " + data.getName());
               }
            }
         }
      }
      finally
      {
         lock.releaseLock(fqn);
      }
   }

   /* ----------------------- Private methods ------------------------ */

   File getDirectory(Fqn fqn, boolean create) throws IOException
   {
      File f = new File(getFullPath(fqn));
      if (!f.exists())
      {
         if (create)
         {
            boolean make = f.mkdirs();
            if (!make)
               throw new IOException("Unable to mkdirs " + f);
         }
         else
         {
            return null;
         }
      }
      return f;
   }


   /**
    * Recursively removes this and all subdirectories, plus all DATA files in them. To prevent damage, we only
    * remove files that are named DATA (data.dat) and directories which end in ".fdb". If there is a dir or file
    * that isn't named this way, the recursive removal will fail
    *
    * @return <code>true</code> if directory was removed,
    *         <code>false</code> if not.
    */
   boolean removeDirectory(File dir, boolean include_start_dir)
   {
      boolean success = true;
      File[] subdirs = dir.listFiles();
      if (subdirs == null)
      {
         if (log.isWarnEnabled()) log.warn("Null list of files for dir: " + dir.getAbsolutePath());
         return false;
      }
      for (File file : subdirs)
      {
         if (file.isFile() && file.getName().equals(DATA))
         {
            if (!file.delete())
            {
               success = false;
            }
            continue;
         }
         if (file.isDirectory() && file.getName().endsWith(DIR_SUFFIX))
         {
            if (!removeDirectory(file, false))
            {
               success = false;
            }
            if (!file.delete())
            {
               success = false;
            }
         }
      }

      if (include_start_dir && !dir.equals(root))
      {
         if (dir.delete())
         {
            return success;
         }
         success = false;
      }

      return success;
   }

   String getFullPath(Fqn fqn)
   {
      StringBuilder sb = new StringBuilder(rootPath);
      for (int i = 0; i < fqn.size(); i++)
      {
         Object tmp = fqn.get(i);
         // This is where we convert from Object to String!
         String tmp_dir = tmp.toString(); // returns tmp.this if it's a String         
         sb.append(tmp_dir).append(".").append(DIR_SUFFIX).append(File.separator);
      }
      return sb.toString();
   }

   protected Map loadAttributes(Fqn fqn) throws Exception
   {
      File f = getDirectory(fqn, false);
      if (f == null) return null; // i.e., this node does not exist.
      // this node exists so we should never return a null after this... at worst case, an empty HashMap.
      File child = new File(f, DATA);
      if (!child.exists()) return new HashMap(0); // no node attribs exist hence the empty HashMap.
      //if(!child.exists()) return null;

      Map m;
      try
      {
         //m = (Map) unmarshall(child);
         m = (Map) regionAwareUnmarshall(fqn, child);
      }
      catch (FileNotFoundException fnfe)
      {
         // child no longer exists!
         m = Collections.emptyMap();
      }
      return m;
   }

   protected void storeAttributes(Fqn fqn, Map attrs) throws Exception
   {
      if (attrs != null && !(attrs instanceof HashMap))
         throw new RuntimeException("Unsupporte dmap type " + attrs.getClass());
      regionAwareMarshall(fqn, attrs);
   }

   @Override
   protected void doMarshall(Fqn fqn, Object toMarshall) throws Exception
   {
      Map attrs = (Map) toMarshall;

      if (attrs != null && !(attrs instanceof HashMap)) throw new RuntimeException("Map is " + attrs.getClass());

      File f = getDirectory(fqn, true);
      File child = new File(f, DATA);
      if (!child.exists())
      {
         if (config.isCheckCharacterPortability())
         {
            /* Check whether the entire file path (root + fqn + data file name), is length portable */
            isLengthPortablePath(child.getAbsolutePath());
            /* Check whether the fqn tree we're trying to store could contain non portable characters */
            isCharacterPortableTree(fqn);
         }

         if (!child.createNewFile())
         {
            throw new IOException("Unable to create file: " + child);
         }
      }
      FileOutputStream fileOut = new FileOutputStream(child);
      ObjectOutputStream output = new ObjectOutputStream(fileOut);
      getMarshaller().objectToObjectStream(attrs, output);
      output.close();
   }

   @Override
   protected Object doUnmarshall(Fqn fqn, Object fromFile) throws Exception
   {
      FileInputStream fileIn = new FileInputStream((File) fromFile);
      ObjectInputStream input = new MarshalledValueInputStream(fileIn);
      Object unmarshalledObj = getMarshaller().objectFromObjectStream(input);
      input.close();
      return unmarshalledObj;
   }


   protected boolean isCharacterPortableLocation(String fileAbsolutePath)
   {
      Matcher matcher = PATH_PATTERN.matcher(fileAbsolutePath);
      if (matcher.find())
      {
         log.warn("Cache loader location ( " + fileAbsolutePath + " ) contains one of these characters: '*' '<' '>' '|' '\"' '?'");
         log.warn("Directories containing these characters are illegal in some operative systems and could lead to portability issues");
         return false;
      }

      return true;
   }

   protected boolean isCharacterPortableTree(Fqn fqn)
   {
      List elements = fqn.peekElements();
      // Don't assume the Fqn is composed of Strings!!
      for (Object anElement : elements)
      {
         // getFullPath converts Object to String via toString(), so we do too
         Matcher matcher = FQN_PATTERN.matcher(anElement.toString());
         if (matcher.find())
         {
            log.warn("One of the Fqn ( " + fqn + " ) elements contains one of these characters: '*' '<' '>' '|' '\"' '?' '\\' '/' ':' ");
            log.warn("Directories containing these characters are illegal in some operating systems and could lead to portability issues");
            return false;
         }
      }

      return true;
   }

   protected boolean isLengthPortablePath(String absoluteFqnPath)
   {

      if (isOldWindows && absoluteFqnPath.length() > 255)
      {
         log.warn("The full absolute path to the fqn that you are trying to store is bigger than 255 characters, this could lead to problems on certain Windows systems: " + absoluteFqnPath);
         return false;
      }

      return true;
   }
}
