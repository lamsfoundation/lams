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
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
	-->        
    </script>
</lams:head>

<body class="stripes">
	<div id="content">
		<h1>
			<c:out value="${sessionMap.title}" escapeXml="true"/>
		</h1>
				
		<div class="warning">
			<fmt:message key="authoring.info.teacher.set.restriction" >
				<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
			</fmt:message>
		</div>

		<div class="space-bottom-top align-right">
			<html:link href="#nogo" property="FinishButton" styleId="finishButton"
				onclick="return finishSession()" styleClass="button">
				<span class="nextActivity">
					<c:choose>
						<c:when test="${sessionMap.activityPosition.last}">
							<fmt:message key="label.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.finished" />
						</c:otherwise>
					</c:choose>
				</span>
			</html:link>
		</div>
	</div>
	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
