<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.service.UserManagementService" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.Role" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.User" %>
<%@ page import="org.lamsfoundation.lams.lesson.Lesson" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO" %>
<%@ page import="org.apache.commons.collections.functors.WhileClosure" %>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<form name="form" method="post">
<table width="98%" height="100%" border="0" align="center" cellpadding="2" cellspacing="0">
	<tr>
		<td align="center" valign="middle"><img height="7" src="images/spacer.gif" width="10" alt="spacer.gif"/></td>
	</tr>
	<tr>
		<td valign="top">
			<%String login = request.getRemoteUser();
			WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()); 
			UserManagementService service = (UserManagementService)ctx.getBean("userManagementServiceTarget");
			User user = service.getUserByLogin(login);
			if ( login==null ){%>
				<P class="error">An error has occured. You have tried to log
				in but we didn't get the username. Try closing your browser and starting
				again.</p>
			<%}%>
		</td>
	</tr>
	<tr>
		<td valign="top">
			<table height="100%" border="0" align="center" cellpadding="2" cellspacing="0">
				<tr>
					<td align="center" valign="top" colspan="2">
						<p class="mainHeader">Welcome <%=user.getFirstName()%></p>
					</td>
				</tr>
				<tr>
				<td align="center" colspan="2">
					<!-- If we are a sysadmin for any org, then we are sysadmin for everything -->
					<% ArrayList roleList = new ArrayList();
					   roleList.add(Role.SYSADMIN);
					   OrganisationDTO orgDTO = service.getOrganisationsForUserByRole(user,roleList);
						if(orgDTO!=null){%>
							<input name="sysadmin" type="button" id="sysadmin" onClick="openSysadmin(1);" value="System Adminstration"/>
					<%}%>
					<!-- If we are a author for any org, then we show the authoring button once -->
					<% roleList.clear();
					   roleList.add(Role.AUTHOR);
					   orgDTO = service.getOrganisationsForUserByRole(user,roleList);
						if(orgDTO!=null){%>
							 <input name="author" type="button" id="author" onClick="openAuthor(1);" value="Author"/>
					<%}
					   roleList.clear();
					   roleList.add(Role.STAFF);
					   orgDTO = service.getOrganisationsForUserByRole(user,roleList);
						if(orgDTO!=null){%>
					   <A HREF="monitoring/dummy.jsp" target="mWindow"/>Dummy Monitoring Screen</A>
					<%}
					   roleList.clear();
					   roleList.add(Role.LEARNER);
					   orgDTO = service.getOrganisationsForUserByRole(user,roleList);
						if(orgDTO!=null){%>
					   <A HREF="learning/dummymain.jsp" target="lWindow"/>Dummy Learning Screen</A>
					<%}%>
				</td>
				</tr>

				<%orgDTO = service.getOrganisationsForUserByRole(user,null);
				  if(orgDTO!=null){
						Vector courses = orgDTO.getNodes();
						Iterator courseIter = courses.iterator();
						while ( courseIter.hasNext() ) {
						
							OrganisationDTO course = (OrganisationDTO)courseIter.next();%>

							<tr><td align="left">Course: <%=course.getName()%>:</td>
							<td align="right" >
							<% Vector roleNames	= course.getRoleNames();
								if ( roleNames.contains(Role.STAFF) ) {%>
									<input name="addLesson" type="button" id="addLesson" onClick="openAddLesson(<%=course.getOrganisationID()%>,null);" value="Add Lesson"/>
							</td>
							</tr>
							<%		List lessons = service.getMonitorLessonsFromOrganisation(user.getUserId(),course.getOrganisationID());
									Iterator lessonIterator = lessons.iterator();
									while ( lessonIterator.hasNext() ) {
										Lesson lesson = (Lesson) lessonIterator.next();
							%>
										<TR><TD align="left">Lesson: <%=lesson.getLessonName()%></TD>
										<TD align="right"><input name="monitorLesson" type="button" id="monitorLesson" onClick="openMonitorLesson(<%=lesson.getLessonId()%>);" value="Monitoring"/></TD></TR>
							<%		}
								} %> 
							<%		List lessons = service.getLearnerLessonsFromOrganisation(user.getUserId(),course.getOrganisationID());
									Iterator lessonIterator = lessons.iterator();
									while ( lessonIterator.hasNext() ) {
										Lesson lesson = (Lesson) lessonIterator.next();
							%>
										<TR><TD align="left">Lesson: <%=lesson.getLessonName()%></TD>
										<TD align="right"><input name="learner" type="button" id="learner" onClick="openLearner(<%=lesson.getLessonId()%>);" value="Learner"/></TD></TR>
							<%		} %>
							
							<% 
							Vector classes = course.getNodes();
							Iterator classIter = classes.iterator();
							while ( classIter.hasNext() ) {
								OrganisationDTO courseClass = (OrganisationDTO)classIter.next(); %>
												
								<tr><td align="left">Class: <%=courseClass.getName()%>:</td>
								<td align="right" >
								<% Vector classRoleNames	= course.getRoleNames();
									if ( classRoleNames.contains(Role.STAFF)  ) {%>
									<input name="addLesson" type="button" id="addLesson" onClick="openAddLesson(<%=course.getOrganisationID()%>,<%=courseClass.getOrganisationID()%>);" value="Add Lesson"/>
								</td>
								</tr>
								<%		lessons = service.getMonitorLessonsFromOrganisation(user.getUserId(),courseClass.getOrganisationID());
										lessonIterator = lessons.iterator();
										while ( lessonIterator.hasNext() ) {
											Lesson lesson = (Lesson) lessonIterator.next();
								%>
											<TR><TD align="left">Lesson: <%=lesson.getLessonName()%></TD>
											<TD align="right"><input name="monitorLesson" type="button" id="monitorLesson" onClick="openMonitorLesson(<%=lesson.getLessonId()%>);" value="Monitoring"/></TD></TR>
								<%		} 
								
								 } %> 
								<%		lessons = service.getLearnerLessonsFromOrganisation(user.getUserId(),courseClass.getOrganisationID());
										lessonIterator = lessons.iterator();
										while ( lessonIterator.hasNext() ) {
											Lesson lesson = (Lesson) lessonIterator.next();
								%>
											<TR><TD align="left">Lesson: <%=lesson.getLessonName()%></TD>
											<TD align="right"><input name="learner" type="button" id="learner" onClick="openLearner(<%=lesson.getLessonId()%>);" value="Learner"/></TD></TR>
								<%		} %>
							<% }%>
					  <% } %>
				<%}%>
			</table>
		</td>
	</tr>
	<tr>
		<td valign="top">
			<table height="100%" border="0" align="center" cellpadding="2" cellspacing="0">
				<tr align="center" valign="bottom">
					<td colspan="2" ><img height="274" src="images/launch_page_graphic.jpg" width="587" alt="launch_page_graphic.jpg"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr valign="bottom">
		<td>	
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="lightNote">
				<tr valign="bottom">
					<td height="12">
						<a href="javascript:alert('LAMS&#8482; &copy; 2002-2005 LAMS Foundation. 
							\nAll rights reserved.
							\n\nLAMS is a trademark of LAMS Foundation.
							\nDistribution of this software is prohibited.');" 
							class="lightNoteLink">&copy; 2002-2006 LAMS Foundation.
						</a>
					</td>
					<td align="right">Version 1.1</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</html>