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
package org.jboss.security;

import javax.security.auth.Subject;

/**
 * <p>
 * This interface represents a factory for {@code Subject}s. Implementations are responsible for creating and
 * populating a {@code Subject} object with the relevant information.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 * @author Anil.Saldhana@redhat.com
 */
public interface SubjectFactory
{

   /**
    * <p>
    * The {@code Subject} factory method. 
    * Implementations will construct the {@code Subject} object and insert all
    * relevant information like credentials, roles, etc into the created object.
    * 
    * <b>Note:</b> The Security Domain of "other" is assumed
    * </p>
    * 
    * @return a reference to the constructed {@code Subject} object.
    */
   public Subject createSubject();
   
   /**
    * Create a subject given the security domain name
    * @param securityDomain Name of the Security Domain
    * @return
    */
   public Subject createSubject(String securityDomain);
}
