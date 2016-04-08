package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class PopulateMarksServlet extends HttpServlet {

    private static final long serialVersionUID = 121580330937986546L;
    private static Logger log = Logger.getLogger(PopulateMarksServlet.class);
    
    private IMcService service;

    @Override
    public void init() throws ServletException {
	service = McServiceProxy.getMcService(getServletContext());
	super.init();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	PrintWriter out = response.getWriter();
	    Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	    HttpSession ss = SessionManager.getSession();
	    UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);

	    service.recalculateMarkForLesson(userDTO, lessonId);
	    log.debug("recalculateMarkForLesson lessonId:" + lessonId);
	    out.println("recalculateMarkForLesson uid:" + lessonId);

	out.println("OK. User marks have been updated.");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	doGet(request, response);
    }

}
