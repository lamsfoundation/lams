<%// Discussion Grader, Copyright 2004 Joliet Junior College  
// Home Page : http://www.jjc.edu/distance/
// Author : Jeff Nuckles jnuckles@jjc.edu
%>
<%@ page import="java.util.*,
                java.text.*,
                blackboard.data.*,
                blackboard.data.course.*,
                blackboard.persist.*,
                blackboard.persist.course.*,
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
				blackboard.persist.discussionboard.*,
				blackboard.data.discussionboard.*,
				blackboard.platform.security.*"
        errorPage="/error.jsp"
 %>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbUI" prefix="bbUI"%>
<bbData:context  id="ctx"  entitlement="course.gradebook.MODIFY">
<bbUI:docTemplate title = "Select">
<%
BbSessionManagerService sessionService = BbServiceManager.getSessionManagerService();   
BbSession bbSession = sessionService.getSession( request );
Course course = ctx.getCourse();
Id courseId = course.getId();
String cidString = courseId.toExternalString();
cidString = cidString.substring(1,cidString.lastIndexOf("_"));
ConferenceDbLoader cfLoader = ConferenceDbLoader.Default.getInstance();
Conference cf = cfLoader.loadByCourseId(courseId);
Id cfId = cf.getId();
ForumDbLoader fLoader = ForumDbLoader.Default.getInstance();
BbList forum = fLoader.loadByConferenceId(cfId);
BbList.Iterator forumListIter = forum.getFilteringIterator();
GroupDbLoader gLoader = GroupDbLoader.Default.getInstance();
BbList gList = gLoader.loadByCourseId(courseId);
BbList.Iterator groupListIter = gList.getFilteringIterator();
%>

<bbUI:breadcrumbBar handle="jjcd-jjcdg-nav-1" isContent="true">
</bbUI:breadcrumbBar>

<!--bbUI:actionBar-->
<!--bbUI:actionItem folder="true" title="test" href="http://www.jjc.edu" imgUrl="http://www.vlegenius.com/images/icons/demo_16.gif"/-->
<!--/bbUI:actionBar-->
<bbUI:titleBar iconUrl ="/images/ci/icons/edit_u.gif">Discussion Grader</bbUI:titleBar>

<bbUI:instructionBar>
Select the discussion forum that you wish to grade from the list below.
</bbUI:instructionBar>


	<table border=0 cellpadding="5" cellspacing="5">
	<%
while (forumListIter.hasNext()) {
	Forum cForum = (Forum)forumListIter.next();
	String fid = cForum.getId().toExternalString();
	fid = fid.substring(1,fid.lastIndexOf("_"));
	%>
	<tr><td width=20 valign=top><img align=middle border=0 src=/images/ci/icons/arrow.gif width=16 height=14/></td><td width=100% valign=top><span class=caretTitle><a href="select.jsp?course_id=<%=cidString%>&forum_pk1=<%=fid%>"><%=cForum.getTitle()%></a></span><br/><span class=caretDescription><%=cForum.getDescription().getText()%></span></td></tr>
<%}%>
</table>
<%if (gList.size() > 0){%>
<bbUI:instructionBar>
Select a group discussion forum that you wish to grade from the list below.
</bbUI:instructionBar>

<table border=0 cellpadding="5" cellspacing="5">
	<%
while (groupListIter.hasNext()) {
	Group group = (Group)groupListIter.next();
	Conference conf = cfLoader.loadByGroupId(group.getId());
	BbList frmList = fLoader.loadByConferenceId(conf.getId());
	BbList.Iterator fListIter = frmList.getFilteringIterator();
	while (fListIter.hasNext()) {
		Forum cForum = (Forum)fListIter.next();
	    String fid = cForum.getId().toExternalString();
	    fid = fid.substring(1,fid.lastIndexOf("_"));
		String gid = group.getId().toExternalString();
		gid = gid.substring(1,gid.lastIndexOf("_"));
	%>
	<tr><td width=20 valign=top><img align=middle border=0 src=/images/ci/icons/arrow.gif width=16 height=14/></td><td width=100% valign=top><span class=caretTitle><a href="select.jsp?course_id=<%=cidString%>&forum_pk1=<%=fid%>&group_pk1=<%=gid%>"><%=group.getTitle()%> - <%=cForum.getTitle()%></a></span><br/><span class=caretDescription><%=cForum.getDescription().getText()%></span></td></tr>
<%}%>
</table>
<%}}%>


</bbUI:docTemplate>
</bbData:context>



