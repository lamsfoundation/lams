<%@ page import="java.util.*,
                java.util.Date,
                java.text.SimpleDateFormat,
                blackboard.data.*,
                blackboard.persist.*,
                blackboard.data.course.*,
                blackboard.data.user.*,
                blackboard.persist.course.*,
                blackboard.data.content.*,
                blackboard.persist.content.*,
                blackboard.db.*,
                blackboard.base.*,
                blackboard.platform.*,
                blackboard.platform.plugin.*"
	errorPage="/error.jsp"
%>
<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<bbData:context id="ctx">

<%
    String NOT_AVAILABLE = "<i>Item is not available.</i><br>";

    //check permission
    if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
        return;

    BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
    Container bbContainer = bbPm.getContainer();
    
    Id contentId = new PkId( bbContainer, CourseDocument.COURSE_DOCUMENT_DATA_TYPE, request.getParameter("content_id") );
    
    ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader( ContentDbLoader.TYPE );
    Content courseDoc = (Content)courseDocumentLoader.loadById( contentId );

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date startDate = null;
    Date endDate = null;
    
    if(courseDoc.getStartDate() != null)
        startDate = courseDoc.getStartDate().getTime(); 
    if(courseDoc.getEndDate() != null)
        endDate = courseDoc.getStartDate().getTime();
        
    FormattedText desc = courseDoc.getBody();
    String description = desc.getText().replaceFirst(NOT_AVAILABLE, ""); //remove the NOT_AVAILABLE substring
%>

<bbUI:docTemplate title="Modify LAMS">
<bbUI:coursePage courseId="<%=PlugInUtil.getCourseId(request)%>">	 

<bbUI:breadcrumbBar  environment="CTRL_PANEL"  handle="control_panel" isContent="true">
  <bbUI:breadcrumb>Modify HTML Block</bbUI:breadcrumb>
</bbUI:breadcrumbBar>
<bbUI:titleBar iconUrl="/images/ci/icons/tools_u.gif">Modify LAMS</bbUI:titleBar>
<form name="lams_form" action="modify_proc.jsp" method="post">
    <input type=hidden name=content_id value="<%=request.getParameter("content_id")%>">
    <input type=hidden name=course_id value="<%=request.getParameter("course_id")%>">
    
    <bbUI:step title="Select an existing sequence or create a new sequence">        
        <bbUI:dataElement label="Name">
            <input type="text" name="title" value="<%=courseDoc.getTitle()%>">
        </bbUI:dataElement>
    
        <bbUI:dataElement label="Description">
            <bbUI:textbox name="description" rows="12" cols="55" text="<%=description%>" format="<%=desc.getType().toFieldName().substring(0,1)%>"></bbUI:textbox>
        </bbUI:dataElement>
    </bbUI:step>
    
    <bbUI:step title="Options">
        <bbUI:dataElement label="Do you want to make LAMS visible?">
            <input type="Radio" name="isAvailable" value="true" <%=(courseDoc.getIsAvailable())?"checked":""%>>Yes 
            <input type="Radio" name="isAvailable" value="false" <%=(courseDoc.getIsAvailable())?"":"checked"%>>No 
        </bbUI:dataElement>
        <bbUI:dataElement label="Track number of views">
            <input type="radio" name="isTracked" value="true" <%=(courseDoc.getIsTracked())?"checked":""%>>Yes
            <input type="radio" name="isTracked" value="false" <%=(courseDoc.getIsTracked())?"":"checked"%>>No
        </bbUI:dataElement>
        <bbUI:dataElement label="Choose date restrictions">
            <bbUI:dateAvailability formName="lams_form" 
                                   startDateField="startDate" endDateField="endDate" 
                                   startDate="<%=startDate%>" endDate="<%=endDate%>"/>
        </bbUI:dataElement>
    </bbUI:step>
    
    <bbUI:stepSubmit title="Submit"/>
</form>
</bbUI:coursePage>
</bbUI:docTemplate>						  
</bbData:context>

