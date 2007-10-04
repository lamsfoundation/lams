<%// Discussion Grader, Copyright 2004 Joliet Junior College  
// Home Page : http://www.jjc.edu/distance/
// Author : Jeff Nuckles jnuckles@jjc.edu
%>
<%@ page import="java.util.*,
                java.text.*,
                blackboard.data.*,
                blackboard.data.course.*,
                blackboard.data.user.*,
                blackboard.persist.*,
                blackboard.persist.course.*,
                blackboard.persist.user.*,
				blackboard.persist.gradebook.*,
				blackboard.data.gradebook.*,
                blackboard.base.*,
                blackboard.platform.*,
                blackboard.platform.log.*,
                blackboard.platform.session.*,
                blackboard.platform.persistence.*,                              
                blackboard.platform.BbServiceManager.*,                
                blackboard.platform.context.*,
         		blackboard.base.BbList.*,
                blackboard.platform.plugin.PlugInUtil,
				java.util.Calendar,
				java.lang.*,
				blackboard.platform.security.*"
        errorPage="/error.jsp"
 %>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbUI" prefix="bbUI"%>
<bbData:context  entitlement="course.gradebook.MODIFY">
<bbUI:docTemplate title = "Select">
<%
if (request.getParameter("score") != null) { 
%>
<%
BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
UserDbLoader uLoader = UserDbLoader.Default.getInstance();
CourseDbLoader cLoader = CourseDbLoader.Default.getInstance();
CourseMembershipDbLoader memLoader =(CourseMembershipDbLoader) bbPm.getLoader(CourseMembershipDbLoader.TYPE);
ScoreDbLoader scoreLoader = (ScoreDbLoader) bbPm.getLoader(ScoreDbLoader.TYPE);
ScoreDbPersister scorePersister = (ScoreDbPersister) bbPm.getPersister(ScoreDbPersister.TYPE);
String cid = request.getParameter("course_id");
String lid = request.getParameter("lineitem_pk1");
String fid = request.getParameter("forum_pk1");
String uid = request.getParameter("UserId");
String gid = null;
if (request.getParameter("group_pk1") != null){
	gid = request.getParameter("group_pk1");
}


Course courseId = cLoader.loadById(Id.generateId(new DataType(Course.class),cid));
User user = uLoader.loadById(Id.generateId(new DataType(User.class),uid));
CourseMembership cms = memLoader.loadByCourseAndUserId(courseId.getId(), user.getId());
Score current_score = null;
try {
current_score = scoreLoader.loadByCourseMembershipIdAndLineitemId(cms.getId(),Id.generateId(new DataType(Lineitem.class),lid));
} catch (KeyNotFoundException c){
current_score = new Score();
current_score.setLineitemId(Id.generateId(new DataType(Lineitem.class),lid));
current_score.setCourseMembershipId(cms.getId());
}

String current_points = current_score.getGrade();
current_score.setGrade(request.getParameter("score"));
try {
current_score.validate();
scorePersister.persist(current_score);
%>
<bbUI:breadcrumbBar handle="jjcd-jjcdg-nav-1">
<bbUI:breadcrumb>GRADE CHANGE</bbUI:breadcrumb>
</bbUI:breadcrumbBar>
<bbUI:titleBar iconUrl ="/images/ci/icons/edit_u.gif">Discussion Grader</bbUI:titleBar>

<table cellspacing="0" cellpadding="0" width="100%">
  <tr> 
    <td> 
      <table cellspacing="0" border="0" cellpadding="5" width="100%">
        <tr>  
          <td width="20" valign="top"><img src="/images/spacer.gif" height="22" width="22" hspace="0" vspace="0" alt="" border="0" /></td>           
          <td width="100%" valign="top"> 
A grade has been successfully entered into the gradebook for <%=user.getGivenName()%> <%=user.getFamilyName()%>.
<br>         
<br>         
<span class="receiptDate"><%=(new Date()).toString()%></span>  
</td>                                        
                </tr>                        
                <tr>                         
                        <td align="right" colspan="6">    
<a href="select.jsp?course_id=<%=cid%>&forum_pk1=<%=fid%><%if (gid!=null){%>&group_pk1=<%=gid%><%}%>"><img ALT="OK" name="img_ok" src="/images/ci/formbtns/ok_off.gif" WIDTH="69" HEIGHT="20" BORDER="0" HSPACE="5"></a>

          </td>      
                </tr>
                <tr> 
                        <td align="right" colspan="6"><img src="/images/spacer.gif" height="1" width="10" hspace="0" vspace="0" alt="" border="0" /></td>                         
                </tr>       
        </table></td>       
</tr>                       

</table>        


<%
} catch (ValidationException e) {
%>Invalid Score <%=e%>
<%
}
}
%>
</bbUI:docTemplate>
</bbData:context>



