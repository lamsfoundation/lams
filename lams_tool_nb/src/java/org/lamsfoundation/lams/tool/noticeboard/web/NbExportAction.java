/*
 * Created on Jul 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.noticeboard.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.web.NbExportForm;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import java.util.HashSet;
import java.util.Set;
/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/exportPortfolio" name="NbExportForm" scope="session" type="org.lamsfoundation.lams.tool.noticeboard.web.NbExportAction"
 *                validate="false" parameter="mode"
 * @struts.action-exception key="error.exception.NbApplication" scope="request"
 *                          type="org.lamsfoundation.lams.tool.noticeboard.NbApplicationException"
 *                          path=".error"
 *                          handler="org.lamsfoundation.lams.tool.noticeboard.web.CustomStrutsExceptionHandler"
 * @struts:action-forward name="exportPortfolio" path=".exportPortfolio"
 * ----------------XDoclet Tags--------------------
 */
public class NbExportAction extends LamsDispatchAction {
    
    static Logger logger = Logger.getLogger(NbExportForm.class.getName());

    public ActionForward unspecified(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response)
    {
        return mapping.findForward(NoticeboardConstants.EXPORT_PORTFOLIO);
    }
    
    public ActionForward learner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
       //parameters given are the toolSessionId and userId
        NbExportForm exportForm = (NbExportForm)form;
        Long toolSessionId = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.TOOL_SESSION_ID));
        Long userId = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.USER_ID));
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
        
        if (userId == null || toolSessionId == null)
        {
            String error = "Tool session Id or user Id is null. Unable to continue";
            logger.error(error);
            throw new NbApplicationException(error);
        }
        
        NoticeboardSession session = nbService.retrieveNbSessionByUserID(userId);
        
        Set userList = session.getNbUsers();
      /** TODO: check whether user belongs to session or not */
    /*   if (session.getNbSessionId().toString().equals(toolSessionId.toString()))
        {
            String error="User does not belong to this session. ";
            logger.error(error);
            throw new NbApplicationException(error);
        } */
        
        NoticeboardContent content = session.getNbContent();
        
        if (content == null)
        {
            String error="Data is missing from the database. Unable to Continue";
            logger.error(error);
            throw new NbApplicationException(error);
        }
        //check if user belong to that session too.
        
        //check if the given user exists on the db or not, if they are not, do not export content.
        exportForm.populateForm(content);
        return mapping.findForward(NoticeboardConstants.EXPORT_PORTFOLIO);
    }
    
    public ActionForward teacher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
        //given the toolcontentId as a parameter
        NbExportForm exportForm = (NbExportForm)form;
        INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
        
        Long toolContentId = NbWebUtil.convertToLong(request.getParameter(NoticeboardConstants.TOOL_CONTENT_ID));
        //check if toolContentId exists in db or not
        if (toolContentId==null)
        {
            String error="Tool Content Id is missing. Unable to continue";
            logger.error(error);
            throw new NbApplicationException(error);
        }
        
        NoticeboardContent content = nbService.retrieveNoticeboard(toolContentId);
        
        if (content == null)
        {
            String error="Data is missing from the database. Unable to Continue";
            logger.error(error);
            throw new NbApplicationException(error);
        }
        
        exportForm.populateForm(content);
        
   		return mapping.findForward(NoticeboardConstants.EXPORT_PORTFOLIO);
    }
    
    
}
