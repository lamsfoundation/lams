<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${mdlChoiceDTO}" />

<h1><fmt:message key="monitor.sessions" /></h1>
<br />
<c:forEach var="session" items="${dto.sessionDTOs}">
	<p>	
	<a href="javascript:launchPopup('${session.runTimeUrl}');" >
		${session.sessionName}
	</a>
	</p>
</c:forEach>
