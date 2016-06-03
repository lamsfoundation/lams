<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>


<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
		function submitForm(methodName) {
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

		<lams:Page type="learner" title="${sessionMap.title}">

			<%@ include file="/common/messages.jsp"%>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
			</p>

			<html:textarea cols="60" styleId="notebookEntry" property="entryText" styleClass="form-control" />

			<div class="voffset10">
				<html:link href="#nogo" styleClass="btn btn-primary pull-right na" styleId="finishButton"
					onclick="submitForm('finish')">
					<span class="nextActivity"> <c:choose>
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

			<div id="footer"></div>
			<!--closes footer-->
		</lams:Page>
			
	</html:form>

</body>
</lams:html>
