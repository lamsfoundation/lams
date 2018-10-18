/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.auth.spi;

/**
 * <p>
 * The {@code InputValidationException} is thrown by the {@code InputValidator}s to indicate that information supplied
 * by clients (e.g. username and password) is not valid (has unexpected tokens, doens't adhere to a pre-defined pattern,
 * etc).
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
@SuppressWarnings("serial")
public class InputValidationException extends Exception
{

   /**
    * <p>
    * Creates an instance of {@code InputValidationException}.
    * </p>
    */
   public InputValidationException()
   {
      super();
   }

   /**
    * <p>
    * Creates an instance of {@code InputValidationException} with the specified error message.
    * </p>
    * 
    * @param message a {@code String} representing the exception's message.
    */
   public InputValidationException(String message)
   {
      super(message);
   }

   /**
    * <p>
    * Creates an instance of {@code InputValidationException} with the specified message and cause.
    * </p>
    * 
    * @param message a {@code String} representing the exception's message.
    * @param cause a {@code Throwable} representing the cause of the exception, if available.
    */
   public InputValidationException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
