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
/* $Id$ */

package org.lamsfoundation.lams.tool.bbb.util;

import org.lamsfoundation.lams.contentrepository.client.ToolContentHandler;

/**
 * Simple client for accessing the content repository.
 */
public class BbbToolContentHandler extends ToolContentHandler {

    public static String repositoryWorkspaceName = "bbbworkspace";

    public static String repositoryUser = "bbb";

    public static char[] repositoryId = { 'l', 'a', 'm', 's', '-', 'e', 'x' };

    /**
     *
     */
    public BbbToolContentHandler() {
	super();
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.lamsfoundation.lams.contentrepository.client.ToolContentHandler# getRepositoryWorkspaceName()
     */
    @Override
    public String getRepositoryWorkspaceName() {
	return repositoryWorkspaceName;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.lamsfoundation.lams.contentrepository.client.ToolContentHandler# getRepositoryUser()
     */
    @Override
    public String getRepositoryUser() {
	return repositoryUser;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.lamsfoundation.lams.contentrepository.client.ToolContentHandler# getRepositoryId()
     */
    @Override
    public char[] getRepositoryId() {
	return repositoryId;
    }

}
