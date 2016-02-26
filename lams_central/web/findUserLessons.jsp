<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-function" prefix="fn"%>

<!DOCTYPE html>

<c:set var="lams">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="index.searchlesson" /></title>

	<link rel="stylesheet" href="${lams}/css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<style media="screen,projection" type="text/css">
li.ui-menu-item {
	list-style: none;
}
</style>

	<script type="text/javascript" src="${lams}/includes/javascript/getSysInfo.js"></script>
	<script language="javascript" type="text/javascript" src="${lams}/loadVars.jsp"></script>
	<script type="text/javascript" src="${lams}/includes/javascript/openUrls.js"></script>
	<script type="text/javascript" src="${lams}/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}/includes/javascript/jquery-ui.js"></script>

	<script type="text/javascript">
		window.onload = (function() {
				document.getElementById("query").focus();
			
			try {
				var data = "Core Selectors Attributes Traversing Manipulation CSS Events Effects Ajax Utilities"
						.split(" ");
				var url = "${lams}/findUserLessons.do?courseID=${courseID}&dispatch=autocomplete";

				$("#query").autocomplete({
					source : url
				});
			} catch (e) {
			}
		});
	</script>
</lams:head>
<body class="stripes">

	<c:set var="title">
		<fmt:message key="lessonsearch.title" />
	</c:set>

	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>

	<lams:Page type="admin" title="${title}">

		<div class="panel">
			<fmt:message key="lessonsearch.instuctions" />
		</div>

		<form action="${lams}/findUserLessons.do">
			<input type="hidden" name="dispatch" value="getResults"> 
			<input type="text" id="query" name="query"> 
			<input type="hidden" name="courseID" value="${courseID}">
			<button type="submit" class="btn btn-sm btn-primary"><i class="fa fa-sm fa-search"></i></button>
		</form>
		
		<div class="voffset10"></div>

		<c:choose>
			<c:when test="${userLessonsMap == null}">
				<!-- don't display anything -->
			</c:when>

			<c:when test="${empty userLessonsMap}">
				<p>
					<fmt:message key="lessonsearch.noresults" />
				</p>
			</c:when>

			<c:otherwise>
				<h5>
					<fmt:message key="lessonsearch.results">
						<fmt:param value="${fn:escapeXml(originalQuery)}" />
					</fmt:message>
				</h5>

				<c:forEach var="user" items="${userLessonsMap}">
					<div class="user">
						<strong><c:out value="${user.key.firstName} ${user.key.lastName}" /></strong>
						<c:choose>
							<c:when test="${user.value ne null and not empty user.value}">
								<ul>
									<c:forEach var="lessonDto" items="${user.value}">
										<li><c:out value="${lessonDto.lessonName}" escapeXml="true" /> <c:if test="${lessonDto.displayMonitor}">
												<a href="javascript:openMonitorLesson(${lessonDto.lessonID})"> <fmt:message key="index.monitor" /></a>
											</c:if></li>
									</c:forEach>
								</ul>
								<br>
							</c:when>
							<c:otherwise>
								<div id="noResults">
									<fmt:message key="lessonsearch.noresults" />
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>

	</lams:Page>
</lams:html>
