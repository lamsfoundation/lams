<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<!-- Wraps up topicview.jsp for paging. The first batch of messages is in viewtopic.jsp then the rest are loaded via scrolling using this jsp. -->

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<script type="text/javascript" src="${tool}includes/javascript/jquery.treetable.js"></script>

<%@ include file="topicview.jsp"%>


