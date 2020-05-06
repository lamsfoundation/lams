/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security.audit.providers;
 
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.audit.AbstractAuditProvider;
import org.jboss.security.audit.AuditEvent;

/**
 *  Audit Provider that just logs the audit event using a Logger.
 *  The flexibility of passing the audit log entries to a different
 *  sink (database, jms queue, file etc) can be controlled in the
 *  logging configuration (Eg: log4j.xml in log4j)
 *  <p>
 *  Ensure that the appender is configured properly in the 
 *  global log4j.xml for log entries to go to a log, separate
 *  from the regular server logs.
 *  </p>
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 *  @since  Aug 21, 2006
 */
public class LogAuditProvider extends AbstractAuditProvider
{ 

   public void audit(AuditEvent auditEvent)
   {
      if(!PicketBoxLogger.AUDIT_LOGGER.isTraceEnabled())
      {
          return;
      }
      Exception e = auditEvent.getUnderlyingException();
      if(e != null)
      {
          PicketBoxLogger.AUDIT_LOGGER.trace(auditEvent, e);
      }
      else
      {
          PicketBoxLogger.AUDIT_LOGGER.trace(auditEvent);
      }
   } 
}
