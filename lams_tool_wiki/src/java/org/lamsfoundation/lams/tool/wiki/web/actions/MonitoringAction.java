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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.wiki.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.wiki.dto.WikiDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiUserDTO;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiUser;
import org.lamsfoundation.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.tool.wiki.service.WikiServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/monitoring" parameter="dispatch" scope="request"
 *                name="monitoringForm" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/monitoring/main"
 * @struts.action-forward name="wiki_display"
 *                        path="tiles:/monitoring/wiki_display"
 * 
 */
public class MonitoringAction extends LamsDispatchAction implements IWikiPageAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);

    public IWikiService wikiService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Wiki wiki = wikiService.getWikiByContentId(toolContentID);

	if (wiki == null) {
	    // TODO error page.
	}

	WikiDTO wikiDT0 = new WikiDTO(wiki);

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	wikiDT0.setCurrentTab(currentTab);

	request.setAttribute("wikiDTO", wikiDT0);
	request.setAttribute("contentFolderID", contentFolderID);
	return mapping.findForward("success");
    }

    public ActionForward showWiki(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long uid = new Long(WebUtil.readLongParam(request, "userUID"));

	WikiUser user = wikiService.getUserByUID(uid);
	NotebookEntry entry = wikiService.getEntry(user.getEntryUID());

	WikiUserDTO userDTO = new WikiUserDTO(user, entry);

	request.setAttribute("userDTO", userDTO);

	return mapping.findForward("wiki_display");
    }

    /**
     * set up wikiService
     */
    private void setupService() {
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}
    }

    public ActionForward editPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return null;
    }

    public ActionForward addPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return null;
    }

    public ActionForward removePage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return null;
    }
}
