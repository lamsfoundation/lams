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
		
		//get descriptions entered
		//FormattedText.Type descType = FormattedText.Type.DEFAULT; //type of description (S|H|P)
	    //switch(p_descType.charAt(0)){
	    //    case 'H':
	    //        descType = FormattedText.Type.HTML; break;
	    //    case 'S':
	    //        descType = FormattedText.Type.SMART_TEXT; break;
	    //    case 'P':
	    //        descType = FormattedText.Type.PLAIN_TEXT; break;
	    //}
	    //if(!myContent.getIsAvailable()){
	    //    p_descText = "<i>Item is not available.</i><br>" + p_descText;
	    //}
	    //FormattedText description = new FormattedText(p_descText,descType);
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
		    learningSessionId = Long.toString(lsId);
		} catch (Exception e){
			throw new ServletException(e.getMessage(), e);
		}
		
		int bbport = request.getServerPort();
		String bbportstr;
		if (new Integer(bbport).equals(null) || bbport==0)
		{
			bbportstr = "";
		}
		else
		{
			bbportstr = ":" + bbport;
		}
		
		//String contentUrl = LamsSecurityUtil.generateRequestURL(ctx, "learner") + "&lsid=" + learningSessionId;
		String contentUrl = "http://" + request.getServerName() + 
									    bbportstr +
										request.getContextPath() + 
										"/modules/learnermonitor.jsp?lsid=" + learningSessionId + 
										"&course_id=" + request.getParameter("course_id");
		
	
		
		myContent.setUrl(contentUrl);
		
		
		//myContent.setPosition(0);
		//myContent.setLaunchInNewWindow(true);
		
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