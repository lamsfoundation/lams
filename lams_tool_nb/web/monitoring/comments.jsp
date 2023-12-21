<!DOCTYPE html>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>

<c:set var="title"><fmt:message key="activity.title" />: <fmt:message key="label.view.comments" /></c:set>
<lams:PageLearner toolSessionID="${requestScope.toolSessionID}" title="${title}" hideHeader="true">
	<div id="container-main">
		<lams:Comments toolSessionId="${requestScope.toolSessionID}" toolSignature="<%=NoticeboardConstants.TOOL_SIGNATURE%>"
				mode="teacher" anonymous="${anonymous}"/>
	</div>
</lams:PageLearner>
