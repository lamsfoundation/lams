/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.util;

import org.lamsfoundation.lams.contentrepository.client.ToolContentHandler;

/**
 * Simple client for accessing the content repository.
 */
public class VoteToolContentHandler extends ToolContentHandler {

    private static String repositoryWorkspaceName = "vote11";
    private final String repositoryUser = "vote11";
    private final char[] repositoryId = { 'v', 'o', 't', 'e', '_', '1', '1' };

    private VoteToolContentHandler() {
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
