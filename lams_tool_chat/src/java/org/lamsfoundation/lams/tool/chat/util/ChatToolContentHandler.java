/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.chat.util;

import org.lamsfoundation.lams.contentrepository.client.ToolContentHandler;

/**
 * Simple client for accessing the content repository.
 */
public class ChatToolContentHandler extends ToolContentHandler {

    // TODO these three fields were changed to public, since accessor methods
    // cannot be made static.  Check if we can do this a better way.
    public static String repositoryWorkspaceName = "chatworkspace";

    public static String repositoryUser = "chat";

    public static char[] repositoryId = { 'l', 'a', 'm', 's', '-', 'e', 'x' };

    /**
     * 
     */
    public ChatToolContentHandler() {
	super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.contentrepository.client.ToolContentHandler#getRepositoryWorkspaceName()
     */
    @Override
    public String getRepositoryWorkspaceName() {
	return repositoryWorkspaceName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.contentrepository.client.ToolContentHandler#getRepositoryUser()
     */
    @Override
    public String getRepositoryUser() {
	return repositoryUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.contentrepository.client.ToolContentHandler#getRepositoryId()
     */
    @Override
    public char[] getRepositoryId() {
	return repositoryId;
    }

}
