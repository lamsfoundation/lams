<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>

<c:choose>
	<c:when test="${creatingUsers == 'true'}">
		<c:set var="refresh">2</c:set>
		<c:set var="defineLaterLabel">creating.users.message</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="refresh">60</c:set>
	</c:otherwise>
</c:choose>

<lams:DefineLater toolSessionID="${toolSessionId}" defineLaterLabel="${defineLaterLabel}" refresh="${refresh}"/>
