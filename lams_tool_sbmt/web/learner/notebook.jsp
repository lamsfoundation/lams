<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
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
	<html:form action="/learning/submitReflection" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
		<html:hidden property="userID" />
		<html:hidden property="sessionMapID" />
		<div id="content">
			<h1>
				<c:out value="${sessionMap.title}"/>
			</h1>

			<%@ include file="/common/messages.jsp"%>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" />
			</p>

			<html:textarea cols="60" rows="8" property="entryText"
				styleClass="text-area" />

			<div class="space-bottom-top align-right">
				<html:link href="#nogo" styleClass="button" styleId="finishButton" onclick="submitForm('finish')">
					<span class="nextActivity">
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>
					</span>
				</html:link>
			</div>
	</html:form>
	<div id="footer"></div>

</body>
</lams:html>
