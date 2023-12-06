<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.rsrc.ResourceConstants"%>
<c:set var="title">${requestScope.itemTitle}: <fmt:message key="label.view.comments" /></c:set>

<lams:PageLearner toolSessionID="${requestScope.toolSessionID}" title="${title}" hideHeader="true">
	<div id="container-main">
		<lams:Comments toolSessionId="${requestScope.toolSessionID}" toolSignature="<%=ResourceConstants.TOOL_SIGNATURE%>"
			mode="${requestScope.mode}" toolItemId="${requestScope.itemUid}"  bootstrap5="true"
		/>
	</div>
</lams:PageLearner>


