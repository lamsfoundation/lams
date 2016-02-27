<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<script type="text/javascript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
		
		function submitForm(methodName){
			var f = document.getElementById('messageForm');
			f.submit();
		}
	</script>
</lams:head>
<body class="stripes">

	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<lams:Page type="learner" title="${sessionMap.title}">
	
	<html:form action="/learning/submitReflection" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
		<html:hidden property="userID" />
		<html:hidden property="sessionMapID" />

			<%@ include file="/common/messages.jsp"%>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
			</p>

			<html:textarea cols="60" rows="8" property="entryText"
				styleClass="text-area" />

			<div class="space-bottom-top align-right">
				<html:link href="#nogo" styleClass="btn btn-primary pull-right na" styleId="finishButton" onclick="submitForm('finish')">
						<c:choose>
							<c:when test="${sessionMap.activityPosition.last}">
								<fmt:message key="label.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.finished" />
							</c:otherwise>
						</c:choose>
				</html:link>
			</div>
	</html:form>
	</lams:Page>

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
