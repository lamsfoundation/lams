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
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.notebook.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.INotebookService;
import org.lamsfoundation.lams.notebook.service.NotebookServiceProxy;

/**
 * @author daveg
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/notebook/DisplayEntry" name="notebookForm"
 *                validate="false" scope="request"
 * 
 * @struts:action-forward name="displayEntry" path=".journalDetail"
 *
 */
public class DisplayNotebookEntry extends NotebookAction {
    
    protected static String className = "DisplayNotebookEntry";

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		NotebookForm notebookForm = (NotebookForm)actionForm;

		SessionBean sessionBean = getSessionBean(request);
		if (sessionBean == null) {
			// forward to the no session error page
			return mapping.findForward(ActivityMapping.NO_SESSION_ERROR);
		}

		NotebookEntry notebookEntry;
		if (notebookForm.getNotebookEntryId() == null) {
			notebookEntry = new NotebookEntry();
		}
		else {
			INotebookService notebookService = NotebookServiceProxy.getNotebookService(this.servlet.getServletContext());
			notebookEntry = notebookService.getNotebookEntry(notebookForm.getNotebookEntryId());
			// check whether user in session has access to this notebook entry
			if (!notebookEntry.isViewable(sessionBean.getLearner())) {
				log.error(className+": User with Id "+sessionBean.getLearner().getUserId()+" does not have access to NotebookEntry with Id "+notebookEntry.getNotebookEntryId());
				return mapping.findForward(ActivityMapping.NO_ACCESS_ERROR);
			}
		}
		notebookForm.setNotebookEntry(notebookEntry);
		
		return mapping.findForward("displayEntry");
	}
	
}
