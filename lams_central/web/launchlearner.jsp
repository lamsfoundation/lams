<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ page import="org.lamsfoundation.lams.lesson.Lesson" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>

<lams:html>
<lams:head>
	<title><fmt:message key="index.welcome" /></title>
	<lams:css />
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL />includes/javascript/getSysInfo.js"></script>
	<script language="javascript" type="text/javascript" src="<lams:LAMSURL />loadVars.jsp"></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL />includes/javascript/openUrls.js"></script>
</lams:head>

<body class="stripes">
	<div id="page">
	
	<div id="header-no-tabs">
	</div>

	<div id="content">
		<h1 class="no-tabs-below"><c:out value="${name}"/></h1>
		<p><c:out value="${description}" escapeXml="false"/></p>
		<p>
		
			<!-- defined Lesson states -->
			<c:set var="created"><%= Lesson.CREATED %></c:set>
			<c:set var="notstarted"><%= Lesson.NOT_STARTED_STATE %></c:set>
			<c:set var="started"><%= Lesson.STARTED_STATE %></c:set>
			<c:set var="finished"><%= Lesson.FINISHED_STATE %></c:set>
			<c:set var="suspended"><%= Lesson.SUSPENDED_STATE %></c:set>
			<c:set var="archived"><%= Lesson.ARCHIVED_STATE %></c:set>
			<c:set var="removed"><%= Lesson.REMOVED_STATE %></c:set>

			<c:if test="${status == created || status == notstarted}">
				<fmt:message key="label.msg.status">
					<fmt:param>
						<fmt:message key="msg.status.not.stated"/>
					</fmt:param>
				</fmt:message>
				<c:set var="button" value="false"/>
			</c:if>
			<c:if test="${status == started}">
				<c:set var="button" value="true"/>
			</c:if>
			<c:if test="${status == suspended}">
				<fmt:message key="label.msg.status">
					<fmt:param>
						<fmt:message key="msg.status.disabled"/>
					</fmt:param>
				</fmt:message>
				<c:set var="button" value="false"/>
			</c:if>
			<c:if test="${status == finished || status == archived}">
				<fmt:message key="label.msg.status">
					<fmt:param>
						<fmt:message key="msg.status.finished"/>
					</fmt:param>
				</fmt:message>
				<c:set var="button" value="true"/>
			</c:if>
			<c:if test="${status == removed}">
				<fmt:message key="label.msg.status">
					<fmt:param>
						<fmt:message key="msg.status.removed"/>
					</fmt:param>
				</fmt:message>
				<c:set var="button" value="false"/>
			</c:if>
		</p>
		<c:if test="${button}">
			<a href='javascript:openLearnerShortenedUrl(<c:out value="${lessonID}"/>)' class='button'><fmt:message key="label.open.lesson"/></a>
		</c:if>
	</div>
	<div id="footer">
	</div><!--closes footer-->
		
	</div>
</body>
</lams:html>