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


/*
 * Created on Sep 14, 2005
 *
 */
package org.lamsfoundation.lams.learning.export.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learning.export.service.IExportPortfolioService;
import org.lamsfoundation.lams.learning.export.service.ExportPortfolioServiceProxy;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/** ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/exportPortfolio" type="org.lamsfoundation.lams.learning.export.web.action.ExportPortfolioAction"
 *                       validate="false"
 * @struts.action-exception key="error.system.exportPortfolio" scope="request"
 *                          type="org.lamsfoundation.lams.learning.export.ExportPortfolioException"
 *                          path=".systemError"
 * 							handler="org.lamsfoundation.lams.learning.util.CustomStrutsExceptionHandler"
 * 
 * @struts:action-forward name="displayPrintVersion" path="/exportPortfolioPrintVersion.jsp"
 * @struts:action-forward name="displayPortfolio" path="/exportPortfolio.jsp"
 * @struts:action-forward name="loadExportPage" path="/exportWaitingPage.jsp" 
 * -------------------------------------------------
 **/

public class ExportPortfolioAction extends LamsAction {
    
	public final String PARAM_PORTFOLIO_LIST = "portfolioList";

	public ActionForward execute(ActionMapping mapping,
						            ActionForm form,
						            HttpServletRequest request,
						            HttpServletResponse response) throws ExportPortfolioException
	{
		Portfolio[] portfolios = null;
		String mode = WebUtil.readStrParam(request, AttributeNames.PARAM_MODE);
		IExportPortfolioService exportService = ExportPortfolioServiceProxy.getExportPortfolioService(getServlet().getServletContext());
		
		//get the cookies that came along with the request and pass it onto export service method
		Cookie[] cookies = request.getCookies();
		
		if (mode.equals(ToolAccessMode.LEARNER.toString()))
		{
			//get the learnerprogress id
		    HttpSession session = SessionManager.getSession();
		    UserDTO userDto = (UserDTO)session.getAttribute(AttributeNames.USER);
		    Integer userId = userDto.getUserID();
		    
		    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
		    portfolios = exportService.exportPortfolioForStudent(userId, lessonID, true, cookies);
		}
		else if(mode.equals(ToolAccessMode.TEACHER.toString()))
		{
			//get the lesson data
			//done in the monitoring environment
		    Long lessonID = new Long(WebUtil.readLongParam(request,"lessonID"));
			
			portfolios = exportService.exportPortfolioForTeacher(lessonID, cookies);
		}
		
		request.setAttribute(PARAM_PORTFOLIO_LIST, portfolios);
		
		return mapping.findForward("displayPortfolio");
	}
	

}
