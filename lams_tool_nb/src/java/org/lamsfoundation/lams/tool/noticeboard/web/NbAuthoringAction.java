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

package org.lamsfoundation.lams.tool.noticeboard.web;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;

import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;

import org.lamsfoundation.lams.tool.noticeboard.util.NbAuthoringUtil;



/**
 * Creation Date: 20-05-05
 * Modified Date: 03-06-05
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/tool/nb/authoring" name="NbAuthoringForm" scope="session"
 * 				  type="org.lamsfoundation.lams.tool.noticeboard.web.NbAuthoringAction"
 *                input=".nbForm" parameter="method"
 * @struts:action-forward name="loadNbForm" path=".nbForm"
 * @struts:action-forward name="basic" path=".nb_basic"
 * @struts:action-forward name="advanced" path=".nb_advanced"
 * @struts:action-forward name="instructions" path=".nb_instructions"
 * 
 * ----------------XDoclet Tags--------------------
 */

public class NbAuthoringAction extends LookupDispatchAction {
	

	protected Map getKeyMethodMap()
	{
		Map map = new HashMap();
		map.put(NoticeboardConstants.BUTTON_BASIC, "basic" );
		map.put(NoticeboardConstants.BUTTON_ADVANCED, "advanced" );
		map.put(NoticeboardConstants.BUTTON_INSTRUCTIONS, "instructions");
		map.put(NoticeboardConstants.BUTTON_DONE, "done");
		map.put(NoticeboardConstants.BUTTON_SAVE, "save");
		
		return map;
	}

	/**
	 * Forwards to the basic page.
	 */
	public ActionForward basic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
	    copyRichTextOnlineInstrnValue(request, (NbAuthoringForm)form);
	    copyRichTextOfflineInstrnValue(request, (NbAuthoringForm)form);
	    return mapping.findForward(NoticeboardConstants.BASIC_PAGE);
	}
	
	/**
	 * Forwards to the advanced page.
	 */
	
	public ActionForward advanced(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward(NoticeboardConstants.ADVANCED_PAGE);
	}
	
	/**
	 * Forwards to the instructions page.
	 */
	
	public ActionForward instructions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		copyRichTextContentValue(request, (NbAuthoringForm)form);
		
	    return mapping.findForward(NoticeboardConstants.INSTRUCTIONS_PAGE);
	}	
	
	/**
	 * Online/Offline instructions entered, form values saved and forward to the basic page.
	 */
	
	public ActionForward done(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	    copyRichTextOnlineInstrnValue(request, (NbAuthoringForm)form);
	    copyRichTextOfflineInstrnValue(request, (NbAuthoringForm)form);
	    return mapping.findForward(NoticeboardConstants.BASIC_PAGE);
	}
	
	/**
	 * Persist the values of the noticeboard content (title, content, online and offline instructions)
	 * into the database. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws NbApplicationException
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NbApplicationException {
		
		NbAuthoringForm nbForm = (NbAuthoringForm)form;
		INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
		Long content_id = (Long)request.getSession().getAttribute(NoticeboardConstants.TOOL_CONTENT_ID);
		
		copyRichTextContentValue(request, nbForm);
		copyRichTextOnlineInstrnValue(request, nbForm);
	    copyRichTextOfflineInstrnValue(request, nbForm);
		
		//throws exception if the content id does not exist
		checkContentId(content_id);
		
		NoticeboardContent nbContent = nbService.retrieveNoticeboard(content_id);
		nbForm.copyValuesIntoNbContent(nbContent);
		nbService.updateNoticeboard(nbContent);
		
		return mapping.findForward(NoticeboardConstants.BASIC_PAGE);
	}
	
	/**
	 * The form bean property <code>content</code> is set 
	 * with the value of <code>richTextContent</code> 
	 * @param request
	 * @param form
	 */
	private void copyRichTextContentValue(HttpServletRequest request, NbAuthoringForm form)
	{
	    String content = (String)request.getParameter(NoticeboardConstants.RICH_TEXT_CONTENT);
	    if(content != null)
	    {
	        form.setContent(content);
	        request.getSession().setAttribute(NoticeboardConstants.RICH_TEXT_CONTENT, content);
	        
	    }
	}
	
	/**
	 * The form bean property <code>onlineInstructions</code> is set 
	 * with the value of <code>richTextOnlineInstructions</code> 
	 * @param request
	 * @param form
	 */
	private void copyRichTextOnlineInstrnValue(HttpServletRequest request, NbAuthoringForm form)
	{
	    String onlineInstruction = (String)request.getParameter(NoticeboardConstants.RICH_TEXT_ONLINE_INSTRN);
	    if(onlineInstruction != null)
	    {
	        form.setOnlineInstructions(onlineInstruction);
	        request.getSession().setAttribute(NoticeboardConstants.RICH_TEXT_ONLINE_INSTRN, onlineInstruction);
	        
	    }
	}
	
	/**
	 * The form bean property <code>offlineInstructions</code> is set 
	 * with the value of <code>richTextOfflineInstructions</code> 
	 * @param request
	 * @param form
	 */
	private void copyRichTextOfflineInstrnValue(HttpServletRequest request, NbAuthoringForm form)
	{
	    String offlineInstruction = (String)request.getParameter(NoticeboardConstants.RICH_TEXT_OFFLINE_INSTRN);
	    if(offlineInstruction != null)
	    {
	        form.setOfflineInstructions(offlineInstruction);
	        request.getSession().setAttribute(NoticeboardConstants.RICH_TEXT_OFFLINE_INSTRN, offlineInstruction);
	        
	    }
	}
	
	/**
	 * It is assumed that the contentId is passed as a http parameter
	 * if the contentId is null, an exception is thrown, otherwise proceed as normal
	 * 
	 * @param contentId the <code>toolContentId</code> to check
	 */
	private void checkContentId(Long contentId)
	{
	    if (contentId == null)
		{
			String error = "Tool content id missing. Unable to continue.";
			
			throw new NbApplicationException(error);
		}
	}
	

	
	
	
	
	
	
	
	
	
	
}