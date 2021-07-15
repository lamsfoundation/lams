<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>


<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', function(){
			$('#messageForm').submit();
		});
		
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
	</script>
</lams:head>
<body class="stripes">

	<form:form action="submitReflection.do" modelAttribute="reflectionForm" method="post" onsubmit="disableFinishButton();" id="messageForm">
		<form:hidden path="userID" />
		<form:hidden path="sessionMapID" />

		<lams:Page type="learner" title="${sessionMap.title}" formID="messageForm">

			<lams:errors/>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
			</p>

			<textarea cols="60" id="notebookEntry" name="entryText" class="form-control" rows="5"></textarea>

			<div class="voffset10">
				<button type="button" class="btn btn-primary pull-right na" id="finishButton">
					<span class="nextActivity"> <c:choose>
							<c:when test="${sessionMap.isLastActivity}">
								<fmt:message key="label.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.finished" />
							</c:otherwise>
						</c:choose>
					</span>
				</button>
			</div>

			<div id="footer"></div>
			<!--closes footer-->
		</lams:Page>
			
	</form:form>

</body>
</lams:html>
