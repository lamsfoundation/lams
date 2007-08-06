<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="blackboard.base.FormattedText"%>
<%@ page import="blackboard.data.course.Course"%>
<%@ page import="blackboard.data.content.Content"%>
<%@ page import="blackboard.data.content.ContentFolder"%>
<%@ page import="blackboard.data.content.CourseDocument"%>

<%@ page import="blackboard.persist.Id"%>
<%@ page import="blackboard.persist.BbPersistenceManager"%>
<%@ page import="blackboard.persist.content.ContentDbPersister"%>
<%@ page import="blackboard.persist.content.ContentDbLoader"%>
<%@ page import="blackboard.platform.session.BbSessionManagerService"%>
<%@ page import="blackboard.platform.session.BbSession"%>
<%@ page import="blackboard.platform.*"%>
<%@ page import="blackboard.platform.plugin.PlugInUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsServiceUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsPluginUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.Constants"%>

                
<%@ page errorPage="/error.jsp"%><%@ page import="org.lamsfoundation.ld.integration.Constants" %>      

<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<bbData:context id="ctx">
  
<jsp:useBean id="myContent" scope="page" class="blackboard.data.content.CourseDocument"/>
<jsp:setProperty name="myContent" property="title"/>
<jsp:setProperty name="myContent" property="isAvailable"/>
<jsp:setProperty name="myContent" property="isTracked"/>

<%

	// SECURITY!
    //AccessManagerService accessManager = (AccessManagerService) BbServiceManager.lookupService(AccessManagerService.class);
	if (!PlugInUtil.authorizeForCourseControlPanel(request,response)){
		//accessManager.sendAccessDeniedRedirect(request,response);
		//TODO: redirect user to login page, since sendAccessDeniedRedirect is deprecated another way is needed
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		return;
	}
	
	//check parameters
	String p_courseId = request.getParameter("course_id");
	String p_contentId = request.getParameter("content_id");
	String p_learningDesignId = request.getParameter("sequence");
	String p_descText = request.getParameter("descriptiontext");
	String p_descType = request.getParameter("descriptiontype");
	String p_title = request.getParameter("title");
	if(p_courseId==null || p_contentId==null || p_learningDesignId==null || p_title==null || p_descText==null || p_descType==null){
		throw new ServletException("requested parameters missing");
	}
    
    // retrieve the Db persistence manager from the persistence service
    BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
    ContentDbPersister contentPersister = (ContentDbPersister) bbPm.getPersister( ContentDbPersister.TYPE );
    
    // internal IDs for the course and parent content item
    Id courseId = bbPm.generateId(Course.DATA_TYPE,request.getParameter("course_id"));
    Id folderId = bbPm.generateId(CourseDocument.DATA_TYPE,request.getParameter("content_id"));
    
    // learning design id
    long ldId = Long.parseLong(p_learningDesignId);
    
    //load parent content item
    ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader( ContentDbLoader.TYPE );
    ContentFolder courseDocParent = (ContentFolder)courseDocumentLoader.loadById( folderId );

	//get the session object to obtain the user and course object
    BbSessionManagerService sessionService = BbServiceManager.getSessionManagerService();
    BbSession bbSession = sessionService.getSession( request );
    
    //get username
    String username = bbSession.getUserName();

    //set LAMS content data
    myContent.setTitle(p_title);
    myContent.setIsAvailable(request.getParameter("isAvailable").equals("true")?true:false);
    myContent.setIsTracked(request.getParameter("isTracked").equals("true")?true:false);

    //get descriptions entered
    FormattedText.Type descType = FormattedText.Type.DEFAULT; //type of description (S|H|P)
    switch(p_descType.charAt(0)){
        case 'H':
            descType = FormattedText.Type.HTML; break;
        case 'S':
            descType = FormattedText.Type.SMART_TEXT; break;
        case 'P':
            descType = FormattedText.Type.PLAIN_TEXT; break;
    }
    if(!myContent.getIsAvailable()){
        p_descText = "<i>Item is not available.</i><br>" + p_descText;
    }
    FormattedText description = new FormattedText(p_descText,descType);
    

    //set core course data
    myContent.setContentHandler( LamsPluginUtil.CONTENT_HANDLE );
    myContent.setCourseId( courseId );
    myContent.setParentId( folderId );
       
    //set LAMS content data
    myContent.setRenderType(Content.RenderType.URL);
    myContent.setBody(description);

    String learningSessionId = null;
        
    //tell lams to create the learning session using webservices
    try{
	    long lsId = LamsServiceUtil.createLearningSession(username, ldId, courseId.toExternalString(), p_title, p_descText);
	    learningSessionId = Long.toString(lsId);
	} catch (Exception e){
		throw new ServletException(e.getMessage(), e);
	}

    
    String contentUrl = request.getContextPath() + Constants.SERVLET_ACTION_REQUEST + '?' + 
    					Constants.PARAM_METHOD + '=' + Constants.METHOD_LEARNER + '&' + 
    					Constants.PARAM_LEARNING_SESSION_ID + '=' + learningSessionId + '&' + 
    					Constants.PARAM_COURSE_ID + '=' + courseId.toExternalString();
	myContent.setUrl(contentUrl);
    
    
    
    
    //Parse start/end Date from the <bbUI:dateAvailability>
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cstart = Calendar.getInstance();
    Calendar cend = Calendar.getInstance();
    cstart.setTime(formatter.parse(request.getParameter("startDate")));
    cend.setTime(formatter.parse(request.getParameter("endDate")));

    // Set Availability Dates
    myContent.setStartDate(cstart);
    if (request.getParameter("restrict_end") != null){
        if (request.getParameter("restrict_end").equals("1")){
            myContent.setEndDate(cend);
        }
    }
    
    ContentDbPersister persister= (ContentDbPersister) bbPm.getPersister( ContentDbPersister.TYPE );
    persister.persist( myContent );
    
    String strReturnUrl = PlugInUtil.getEditableContentReturnURL(myContent.getParentId());
%>
<bbUI:docTemplate title="Learning Activity Management System">
<bbUI:coursePage courseId="<%=PlugInUtil.getCourseId(request)%>">

<bbUI:breadcrumbBar environment="ctrl_panel" handle="control_panel" isContent="true" >
  <bbUI:breadcrumb>LAMS</bbUI:breadcrumb>
</bbUI:breadcrumbBar>

<bbUI:receipt type="SUCCESS" iconUrl="/images/ci/icons/tools_u.gif" title="Content Added" recallUrl="<%=strReturnUrl%>">Content successfully added.</bbUI:receipt><br>
<br>
<br>
 

</bbUI:coursePage>
</bbUI:docTemplate>
</bbData:context>
