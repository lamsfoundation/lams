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
			var f = document.getElementById('reflectionForm');
			f.submit();
		}
	</script>
</lams:head>
<body class="stripes">

	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<lams:Page type="learner" title="${sessionMap.title}" formID="reflectionForm">
	
	<form:form action="submitReflection.do" method="post" onsubmit="disableFinishButton();" id="reflectionForm" modelAttribute="reflectionForm">
		<form:hidden path="userID" />
		<form:hidden path="sessionMapID" />

			<lams:errors/>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
			</p>

			<textarea id="focused" rows="5" name="entryText" class="form-control">${reflectionForm.entryText}</textarea>

			<a href="#nogo" class="btn btn-primary voffset5 pull-right na" id="finishButton" onclick="submitForm('finish')">
				<c:choose>
					<c:when test="${sessionMap.isLastActivity}">
						<fmt:message key="label.submit" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.finished" />
					</c:otherwise>
				</c:choose>
			</a>

	</form:form>
	
	<div id="footer"/>
	
	</lams:Page>

</body>

<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
</script>

</lams:html>
