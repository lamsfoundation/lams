/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security.audit;
 
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap; 
import java.util.Map;

/**
 *  Holder of audit information
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 *  @since  Aug 21, 2006
 */
public class AuditEvent
{
   private String auditLevel = AuditLevel.INFO;
   
   private Map<String,Object> contextMap = new HashMap<String,Object>();
   
   private Exception underlyingException = null;
   
   public AuditEvent(String level)
   {
      this.auditLevel = level;
   }
   
   public AuditEvent(String level, Map<String,Object> map)
   {
      this(level);
      this.contextMap = map;
   }
   
   public AuditEvent(String level, Map<String,Object> map, Exception ex)
   {
      this(level,map);
      this.underlyingException = ex;
   }
   
   /**
    * Return the Audit Level
    * @return
    */
   public String getAuditLevel()
   {
      return this.auditLevel;
   }
   
   /**
    * Get the Contextual Map
    * @return Map that is final 
    */
   public Map<String,Object> getContextMap()
   {
      return contextMap;
   }
   
   /**
    * Set a non-modifiable Context Map
    * @param cmap Map that is final
    */
   public void setContextMap(final Map<String,Object> cmap)
   {
      this.contextMap = cmap;
   }
   
   /**
    * Get the Exception part of the audit
    * @return
    */
   public Exception getUnderlyingException()
   {
      return underlyingException;
   }

   /**
    * Set the exception on which an audit is happening
    * @param underlyingException
    */
   public void setUnderlyingException(Exception underlyingException)
   {
      this.underlyingException = underlyingException;
   }

   public String toString()
   {
      StringBuilder sbu  = new StringBuilder();
      sbu.append("[").append(auditLevel).append("]");
      sbu.append(dissectContextMap());
      return sbu.toString();
   }
   
   /**
    * Provide additional information about the entities in
    * the context map
    * @return
    */
   @SuppressWarnings("unchecked")
   private String dissectContextMap()
   {
      StringBuilder sbu  = new StringBuilder(); 
      if(contextMap != null)
      {
         for(String key:contextMap.keySet())
         { 
            sbu.append(key).append("=");
            Object obj = contextMap.get(key);
            if(obj instanceof Object[])
            {
               Object[] arr = (Object[])obj;
               obj = Arrays.asList(arr); 
            } 
            if(obj instanceof Collection)
            {
               Collection<Object> coll = (Collection<Object>)obj;
               for(Object o:coll)
               {
                  sbu.append(o).append(";");
               }
            }
            else
               sbu.append(obj).append(";");
         } 
      }
      return sbu.toString();
   }
}