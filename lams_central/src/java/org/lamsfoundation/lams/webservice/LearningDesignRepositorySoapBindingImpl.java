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

import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.lamsfoundation.lams.workspace.service.WorkspaceManagementService;
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

	private static IntegrationService integrationService = (IntegrationService) WebApplicationContextUtils
			.getRequiredWebApplicationContext(HttpSessionManager.getInstance().getServletContext())
			.getBean("integrationService");

	private static IWorkspaceManagementService service = (IWorkspaceManagementService) WebApplicationContextUtils
			.getRequiredWebApplicationContext(HttpSessionManager.getInstance().getServletContext())
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
		 * the format should be something like this:
		 	[
				['My Workspace', null,
					['Mary Morgan Folder', null,
						['3 activity sequence','1024']
					],
					['Organisations', null,
						['Developers Playpen',   null,
							['Lesson Sequence Folder', null,
								['',null]
							]
						],
						['MATH111',  null,
							['Lesson Sequence Folder', null,
								['',null]
							]
						]
					]
				]
			]
		 */
		public String toString(){
			return '['+convert()+']';
		}
		
		String convert(){
			StringBuilder sb = new StringBuilder();
			if(content.getResourceType().equals(FolderContentDTO.FOLDER)){
				sb.append('[');
				sb.append(content.getName()).append(',').append("null").append(',');
				if(children.size() == 0){
					sb.append("['',null]");
				}else{
					sb.append(children.get(0).convert());
					for(int i=1; i<children.size(); i++){
						sb.append(',').append(children.get(0).convert());
					}
				}
				sb.append(']');
			}else if(content.getResourceType().equals(FolderContentDTO.DESIGN)){
				sb.append('[');
				sb.append('\'').append(content.getName()).append('\'').append(',')
				.append('\'').append(content.getResourceID()).append('\'');
				sb.append(']');
			}
			return sb.toString();
		}
	}

	/**
	 * <p>
	 * The returned string is formatted this way for convenience of tigra tree menu javascript library.
	 * This is bad design since it make client and server coupled together a bit too tightly. If we change
	 * javascript library on client side. The returned value may be not very easy to parse.
	 * </p> 
	 * The main reason is:
	 * <ul>
	 * <li>String as return type make webservice client end easier to implement. 
	 * 	   Some webservice lib in some language(like SOAP::Lite in Perl) may haven't support 
	 *     complicated data structure as return type
	 * </li>
	 * </ul>
	 */
	public String getLearningDesigns(String serverId, String datetime, String hashValue,
			String username, Integer mode) throws RemoteException {
		try {
			ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(serverId);
			Authenticator.authenticate(serverMap,datetime,username,hashValue);
			ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(serverMap, username);
			return buildContentTree(userMap.getUser().getUserId()).toString();
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}

	}

	private ContentTreeNode buildContentTree(Integer userId) throws IOException,
			UserAccessDeniedException, RepositoryCheckedException {
		FolderContentDTO rootFolder = new FolderContentDTO(msgService
				.getMessage("label.workspace.root_folder"), msgService.getMessage("folder"), null,
				null, FolderContentDTO.FOLDER, WorkspaceAction.BOOTSTRAP_FOLDER_ID.longValue(),
				WorkspaceFolder.READ_ACCESS, null);
		ContentTreeNode root = new ContentTreeNode(rootFolder);
		FolderContentDTO userFolder = service.getUserWorkspaceFolder(userId);
		root.addChild(buildContentTreeNode(userFolder, userId));
		FolderContentDTO dummyOrgFolder = new FolderContentDTO(msgService
				.getMessage("organisations"), msgService.getMessage("folder"), null, null,
				FolderContentDTO.FOLDER, new Long(WorkspaceAction.ORG_FOLDER_ID.longValue()),
				WorkspaceFolder.READ_ACCESS, null);
		ContentTreeNode dummyOrgNode = new ContentTreeNode(dummyOrgFolder);
		Vector orgFolders = service.getAccessibleOrganisationWorkspaceFolders(userId);
		for (int i = 0; i < orgFolders.size(); i++) {
			FolderContentDTO orgFolder = (FolderContentDTO) orgFolders.get(i);
			dummyOrgNode.addChild(buildContentTreeNode(orgFolder, userId));
		}
		root.addChild(dummyOrgNode);
		return root;
	}

	private ContentTreeNode buildContentTreeNode(FolderContentDTO folder, Integer userId)
			throws UserAccessDeniedException, RepositoryCheckedException {
		ContentTreeNode node = new ContentTreeNode(folder);
		if (folder.getResourceType().equals(FolderContentDTO.FOLDER)) {
			WorkspaceFolder wsfolder = service
					.getWorkspaceFolder(folder.getResourceID().intValue());
			Vector items = service.getFolderContentsExcludeHome(userId, wsfolder,
					WorkspaceManagementService.MONITORING);
			for (int i = 0; i < items.size(); i++) {
				FolderContentDTO content = (FolderContentDTO) items.get(i);
				node.addChild(buildContentTreeNode(content, userId));
			}
		}
		return node;
	}

}
