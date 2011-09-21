<%@ page import="blackboard.platform.BbServiceManager"%>
<%@ page import="blackboard.platform.session.BbSession"%>
<%@ page import="blackboard.platform.plugin.*"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsServiceUtil"%>
<%@ page import="lamsws.SimpleLearningDesignVO"%>
<%@ page errorPage="/error.jsp"%>  
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:genericPage title="LAMS Learning Activity Management System" ctxId="ctx">
<%
	/***
	 * this JSP page simply generate a list of all Learning Design from the LearningDesign web service
	 **/

	// SECURITY!
    //AccessManagerService accessManager = (AccessManagerService) BbServiceManager.lookupService(AccessManagerService.class);
	if (!PlugInUtil.authorizeForCourseControlPanel(request,response)){
		//accessManager.sendAccessDeniedRedirect(request,response);
		//TODO: redirect user to login page, since sendAccessDeniedRedirect is deprecated another way is needed
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		return;
	}
    
    //use BbSession to obtain username
    BbSession bbSession = BbServiceManager.getSessionManagerService().getSession( request );
    String username = bbSession.getUserName();
    
	//get All Learning Designs from WebService object
	SimpleLearningDesignVO[] learningDesignArray = LamsServiceUtil.getAllLearningDesign(username);

    response.setContentType("text/xml");

	//open designs XML tag
	%><designs><%
	
    /*this javascript uses the learningDesignArray to get a list of workspace and sequences*/
    for(int i=0; i<learningDesignArray.length; i++){
        String workspace = learningDesignArray[i].getWorkspace();
        String sequence = learningDesignArray[i].getTitle();
        String sid = learningDesignArray[i].getSid().toString();        
        %>
			<design>
				<id><%=sid%></id>
				<workspace><%=workspace%></workspace>
				<sequence><%=sequence%></sequence>
			</design><%
        
    }
    
	//close designs XML tag
	%></designs><%
%>
        
</bbNG:genericPage>