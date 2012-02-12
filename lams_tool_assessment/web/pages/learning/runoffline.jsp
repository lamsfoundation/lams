<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
	<!--
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&runOffline=true"/>';
			return false;
		}
	-->        
    </script>
</lams:head>

<body class="stripes">
	<div id="content">
		<h1>
			${sessionMap.title}
		</h1>

		<c:choose>
			<c:when test="${empty sessionMap.submissionDeadline}">
				<p>
					<fmt:message key="run.offline.message" />
				</p>			
			</c:when>
			<c:otherwise>
				<div class="warning">
					<fmt:message key="authoring.info.teacher.set.restriction" >
						<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
					</fmt:message>
				</div>
			</c:otherwise>
		</c:choose>

		<div class="space-bottom-top align-right">
			<html:link href="#nogo" property="FinishButton" styleId="finishButton"
				onclick="return finishSession()" styleClass="button">
				<span class="nextActivity"><fmt:message key="label.finished" /></span>
			</html:link>
		</div>
	</div>
	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
