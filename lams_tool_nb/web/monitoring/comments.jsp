<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">

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

	<div id="content">
		<lams:Comments toolSessionId="${requestScope.toolSessionID}" toolSignature="<%=NoticeboardConstants.TOOL_SIGNATURE%>" mode="teacher"/>
	</div>
	
</lams:html>

