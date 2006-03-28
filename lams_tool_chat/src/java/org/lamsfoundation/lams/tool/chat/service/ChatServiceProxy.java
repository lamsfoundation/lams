/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */

/* $Id$ */ 
package org.lamsfoundation.lams.tool.chat.service;

import javax.servlet.ServletContext;

import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * <p>This class act as the proxy between web layer and service layer. It is
 * designed to decouple the presentation logic and business logic completely.
 * In this way, the presentation tier will no longer be aware of the changes in
 * service layer. Therefore we can feel free to switch the business logic
 * implementation.</p>
 */

public class ChatServiceProxy {

	public static final IChatService getChatService(ServletContext servletContext)
    {
        return (IChatService)getChatDomainService(servletContext);
    }
	
	private static Object getChatDomainService(ServletContext servletContext)
	{
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    return wac.getBean("chatService");
	}
	
	 /*
     * Return the chat tool version of tool session manager implementation. 
     * It will delegate to the Spring helper method to retrieve the proper 
     * bean from Spring bean factory.
     * @param servletContext the servletContext for current application
     * @return noticeboard service object.*/
    public static final ToolSessionManager getChatSessionManager(ServletContext servletContext)
    {
        return (ToolSessionManager)getChatDomainService(servletContext);
    }
     
    
    /*
     * Return the chat tool version of tool content manager implementation. 
     * It will delegate to the Spring helper method to retrieve the proper 
     * bean from Spring bean factory.
     * @param servletContext the servletContext for current application
     * @return noticeboard service object. */
    public static final ToolContentManager getChatContentManager(ServletContext servletContext)
    {
        return (ToolContentManager)getChatDomainService(servletContext);
    }

}
