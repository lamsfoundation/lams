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

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learning.web.form.NotebookForm;
import org.lamsfoundation.lams.learning.web.util.ActionMappings;
import org.lamsfoundation.lams.notebook.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.INotebookService;
import org.lamsfoundation.lams.notebook.service.NotebookServiceException;
import org.lamsfoundation.lams.notebook.service.NotebookServiceProxy;

/**
 * @author daveg
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/notebook/SaveEntry" name="notebookForm"
 *                validate="false" scope="request"
 * 
 * @struts:action-forward name="displayEntry" path="/DisplayEntry" redirect="true"
 *
 */
public class SaveNotebookEntry extends NotebookAction {

    protected static String className = "SaveNotebookEntry";
    
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		NotebookForm notebookForm = (NotebookForm)actionForm;
		
		SessionBean sessionBean = getSessionBean(request);
		if (sessionBean == null) {
			// forward to the no session error page
			return mapping.findForward(ActionMappings.NO_SESSION_ERROR);
		}

		INotebookService notebookService = NotebookServiceProxy.getNotebookService(this.servlet.getServletContext());
		NotebookEntry notebookEntry;
		if (notebookForm.getNotebookEntryId() == null) {
			notebookEntry = new NotebookEntry();
			notebookEntry.setUser(sessionBean.getLearner());
			notebookEntry.setLesson(sessionBean.getLesson());
		}
		else {
			notebookEntry = notebookService.getNotebookEntry(notebookForm.getNotebookEntryId());
			// check whether user in session has access to update this notebook entry
			if (!notebookEntry.isUpdateable(sessionBean.getLearner())) {
				log.error(className+": User with Id "+sessionBean.getLearner().getUserId()+" does not have access to NotebookEntry with Id "+notebookEntry.getNotebookEntryId());
				return mapping.findForward(ActionMappings.NO_ACCESS_ERROR);
			}
		}
		
		notebookEntry.setTitle(notebookForm.getTitle());
		notebookEntry.setBody(notebookForm.getBody());
		try {
			notebookService.saveNotebookEntry(notebookEntry);
		}
		catch (NotebookServiceException e) {
			log.error(className+": NootebookServiceException saving notebook entry", e);
			return mapping.findForward(ActionMappings.ERROR);
		}
		
		return mapping.findForward("displayEntry");
	}
	
}
