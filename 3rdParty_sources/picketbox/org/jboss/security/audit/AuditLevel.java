/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security.audit;

/**
 *  Define the Audit Levels of Severity
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 *  @since  Aug 21, 2006
 */
public interface AuditLevel
{
   /** Denotes situations where there has been a server exception */
  String ERROR = "Error";
  
  /** Denotes situations when there has been a failed attempt */
  String FAILURE = "Failure";
  
  String SUCCESS = "Success";
  
  /** Just some info being passed into the audit logs */
  String INFO = "Info";
}
