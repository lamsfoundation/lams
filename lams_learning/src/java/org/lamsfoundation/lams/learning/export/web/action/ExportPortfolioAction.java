/*
 * Created on Sep 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.export.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.web.action.LamsAction;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.apache.struts.action.ActionForward;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.learning.export.service.IExportPortfolioService;
import org.lamsfoundation.lams.learning.export.service.ExportPortfolioServiceProxy;
import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.lesson.Lesson;

import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;

/** ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/exportPortfolio" 
 *                       validate="false"
 * @struts.action-exception key="error.system.exportPortfolio" scope="request"
 *                          type="org.lamsfoundation.lams.learning.export.ExportPortfolioException"
 *                          path=".systemError"
 * 							handler="org.lamsfoundation.lams.learning.util.CustomStrutsExceptionHandler"
 * 
 * @struts:action-forward name="displayPortfolio" path="/exportPortfolio.jsp"
 * -------------------------------------------------
 **/

public class ExportPortfolioAction extends LamsAction{
	public final String PARAM_PORTFOLIO_LIST = "portfolioList";

	public ActionForward execute(ActionMapping mapping,
						            ActionForm form,
						            HttpServletRequest request,
						            HttpServletResponse response) throws ExportPortfolioException
	{
		Portfolio[] portfolios = null;
		String mode = WebUtil.readStrParam(request, WebUtil.PARAM_MODE);
		IExportPortfolioService exportService = ExportPortfolioServiceProxy.getExportPortfolioService(getServlet().getServletContext());
		
		if (mode.equals(ToolAccessMode.LEARNER.toString()))
		{
			//get the learnerprogress id
			User learner = LearningWebUtil.getUserData(request, getServlet().getServletContext());
			LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgressByUser(request, getServlet().getServletContext());
			Long progressId = learnerProgress.getLearnerProgressId();
			
			portfolios = exportService.exportPortfolioForStudent(progressId, learner, true);
		}
		else if(mode.equals(ToolAccessMode.TEACHER.toString()))
		{
			//get the lesson data
			//done in the monitoring environment
			Long lessonID = new Long(WebUtil.readLongParam(request,"lessonID"));
			ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
			Lesson lesson = learnerService.getLesson(lessonID);
			
			portfolios = exportService.exportPortfolioForTeacher(lesson);
		}
		
		request.setAttribute(PARAM_PORTFOLIO_LIST, portfolios);
		return mapping.findForward("displayPortfolio");
	}
}
