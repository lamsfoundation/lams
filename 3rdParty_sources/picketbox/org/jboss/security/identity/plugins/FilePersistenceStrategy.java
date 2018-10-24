/*
  * JBoss, Home of Professional Open Source
  * Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.security.identity.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.jboss.security.identity.Identity;

/**
 * An implementation of <code>PersistenceStrategy</code> that serializes the 
 * <code>Identity</code> to a file.
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 * @version $Revision: 1.1 $
 */
public class FilePersistenceStrategy implements PersistenceStrategy
{
   private String path;

   /**
    * Create a new FilePersistenceStrategy.
    * 
    * @param path directory where the files will be stored.
    */
   public FilePersistenceStrategy(String path)
   {
      this.path = path;
   }

   /**
    * @see PersistenceStrategy#persistIdentity(Identity).
    */
   public Identity persistIdentity(Identity identity)
   {
      ObjectOutputStream oos = null;
      FileOutputStream fos = null;
      try
      {
         File file = new File(path + File.separator + identity.getName());
         if (file.exists())
         {
            // identity already exists
            return null;
         }
         fos = new FileOutputStream(file);
         oos = new ObjectOutputStream(fos);
         oos.writeObject(identity);
         return identity;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
      finally
      {
         safeClose(oos);
         safeClose(fos);
      }
   }

   /**
    * @see PersistenceStrategy#getIdentity(String).
    */
   public Identity getIdentity(String name)
   {
      ObjectInputStream ois = null;
      FileInputStream fis = null;
      try
      {
         fis = new FileInputStream(path + File.separator + name);
         ois = new ObjectInputStream(fis);
         Identity identity = (Identity) ois.readObject();
         return identity;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
      finally
      {
         safeClose(ois);
         safeClose(fis);
      }
   }

   /**
    * @see PersistenceStrategy#removeIdentity(Identity).
    */
   public boolean removeIdentity(Identity identity)
   {
      File file = new File(path + File.separator + identity.getName());

      return file.delete();
   }

   /**
    * @see PersistenceStrategy#updateIdentity(Identity).
    */
   public Identity updateIdentity(Identity identity)
   {
      if (removeIdentity(identity))
      {
         return persistIdentity(identity);
      }

      return null;
   }

   private void safeClose(InputStream fis)
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

   private void safeClose(OutputStream os)
   {
      try
      {
         if(os != null)
         {
            os.close();
         }
      }
      catch(Exception e)
      {}
   }
}