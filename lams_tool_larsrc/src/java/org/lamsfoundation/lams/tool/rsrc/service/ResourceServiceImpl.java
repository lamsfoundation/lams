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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.rsrc.service;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceAttachmentDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceSessionDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceUserDAO;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.util.MessageService;

/**
 * 
 * @author Dapeng.Ni 
 * 
 */
public class ResourceServiceImpl implements
                              IResourceService
               
{
	static Logger logger = Logger.getLogger(ResourceServiceImpl.class.getName());
	private ResourceDAO resourceDao;
	private ResourceItemDAO resourceItemDao;
	private ResourceAttachmentDAO resourceAttachmentDao;
	private ResourceUserDAO resourceUserDao;
	private ResourceSessionDAO resourceSessionDao;
	//tool service
	private ResourceToolContentHandler resourceToolContentHandler;
	private MessageService messageService;
	//system services
	private IRepositoryService repositoryService;
	private ILamsToolService toolService;
	private ILearnerService learnerService;
	
	
	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public void setResourceAttachmentDao(ResourceAttachmentDAO resourceAttachmentDao) {
		this.resourceAttachmentDao = resourceAttachmentDao;
	}
	public void setResourceDao(ResourceDAO resourceDao) {
		this.resourceDao = resourceDao;
	}
	public void setResourceItemDao(ResourceItemDAO resourceItemDao) {
		this.resourceItemDao = resourceItemDao;
	}
	public void setResourceSessionDao(ResourceSessionDAO resourceSessionDao) {
		this.resourceSessionDao = resourceSessionDao;
	}
	public void setResourceToolContentHandler(ResourceToolContentHandler resourceToolContentHandler) {
		this.resourceToolContentHandler = resourceToolContentHandler;
	}
	public void setResourceUserDao(ResourceUserDAO resourceUserDao) {
		this.resourceUserDao = resourceUserDao;
	}
	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}
}
