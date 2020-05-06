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
package org.jboss.security.plugins;

import java.lang.reflect.Method;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;

import org.jboss.logging.Logger;
import org.jboss.security.PicketBoxLogger;

/**
 *  Locate a Transaction Manager
 *  @author Anil.Saldhana@redhat.com
 *  @since  May 13, 2007 
 *  @version $Revision$
 */
public class TransactionManagerLocator
{
   private static Logger log = Logger.getLogger(TransactionManagerLocator.class);
   private boolean trace = log.isTraceEnabled();
   private static TransactionManager transactionManager;
   
   public TransactionManagerLocator()
   {   
   }
   
   /**
    * Get the TransactionManager provided a JNDI Name for the
    * Transaction Manager <br/>
    * Note: If the TM is not bound to JNDI, an attempt is made
    * to obtain the JBoss TxManager instance via reflection
    * @param jndiName
    * @return
    * @throws NamingException
    */
   public TransactionManager getTM(String jndiName) throws NamingException
   {
      TransactionManager tm = null;
      InitialContext ctx = new InitialContext();
      try
      { 
         tm = (TransactionManager) ctx.lookup(jndiName);
      }
      catch(NameNotFoundException nfe)
      {
         try
         {
            tm = this.getJBossTM();
         }
         catch (Exception ignore)
         {
            PicketBoxLogger.LOGGER.debugIgnoredException(ignore);
            if (transactionManager != null)
               tm = transactionManager;
         }
      } 
      return tm;
   } 
   
   private TransactionManager getJBossTM() throws Exception
   {
      ClassLoader tcl = SubjectActions.getContextClassLoader();
      Class<?> clz = tcl.loadClass("org.jboss.tm.TransactionManagerLocator");
      Method m = clz.getMethod("locate", new Class[]{});
      return (TransactionManager) m.invoke(null, new Object[0]); 
   }
   
   public static void setTransactionManager(TransactionManager transactionManager)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(TransactionManagerLocator.class.getName() + ".setTransactionManager"));
      }
      TransactionManagerLocator.transactionManager = transactionManager;
   }
}