<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.lesson.Lesson" %>
<!-- defined Lesson states -->
<c:set var="created"><%= Lesson.CREATED %></c:set>
<c:set var="notstarted"><%= Lesson.NOT_STARTED_STATE %></c:set>
<c:set var="started"><%= Lesson.STARTED_STATE %></c:set>
<c:set var="finished"><%= Lesson.FINISHED_STATE %></c:set>
<c:set var="suspended"><%= Lesson.SUSPENDED_STATE %></c:set>
<c:set var="archived"><%= Lesson.ARCHIVED_STATE %></c:set>
<c:set var="removed"><%= Lesson.REMOVED_STATE %></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="index.welcome" /></title>
	<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<lams:css />

	<lams:JSImport src="includes/javascript/getSysInfo.js" />
	<lams:JSImport src="includes/javascript/openUrls.js" />
</lams:head>

<body class="stripes">
<c:set var="title"><fmt:message key="label.open.lesson"/></c:set>
<lams:Page type="learner" title="${title}">

	<div class="text-center" style="margin-top: 20px; margin-bottom: 20px;">
					
		<h1 class="no-tabs-below"><c:out value="${name}"/></h1>
		<p><c:out value="${description}" escapeXml="false"/></p>
		<p>
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
			<a href='javascript:openLearnerShortenedUrl(<c:out value="${lessonID}"/>)' class='btn btn-default'>
				<fmt:message key="label.open.lesson"/>
			</a>
		</c:if>
	</div>
	
	<div id="footer"></div><!--closes footer-->

</lams:Page>
</body>
</lams:html>