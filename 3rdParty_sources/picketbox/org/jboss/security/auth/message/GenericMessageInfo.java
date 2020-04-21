/*
  * JBoss, Home of Professional Open Source
  * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.security.auth.message;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.MessageInfo;
 

/**
 *  Generic MessageInfo
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  May 25, 2006 
 *  @version $Revision: 45271 $
 */
public class GenericMessageInfo implements MessageInfo
{ 
   /** The serialVersionUID */
   private static final long serialVersionUID = -8148794884261757664L;

   protected Object request = null;
   protected Object response = null;

   private Map<Object,Object> map = new HashMap<Object,Object>(); 

   public GenericMessageInfo()
   {   
   }
   
   
   public GenericMessageInfo(Object request, Object response)
   { 
      this.request = request;
      this.response = response;
   }


   /**
    * @see MessageInfo#getRequestMessage()
    */
   public Object getRequestMessage()
   { 
      return request;
   }

   /**
    * @see MessageInfo#getResponseMessage()
    */
   public Object getResponseMessage()
   { 
      return response;
   }
   
   /**
    * @see MessageInfo#setRequestMessage(Object)
    */
   public void setRequestMessage(Object request)
   { 
      this.request = request;
   }

   /**
    * @see MessageInfo#setResponseMessage(Object)
    */
   public void setResponseMessage(Object response)
   { 
      this.response = response;
   }


   public Map<Object,Object> getMap()
   { 
      return this.map ;
   } 
}
