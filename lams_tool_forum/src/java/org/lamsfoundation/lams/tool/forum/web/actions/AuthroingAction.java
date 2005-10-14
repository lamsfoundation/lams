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
package org.lamsfoundation.lams.tool.forum.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.service.ForumServiceProxy;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumForm;
import org.lamsfoundation.lams.util.WebUtil;
/**
 *  * @struts.action path="/authoring" 
 * 				  name="ForumForm" 
 * 				  parameter="action"
 *                input="/authoring/authoring.jsp" 
 *                scope="request" 
 *                validate="true"
 * 
 * @struts.action-forward name="success" path="/authoring/success.jsp"
 * 
 * @author Steve.Ni
 * @version $Revision$
 */
public class AuthroingAction extends DispatchAction {
	private static Logger log = Logger.getLogger(AuthroingAction.class);
	/**
	 * This page will display initial submit tool content. Or just a blank page if the toolContentID does not
	 * exist before. 
	 *  
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		
		Long contentID = new Long(WebUtil.readLongParam(request,ForumConstants.TOOL_CONTENT_ID));
		
		//get back the topic list and display them on page
		IForumService forumService = ForumServiceProxy.getForumService(this
				.getServlet().getServletContext());
		
		try {
			Forum forum = forumService.getForum(contentID);
			((ForumForm)form).setForum(forum);
		} catch (PersistenceException e) {
			log.error(e);
			return mapping.findForward("error");
		}
		//set back STRUTS component value
		return mapping.getInputForward();
	}

}
