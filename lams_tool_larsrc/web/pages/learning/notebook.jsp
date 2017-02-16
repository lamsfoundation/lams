<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
        

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>
	
	<script type="text/javascript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
	</script>
</lams:head>
<body class="stripes">

	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<lams:Page type="learner" title="${sessionMap.title}">
	
	<html:form action="/learning/submitReflection" method="post" onsubmit="disableFinishButton();" styleId="reflectionForm">
		<html:hidden property="userID" />
		<html:hidden property="sessionMapID" />

		<div id="content">
			<%@ include file="/common/messages.jsp"%>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
			</p>

			<html:textarea rows="8" property="entryText" styleId="focused" styleClass="form-control" />

			<div class="space-bottom-top align-right">
				<button type="submit" class="btn btn-primary voffset5 pull-right na" id="finishButton">
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
				</button>
			</div>
		</div>
	</html:form>

	<div id="footer">
	</div>
	<!--closes footer-->

	</lams:Page>
	
</body>
</lams:html>
