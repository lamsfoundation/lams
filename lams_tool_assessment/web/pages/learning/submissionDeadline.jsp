<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="title" scope="request"><fmt:message key="activity.title" /></c:set>
	
<lams:html>
<lams:head>
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
	
		<html:link href="#nogo" property="FinishButton" styleId="finishButton"
				onclick="return finishSession()" styleClass="btn btn-primary voffset10 pull-right na">
			<c:choose>
				<c:when test="${sessionMap.activityPosition.last}">
					<fmt:message key="label.submit" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.finished" />
				</c:otherwise>
			</c:choose>
		</html:link>
	
	</lams:Page>
</body>
</lams:html>
