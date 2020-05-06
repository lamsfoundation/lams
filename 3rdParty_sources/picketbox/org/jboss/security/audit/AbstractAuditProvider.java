/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security.audit;

/**
 *  Abstract class of Audit Providers.
 *  <p>An audit provider is one that can be used to log audit events in 
 *  a file, a database or any external sink of choice</p>
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 *  @since  Aug 21, 2006
 */
public abstract class AbstractAuditProvider implements AuditProvider
{
   /** 
    * @see AuditProvider#audit(AuditEvent)
    */
   public abstract void audit(AuditEvent auditEvent);
}
