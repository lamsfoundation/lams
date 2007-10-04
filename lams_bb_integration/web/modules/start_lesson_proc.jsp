<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="blackboard.base.FormattedText"%>
<%@ page import="blackboard.data.course.Course"%>
<%@ page import="blackboard.data.content.Content"%>
<%@ page import="blackboard.data.content.ContentFile"%>
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
<%@ page import="blackboard.data.gradebook.Lineitem" %>
<%@ page import="blackboard.persist.gradebook.LineitemDbPersister" %>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsPluginUtil"%>    
               
<%@ page errorPage="/error.jsp"%><%@ page import="org.lamsfoundation.ld.integration.Constants" %>      

<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>


<bbData:context id="ctx">
	
	<jsp:useBean id="myContent" scope="page" class="blackboard.data.content.CourseDocument"/>
	<jsp:setProperty name="myContent" property="title"/>
	<jsp:setProperty name="myContent" property="isAvailable"/>
	<jsp:setProperty name="myContent" property="isTracked"/>
	
	<%
		String title = request.getParameter("title").trim();
		String description = request.getParameter("description").trim();	
		
		// TODO: Use bb text area instead
		//String p_descText = request.getParameter("descriptiontext");
		//String p_descType = request.getParameter("descriptiontype");
		long ldId = Long.parseLong(request.getParameter("sequence_id").trim());
		//TODO: Get and validate dates and options
		
		//public static Long scheduleLesson(Context ctx, long ldId, String title, String desc, String startDate)		//public static Long startLesson(Context ctx, long ldId, String title, String desc)
		
		// retrieve the Db persistence manager from the persistence service
	    BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
	    ContentDbPersister contentPersister = (ContentDbPersister) bbPm.getPersister( ContentDbPersister.TYPE );
	    
		// internal IDs for the course and parent content item
	    Id courseId = bbPm.generateId(Course.DATA_TYPE,request.getParameter("course_id"));
	    Id folderId = bbPm.generateId(CourseDocument.DATA_TYPE,request.getParameter("content_id"));
	    
	    // load parent content item
	    ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader( ContentDbLoader.TYPE );
	    ContentFolder courseDocParent = (ContentFolder)courseDocumentLoader.loadById( folderId );
	
		// get the session object to obtain the user and course object
	    BbSessionManagerService sessionService = BbServiceManager.getSessionManagerService();
	    BbSession bbSession = sessionService.getSession( request );
	    
		// set LAMS content data
	    myContent.setTitle(title);
	    myContent.setIsAvailable(request.getParameter("isAvailable").equals("true")?true:false);
	    myContent.setIsTracked(request.getParameter("isTracked").equals("true")?true:false);
		
	   	FormattedText fdescription = new FormattedText(description, FormattedText.Type.HTML);

		// set core course data
	    myContent.setContentHandler( LamsPluginUtil.CONTENT_HANDLE );
	    myContent.setCourseId( courseId );
	    myContent.setParentId( folderId );
	    
		// set LAMS content data
	    myContent.setRenderType(Content.RenderType.URL);
		//myContent.setRenderType(Content.RenderType.LINK);
	    myContent.setBody(fdescription);
	    String learningSessionId = null;
	    
	    //	  tell lams to create the learning session using webservices
	    try{  	
	    	long lsId = LamsSecurityUtil.startLesson(ctx, ldId, title, description);
		    
	    	//error checking
	    	if (lsId == -1)
	    	{
	    		response.sendRedirect("lamsServerDown.jsp");
	    		System.exit(1);
	    	}
	    	
	    	learningSessionId = Long.toString(lsId);
	    	
	    
	    } catch (Exception e){
			throw new ServletException(e.getMessage(), e);
		}
		
	    
	    // Creating the gradebook row for this lesson
		Id lineitemId = bbPm.generateId(Lineitem.LINEITEM_DATA_TYPE,learningSessionId);
	    
	    Lineitem lineitem = new Lineitem();
	    lineitem.setCourseId(courseId);
	    lineitem.setName("LAMS Lesson: " + title);
	    lineitem.setId(lineitemId);
	    lineitem.setAssessmentId(learningSessionId,Lineitem.AssessmentLocation.EXTERNAL);
	    lineitem.setAssessmentLocation( Lineitem.AssessmentLocation.EXTERNAL );
	    lineitem.setDateAdded();
	    lineitem.setIsAvailable(true);
	    lineitem.setType("LAMS");
	    lineitem.setColumnOrder(3000);
	    lineitem.validate();
	    lineitem.setPointsPossible(1);
	    
	    
	    
	    // add port to the url if the port is in the blackboard url.
		int bbport = request.getServerPort();
		String bbportstr = bbport != 0 ? ":" + bbport : "";
		
		//String contentUrl = LamsSecurityUtil.generateRequestURL(ctx, "learner") + "&lsid=" + learningSessionId;
		String contentUrl = request.getScheme()
										+ "://" +
										request.getServerName() + 
									    bbportstr +
										request.getContextPath() + 
										"/modules/learnermonitor.jsp?lsid=" + learningSessionId + 
										"&course_id=" + request.getParameter("course_id") +
										"&lineitem_id=" + lineitemId.getExternalString();
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
	    
	    
	   
	    
	    //LineitemDbLoader liLoader = (LineitemDbLoader) bbPm.getLoader(LineitemDbLoader.TYPE);
	    LineitemDbPersister lineItempersist = (LineitemDbPersister) bbPm.getPersister(LineitemDbPersister.TYPE);
	    lineItempersist.persist(lineitem);
	    
	    
	    ContentDbPersister persister= (ContentDbPersister) bbPm.getPersister( ContentDbPersister.TYPE );
	    persister.persist( myContent );
	    
	    String strReturnUrl = PlugInUtil.getEditableContentReturnURL(myContent.getParentId());
	%>
	<bbUI:docTemplate title="LAMS Learning Activity Management System">
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