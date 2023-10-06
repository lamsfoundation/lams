<!DOCTYPE html>
	

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.rsrc.ResourceConstants"%>

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
	<title>${requestScope.itemTitle}</title>
</lams:head>

<body class="stripes">

	<c:set var="title">${requestScope.itemTitle}: <fmt:message key="label.view.comments" /></c:set>
	<lams:Page title="${title}" type="learner" hideProgressBar="true">

		<lams:Comments toolSessionId="${requestScope.toolSessionID}" toolSignature="<%=ResourceConstants.TOOL_SIGNATURE%>"
			mode="${requestScope.mode}" toolItemId="${requestScope.itemUid}"  bootstrap5="false"
		/>

	</lams:Page>
	
</lams:html>

