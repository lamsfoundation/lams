/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.webservice;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServlet;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.lamsfoundation.lams.workspace.web.WorkspaceAction;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * <a href="LearningDesignRepositorySoapBindingImpl.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class LearningDesignRepositorySoapBindingImpl implements LearningDesignRepository {

    private static Logger log = Logger.getLogger(LearningDesignRepositorySoapBindingImpl.class);

    private static MessageContext context = MessageContext.getCurrentContext();

    private static IntegrationService integrationService = (IntegrationService) WebApplicationContextUtils
	    .getRequiredWebApplicationContext(
		    ((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())
	    .getBean("integrationService");

    private static IWorkspaceManagementService service = (IWorkspaceManagementService) WebApplicationContextUtils
	    .getRequiredWebApplicationContext(
		    ((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())
	    .getBean("workspaceManagementService");

    private static MessageService msgService = service.getMessageService();

    private static class ContentTreeNode {
	FolderContentDTO content;

	List<ContentTreeNode> children;

	ContentTreeNode(FolderContentDTO content) {
	    this.content = content;
	    children = new LinkedList<ContentTreeNode>();
	}

	List<ContentTreeNode> getChildren() {
	    return children;
	}

	void setChildren(List<ContentTreeNode> children) {
	    this.children = children;
	}

	FolderContentDTO getContent() {
	    return content;
	}

	void setContent(FolderContentDTO content) {
	    this.content = content;
	}

	void addChild(FolderContentDTO content) {
	    children.add(new ContentTreeNode(content));
	}

	void addChild(ContentTreeNode node) {
	    children.add(node);
	}

	/**
	 * the format should be something like this: [ ['My Workspace', null,
	 * ['Mary Morgan Folder', null, ['3 activity sequence','1024'] ],
	 * ['Organisations', null, ['Developers Playpen', null, ['Lesson
	 * Sequence Folder', null, ['',null] ] ], ['MATH111', null, ['Lesson
	 * Sequence Folder', null, ['',null] ] ] ] ] ]
	 */
	@Override
	public String toString() {
	    return '[' + convert() + ']';
	}

	String convert() {
	    StringBuilder sb = new StringBuilder();
	    if (content.getResourceType().equals(FolderContentDTO.FOLDER)) {
		sb.append("['");
		sb.append(content.getName()).append("',").append("null").append(',');
		if (children.size() == 0) {
		    sb.append("['',null]");
		} else {
		    sb.append(children.get(0).convert());
		    for (int i = 1; i < children.size(); i++) {
			sb.append(',').append(children.get(i).convert());
		    }
		}
		sb.append(']');
	    } else if (content.getResourceType().equals(FolderContentDTO.DESIGN)) {
		sb.append('[');
		sb.append('\'').append(content.getName()).append('\'').append(',').append('\'')
			.append(content.getResourceID()).append('\'');
		sb.append(']');
	    }
	    return sb.toString();
	}
    }

    /**
     * <p>
     * The returned string is formatted this way for convenience of tigra tree
     * menu javascript library. This is bad design since it make client and
     * server coupled together a bit too tightly. If we change javascript
     * library on client side, the returned value may be not very easy to parse.
     * </p>
     * The main reason is:
     * <ul>
     * <li>String as return type make webservice client end easier to be
     * implemented. Some webservice lib in some language(like SOAP::Lite in
     * Perl) may haven't support for complicated data structure as return type
     * yet</li>
     * </ul>
     */
    @Override
    public String getLearningDesigns(String serverId, String datetime, String hashValue, String username,
	    String courseId, Integer mode, String country, String lang) throws RemoteException {
	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);
	    integrationService.getExtCourseClassMap(extServer, userMap, courseId, country, lang, null,
		    LoginRequestDispatcher.METHOD_MONITOR);
	    return buildContentTree(userMap.getUser().getUserId(), mode).toString();
	} catch (Exception e) {
	    log.debug(e.getMessage(), e);
	    throw new RemoteException(e.getMessage(), e);
	}

    }

    private ContentTreeNode buildContentTree(Integer userId, Integer mode)
	    throws IOException, UserAccessDeniedException, RepositoryCheckedException {
	log.debug("User Id - " + userId);
	FolderContentDTO rootFolder = new FolderContentDTO(msgService.getMessage("label.workspace.root_folder"),
		msgService.getMessage("folder"), null, null, FolderContentDTO.FOLDER,
		WorkspaceAction.BOOTSTRAP_FOLDER_ID.longValue(), WorkspaceFolder.READ_ACCESS, null);
	ContentTreeNode root = new ContentTreeNode(rootFolder);
	FolderContentDTO userFolder = service.getUserWorkspaceFolder(userId);
	root.addChild(buildContentTreeNode(userFolder, userId, mode));

	FolderContentDTO dummyOrgFolder = new FolderContentDTO(msgService.getMessage("organisations"),
		msgService.getMessage("folder"), null, null, FolderContentDTO.FOLDER,
		new Long(WorkspaceAction.ORG_FOLDER_ID.longValue()), WorkspaceFolder.READ_ACCESS, null);
	ContentTreeNode dummyOrgNode = new ContentTreeNode(dummyOrgFolder);
	//tried using service.getAccessibleOrganisationWorkspaceFolders(userId) api, 
	//but it doesn't work, the userOrganisations set of the user 
	// got from workspaceManagementService with the userId supplied is empty, which is not true.
	Vector orgFolders = service.getAccessibleOrganisationWorkspaceFolders(userId);
	for (int i = 0; i < orgFolders.size(); i++) {
	    FolderContentDTO orgFolder = (FolderContentDTO) orgFolders.get(i);
	    dummyOrgNode.addChild(buildContentTreeNode(orgFolder, userId, mode));
	}
	root.addChild(dummyOrgNode);

	FolderContentDTO publicFolder = service.getPublicWorkspaceFolder(userId);
	if (publicFolder != null) {
	    root.addChild(buildContentTreeNode(publicFolder, userId, mode));
	}

	return root;
    }

    private ContentTreeNode buildContentTreeNode(FolderContentDTO folder, Integer userId, Integer mode)
	    throws UserAccessDeniedException, RepositoryCheckedException {
	log.debug("build content tree node for folder - " + folder.getName());
	ContentTreeNode node = new ContentTreeNode(folder);
	if (folder.getResourceType().equals(FolderContentDTO.FOLDER)) {
	    log.debug(folder.getName() + " is a folder");
	    WorkspaceFolder wsfolder = service.getWorkspaceFolder(folder.getResourceID().intValue());
	    Vector items = service.getFolderContentsExcludeHome(userId, wsfolder, mode);
	    for (int i = 0; i < items.size(); i++) {
		FolderContentDTO content = (FolderContentDTO) items.get(i);
		node.addChild(buildContentTreeNode(content, userId, mode));
	    }
	}
	return node;
    }

}
