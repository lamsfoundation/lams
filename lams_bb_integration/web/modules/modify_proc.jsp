<%@ page import="java.util.*,
                java.text.*,
                java.io.*,
                blackboard.data.*,
                blackboard.persist.*,
                blackboard.data.course.*,
                blackboard.data.user.*,
                blackboard.persist.course.*,
                blackboard.data.content.*,
                blackboard.persist.content.*,
                blackboard.base.*,
                blackboard.platform.*,
                blackboard.platform.persistence.*,
                blackboard.platform.plugin.*"
          errorPage="/error.jsp"
%>
<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<bbData:context id="ctx">
  
<%
    //check permission
    if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
        return;

    BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
    Container bbContainer = bbPm.getContainer();

    Id contentId = new PkId( bbContainer, CourseDocument.COURSE_DOCUMENT_DATA_TYPE, request.getParameter("content_id") );

    ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader( ContentDbLoader.TYPE );
    Content myContent = (Content)courseDocumentLoader.loadById( contentId );
    
    //set LAMS content data
    myContent.setTitle(request.getParameter("title"));
    myContent.setIsAvailable(request.getParameter("isAvailable").equals("true")?true:false);
    myContent.setIsTracked(request.getParameter("isTracked").equals("true")?true:false);
    

    //get descriptions entered
    String descText = request.getParameter("descriptiontext");
    FormattedText.Type descType = FormattedText.Type.DEFAULT; //type of description (S|H|P)
    switch(request.getParameter("descriptiontype").charAt(0)){
        case 'H':
            descType = FormattedText.Type.HTML; break;
        case 'S':
            descType = FormattedText.Type.SMART_TEXT; break;
        case 'P':
            descType = FormattedText.Type.PLAIN_TEXT; break;
    }
    if(!myContent.getIsAvailable()){
        descText = "<i>Item is not available.</i><br>" + descText;
    }
    FormattedText description = new FormattedText(descText,descType);
    
    myContent.setBody(description);

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


    <bbUI:docTemplate title="Modify LAMS">
        <bbUI:coursePage courseId="<%=PlugInUtil.getCourseId(request)%>">
        
            <bbUI:breadcrumbBar  environment="CTRL_PANEL"  handle="control_panel" isContent="true" >
              <bbUI:breadcrumb>Modify LAMS</bbUI:breadcrumb>
            </bbUI:breadcrumbBar>
            <bbUI:receipt type="SUCCESS" 
                          iconUrl="/images/ci/icons/tools_u.gif" 
                          title="Content Modified" 
                          recallUrl="<%=strReturnUrl%>">
                Content successfully modified.<br>
            </bbUI:receipt>
        </bbUI:coursePage>
    </bbUI:docTemplate>
</bbData:context>
