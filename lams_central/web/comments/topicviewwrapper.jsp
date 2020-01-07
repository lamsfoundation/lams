<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<!-- Wraps up topicview.jsp for paging. The first batch of messages is in viewtopic.jsp then the rest are loaded via scrolling using this jsp. -->

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<script type="text/javascript" src="${lams}includes/javascript/jquery.treetable.js"></script>

<%@ include file="topicview.jsp"%>


