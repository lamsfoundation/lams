<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="title" scope="request"><fmt:message key="activity.title" /></c:set>
	
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		function finishSession() {
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
    </script>
</lams:head>

<body class="stripes">
	<lams:Page type="learner" title="${sessionMap.title}">
			
		<lams:Alert id="submission-deadline" type="danger" close="false">
			<fmt:message key="authoring.info.teacher.set.restriction" >
				<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
			</fmt:message>
		</lams:Alert>
	
		<a href="#nogo" name="FinishButton" id="finishButton"
				onclick="return finishSession()" class="btn btn-primary voffset10 pull-right na">
			<c:choose>
				<c:when test="${sessionMap.isLastActivity}">
					<fmt:message key="label.submit" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.finished" />
				</c:otherwise>
			</c:choose>
		</a>
	
	</lams:Page>
</body>
</lams:html>
