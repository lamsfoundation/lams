<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<!-- Wraps up allview.jsp for refresh/sort change. -->

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<script type="text/javascript" src="${lams}includes/javascript/jquery.treetable.js"></script>

<%@ include file="allview.jsp"%>


