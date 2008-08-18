<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${dotLRNForumDTO}" />

<h1><fmt:message key="monitor.sessions" /></h1>
<br />
<c:forEach var="session" items="${dto.sessionDTOs}">
	<p>	
	<a href="javascript:launchPopup('${dto.extCourseUrl}/forums/forum-view?forum_id=${session.extSessionID}');" >
		${session.sessionName}
	</a>
	</p>
</c:forEach>
