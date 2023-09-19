<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.sessionMapID]}" />

<lams:DefineLater toolSessionID="${sessionMap.toolSessionID}" defineLaterLabel="error.defineLater" />

