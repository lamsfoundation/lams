<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:DefineLater toolSessionID="${sessionMap.toolSessionID}" defineLaterLabel="error.tool.is.not.configured" />

