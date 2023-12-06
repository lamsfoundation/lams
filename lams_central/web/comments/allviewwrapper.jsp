<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<!-- Wraps up allview.jsp for refresh/sort change. -->
<script type="text/javascript" src="${lams}includes/javascript/jquery.treetable.js"></script>

<%@ include file="allview.jsp"%>


