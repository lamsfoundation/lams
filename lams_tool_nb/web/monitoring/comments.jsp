<!DOCTYPE html>
	

<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<script src="${lams}includes/javascript/jquery.js"></script>
	<title><fmt:message key="activity.title"/></title>
</lams:head>

<body class="stripes">

	<c:set var="title"><fmt:message key="activity.title" />: <fmt:message key="label.view.comments" /></c:set>
	<lams:Page title="${title}" type="learner" hideProgressBar="true">

		<lams:Comments toolSessionId="${requestScope.toolSessionID}" toolSignature="<%=NoticeboardConstants.TOOL_SIGNATURE%>"
			mode="teacher" anonymous="${anonymous}"  bootstrap5="false"
		/>

	</lams:Page>

</lams:html>

