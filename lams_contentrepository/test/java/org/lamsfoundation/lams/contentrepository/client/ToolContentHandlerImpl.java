/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/
package org.lamsfoundation.lams.contentrepository.client;

import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;

/**
 * 
 * @author Fiona Malikoff
 *
 * This is a test implementation of the ContentHandler. Each tool would
 * derive its own class similar to this.
 */
public class ToolContentHandlerImpl extends ToolContentHandler {

	protected String repositoryUser = "testcontenthandleruser";
	protected char[] repositoryId = {'t','e','s','t','i','d'}; 
	protected String repositoryWorkspace = "testcontenthandlerworkspace";

    /* (non-Javadoc)
     * @see org.lamsfoundation.lams.contentrepository.client.ContentHandler#getRepositoryWorkspaceName()
     */
    public String getRepositoryWorkspaceName() {
        return repositoryWorkspace;
    }

    /* (non-Javadoc)
     * @see org.lamsfoundation.lams.contentrepository.client.ContentHandler#getRepositoryUser()
     */
    public String getRepositoryUser() {
        return repositoryUser;
    }

    /* (non-Javadoc)
     * @see org.lamsfoundation.lams.contentrepository.client.ContentHandler#getRepositoryId()
     */
    public char[] getRepositoryId() {
        return repositoryId;
    }
    


}
