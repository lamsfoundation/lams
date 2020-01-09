<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="index.searchlesson" /></title>

	<link rel="stylesheet" href="${lams}/css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<style media="screen,projection" type="text/css">
		li.ui-menu-item {
			list-style: none;
		}
	</style>

	<script type="text/javascript" src="${lams}/includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="${lams}/loadVars.jsp"></script>
	<script type="text/javascript" src="${lams}/includes/javascript/openUrls.js"></script>
	<script type="text/javascript" src="${lams}/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}/includes/javascript/jquery-ui.js"></script>

	<script type="text/javascript">
		$(document).ready(function(){
			$("#query").focus().autocomplete({
				'source' : "${lams}/findUserLessons/autocomplete.do?courseID=${courseID}",
				'delay'  : 700,
				'select' : function(event, ui){
					// display results for an exact user ID
					window.location.href = "${lams}/findUserLessons/getResults.do?courseID=${courseID}&userID="
											+ ui.item.value; 
			    }
			});
		})
	</script>
</lams:head>
<body class="stripes">

	<c:set var="title">
		<fmt:message key="lessonsearch.title" />
	</c:set>
	
	<lams:Page type="admin" title="${title}">

		<div class="panel">
			<fmt:message key="lessonsearch.instuctions" />
		</div>

		<form action="${lams}/findUserLessons/getResults.do">
			<input type="text" id="query" name="query" />
			<input type="hidden" name="courseID" value="${courseID}" />
			<button type="submit" class="btn btn-sm btn-primary">
				<i class="fa fa-sm fa-search"></i>
			</button>
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
					<div style="display: inline-block">
						<span class="loffset10"><lams:Portrait userId="${user.key.userId}"/>&nbsp;<strong><c:out
								value="${user.key.firstName} ${user.key.lastName}" /></strong></span>
						<c:choose>
							<c:when test="${user.value ne null and not empty user.value}">
								<ul>
									<c:forEach var="lessonDto" items="${user.value}">
										<li><c:out value="${lessonDto.lessonName}"
												escapeXml="true" /> <c:if
												test="${lessonDto.displayMonitor}">
												<a href="javascript:openMonitorLesson(${lessonDto.lessonID})">
													<fmt:message key="index.monitor" />
												</a>
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
