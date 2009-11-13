<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-html" prefix="html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<c:set var="lams">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<title>
		<fmt:message key="index.searchlesson" />
	</title>

	<link rel="stylesheet"
		href="${lams}/includes/javascript/jquery-ui/themes/default/ui.all.css"
		type="text/css" media="screen" title="Flora (Default)" >

	<script type="text/javascript"
		src="${lams}/includes/javascript/getSysInfo.js"></script>
	<script language="javascript" type="text/javascript"
		src="${lams}/loadVars.jsp"></script>

	<script type="text/javascript"
		src="${lams}/includes/javascript/openUrls.js"></script>
	<script type="text/javascript"
		src="${lams}/includes/javascript/jquery-latest.pack.js"></script>
	<script type="text/javascript"
		src="${lams}/includes/javascript/jquery-ui/jquery-ui-autocomplete-1.6rc2.min.js"></script>

	<script type="text/javascript">
	window.onload = ( function() {
		try {
			var data = "Core Selectors Attributes Traversing Manipulation CSS Events Effects Ajax Utilities"
					.split(" ");
			var url = "${lams}/findUserLessons.do?courseID=${courseID}&dispatch=autocomplete";

			$("#query").autocomplete( {
				url :url
			});
		} catch (e) {
		}
	});
</script>
</lams:head>

<body class="stripes">
<div id="content"><c:set var="lams">
	<lams:LAMSURL />
</c:set>

<h1><fmt:message key="lessonsearch.title" /></h1>

<p><fmt:message key="lessonsearch.instuctions" /></p>

<form action="${lams}/findUserLessons.do"><input type="hidden"
	name="dispatch" value="getResults"> <input type="text"
	id="query" name="query"> <input type="submit" value="Search"
	class="button"> <input type="hidden" name="courseID"
	value="${courseID}"></form>

<div class="space-bottom-top"></div>

<c:choose>
	<c:when test="${userLessonsMap == null}">
		<!-- don't display anything -->
	</c:when>

	<c:when test="${empty userLessonsMap}">
		<p><span style="font-style: italic"><fmt:message
			key="lessonsearch.noresults" /></span></p>
	</c:when>

	<c:otherwise>
		<h1><fmt:message key="lessonsearch.results">
			<fmt:param value="${originalQuery}" />
		</fmt:message></h1>

		<c:forEach var="user" items="${userLessonsMap}">
			<div><strong>${user.key.firstName}
			${user.key.lastName}</strong> <c:choose>
				<c:when test="${user.value ne null and not empty user.value}">
					<ul>
						<c:forEach var="lessonDto" items="${user.value}">
							<li>${lessonDto.lessonName} 
								<c:if test="${lessonDto.displayMonitor}">
								<a href="javascript:openMonitorLesson(${lessonDto.lessonID})">
								<fmt:message key="index.monitor" /></a>
								</c:if>
							</li>
						</c:forEach>
					</ul>
					<br>
				</c:when>
				<c:otherwise>
					<p><span style="font-style: italic"><fmt:message
						key="lessonsearch.noresults" /></span></p>
				</c:otherwise>
			</c:choose></div>
		</c:forEach>
	</c:otherwise>
</c:choose></div>
</lams:html>