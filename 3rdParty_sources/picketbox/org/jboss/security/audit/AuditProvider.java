/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security.audit;

/**
 *  Audit Provider that can log audit events to an external
 *  sink
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 *  @since  Aug 21, 2006
 */
public interface AuditProvider
{
   /**
    * Perform an audit of the event passed
    * A provider can log the audit as per needs.
    * @param ae audit event that holds information on the audit
    * @see AuditEvent
    */
  public void audit(AuditEvent ae);
}
